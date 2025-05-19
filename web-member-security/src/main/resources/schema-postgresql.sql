-- AUTO_INCREMENT for MySQL, SERIAL for PostgreSQL (replacement for INTEGER AUTO_INCREMENT) or GENERATED ALWAYS AS IDENTITY
-- DATETIME for MySQL, TIMESTAMP for PostgreSQL

CREATE TABLE IF NOT EXISTS member (
    id SERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    email VARCHAR(256) NOT NULL UNIQUE,
    password VARCHAR(256)
);

CREATE TABLE IF NOT EXISTS authority (
    id SERIAL PRIMARY KEY,
    authority VARCHAR(256),
    member_id INTEGER,
    FOREIGN KEY(member_id) REFERENCES member(id)
);

CREATE TABLE IF NOT EXISTS article (
    id SERIAL PRIMARY KEY,
    title VARCHAR(256),
    description VARCHAR(4096),
    created TIMESTAMP,
    updated TIMESTAMP,
    member_id INTEGER,
    FOREIGN KEY(member_id) REFERENCES member(id)
);

--DROP TABLE article;
--DROP TABLE authority;
--DROP TABLE member;