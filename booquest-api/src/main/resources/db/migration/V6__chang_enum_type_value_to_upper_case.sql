ALTER TYPE mission_status RENAME VALUE 'planned' TO 'PLANNED';
ALTER TYPE mission_status RENAME VALUE 'in_progress' TO 'IN_PROGRESS';
ALTER TYPE mission_status RENAME VALUE 'completed' TO 'COMPLETED';
ALTER TYPE mission_status RENAME VALUE 'cancelled' TO 'CANCELLED';

ALTER TYPE step_status RENAME VALUE 'planned' TO 'PLANNED';
ALTER TYPE step_status RENAME VALUE 'in_progress' TO 'IN_PROGRESS';
ALTER TYPE step_status RENAME VALUE 'completed' TO 'COMPLETED';
ALTER TYPE step_status RENAME VALUE 'skipped' TO 'SKIPPED';