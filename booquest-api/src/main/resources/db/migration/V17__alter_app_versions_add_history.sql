-- 1. 컬럼 이름 변경
ALTER TABLE app_versions
    RENAME COLUMN latest_version TO version;

ALTER TABLE app_versions
    RENAME COLUMN latest_build_number TO build_number;

-- 2. 새로운 컬럼 추가
ALTER TABLE app_versions
    ADD COLUMN is_force_update BOOLEAN NOT NULL DEFAULT false,
    ADD COLUMN description VARCHAR(1000) NOT NULL DEFAULT '정보 없음',
    ADD COLUMN released_at TIMESTAMPTZ NOT NULL DEFAULT now();

-- 3. 플랫폼 + 버전 조합이 고유해야 함 (이력 관리용)
DROP INDEX IF EXISTS uq_app_versions_platform;

CREATE UNIQUE INDEX uq_app_versions_platform_version
    ON app_versions (platform, version);

-- 4. 기존 데이터 보정
UPDATE app_versions
SET is_force_update = false,
    released_at = now();
