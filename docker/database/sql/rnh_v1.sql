-- Drop existing database
DROP DATABASE IF EXISTS rnh;
DROP DATABASE IF EXISTS rnh-test;

-- Database
CREATE DATABASE rnh;
CREATE DATABASE rnh-test;

-- Drop existing tables
DROP TABLE IF EXISTS subscription;
DROP TABLE IF EXISTS entity;
DROP TABLE IF EXISTS app_user;

-- Create the 'app_user' table with an array of subscription_id
CREATE TABLE app_user (
    id VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255) NOT NULL
    -- Add other user-related columns as needed
);

-- Create the 'entity' table
CREATE TABLE entity (
    id VARCHAR(255) PRIMARY KEY,
    label VARCHAR(255) NOT NULL,
    description TEXT
);

-- Create the 'subscription' table with a foreign key reference to 'entity' table and a unique constraint on user_id
CREATE TABLE subscription (
    id VARCHAR(255) PRIMARY KEY,
    counter INT NOT NULL,
    entity_id VARCHAR(255) REFERENCES entity(id),
    user_id VARCHAR(255) UNIQUE REFERENCES app_user(id)  -- Change 'user' to 'app_user'
    -- Add other subscription-related columns as needed
);

-- Insert data into the 'app_user' table
INSERT INTO app_user (id, username) VALUES
  ('448119cb-fa88-4c3c-a0fe-1e269726018e', 'User1'),
  ('203e1fab-e0d6-432b-ba30-e202c15320dd', 'User2'),
  ('3048e9c0-89c6-4d55-a984-d08b1f631f37', 'User3');

-- Insert data into the 'entity' table
INSERT INTO entity (id, label, description) VALUES
  ('e1c21a7c-3c87-4a07-a4fd-6f9a3a76e53b', 'Entity1', 'Description for Entity1'),
  ('e2e14cb7-858d-4d87-82b1-1a71110f8165', 'Entity2', 'Description for Entity2'),
  ('e3f4d8e9-5a22-4931-ba12-0a1d2d8e99c7', 'Entity3', 'Description for Entity3');

-- Insert data into the 'subscription' table with a unique user_id constraint
INSERT INTO subscription (id, counter, entity_id, user_id) VALUES
  ('f87fd91f-4efd-4f67-b203-c99a479f72fb', 1, 'e1c21a7c-3c87-4a07-a4fd-6f9a3a76e53b', '448119cb-fa88-4c3c-a0fe-1e269726018e'),
  ('23fd2721-7e9a-4f39-a39a-31f409e7b8cd', 2, 'e2e14cb7-858d-4d87-82b1-1a71110f8165', '203e1fab-e0d6-432b-ba30-e202c15320dd'),
  ('18f52d89-4cd8-43d8-8a61-95208f831be9', 3, 'e3f4d8e9-5a22-4931-ba12-0a1d2d8e99c7', '3048e9c0-89c6-4d55-a984-d08b1f631f37');
