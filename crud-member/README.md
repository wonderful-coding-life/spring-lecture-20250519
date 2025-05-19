# RESTful API 작성하기
사용자에 대해 생성, 조회, 수정, 삭제를 하는 RESTful API를 만들어 봅니다.
## 프로젝트 생성
### 의존성 추가
- Spring Web
- Spring Data JPA
- H2 Database
- Lombok
# RESTful API 설계
- GET /api/members (전체 목록 조회)
- GET /api/members/:id (아이디로 조회)
- POST /api/members (생성)
- PUT /api/members/:id (수정)
- PATCH /api/members/:id (패치, 일부 필드만 수정)
## 자바 객체와 데이터메이스 테이블(엔티티) 매핑
- @Entity: 자바 객체와 데이터베이스 테이블(Entity) 매핑, 디폴트로 객체 이름은 테이블 이름으로 필드 이름은 컬럼 이름으로 사용되며, 자바의 CamelCase는 언더스코어로 분리된 이름으로 매핑된다. 예, ServiceMember --> SERVICE_MEMBER
- @Data: Getter, Setter 및 기본 생성자 생성
- @Id: 엔티티의 기본키(Primary Key)로 지정
- @GeneratedValue: 데이터베이스에서 자동 생성된 값을 사용
- 필드는 원시타입(Primitive) 보다는 객체타입을 사용하는 것이 Null 값을 처리할 수 있어 용이 (예, RESTful API의 PATCH 구현시 용이)
```
@Entity
@Data
public class Member {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String password;
    private String email;
    private Integer age;
    private Boolean enabled;
}
```
## JPA Repository for Member
```
public interface UserRepository extends JpaRepository<Member, Long> {
}
```
## 컨트롤러에 API 생성
```
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    - 생략 -
```
## application.properties
- spring.application.name: 프로젝트 이름(사용 예: 스프링 클라우드의 유레카에서 서비스 이름 등록/검색)
- spring.datasource.url: 데이터소스 URL(H2 데이터베이스에서 지정하지 않으면 매번 바뀌어 실행 로그를 찾아 보아야 함)
- spring.h2.console.enabled: H2 데이터베이스 콘솔 활성화(브라우저에서 콘솔 접속 가능 /h2-console)
- spring.jpa.show-sql: JPA 메서드 실행시 데이터베이스에 어떠한 SQL문으로 변환되어 수행되는지 로그 출력
- spring.jpa.properties.hibernate.format_sql: SQL 로그 출력시 줄바꿈을 하여 가독성을 높임
```
spring.application.name=demo
spring.datasource.url=jdbc:h2:mem:demo
spring.h2.console.enabled=true
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

## RESTful API Response에 XML 지원하기
- 클라이언트가 헤더에 Accept application/json 대신 application/xml을 전달할 경우 XML을 반환할 수 있다.
- build.gradle에 아래의 의존성을 추가하면 된다.
```
implementation 'com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.18.1'
```