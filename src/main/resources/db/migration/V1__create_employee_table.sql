-- Create sequence for employee_id
CREATE SEQUENCE employee_id_seq START 1;

-- Create table for employees
CREATE TABLE employees
(
    employee_id INT DEFAULT NEXTVAL('employee_id_seq') PRIMARY KEY,
    firstname   VARCHAR(100)        NOT NULL,
    lastname    VARCHAR(100)        NOT NULL,
    username    VARCHAR(100) UNIQUE NOT NULL,
    email       VARCHAR(100) UNIQUE NOT NULL
);

-- Insert initial data into employees table
INSERT INTO employees (employee_id, firstname, lastname, username, email)
VALUES (nextval('employee_id_seq'), 'John', 'Doe', 'johndoe', 'john.doe@example.com'),
       (nextval('employee_id_seq'), 'Jane', 'Smith', 'janesmith', 'jane.smith@example.com'),
       (nextval('employee_id_seq'), 'Alice', 'Johnson', 'alicej', 'alice.johnson@example.com'),
       (nextval('employee_id_seq'), 'Bob', 'Brown', 'bobb', 'bob.brown@example.com'),
       (nextval('employee_id_seq'), 'Charlie', 'Davis', 'charlied', 'charlie.davis@example.com');

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
INSERT INTO roles (role_id, name, code)
VALUES (nextval('role_id_seq'),'Admin', 'ADMIN'),
       (nextval('role_id_seq'),'User', 'USER'),
       (nextval('role_id_seq'),'Manager', 'MANAGER'),
       (nextval('role_id_seq'),'Guest', 'GUEST'),
       (nextval('role_id_seq'),'Moderator', 'MODERATOR');

-- Create sequence for accounts
CREATE SEQUENCE IF NOT EXISTS account_id_seq START 1;

-- Create accounts table
CREATE TABLE IF NOT EXISTS accounts
(
    account_id         BIGINT                DEFAULT NEXTVAL('account_id_seq') PRIMARY KEY,
    username           VARCHAR(255) UNIQUE NOT NULL,
    password           VARCHAR(200)  NOT NULL,
    enabled            BOOLEAN      NOT NULL DEFAULT TRUE,
    locked             BOOLEAN      NOT NULL DEFAULT FALSE,
    expired            BOOLEAN      NOT NULL DEFAULT FALSE,
    credential_expired BOOLEAN      NOT NULL DEFAULT FALSE
);

-- Insert initial data into accounts table
INSERT INTO accounts (account_id, username, password, enabled, locked, expired, credential_expired)
VALUES (nextval('account_id_seq'), 'admin', 'admin1234', TRUE, FALSE, FALSE, FALSE),
       (nextval('account_id_seq'), 'user', 'user1234', TRUE, FALSE, FALSE, FALSE),
       (nextval('account_id_seq'), 'manager', 'manager1234', TRUE, FALSE, FALSE, FALSE),
       (nextval('account_id_seq'), 'guest', 'guest1234', TRUE, FALSE, FALSE, FALSE),
       (nextval('account_id_seq'), 'moderator', 'moderator1234', TRUE, FALSE, FALSE, FALSE);

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
       ((SELECT account_id FROM accounts WHERE username = 'manager'), (SELECT role_id FROM roles WHERE code = 'MANAGER')),
       ((SELECT account_id FROM accounts WHERE username = 'guest'), (SELECT role_id FROM roles WHERE code = 'GUEST')),
       ((SELECT account_id FROM accounts WHERE username = 'moderator'), (SELECT role_id FROM roles WHERE code = 'MODERATOR'));
