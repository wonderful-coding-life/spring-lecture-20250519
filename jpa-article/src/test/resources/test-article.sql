DROP TABLE IF EXISTS member CASCADE;
DROP TABLE IF EXISTS article CASCADE;

CREATE TABLE member (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    email VARCHAR(256) NOT NULL UNIQUE,
    age INTEGER
);

CREATE TABLE article (
   id INTEGER AUTO_INCREMENT PRIMARY KEY,
   title VARCHAR(256),
   description VARCHAR(4096),
   created DATETIME,
   updated DATETIME,
   member_id INTEGER,
   FOREIGN KEY(member_id) REFERENCES member(id) ON DELETE CASCADE
);

INSERT INTO member(name, email, age) VALUES('윤서준', 'SeojunYoon@hanbit.co.kr', 10);
INSERT INTO member(name, email, age) VALUES('윤광철', 'KwangcheolYoon@hanbit.co.kr', 43);
INSERT INTO member(name, email, age) VALUES('공미영', 'MiyeongKong@hanbit.co.kr', 23);
INSERT INTO member(name, email, age) VALUES('김도윤', 'DoyunKim@hanbit.co.kr', 10);

INSERT INTO article(title, description, created, updated, member_id) VALUES('첫번째 제목', '첫번째 본문', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);
INSERT INTO article(title, description, created, updated, member_id) VALUES('두번째 제목', '두번째 본문', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2);
INSERT INTO article(title, description, created, updated, member_id) VALUES('세번째 제목', '세번째 본문', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);
INSERT INTO article(title, description, created, updated, member_id) VALUES('네번째 제목', '네번째 본문', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2);

