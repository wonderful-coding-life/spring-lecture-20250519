package com.example.demo.repository;

import com.example.demo.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
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
}
