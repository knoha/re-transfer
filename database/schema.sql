-- DDL
CREATE TABLE users (
  id         BIGINT PRIMARY KEY,
  first_name VARCHAR(50),
  last_name  VARCHAR(50),
  username   VARCHAR(30) NOT NULL,
  email      VARCHAR(100),
  CONSTRAINT users_uni01 UNIQUE (username)
);

CREATE TABLE bank_accounts (
  id            BIGINT PRIMARY KEY,
  number        VARCHAR(36) NOT NULL,
  currency_code VARCHAR(3)  NOT NULL,
  balance       DECIMAL(20, 2),
  owner         BIGINT,
  CONSTRAINT bank_accounts_fk01 FOREIGN KEY (owner) REFERENCES users (id),
  CONSTRAINT bank_accounts_uni01 UNIQUE (number)
);

--- DML
INSERT INTO users VALUES (1, 'John', 'Doe', 'jdoe', 'jdoe@gmail.com');
INSERT INTO users VALUES (2, 'Sam', 'Fisher', 'sfisher', 'sfisher@gmail.com');

INSERT INTO bank_accounts VALUES (1, 'a6b6f9b9-6e62-4fe6-a689-37e3ab0d812c', 'USD', 10000.59, 1);
INSERT INTO bank_accounts VALUES (2, 'bfe87d38-0c94-4389-8416-602151e58481', 'USD', 245.21, 2);