CREATE TABLE income (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    user_side_job_id BIGINT NOT NULL REFERENCES user_side_jobs(id),
    title VARCHAR(200) NOT NULL,
    amount INTEGER NOT NULL CHECK (amount > 0),
    income_date DATE NOT NULL,
    memo TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_income_user_id ON income(user_id);
CREATE INDEX idx_income_user_side_job_id ON income(user_side_job_id);
CREATE INDEX idx_income_income_date ON income(income_date);
CREATE INDEX idx_income_created_at ON income(created_at);

-- 조회 성능 최적화
CREATE INDEX idx_income_user_date ON income(user_id, income_date DESC);
CREATE INDEX idx_income_sidejob_date ON income(user_side_job_id, income_date DESC);