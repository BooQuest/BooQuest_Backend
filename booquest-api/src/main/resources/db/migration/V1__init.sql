-- V1__init.sql

-- Enum Types

CREATE TYPE mission_status AS ENUM ('planned', 'in_progress', 'completed', 'cancelled');
CREATE TYPE step_status     AS ENUM ('planned', 'in_progress', 'completed', 'skipped');

-- Tables

-- users
CREATE TABLE users (
                       id               BIGSERIAL PRIMARY KEY,
                       provider         VARCHAR(20),
                       provider_user_id VARCHAR(190),
                       email            VARCHAR(255),
                       nickname         VARCHAR(100),
                       created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                       updated_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                       CONSTRAINT uq_users_provider_uid UNIQUE (provider, provider_user_id)
);

-- onboarding_profiles
CREATE TABLE onboarding_profiles (
                                     user_id    BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE RESTRICT,
                                     metadata   JSONB,
                                     created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                     updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- side_jobs (유저별 부업/테마 세션)
CREATE TABLE side_jobs (
                           id          BIGSERIAL PRIMARY KEY,
                           user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
                           title       VARCHAR(200) NOT NULL,
                           description TEXT,
                           prompt_meta JSONB,
                           is_active   BOOLEAN NOT NULL DEFAULT FALSE,
                           started_at  TIMESTAMPTZ,
                           ended_at    TIMESTAMPTZ,
                           created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                           updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- missions
CREATE TABLE missions (
                          id            BIGSERIAL PRIMARY KEY,
                          sidejob_id    BIGINT NOT NULL REFERENCES side_jobs(id) ON DELETE CASCADE,
                          user_id       BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
                          title         VARCHAR(200) NOT NULL,
                          status        mission_status NOT NULL DEFAULT 'planned',
                          order_no      INT NOT NULL,
                          design_notes  JSONB,
                          created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                          updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                          CONSTRAINT uq_missions_sidejob_order UNIQUE (sidejob_id, order_no)
    -- 주의: user_id는 조회 편의 컬럼. 애플리케이션에서 side_jobs.user_id와 일관성 보장 필요
);

-- mission_steps
CREATE TABLE mission_steps (
                               id         BIGSERIAL PRIMARY KEY,
                               mission_id BIGINT NOT NULL REFERENCES missions(id) ON DELETE CASCADE,
                               seq        INT NOT NULL,
                               title      VARCHAR(200) NOT NULL,
                               status     step_status NOT NULL DEFAULT 'planned',
                               detail     JSONB,
                               created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                               updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                               CONSTRAINT uq_mission_steps_seq UNIQUE (mission_id, seq)
);

-- step_progress (사용자별 스텝 진행 현황)
CREATE TABLE step_progress (
                               id              BIGSERIAL PRIMARY KEY,
                               step_id         BIGINT NOT NULL REFERENCES mission_steps(id) ON DELETE CASCADE,
                               user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
                               status          step_status NOT NULL DEFAULT 'planned',
                               difficulty_rate INT,
                               prefer_similar  BOOLEAN,
                               feedback_text   TEXT,
                               extra           JSONB,
                               started_at      TIMESTAMPTZ,
                               completed_at    TIMESTAMPTZ,
                               created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                               CONSTRAINT uq_step_progress UNIQUE (step_id, user_id),
                               CONSTRAINT ck_difficulty_rate_range CHECK (
                                   difficulty_rate IS NULL OR (difficulty_rate BETWEEN 1 AND 5)
                                   )
    -- 교차 무결성(같은 유저의 스텝인지)은 애플리케이션에서 검증 필요
);

-- user_characters (게이미피케이션 프로필)
CREATE TABLE user_characters (
                                 user_id    BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE RESTRICT,
                                 name       VARCHAR(100),
                                 level      INT NOT NULL DEFAULT 1,
                                 exp        INT NOT NULL DEFAULT 0,
                                 avatar_url VARCHAR(255),
                                 updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- user_stats (읽기 최적화 통계 뷰/모델)
CREATE TABLE user_stats (
                            user_id            BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE RESTRICT,
                            total_missions     INT NOT NULL DEFAULT 0,
                            completed_missions INT NOT NULL DEFAULT 0,
                            completion_rate    NUMERIC(5,2) NOT NULL DEFAULT 0.00,
                            last_active_at     TIMESTAMPTZ,
                            updated_at         TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
