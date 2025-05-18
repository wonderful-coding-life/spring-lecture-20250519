# Spring Boot AoP
## Add dependency at build.gradle
- 스프링부트 스타터 AOP는 스프링 이니셜라이저를 통해 추가할 수 없으므로 직접 build.gradle에 추가
- mvnrepository에서 검색하여 추가
- implementation 'org.springframework.boot:spring-boot-starter-aop'
## Define annotation PrintExecutionTime.java
  - new Annotation
  - @Target(ElementType.METHOD)
  - @Retention(RetentionPolicy.RUNTIME)
## Implement advice PrintExecutionTimeAspect.java
  - @Around("@annotation(PrintExecutionTime)") - 조인포인트 앞뒤로 어드바이스 구현
  - @Before("@annotation(PrintExecutionTime)") - 조인포인트 앞에 어드바이스 구현
  - @After("@annotation(PrintExecutionTime)") - 조인포인트 뒤에 어드바이스 구현
  - @AfterReturning(pointcut = "@annotation(PrintExecutionTime)", returning="result") - 조인포인트가 반환된 이후에 어드바이스 구현
  - @AfterThrowing(pointcut = "@annotation(PrintExecutionTime)", throwing="ex") - 조인포인트에서 예외 발생한 후에 로직 구현
## Use @PrintExecutionTime on methods
  - Pi.java - Calculate PI using Monte Carlo method
  - PiApplication.java - Test AOP

## Test results
  - AMD Ryzen™ 5 7600
```
executed execution(Pi.calculate(..)) with 1 args in 1ms.
PI with 10,000 points = 3.1316
executed execution(Pi.calculate(..)) with 1 args in 5ms.
PI with 100,000 points = 3.14288
executed execution(Pi.calculate(..)) with 1 args in 20ms.
PI with 1,000,000 points = 3.141668
executed execution(Pi.calculate(..)) with 1 args in 206ms.
PI with 10,000,000 points = 3.1414464
executed execution(Pi.calculate(..)) with 1 args in 2072ms.
PI with 100,000,000 points = 3.14173068
```
  - Intel 13700K
```
executed execution(Pi.calculate(..)) with 1 args in 1ms.
PI with 10,000 points = 3.1564
executed execution(Pi.calculate(..)) with 1 args in 5ms.
PI with 100,000 points = 3.13832
executed execution(Pi.calculate(..)) with 1 args in 32ms.
PI with 1,000,000 points = 3.143264
executed execution(Pi.calculate(..)) with 1 args in 327ms.
PI with 10,000,000 points = 3.1415308
executed execution(Pi.calculate(..)) with 1 args in 3289ms.
PI with 100,000,000 points = 3.14134412
```