# 게시판 CRUD RESTful API 작성
게시글에 대한 CRUD를 담당하는 RESTful API를 작성합니다.
- 생성 POST /api/members/:memberId/articles
- 전체 조회 GET /api/articles
- 전체 조회 GET /api/articles?page=1&size=3&sort=title,asc&sort=description,desc
- 글쓴이 게시글 조회 GET /api/members/:memberId/articles
- 글쓴이 게시글 조회 GET /api/articles?memberId={memberId}
- 수정 PUT /api/articles/:id
- 삭제 DELETE /api/articles/:id

# Member와 Article의 1:N 관계 정의
- Article에서 부모인 Member의 레퍼런스 member 선언하고 @ManyToOne 선언
- 만약 Member가 삭제시 자동으로 Article을 삭제하려면 Member에서도 List<Articles> articles 선언하고 @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true) 선언

# RESTful API 보안 (API Key)
- HTTP Header에 Authorization에 Bearer 액세스키를 사용
- AccessKey, AccessKeyRepository, AccessKeyFilter (OncePerRequestFilter는 한번 거쳐간 필터는 마킹을 하여 다음번에는 건너뛰도록 하기 때문에 forward 발생시 필터를 거치지 않게 한다. redirect는 어차피 클라이언트가 다시 처음부터 요청하는 것이기 때문에 OncePerRequestFilter라 하더라도 다시 거친다.)
- 스프링컨테이너에 등록하는 것이 아닌 서블릿 필터로 등록해야 하기 때문에 Application Main 클래스에 @ServletComponentScan 추가
- data.sql 초기 데이터
- 데이터 초기화를 진행하기 위한 설정
```
#H2 데이터베이스는 default가 always, MySQL은 아님
spring.sql.init.mode=always
# JPA 초기화를 끝낸 이후에 초기화가 실행되도록 함
spring.jpa.defer-datasource-initialization=true
```

# H2 데이터베이스 대신 MySQL 데이터베이스 사용
- 어플리케이션이 사용할 데이터베이스와 계정 생성
```
create database lecture;
create member 'hanbit'@'%' identified by 'hanbit1234';
grant all on lecture.* to 'hanbit'
```
- 어플리케이션 설정에서 데이터베이스 접속 URL과 계정 정보 추가 및 ddl-auto 설정
```
spring.datasource.url=jdbc:mysql://localhost:3306/lecture
spring.datasource.username=demo
spring.datasource.password=demo1234
spring.jpa.hibernate.ddl-auto=update
```
- build.gradle 설정 파일 열어서 H2 대신 MySQL 의존성 추가
```
runtimeOnly 'com.mysql:mysql-connector-j'
```

