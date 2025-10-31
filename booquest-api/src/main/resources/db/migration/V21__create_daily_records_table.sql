CREATE TABLE daily_records (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    record_date DATE NOT NULL,
    content TEXT,
    image_url VARCHAR(500),
    xp_granted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uk_user_record_date UNIQUE (user_id, record_date)
);

CREATE INDEX idx_daily_records_user_id ON daily_records(user_id);
CREATE INDEX idx_daily_records_record_date ON daily_records(record_date);
CREATE INDEX idx_daily_records_user_date_range ON daily_records(user_id, record_date);