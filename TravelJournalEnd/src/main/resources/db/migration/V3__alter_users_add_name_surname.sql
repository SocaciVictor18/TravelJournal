ALTER TABLE users
    ADD COLUMN name    TEXT,
    ADD COLUMN surname TEXT;


ALTER TABLE users
    ALTER COLUMN name    SET NOT NULL,
    ALTER COLUMN surname SET NOT NULL;

ALTER TABLE users
    DROP CONSTRAINT IF EXISTS users_username_key;

ALTER TABLE users
    DROP COLUMN username;