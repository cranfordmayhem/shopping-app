ALTER TABLE user_account
ADD COLUMN created_by VARCHAR(255),
ADD COLUMN created_date TIMESTAMP,
ADD COLUMN last_modified_by VARCHAR(255),
ADD COLUMN last_modified_date TIMESTAMP;

ALTER TABLE user_profile
ADD COLUMN created_by VARCHAR(255),
ADD COLUMN created_date TIMESTAMP,
ADD COLUMN last_modified_by VARCHAR(255),
ADD COLUMN last_modified_date TIMESTAMP;

ALTER TABLE refresh_token
ADD COLUMN created_by VARCHAR(255),
ADD COLUMN created_date TIMESTAMP,
ADD COLUMN last_modified_by VARCHAR(255),
ADD COLUMN last_modified_date TIMESTAMP;