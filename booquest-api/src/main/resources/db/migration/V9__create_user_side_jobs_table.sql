CREATE TYPE sidejob_status AS ENUM ('PLANNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED');

CREATE TABLE user_side_jobs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    side_job_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    status sidejob_status NOT NULL DEFAULT 'PLANNED',
    started_at TIMESTAMPTZ,
    ended_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_user_side_jobs_user_id ON user_side_jobs(user_id);
CREATE INDEX idx_user_side_jobs_started_at ON user_side_jobs(started_at);
CREATE INDEX idx_user_side_jobs_ended_at ON user_side_jobs(ended_at);

-- 조인 최적화
CREATE INDEX IF NOT EXISTS idx_user_side_jobs_side_job_id
  ON user_side_jobs(side_job_id);

-- 기간 무결성
ALTER TABLE user_side_jobs
  ADD CONSTRAINT chk_usj_period
  CHECK (ended_at IS NULL OR started_at IS NULL OR ended_at >= started_at);