CREATE TYPE proof_type AS ENUM ('LINK', 'TEXT', 'IMAGE');

CREATE TABLE proofs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    step_id BIGINT NOT NULL REFERENCES mission_steps(id) ON DELETE CASCADE,
    proof_type proof_type NOT NULL,
    content TEXT,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_user_step_proof UNIQUE (user_id, step_id)
);

CREATE TABLE ad_views (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    step_id BIGINT REFERENCES mission_steps(id) ON DELETE CASCADE,
    ad_session_id VARCHAR(255),
    receipt TEXT,
    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_user_step_adview UNIQUE (user_id, step_id)
);

CREATE INDEX idx_proofs_user_id ON proofs(user_id);
CREATE INDEX idx_proofs_step_id ON proofs(step_id);
CREATE INDEX idx_ad_views_user_id ON ad_views(user_id);
CREATE INDEX idx_ad_views_step_id ON ad_views(step_id);