
-- 사용자 온보딩 프로필 테이블
CREATE TABLE IF NOT EXISTS onboarding_profiles (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    nickname VARCHAR(100) NOT NULL,
    job VARCHAR(255) NOT NULL,
    hobbies JSONB NOT NULL DEFAULT '[]',
    expression_style VARCHAR(50) NOT NULL,
    strength_type VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 인덱스 생성
CREATE INDEX IF NOT EXISTS idx_onboarding_profiles_user_id ON onboarding_profiles(user_id);
CREATE INDEX IF NOT EXISTS idx_onboarding_profiles_created_at ON onboarding_profiles(created_at);

-- 6. SNS 트렌드 테이블 (스케줄링에서 사용)
CREATE TABLE IF NOT EXISTS sns_trends (
    id SERIAL PRIMARY KEY,
    uuid UUID UNIQUE NOT NULL DEFAULT gen_random_uuid(),
    platform VARCHAR(50) NOT NULL,
    trend_type VARCHAR(50) NOT NULL,
    title VARCHAR(500) NOT NULL,
    content TEXT NOT NULL,
    url VARCHAR(1000),
    tags JSONB,
    engagement_metrics JSONB,
    legal_implications TEXT,
    is_active BOOLEAN DEFAULT TRUE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 인덱스 생성
CREATE INDEX IF NOT EXISTS idx_sns_trends_uuid ON sns_trends(uuid);
CREATE INDEX IF NOT EXISTS idx_sns_trends_platform ON sns_trends(platform);
CREATE INDEX IF NOT EXISTS idx_sns_trends_trend_type ON sns_trends(trend_type);
CREATE INDEX IF NOT EXISTS idx_sns_trends_is_active ON sns_trends(is_active);
CREATE INDEX IF NOT EXISTS idx_sns_trends_created_at ON sns_trends(created_at);

-- 트렌드 임베딩 테이블 (벡터 검색용)
CREATE TABLE IF NOT EXISTS trend_embeddings (
    id SERIAL PRIMARY KEY,
    trend_uuid UUID NOT NULL,
    embedding vector(384), -- sentence-transformers/all-MiniLM-L6-v2 모델의 384차원
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 인덱스 생성
CREATE INDEX IF NOT EXISTS idx_trend_embeddings_trend_uuid ON trend_embeddings(trend_uuid);
CREATE INDEX IF NOT EXISTS idx_trend_embeddings_created_at ON trend_embeddings(created_at);

-- 벡터 유사도 검색을 위한 인덱스 (HNSW 알고리즘 사용)
CREATE INDEX IF NOT EXISTS idx_trend_embeddings_vector_cosine
ON trend_embeddings USING hnsw (embedding vector_cosine_ops)
WITH (m = 16, ef_construction = 64);