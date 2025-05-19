# 사전 준비
- OpenAPI API 호출을 위한 [API keys 생성](https://platform.openai.com/api-keys)

# 토큰이란?
더 이상 나눌 수 없는 가장 작은 단위의 단어. 예를 들어 "예쁜 꽃"은 "예쁜" + "꽃" 두개의 토큰입니다. "예쁜"은 "예", "쁜"으로 나누게 되면 의미가 없어지기 때문에 "예쁜"이 토큰이 됩니다.
하지만 영어는 정확하게 토큰으로 나눌 수 있으나 실제 한글은 토큰으로 나누는 방식이 아쉬운데요, 그래서 어쩔수 없이 한글로 질문하고 답변을 받으면 토큰의 소모가 큽니다.
실제로 문장이 어떻게 토큰으로 구성되는지 https://platform.openai.com/tokenizer 에서 토큰을 테스트 해 볼 수 있습니다.
현재 GPT-4o에서 인식할 수 있는 최대 토큰은 128k으로 충분하나, 더 긴 문장이 필요하면 랭체인을 사용하면 됩니다.

# 프로젝트 생성
- Open AI 의존성 추가
- Spring Web
- Lombok

# 스프링 문서
- [OpenAI](https://docs.spring.io/spring-ai/reference/api/chat/openai-chat.html)

# 어플리케이션 설정
```
ai.openai.api-key=xxx
ai.openai.chat.options.model=gpt-40
ai.openai.chat.options.temperature=0.6
```
- spring.ai.retry.on-client-errors : If false, throw a NonTransientAiException, and do not attempt retry for 4xx client error codes (Default false)
- spring.ai.openai.api-key : The API Key
- spring.ai.openai.chat.options.model : Name of the OpenAI Chat model to use. You can select between models such as: gpt-4o, gpt-4o-mini, gpt-4-turbo, gpt-3.5-turbo …​ See the models page for more information.
- spring.ai.openai.chat.options.temperature : The sampling temperature to use that controls the apparent creativity of generated completions. Higher values will make output more random while lower values will make results more focused and deterministic. It is not recommended to modify temperature and top_p for the same completions request as the interaction of these two settings is difficult to predict. 온도는 0에서 2사이로 설정할 수 있으며 0은 정확한 사실만으로 답변을 생성하고, 숫자가 클 수록 기존의 지식을 바탕으로 새로운 답변을 창의적으로 생성합니다. 온도가 높으면 상대적으로 응답 속도가 느리며 따라서 창의력을 높이더라도 1.4 이내로 설정하는 것이 좋습니다.

# RestController 작성
```
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatApiController {
    private final OpenAiChatModel chatModel;

    @GetMapping
    public Map get(@RequestParam("message") String message) {
        return Map.of("generation", chatModel.call(message));
    }
}
```
# 메시지 종류
- 시스템 메시지 - ChatGPT에게 역할을 부여하거나 예를 들어 "초등학생 선생님처럼 아이들에게 이해하기 쉽도록 답변해 주세요."등을 할 수 있다. 시스템 메시지를 사용자 메시지에 적절히 섞어도 답변을 받을 수 있으나, 별도로 분리하는 것이 일반적이다.

# AI 모델에서 임베딩(Embedding)이란?
임베딩(Embedding)은 텍스트를 수치 벡터(Vector)로 변환하는 기술입니다. 즉, 단어, 문장, 문서를 수학적으로 표현하여 AI가 이해하고 비교할 수 있도록 합니다.
AI 모델인 텍스트 자체를 직접 이해하지 못하고, 숫자로 변환된 데이터(벡터)를 사용하여 의미를 분석합니다.
OpenAI에서는 text-embedding-ada-002를 사용하며 이는 텍스트를 1536 차원의 벡터로 변환합니다. 따라서 의미적으로 비슷한 두 문장은 임베딩을 통해 생성한 벡터가 비슷하게 생성됩니다.
따라서 임베딩을 활용하면 의미적으로 유사한 텍스트를 검색하거나 추천할 수 있습니다.

# 벡터데이터베이스
- Spring AI에서 지원하는 벡터데이터베이스는 아래의 링크 참조
- https://docs.spring.io/spring-ai/reference/api/vectordbs/pgvector.html
- 지원하는 벡터 데이터베이스를 스프링 이니셜라이저에서 선택하여 의존성 추가
- MariaDB 11.7 이상에서 지원한다고 되어 있고 문서에도 부트 스타터까지 언급이 되어 있는데 실제로는 없다(?)

# Postgres Vector
```
docker pull pgvector/pgvector
docker run -it --rm --name postgres -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres pgvector/pgvector

docker pull pgvector/pgvector:0.8.0-pg17
docker run -it --rm --name postgres -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres pgvector/pgvector:0.8.0-pg17
docker run -d --name postgres -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres pgvector/pgvector:0.8.0-pg17
```

어플리케이션 설정
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=postgres
#spring.ai.vectorstore.pgvector.index-type=HNSW
#spring.ai.vectorstore.pgvector.distance-type=COSINE_DISTANCE
#spring.ai.vectorstore.pgvector.dimensions=1536
spring.ai.vectorstore.pgvector.initialize-schema=true
```
* index-type(디폴트 HNSW): 인덱스 타입으로 설정한 HNSW(Hierarchical Navigable Small World)는 근사 최근접 이웃 검색(Approximate Nearest Neighbor Search, ANN)을 수행하기 위한 알고리즘 중의 하나로 특히 고차원 데이터에서 빠르고 효율적인 검색을 가능하게 하는 인덱싱 기법으로 벡터 데이터베이스에서 매우 널리 사용되고 있습니다.
* distance-type(디폴트 COSINE_DISTANCE): 디스턴스(거리)란 두 벡터가 얼마나 유사한지를 나타내는 지표로 COSINE_DISTANCE는 두 벡터가 비슷할수록 코사인 거리는 0에 가까워지고, 반대로 두 벡터가 매우 다를 경우 1에 가까운 값을 가집니다.
* dimensions(디폴트 Embedding Model에서 가져온다. 달라지면 다시 테이블을 생성해야 한다.): 벡터의 차원을 설정합니다. 이 값은 임베딩 모델에 따라 다르며, text-embedding-ada-002는 1536이고, 최근 text-embedding-3-large는 3072입니다.
```java
        // delete
        List<String> ids = results.stream().map(Document::getId).toList();
        vectorStore.delete(ids);
```
```sql
        // delete by sql
        select * from vector_store where metadata ->> 'article' = 'ai';
        delete from vector_store where metadata ->> 'article' = 'ai';
```

PGVector에서 임베딩을 저장할 테이블을 자동으로 생성하도록 설정 가능
application.properties
```
spring.ai.vectorstore.pgvector.initialize-schema=true
```
또는 다음과 같이 직접 작성한다. 벡터 차원은 임베딩 모델에 따라 다르며 text-embedding-ada-002는 1536이고, 최근 text-embedding-3-large는 3072입니다. 
```sql
CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS vector_store (
	id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
	content text,
	metadata json,
	embedding vector(1536) // 1536 is the default embedding dimension
);

CREATE INDEX ON vector_store USING HNSW (embedding vector_cosine_ops);

-- Sueruser 권한이 있어야 한다.
-- 특정 사용자에게 있는 권한 체크
SELECT * FROM pg_roles WHERE rolname = 'tutor';

-- 특정 사용자에게 슈퍼유저 권한 부여
ALTER ROLE tutor WITH SUPERUSER;

-- Superuser 권한이 있는 사용자 목록 확인
SELECT rolname FROM pg_roles WHERE rolsuper = true;

-- 사용자 비밀번호 변경
ALTER USER postgres WITH PASSWORD 'password1234.';
ALTER USER postgres_user WITH PASSWORD 'password1234.';
```

# 스프링에서 비동기 처리
@Async를 사용하면 됨. 다만 기본적으로 비동기 처리가 활성화 되어 있지 않기 때문에 @EnableAync를 메인에 선언해 주어야 함.

# 스프링에서 스케쥴링 처리
스프링에서 스케쥴링을 사용하면 애플리케이션 내에서 주기적으로 실행되는 작업을 간단하게 구성할 수 있습니다.
먼저, 스케줄링 기능을 활성화하려면 애플리케이션 설정 클래스에 @EnableScheduling 애너테이션을 추가해야 합니다.
스케줄링 작업에는 아래와 같은 옵션을 사용할 수 있습니다.

fixedRate
이전 작업의 실행 시간에 관계없이 일정 간격으로 작업을 실행합니다.
예: @Scheduled(fixedRate = 1000)
fixedDelay
이전 작업이 완료된 후 일정 간격으로 작업을 실행합니다.
예: @Scheduled(fixedDelay = 2000)
cron
Cron 표현식을 사용하여 특정 시간에 작업을 실행합니다.
예: @Scheduled(cron = "0 0 * * * *") (매 정각 실행)

4. Cron 표현식 예시
   표현식	설명
   0 0 * * * *	매 정각 실행
   0 */5 * * * *	매 5분마다 실행
   0 0 12 * * *	매일 12시(정오) 실행
   0 0 12 * * MON	매주 월요일 12시에 실행
   0 0 9-18 * * *	9시부터 18시까지 매 정각

스케줄러 스레드 풀 크기 조정
기본적으로 스프링의 스케줄러는 단일 스레드로 동작합니다. 다중 스레드가 필요하면 TaskScheduler를 커스터마이징해야 합니다.

@Configuration
public class SchedulerConfig {
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(20); // 스레드 풀 크기 설정
        scheduler.setThreadNamePrefix("TaskScheduler-");
        return scheduler;
    }
}

# MariaDB Vector Store
- MariaDB에서도 11.7 버전부터 Vector 기능을 지원하기 시작하였다.
- 2025-02-16 테스트
  - PGVector 대신 MariaDB를 사용하기 위해 아래와 같이 의존성을 추가하여 테스트 함
    implementation 'org.springframework.ai:spring-ai-mariadb-store-spring-boot-starter'
  - 현재 1.0.0(M6)에서는 MariaDB를 사용하면 JDBC Driver로 org.mariadb.jdbc:mariadb-java-client 3.4.1이 사용되는데 이 드라이버는 Driver.enquoteIdentifier 메서드가 버그로 빠져있어 오류가 발생한다.
    강제로 3.4.1 대신에 3.5.2을 사용하기 위해 dependencyManagement 블럭에서 강제로 3.5.2 버전을 지정해 주어야 한다.
  - 임베딩 테스트 정상

  https://mariadb.org/projects/mariadb-vector/

# 참고 자료
엔지오브투머로우 대본 PDF
https://indiegroundfilms.wordpress.com/wp-content/uploads/2014/01/all-you-need-is-kill-apr1-10-1st.pdf
연쇄살인법 소설 TEXT
https://www.short-story.me/stories/horror-stories/710-serial-killer
