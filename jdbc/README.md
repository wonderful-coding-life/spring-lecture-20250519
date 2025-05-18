
# 테이블 생성 및 초기 데이터 설정

```sql
-- 멤버 테이블 스키마 생성
CREATE TABLE IF NOT EXISTS member (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    email VARCHAR(256) NOT NULL UNIQUE,
    age INTEGER
);

-- 멤버 테이블 초기 데이터 생성
INSERT INTO member(name, email, age) VALUES('윤서준', 'SeojunYoon@hanbit.co.kr', 10);
INSERT INTO member(name, email, age) VALUES('윤광철', 'KwangcheolYoon@hanbit.co.kr', 43);
INSERT INTO member(name, email, age) VALUES('공미영', 'MiyeongKong@hanbit.co.kr', 23);
INSERT INTO member(name, email, age) VALUES('김도윤', 'DoyunKim@hanbit.co.kr', 10);
```

# 인텔리제이 콘솔에서 System.out.println의 한글 깨지는 문제
Run Configuration > Edit... > Modify options > Add VM option...
```declarative
-Dsun.stdout.encoding=COMPAT
```