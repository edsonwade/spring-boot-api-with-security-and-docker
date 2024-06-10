-- Create sequence for user_id
CREATE SEQUENCE employee_id_seq START 1;

-- Create table for users
CREATE TABLE users
(
    employee_id INT DEFAULT NEXTVAL('employee_id_seq') PRIMARY KEY,
    firstname   VARCHAR(100)        NOT NULL,
    lastname    VARCHAR(100)        NOT NULL,
    username    VARCHAR(100) UNIQUE NOT NULL,
    email       VARCHAR(100) UNIQUE NOT NULL

);

INSERT INTO users (firstname, lastname, username, email)
VALUES ('John', 'Doe', 'johndoe', 'john.doe@example.com'),
       ('Jane', 'Smith', 'janesmith', 'jane.smith@example.com'),
       ('Alice', 'Johnson', 'alicej', 'alice.johnson@example.com'),
       ('Bob', 'Brown', 'bobb', 'bob.brown@example.com'),
       ('Charlie', 'Davis', 'charlied', 'charlie.davis@example.com');

-- Create sequence for roles
CREATE SEQUENCE IF NOT EXISTS role_id_seq START 1;

-- Create roles table
CREATE TABLE IF NOT EXISTS roles
(
    role_id BIGINT DEFAULT NEXTVAL('role_id_seq') PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    code    VARCHAR(255) NOT NULL
);

-- Insert initial data into roles table
INSERT INTO roles (name, code)
VALUES ('Admin', 'ADMIN'),
       ('User', 'USER'),
       ('Manager', 'MANAGER'),
       ('Guest', 'GUEST'),
       ('Moderator', 'MODERATOR');

-- Create sequence for accounts
CREATE SEQUENCE IF NOT EXISTS account_id_seq START 1;

-- Create accounts table
CREATE TABLE IF NOT EXISTS accounts
(
    account_id         BIGINT                DEFAULT NEXTVAL('account_id_seq') PRIMARY KEY,
    username           VARCHAR(255) UNIQUE NOT NULL,
    password           VARCHAR(10)  NOT NULL,
    enabled            BOOLEAN      NOT NULL DEFAULT TRUE,
    locked             BOOLEAN      NOT NULL DEFAULT FALSE,
    expired            BOOLEAN      NOT NULL DEFAULT FALSE,
    credential_expired BOOLEAN      NOT NULL DEFAULT FALSE
);

-- Insert initial data into accounts table
INSERT INTO accounts (username, password, enabled, locked, expired, credential_expired)
VALUES ('admin', 'admin1234', TRUE, FALSE, FALSE, FALSE),
       ('user', 'user1234', TRUE, FALSE, FALSE, FALSE),
       ('manager', 'manager1234', TRUE, FALSE, FALSE, FALSE),
       ('guest', 'guest1234', TRUE, FALSE, FALSE, FALSE),
       ('moderator', 'moderator1234', TRUE, FALSE, FALSE, FALSE);

-- Create AccountRole join table
CREATE TABLE IF NOT EXISTS AccountRole
(
    accountId BIGINT NOT NULL,
    roleId    BIGINT NOT NULL,
    PRIMARY KEY (accountId, roleId),
    FOREIGN KEY (accountId) REFERENCES accounts (account_id) ON DELETE CASCADE,
    FOREIGN KEY (roleId) REFERENCES roles (role_id) ON DELETE CASCADE
);

-- Insert initial data into AccountRole join table
INSERT INTO AccountRole (accountId, roleId)
VALUES ((SELECT account_id FROM accounts WHERE username = 'admin'), (SELECT role_id FROM roles WHERE code = 'ADMIN')),
       ((SELECT account_id FROM accounts WHERE username = 'user'), (SELECT role_id FROM roles WHERE code = 'USER')),
       ((SELECT account_id FROM accounts WHERE username = 'manager'),
        (SELECT role_id FROM roles WHERE code = 'MANAGER')),
       ((SELECT account_id FROM accounts WHERE username = 'guest'), (SELECT role_id FROM roles WHERE code = 'GUEST')),
       ((SELECT account_id FROM accounts WHERE username = 'moderator'),
        (SELECT role_id FROM roles WHERE code = 'MODERATOR'));
