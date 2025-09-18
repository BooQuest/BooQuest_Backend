CREATE TABLE app_versions (
                              id          BIGSERIAL PRIMARY KEY,
                              platform    VARCHAR(20) NOT NULL,   -- 'ANDROID', 'IOS'
                              latest_version     VARCHAR(50) NOT NULL,   -- 예: '1.2.3'
                              latest_build_number    INT NOT NULL   -- 예: '6'
);

CREATE UNIQUE INDEX uq_app_versions_platform ON app_versions (platform);

INSERT INTO app_versions (platform, latest_version, latest_build_number)
VALUES
    ('ANDROID', '1.0.0', 1),
    ('IOS', '1.0.0', 1);
