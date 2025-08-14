package com.booquest.booquest_api.ai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * 진짜 FastAPI까지 때리고 오는 E2E.
 * - 사전조건: uvicorn으로 AI 서버가 {AI_SERVER_PORT} 포트에서 실행 중일 것 (기본 8000)
 * - 프로필: ai_test (DB/JPA 비활성)
 *
 * 안전장치:
 * - 서버가 죽어있으면 테스트를 "실패" 대신 "스킵" 처리(Assume)하여 CI를 흔들지 않음.
 */
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = {
				// DB/JPA 관련 자동구성 전부 비활성화
				"spring.autoconfigure.exclude=" +
						"org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
						"org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration," +
						"org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration," +
						"org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration," +
						"org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration," +
						"org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration" +
						// (부트 3.2+ 사용 중이면 ↓ 이것도 같이 끄면 깔끔)
						",org.springframework.boot.autoconfigure.jdbc.JdbcClientAutoConfiguration"
		}
)
@ActiveProfiles("ai_test")
class AiFlowE2eRealServerTest {
	@LocalServerPort
	int port;

	@Autowired TestRestTemplate rest;

	static final ObjectMapper om = new ObjectMapper();

	// 외부 FastAPI 서버 포트 — 필요 시 환경변수/시스템프로퍼티로 바인딩해도 좋음
	static final int AI_SERVER_PORT = 8000; // uvicorn 기본 예시
	static final String AI_BASE = "http://127.0.0.1:" + AI_SERVER_PORT;

	/** 빠르게 살아있는지 체크해서 아니면 테스트 스킵 */
	static boolean ping(String url) {
		try {
			var u = new URL(url);
			var conn = (HttpURLConnection) u.openConnection();
			conn.setConnectTimeout(1000);
			conn.setReadTimeout(1000);
			conn.setRequestMethod("GET");
			conn.connect();
			// /docs 는 200이면 좋고, /ai/generate-tasks는 POST 전용이라 405여도 "살아있음"으로 간주
			return conn.getResponseCode() < 500;
		} catch (IOException e) {
			return false;
		}
	}

	@BeforeEach
	void assumeAiServerUp() {
		boolean up = ping(AI_BASE + "/docs");
		Assumptions.assumeTrue(
				up,
				() -> "AI 서버가 " + AI_BASE + " 에서 실행 중이어야 합니다 (예: uvicorn app.main:app --port " + AI_SERVER_PORT + ")."
		);
	}

	@Test
	@DisplayName("실제 AI 서버로 /ai/generate-tasks 호출 → 백엔드가 그대로 변환하여 반환")
	void endToEnd_real_ai_ok() throws Exception {
		var req = Map.of(
				"userId", "u1",
				"context", "SNS 부업 초기 큰 임무 5개를 단계별로 만들어줘"
		);

		// 여기서는 스프링 백엔드의 랜덤 포트로 호출한다.
		var resp = rest.postForEntity(
				"http://localhost:" + port + "/api/ai/generate-tasks",
				req,
				String.class
		);

		Assertions.assertThat(resp.getStatusCode().is2xxSuccessful())
				.as("백엔드 응답 HTTP 2xx 여야 함").isTrue();

		Map<String, Object> body = om.readValue(resp.getBody(), new TypeReference<>() {});
		Assertions.assertThat(body).containsKey("tasks");

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> tasks = (List<Map<String, Object>>) body.get("tasks");

		Assertions.assertThat(tasks)
				.as("AI가 big_tasks를 생성해야 함")
				.isNotNull()
				.isNotEmpty();

		Map<String, Object> t0 = tasks.get(0);
		Assertions.assertThat(t0).containsKeys("title", "description");
	}
}
