
## Member Entity Mapping
## Article Entity Mapping - @ManyToOne

## Aggregation queries
MemberRepository -> getMemberStats...
다양한 방식으로 조인을 하거나 또는 GROUP BY와 같은 쿼리를 수행하면 그 결과는 엔티티 객체가 아니기 때문에 일반 모델 객체로 변환하여 가져와야 합니다.
예를 들어 회원(Member)가 작성한 글의 개수를 알고 싶다면 이름과 이메일 그리고 작성한 글의 개수를 프로퍼티로 갖는 객체를 작성합니다.
이 객체는 테이블과 매핑되는 엔티티가 아니므로 엔티티 선언을 하지는 않습니다.
```java
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class MemberStats {
  private String name;
  private String email;
  private Long count;
  }
```
JPQL로 작성하는 방법은 SELECT로 new 객체생성자()를 호출하는 방식으로 수행할 수 있으며 이를 위해 모든 프로퍼티를 사용하여 객체를 생성하는 생성자가 필요합니다.
```java
    @Query("SELECT new com.example.demo.model.MemberStats(m.name, m.email, COUNT(a.id) as count) FROM Member m LEFT JOIN Article a ON a.member = m GROUP BY m ORDER BY count DESC")
    List<MemberStats> getMemberStats();
```
Native 쿼리를 사용하는 방법은 객체 대신 인터페이스를 작성해야 합니다.
```java
public interface MemberStatsNative {
String getName();
String getEmail();
Long getCount();
}
```

Native 쿼리를 사용하여 아래와 같이 작성할 수 있습니다.
```java
@Query(value="SELECT m.name, m.email, count(a.id) AS count FROM member m LEFT JOIN article a ON m.id = a.member_id GROUP BY m.id ORDER BY count DESC", nativeQuery = true)
List<MemberStatsNative> getMemberStatsNative();
```
