

# Srping Web

## 웹에서 페이지 처리
JpaRepository를 확장하여 리파지토리를 만들었다면 Pageable 객체를 사용하여 페이지 단위로 쿼리를 할 수 있습니다.

```java
public String getArticleList(Pageable pageable, Model model) {
    // 생략
}
```
요청은 다음과 같이 page(현재 페이지), size(페이지당 사이즈), sort(정렬 기준, 정렬 방향) 파라미터로 전달할 수 있습니다.
- http://localhost:8080/article/list?page=0&size=20&sort=member.email,desc
- http://localhost:8080/article/list?page=1&size=10&sort=updated,desc

Pageable 파라미터가 전달되지 않는다면 page=0, size=20를 디폴트 값으로 사용됩니다. 만약 디폴트 값을 설정하고 싶다면 다음과 같이 @PageableDefault 애노테이션을 사용할 수 있습니다.
```java
public String getArticleList(@PageableDefault(size = 10, sort="id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
    // 생략
}
```

참고로 Pageable의 sort 설정과 JPA 메서드 쿼리로 findBy...OrderBy...로 설정한 정렬 방법이 다르면 메서드 쿼리의 설정이 우선하게 됩니다.

## 세션
스프링 부트 어플리케이션에서는 브라우저를 통해 어플리케이션의 컨트롤러를 호출하는 순간 세션이 만들어지고 세션 타임 아웃되거나 또는 브라우저가 종료될 때까지 유지됩니다.
또는 스프링 시큐리티를 통해 로그인 할 때에도 새롭게 세션이 만들어지고 로그아웃 하면 세션이 종료되기도 합니다. 세션이 유지되고 있는 동안 세션에 객체를 저장하거나 꺼내어 사용할 수도 있습니다.
--> 세션 설명은 스프링 시큐리티 부분에서 ??? 로그인 된 상태 유지 및 CSRF 토큰 세션에 유지

# Spring Security

## 프로젝트 설정
스프링 시큐리티를 프로젝트에 추가하기 위해 build.gradle에 아래와 같이 시큐리티 스타터 패키지를 포함합니다.
```
implementation 'org.springframework.boot:spring-boot-starter-security'
```
## 스프링 시큐리티 기본 동작
시큐리티 스타터 패키지를 포함하는 것만으로도 스프링 웹 시큐리티가 활성화 되고 기본 구성이 되어 어플리케이션 실행 시 아래와 같이 접속 비밀번호가 로그에 출력됩니다.
```
Using generated security password: 277d3173-083b-4e91-96b1-ad263e095be6
This generated password is for development use only. Your security configuration must be updated before running your application in production.
```
또한 /login과 /logout을 지원하며 웹 페이지 접속 시 사용자 인증이 되지 않은 상태에서 /login으로 리다이렉트되어 스프링 시큐리티가 제공하는 디폴트 로그인 화면을 보여 줍니다.
아이디는 user 패스워드는 로그에 출력된 패스워드를 입력하면 사용자 인증이 되어 웹 화면으로 연결이 됩니다.
이후 /logout이 호출되면 로그아웃 절차를 진행합니다.
스프링 부트는 별도로 설정하지 않아도 아래와 같은 보안이 자동으로 활성화 됩니다.
* CSRF 필터
* 모든 URL 요청에 대해 사용자 인증 요구
* Http Basic Authentication 지원
  이것을 설정으로 표현하면 아래와 같습니다.
```
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());
        return http.build();
    }
}
```
## URL에 따른 접근 권한 설정
URL에 따라 접근 권한을 다르게 가져가고 싶다면 아래와 같이 authorizeHttpRequest 메서드에 requestMatchers()를 통해 설정합니다. 이때 requestMatchers()가 호출되는 순서가 중요하며 확인하고자 하는 URL이 매칭되는 첫번째 requestMachters() 설정에 따라 필요한 권한이 결정됩니다.
또한 anyRequest()는 항상 마지막에 사용해야 하며 그 이후에 있는 requestMatchers()는 의미가 없고 실제로 컴파일 오류가 발생합니다.
requestMatchers() 메서드에는 파라미터로 설정하고자 하는 URL 패턴을 전달하면 되는데 이때 가장 많이 사용하는 것이 AntPathRequestMatcher("URL 패턴") 객체이고 이것을 하나 또는 여러개를 파라미터로 전달할 수 있습니다.
requestMatchers()에 메서드로 설정하고자 하는 URL 패턴을 설정한 다음에는 해당 URL 들에 대해 어떤 검증을 할 것인지 설정하는데 아래와 같은 메서드를 사용할 수 있습니다.
* permitAll() - 사용자 인증 없이도 접근 가능
* authenticated() - 사용자 인증을 한 후에 접근 가능
* hasAuthority("ROLE_ADMIN"), hasAnyAuthorities("ROLE_ADMIN", "ROLE_USER") - 권한이 있는 경우에만 접근 가능
* hasRole("ADMIN), hasAnyRole("ADMIN", "USER") - 역할이 있는 경우에만 접근 가능

스프링에서 권한(Authority)와 역할(Role)은 비슷한 의미를 갖습니다. 실제로 역할 이름은 권한 이름 앞에 "ROLE_"이라는 접두사를 붙인 것과 동일한데요 즉, hasAuthority("ROLE_ADMIN")과 hasRole("ADMIN)은 동일하게 동작합니다.
```
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/signin")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/member/**")).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/"),
                                new AntPathRequestMatcher("/article/list"),
                                new AntPathRequestMatcher("/article/content")).permitAll()
                        .anyRequest().authenticated())
                .httpBasic(withDefaults())
                .formLogin(form -> form.loginPage("/login").permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/"));
        return http.build();
    }
```
## CSRF 필터
스프링 시큐리티는 기본적으로 CSRF 필터를 활성화 시킵니다. 스프링이 제공하는 로그인 폼을 브라우저의 소스코드 보기를 통해 확인하면 아래와 같이 폼 안에 _csrf 라는 이름의 항목이 들어가 있는 것을 볼 수 있습니다.
CSRF 필터는 어플리케이션이 반환하는 HTML 내에 <form action="URL">이 있는 경우 그 폼 안에 _csrf 토큰을 생성하여 추가하고, 브라우저가 입력한 폼을 POST를 사용하여 전달할 때 _csrf 토큰을 검증하여 포스트맨등을 사용하여 악의적으로 POST 요청을 전달하는 것에 대해 방어합니다.
```
<input type="hidden" name="_csrf" value="wtdPh3dRN76sx1tISkOO_RF8wxxqehgaMOfTaBAsoaSJSRl9pu4s40M1BYqB9j4tfW66yycY7iRcSS43B4GwW3VJw5HteitP"/>
```
주의해야 할 것은 반드시 <form> 안에 action이 포함되어 있는 경우에 자동으로 _csrf 토큰을 추가하기 때문에 action을 생략하고자 한다면 모델에 있는 _csrf를 가져와 직접 넣어 주어야 합니다.
```
<form method="post">
    <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>
</form>
```
만약 CSRF 필터를 비활성화 하고자 한다면 시큐리티 필터 체인에서 아래와 같이 비활성화 할 수 있습니다. 하지만 보안을 위해 개발시에만 잠시 꺼두고 운영시에는 CSRF 필터는 꼭 켜두는 것을 권장합니다.
```
http.csrf(csrf -> csrf.disable())
```
한번 생성된 CSRF 토큰은 세션이 유지되는 동안에만 유효하며 따라서 로그아웃 하거나 브라우저가 종료되면 더 이상 유효하지 않습니다.
## 시큐리티 필터 체인 확인
실제로 어떤 시큐리티 필터들이 체인으로 연결되어 있는지 확인하려면 아래와 같이 @EnableWebSecurity(debug = true)를 사용하여 디버그 모드를 활성화 해 줍니다.
```
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {
```
이후 브라우저에서 어플리케이션 페이지에 접속하면 아래와 같이 필터 체인이 로그에 출력되는 것을 볼 수 있습니다.
```
Security filter chain: [
  DisableEncodeUrlFilter
  WebAsyncManagerIntegrationFilter
  SecurityContextHolderFilter
  HeaderWriterFilter
  CorsFilter
  CsrfFilter
  LogoutFilter
  UsernamePasswordAuthenticationFilter
  DefaultLoginPageGeneratingFilter
  DefaultLogoutPageGeneratingFilter
  BasicAuthenticationFilter
  RequestCacheAwareFilter
  SecurityContextHolderAwareRequestFilter
  AnonymousAuthenticationFilter
  ExceptionTranslationFilter
  AuthorizationFilter
]
```
## 사용자 인증하기
스프링 부트 어플리케이션에 스프링 시큐리티가 포함되면 자동 설정에 의해 user라는 사용자가 생성되고 패스워드는 로그로 출력되었습니다.
자동으로 생성된 사용자말고 우리가 직접 사용자를 관리하고자 한다면 스프링 시큐리티가 인증하고자 하는 사용자에 대해 패스워드가 무엇이고 그 사용자가 갖고 있는 권한이 무엇인지 알려주는 UserDetailsService 스프링 빈 객체를 만들면 됩니다.
UserDetailsService 인터페이스에는 loaduserByUsername이라는 메서드가 하나 있으며 파라미터로 스프링 시큐리티가 인증하고자 하는 사용자 아이디가 전달됩니다. 이 메서드에서 우리가 패스워드와 권한 목록을 가진 UserDetails 객체를 만들어 반환하면 됩니다.
아래의 구현을 보면 스프링 시큐리티가 전달한 사용자 아이디(username)에 대해 우리가 관리하는 사용자 리파지토리에서 조회하고 이것을 사용하여 UserDetails 객체를 만들어 반환하는 것을 볼 수 있습니다.
```
@Bean
public UserDetailsService userDetailsService(MemberRepository memberRepository, AuthorityRepository authorityRepository) {
    return new UserDetailsService() {
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Member member = memberRepository.findByEmail(username).orElseThrow();
            List<Authority> authorities = authorityRepository.findByMember(member);
            return new ArticleUserDetails(member, authorities);
        }
    };
}
```
UserDetailsService를 통해 반환된 UserDetails 객체의 패스워드와 로그인 화면에서 입력한 패스워드를 비교하기 위해 PasswordEncoder 인터페이스가 구현된 객체가 필요한데 마찬가지로 아래와 같이 BCryptPasswordEncoder 클래스 객체를 생성하여 등록합니다.
```
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```
참고로 테스트를 위해 BCrypt 암호화한 값이 필요하다면 아래의 사이트를 통해 생성할 수 있습니다.
- https://bcrypt-generator.com/

# 세션 공유
어플리케이션이 여러개 실행되면 앞단의 로드밸런서가 Sticky 설정이 아닌 이상 한쪽 서버로 로그인한 세션 정보가 다른 서버로 공유되지 않아 정상적인 서비스 이용이 불가능합니다.
스프링 어플리케이션은 기본적으로 세션을 메모리에 저장하기 때문이며 이를 해결하기 위해 가장 좋은 방법은 메모리 데이터베이스(예 Redis)에 저장하는 것입니다.
간단하게 해결하는 방법은 JDBC를 이용해서 데이터베이스에 저장하는 것입니다.
아래와 같이 spring-session-jdbc를 의존성으로 포함합니다.
```
	implementation 'org.springframework.session:spring-session-jdbc'
```
의존성을 추가하는 것만으로도 세션은 JDBC를 통해 데이터베이스에 저장되고 관리됩니다.
다만 H2 데이터베이스와 같은 내장 데이터베이스의 경우 세션을 저장하기 위한 스키마를 자동으로 만들지만 외부 데이터베이스는 자동으로 만들지 않기 때문에 다음과 같이 세션 스키마를 생성하도록 설정합니다.
initialize-schema를 설정하면 자동으로 세션 정보를 저장하기 위한 스키마를 만들어 줍니다.
```
# Session
spring.session.jdbc.initialize-schema=always
```


## 외부 데이터베이스에서 사용하기 위해 schema.sql과 data.sql을 h2와 mysql로 분리
- 데이터베이스마다 서로 다른 초기화 스크립트를 사용하기 위해 다음과 같이 설정할 수 있습니다.
```
spring.sql.init.platform=mysql
```
- 그리고 schema-mysql.sql, data-mysql.sql과 같이 schema-{플랫폼 이름}.sql, data-{플랫폼 이름}.sql로 스크립트를 분리할 수 있습니다.
- 외부 데이터베이스의 경우 스키마를 없는 경우에만 생성해야 하므로 아래와 같이 CREATE TABLE IF NOT EXISTS를 사용하도록 수정합니다.
```
CREATE TABLE IF NOT EXISTS member (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    email VARCHAR(256) NOT NULL UNIQUE,
    password VARCHAR(256)
);

CREATE TABLE IF NOT EXISTS authority (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    authority VARCHAR(256),
    member_id INTEGER,
    FOREIGN KEY(member_id) REFERENCES member(id)
);

CREATE TABLE IF NOT EXISTS article (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(256),
    description VARCHAR(4096),
    created DATETIME,
    updated DATETIME,
    member_id INTEGER,
    FOREIGN KEY(member_id) REFERENCES member(id)
);
```
- 초기 데이터의 경우도 외부 MySQL 서버를 사용하므로 관리자 계정만 만들도록 합니다.
```
INSERT IGNORE INTO member(id, name, email, password) VALUES(1, '관리자', 'admin@hanbit.co.kr', '$2a$12$qpOpJ13qb.ROkaYvteEKs.mWhls9lTnGpjLj9h3Gpw0/8Y8r1MR1C');
INSERT IGNORE INTO authority(id, authority, member_id) VALUES(1, 'ROLE_ADMIN', 1);
```

## Health Check
상용 환경에서 어플리케이션을 운영하는 경우 해당 어플리케이션이 정상적으로 실행되고 있는지를 확인하는 경우가 있습니다.
이러한 것을 헬스 체크(Health Check)라고 하며 어플리케이션에서 헬스 체크를 위한 API를 제공하도록 요구하는 경우도 있습니다.
예제에서는 어플리케이션이 프로덕션 프로파일로 실행할 때 /health라는 API를 제공하도록 컨트롤러를 추가합니다.

```
@Profile("health")
@RestController
public class HealthController {

    @GetMapping("/health")
    public Status getHealth() {
        return Status.builder().status("UP").build();
    }

    @Data
    @Builder
    private static class Status {
        public String status;
    }
}
```
어플리케이션이 정상적으로 실행되고 있다면 HTTP 상태 코드 200과 함께 다음과 같이 반환합니다.
http://localhost:8080/health
```
{
  "status": "UP"
}
```
또는 스프링 부트 프로젝트에 actuator 의존성을 추가하면 자동으로 API /actuator/health를 지원합니다.
actuator는 헬스 체크 뿐만 아니라 다양한 기능을 제공하지만 기본적으로 health 관련 end-point만 활성화 합니다.
만약 actuator가 지원하는 다른 기능을 사용하기 위해 다른 end-point도 활성화 한다면 보안에 유의해야 합니다.
```
	implementation 'org.springframework.boot:spring-boot-starter-actuator'
```
이렇게 헬스 체크를 위한 API가 준비되었다면 스프링 시큐리티에서 /actuator/**를 인증 없이 접근할 수 있도록 설정합니다.
예제에서는 헬스 컨트롤러가 제공하는 /health와 actuator가 제공하는 /actuator/health만 제외하도록 설정합니다.
```
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return new WebSecurityCustomizer() {
            @Override
            public void customize(WebSecurity web) {
                web.ignoring().requestMatchers(
                        // new AntPathRequestMatcher("/**"), // 임시로 모든 경로를 풀어서 테스트 할 수 있다.
                        new AntPathRequestMatcher("/h2-console/**"),
                        new AntPathRequestMatcher("/css/**"),
                        new AntPathRequestMatcher("/js/**"),
                        new AntPathRequestMatcher("/image/**"),
                        new AntPathRequestMatcher("/health/**"),
                        new AntPathRequestMatcher("/actuator/**"));
            }
        };
    }
```
스프링시큐리티에서 로그아웃은 CSRF 보안을 위해 기본적으로 POST 방식으로 가능합니다. 따라서 별도의 로그아웃 화면을 사용하지 않고 직접 로그아웃을 하려면 로그아웃 하려는 곳에서 폼을 만들고 POST로 요청해야 합니다.
아래의 예는 Bootstrap의 navbar의 메뉴에 폼을 추가하고 POST 방식의 "로그아웃" 메뉴를 추가한 예입니다.
```html
<li class="nav-item dropdown" sec:authorize="isAuthenticated()">
    <a class="nav-link dropdown-toggle" role="button" data-bs-toggle="dropdown" aria-expanded="false" sec:authentication="principal.displayName">
        윤서준
    </a>
    <ul class="dropdown-menu">
        <li><a class="dropdown-item" th:href="@{/password}">비밀번호 변경</a></li>
        <li><a class="dropdown-item" th:href="@{/logout}">로그아웃</a></li>
        <!-- 확인 없이 바로 로그아웃 수행
        <li>
            <form th:action="@{/logout}" method="post">
                <button type="submit" class="dropdown-item">로그아웃</button>
            </form>
        </li>
        -->
    </ul>
</li>
```