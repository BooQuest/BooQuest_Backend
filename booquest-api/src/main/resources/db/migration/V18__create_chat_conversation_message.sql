-- 대화방
CREATE TABLE chat_conversation (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id BIGINT NOT NULL,
  title TEXT,   -- 첫 질문으로 자동 생성 가능
  summary TEXT, -- 오래된 히스토리 요약(자동)
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX ON chat_conversation (user_id, updated_at DESC);

-- 메시지(역할/순서 보존)
CREATE TABLE chat_message (
  id BIGSERIAL PRIMARY KEY,
  conversation_id UUID NOT NULL REFERENCES chat_conversation(id) ON DELETE CASCADE,
  role TEXT NOT NULL CHECK (role IN ('user','assistant','system','tool')),
  content TEXT NOT NULL,
  tokens INT,       -- 토큰 사용량
  latency_ms INT,   -- 선택: 생성 지연
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX ON chat_message (conversation_id, id); -- 페이징용

-- 트리거 A: chat_conversation이 UPDATE되면 updated_at = NOW() 자동 세팅
CREATE OR REPLACE FUNCTION set_conversation_updated_at()
RETURNS trigger AS $$
BEGIN
  NEW.updated_at := NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_chat_conversation_set_updated_at ON chat_conversation;
CREATE TRIGGER trg_chat_conversation_set_updated_at
BEFORE UPDATE ON chat_conversation
FOR EACH ROW
EXECUTE FUNCTION set_conversation_updated_at();

-- 트리거 B: chat_message INSERT 시 부모 대화 updated_at 갱신(트리거 A 유도)
CREATE OR REPLACE FUNCTION touch_conversation_on_new_message()
RETURNS trigger AS $$
BEGIN
  UPDATE chat_conversation
     SET updated_at = NOW()
   WHERE id = NEW.conversation_id;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_touch_conversation_on_message_insert ON chat_message;
CREATE TRIGGER trg_touch_conversation_on_message_insert
AFTER INSERT ON chat_message
FOR EACH ROW
EXECUTE FUNCTION touch_conversation_on_new_message();