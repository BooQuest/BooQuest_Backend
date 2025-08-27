ALTER TABLE tokens DROP CONSTRAINT IF EXISTS fk_tokens_user_id;
ALTER TABLE tokens ADD CONSTRAINT fk_tokens_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

UPDATE users SET provider = UPPER(provider) WHERE provider IS NOT NULL;