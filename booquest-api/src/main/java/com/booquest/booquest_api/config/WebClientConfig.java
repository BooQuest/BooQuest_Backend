package com.booquest.booquest_api.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean(name = "aiWebClient")
    public WebClient aiWebClient(
            @Value("${ai.base-url:http://localhost:8081}") String baseUrl) {

        // Reactor Netty 타임아웃 설정
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2_000)        // connect timeout
                .responseTimeout(Duration.ofSeconds(25))                     // read timeout
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(25))
                        .addHandlerLast(new WriteTimeoutHandler(25)));

        // (대용량 JSON이 오면 필요) 디코딩 버퍼 상한 조정
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(cfg -> cfg.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)) // 16MB
                .build();

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeaders(h -> {
                    h.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                    h.add("X-Service", "java-api"); // 내부 식별용
                    h.add(HttpHeaders.USER_AGENT, "booquest-api");
                })
                .exchangeStrategies(strategies)
                .build();
    }

}
