-- Apple refresh_token 등을 저장할 user_oauth_token 테이블 생성
CREATE TABLE apple_refresh_token (
                                  user_id BIGINT NOT NULL,
                                  refresh_token TEXT,
                                  issued_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                  PRIMARY KEY (user_id),
                                  CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
