ALTER TABLE daily_records
    ADD COLUMN image_presigned_url VARCHAR(1000),
    ADD COLUMN image_presigned_expires_at TIMESTAMPTZ;

ALTER TABLE daily_records
    RENAME COLUMN image_url TO image_object_key;