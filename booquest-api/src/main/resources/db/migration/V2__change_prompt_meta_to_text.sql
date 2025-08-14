-- prompt_meta 컬럼을 JSONB → TEXT로 변경

-- 1단계: 임시 컬럼 생성
ALTER TABLE side_jobs
    ADD COLUMN prompt_meta_text TEXT;

-- 2단계: 기존 데이터 복사
UPDATE side_jobs
SET prompt_meta_text = prompt_meta::TEXT;

-- 3단계: 기존 컬럼 삭제
ALTER TABLE side_jobs
DROP COLUMN prompt_meta;

-- 4단계: 이름 원상 복구
ALTER TABLE side_jobs
    RENAME COLUMN prompt_meta_text TO prompt_meta;
