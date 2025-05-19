# 서비스 구현
- 프리젠테이션 레이어와 비즈니스 레이어 그리고 데이터 퍼시스턴스 레이어 분리
- 프리젠테이션 레이어 --> Controller
- 비즈니스 레이어 --> Service
- 데이터 퍼시스턴스 레이어 --> JPA
- 데이터 퍼시스턴스 레이어에서 사용되는 데이터를 도메인 모델이라고 하고 프리젠테이션 레이어에서 사용되는 데이터를 DTO(Data Transfer Object)라고 함.
- 프리젠테이션 레이어와 서비스 레이어는 DTO 모델로 연동하고, 서비스 레이어와 데이터 퍼시스턴스 레이어는 도메인 모델로 연동하며 서비스는 DTO와 도메인모델을 변환하며 필요한 여러가지 비즈니스 로직을 수행한다. 
# DTO 모델 (dto package 이름을 보통 사용하고 클래스 이름 뒤에 Dto를 붙인다)
- 예를 들어 password나 enabled와 같은 속성은 프리젠테이션 레이어에 노출하지 않고 내부적으로 다른 경로로 관리하고 싶다면 DTO 객체에서 password와 enabled를 뺀다.
```
@Data
@Builder
public class MemberDto {
    private Long id;
    private String name;
    private String email;
    private Integer age;
}
```
- 또는 Dto 대신 다른 용어를 붙이는 경우도 있다. 예를 들어 요청시 전달되는 Dto 객체와 응답시 전달하는 Dto 객체가 다를때 ...Request 또는 ...Response를 붙이기도 한다.
```
@Data
@Builder
public class MemberRequest {
    private String name;
    private String email;
    private Integer age;
}

@Data
@Builder
public class MemberResponse {
    private Long id;
    private String name;
    private String email;
    private Integer age;
}
```
# 오류 핸들링 (HTTP Status)
- 200번대: 성공 (일반적으로 200, 생성시 201)
- 300번대: 리다이렉션
- 400번대: 클라이언트의 잘못 (없는 리소스 아이디를 전달하거나 - 404 NOT FOUND, 적절하지 않은 요청을 한 경우 - 400 BAD REQUEST)
- 500번대: 서버의 잘못
- 컨트롤러에서 @ResponseStatus로 성공시 반환값(디폴트 200)으로 하고 오류 상황에서는 예외를 HTTP Status와 함께 커스텀으로 만들어서 사용
```
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not Found")
public class NotFoundException extends RuntimeException {
}
```
- @ResponseStatus(HttpStatus.OK)는 디폴트이므로 생략 가능
```
public MemberResponse findById(Long id) {
    Member member = memberRepository.findById(id)
            .orElseThrow(NotFoundException::new);
    return mapToMemberResponse(member);
}
```
## 서비스 레이어 생성
- MemberService
- 서비스에서는 DTO와 모델 객체 변환
- PUT에서도 PATCH처럼 PASSWORD, ENABLED는 제외 
# JPA Query Methods
```
// JPA Query Methods for WHERE
List<Member> findByName(String name);
List<Member> findByNameAndEmail(String name, String email);
List<Member> findByNameOrEmail(String name, String email);
List<Member> findByNameContaining(String name);
List<Member> findByNameLike(String name);
List<Member> findByAgeIs(Integer age);
List<Member> findByAgeIsNull();
List<Member> findByAgeIsNotNull();
List<Member> findByAgeGreaterThan(Integer age);
List<Member> findByAgeGreaterThanEqual(Integer age);
List<Member> findByAgeLessThan(Integer age);
List<Member> findByAgeLessThanEqual(Integer age);

// JPA Query Methods for ORDER BY
List<Member> findAllByOrderByNameAsc();
List<Member> findAllByOrderByNameDesc();
List<Member> findAllByOrderByNameAscAgeDesc();

// JPA Query Methods for WHERE ORDER BY
List<Member> findByNameContainingOrderByNameAsc(String name);

// JPQL (Java Persistence Query Language)
@Query("select m from Member m where m.enabled = :active and m.age >= 19 and m.email is not null order by m.name")
List<Member> getActiveAdultWithEmail(@Param("active") boolean active);
@Query(value = "select * from member where enabled = :active and age >= 19 and email is not null order by name", nativeQuery = true)
List<Member> getActiveAdultWithEmailByNative(@Param("active") boolean active);
```
# Spring Boot Test for testing JPA Query Method
테스트 클래스는 아래와 같은 형식으로 작성합니다.
클래스 이름 위에 @SpringBootTest를 추가하여 테스트 태스크에 포함하도록 명시합니다.
테스트 객체의 경우 생성자를 통해 의존성을 주입 받지 못 하기 때문에 필요한 의존성은 @AutoWired를 통해 주입 받아야 합니다.
@BeforeEach와 @AfterEach는 이름에서 알 수 있듯이 각 테스트 전과 후에 수행되어야 할 작업을 작성합니다. 아래의 UserRepositoryTest 클래스에는 수행해야 할 테스트 메서드가 3개 있으며 모두 @Test  애노테이션을 추가하였습니다.
@Test가 사용된 메서드는 매번 테스트 할 때마다 @BeforeEach와 @AfterEach가 수행되며 반복 수행시에도 마찬가지 입니다.
```
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void doBeforeEach() {
        // 각 테스트 전에 수행되어야 할 작업
    }

    @AfterEach
    public void doAfterEach() {
        // 각 테스트가 종료된 후에 수행되어야 할 작업
    }

    @Test
    public void testMemberCase1() {
        // 첫번째 테스트 코드
    }

    @Test
    public void testMemberCase2() {
        // 두번째 테스트 코드
    }

    @Test
    public void testMemberCase3() {
        // 세번째 테스트 코드
    }
}
```
- @SpringBootTest
- @AutoConfigureMockMvc - 컨트롤러를 테스트하기 위해 톰캣을 사용하지 않고 MockMvc를 사용한다.
- @AutoWired
- @MockBean - MockMvc에서 사용할 Mock 객체를 정의 (해당 인터페이스 객체를 찾을 때 대신 사용함)
```
        // UserService Mock 객체의 동작 케이스 추가
        when(memberService.create(memberRequest)).thenReturn(UserResponse.builder()
                .id(1L)
                .name("윤서준")
                .age(10).build());
```
- @BeforeAll / @AfterAll - must be static
- @BeforeEach / @AfterEach
- @Disabled : 임시로 테스트를 하지 말아야 할 때 @Disabled("설명")
- @DisplayName : 테스트 결과에 표시할 테스트 이름
- @Test : 1회 테스트 수행
- @RepeatedTest : 반복 테스트 수행 예) @RepeatedTest(10)
- assertThat
```agsl
assertThat(sum).isEqualTo(8);
assertThat(sum).isNotEqualTo(5);
assertThat("abc").contains("a", "c");
assertThat("abc").containsAnyOf("c", "d");
assertThat("test").startsWith("te");
assertThat("test").endsWith("st");
assertThat("").isEmpty();
assertThat("hello").isNotEmpty();
assertThat(3).isPositive();
assertThat(0).isNotPositive();
assertThat(0).isZero();
assertThat(3).isGreaterThan(2);
assertThat(3).isGreaterThanOrEqualTo(3);
assertThat(3).isLessThan(5);
assertThat(3).isLessThanOrEqualTo(3);
```
# 트랜잭션 처리
- 메서드 이름 위에 @Transactional 애노테이션을 붙이면 메서드 내에서 데이터베이스에 생성, 수정, 삭제등 변경 처리된 작업들이 모두 성공하거나 모두 롤백하는 방식으로 동작하며 일부만 반영되지 않도록 한다.
- 테스트를 위해 /api/members/batch 라는 API와 createBatch() 서비스 메서드를 만들고 createBatch()에 @Transactional을 붙여 테스트합니다. 생성할 사용자의 이름이 NULL인 경우 예외가 발생하게 되고 이 경우 롤백이 일어납니다. 단, Runtime Exception(Unexpected Exception)만 롤백을 시키며 나머지 Expected Exception(소스코드에서 반드시 Exception 처리를 해야 컴파일이 진행되는 예외)는 롤백을 하지 않는 것이 기본 규칙이다.
## 여기서 잠깐
- @Transactional은 기본적으로 RuntimeException(Unexpedted exception)에 대해서만 롤백을 하고 Expected Exception(코드내에서 명시적으로 예외 처리를 해 주어야한 컴파일이 되는 예외)에 대해서는 커밋을 진행한다. 만약 모든 예외에 대해 롤백을 하려면 아래와 같이 @Transactional에 속성을 붙여준다.
```
@Transactional(rollbackFor = {Exception.class})
```
- @Transactional 애노테이션을 메서드가 아닌 클래스 이름 위에 붙이면 해당 클래스내의 모든 메서드에 트랜잭션이 적용된다.

## 여기서 잠깐
단위테스트(Unit Test)란 다른 의존성에 영향 받지 않고 테스트 하기 위한 방법. 예를 들어 컨트롤러가 서비스에 의존하는데 서비스를 만들기 전에 또는 실제 서비스에 의존하지 않고 가짜 서비스를 만들어 컨트롤러만 단위테스트를 하고자 한다면 아래와 같이 MockMvc를 만들때 MockBean으로 서비스 객체를 선언하고, 사용할 때마다 When().thenReturn()으로 동작방식을 만들어 사용한다.
```
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerUnitTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberService memberService;
    @Autowired
    private ObjectMapper objectMapper;
```

## 테스트 데이터
### 사용자 여러명 배치 생성
```
[
    {
        "name": "윤서준",
        "email": "SeojunYoon@hanbit.co.kr",
        "age": 10
    },
    {
        "name": "윤광철",
        "email": "KwangcheolYoon@hanbit.co.kr",
        "age": 43
    },
    {
        "name": "공미영",
        "email": "SeojunYoon@hanbit.co.kr",
        "age": 26
    },
    {
        "name": "김도윤",
        "email": "DoyunKim@hanbit.co.kr",
        "age": 10
    }
]
```
