CREATE TABLE member (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    email VARCHAR(256) NOT NULL UNIQUE,
    password VARCHAR(256)
);

CREATE TABLE authority (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    authority VARCHAR(256),
    member_id INTEGER,
    FOREIGN KEY(member_id) REFERENCES member(id)
);

CREATE TABLE article (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(256),
    description VARCHAR(4096),
    created DATETIME,
    updated DATETIME,
    member_id INTEGER,
    FOREIGN KEY(member_id) REFERENCES member(id)
);


