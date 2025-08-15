-- ENUMS
CREATE TYPE expression_style AS ENUM ('text', 'drawing', 'video');
CREATE TYPE strength_type AS ENUM ('creation', 'organize_deliver', 'daily_share', 'trend_pickup');

-- onboarding_profiles 구조 변경
ALTER TABLE onboarding_profiles
DROP COLUMN metadata,
    ADD COLUMN job VARCHAR(100),
    ADD COLUMN expression_style expression_style,
    ADD COLUMN strength_type strength_type;

-- onboarding_categories 테이블 생성
CREATE TABLE onboarding_categories (
                                       id           BIGSERIAL PRIMARY KEY,
                                       profile_id   BIGINT NOT NULL REFERENCES onboarding_profiles(id) ON DELETE CASCADE,
                                       category     VARCHAR(100) NOT NULL,
                                       sub_category VARCHAR(100) NOT NULL,
                                       created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
