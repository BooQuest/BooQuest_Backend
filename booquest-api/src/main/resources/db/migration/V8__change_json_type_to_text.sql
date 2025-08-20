BEGIN;

ALTER TABLE side_jobs
    ALTER COLUMN prompt_meta TYPE TEXT USING prompt_meta::text;

ALTER TABLE missions
    ALTER COLUMN design_notes TYPE TEXT USING design_notes::text;

ALTER TABLE mission_steps
    ALTER COLUMN detail TYPE TEXT USING detail::text;

ALTER TABLE step_progress
    ALTER COLUMN extra TYPE TEXT USING extra::text;

COMMIT;