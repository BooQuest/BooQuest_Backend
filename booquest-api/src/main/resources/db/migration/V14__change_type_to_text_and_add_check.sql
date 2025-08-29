ALTER TABLE missions
  ALTER COLUMN status TYPE varchar(32)
  USING status::text;

ALTER TABLE mission_steps
  ALTER COLUMN status TYPE varchar(32)
  USING status::text;

ALTER TABLE missions
  ADD CONSTRAINT missions_status_check
  CHECK (status IN ('PLANNED','IN_PROGRESS','COMPLETED','CANCELLED'));

ALTER TABLE mission_steps
  ADD CONSTRAINT mission_steps_status_check
  CHECK (status IN ('PLANNED','IN_PROGRESS','COMPLETED','SKIPPED'));

