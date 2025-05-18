# MyBatis
- SQL Mapper (XML based vs. Annotation based)
- DTO and Service Layer, Transactional
- Test mapper and service using test sql script
- 테스트에만 사용하는 SQL 스크립트가 있다면 resources 폴더에 스크립트를 작성하고 아래와 같이 테스트 클래스에서 설정할 수 있다.
```
@SpringBootTest
@Slf4j
@Sql(scripts = {"classpath:/test-article.sql"})
public class ArticleServiceTests {
    @Autowired
    private ArticleService articleService;
```
- SQL 스크립트가 만약 테스트에서만 사용된다면 main > resources 에 있을 필요는 없다. 스프링 이니셜라이저에서 프로젝트를 생성하면 기본적으로 test 에는 resources 디렉토리가 없지만 test > resources 디렉토리를 만들고 그 안에 SQL 스크립트를 작성해도 된다. 만약 main > resources 와 test > resources 양쪽에 동일한 이름의 스크립트가 있다면 테스트에서는 test > resources 에 있는 것이 우선적으로 사용된다.
- 만약 spring.sql.init.mode를 true로 설정하여 메인에서도 초기화 스크립트가 존재한다면 테스트 실행시 이 부분도 같이 실행되므로 충돌이 나지 않도록 주의해야 한다.
- 메인의 초기화 스크립트가 아니더라도 각 테스트 케이스는 독립적으로 수행되어 매번 @Sql 스크립트가 실행되므로 당연히 그 안의 스크립트는 매 테스트 케이스마다 실행될 수 있도록 작성되어야 한다.
