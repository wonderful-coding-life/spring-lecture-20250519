package com.example.demo.repository;

import com.example.demo.model.Member;
import com.example.demo.model.MemberStats;
import com.example.demo.model.MemberStatsNative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m WHERE m.name = :name")
    List<Member> findMember(@Param("name") String name);

    @Query("SELECT m FROM Member m WHERE m.name = :name AND m.email = :email")
    List<Member> findMember(@Param("name") String name, @Param("email") String email);

    @Query("""
            SELECT m.name, m.email, COUNT(a.id) as count
                FROM Member m LEFT JOIN Article a ON a.member = m
                GROUP BY m ORDER BY count DESC
    """)
    List<Object[]> getMemberStatsObject();

    @Query("""
        SELECT new com.example.demo.model.MemberStats(m.name, m.email, COUNT(a.id) as count)
            FROM Member m LEFT JOIN Article a ON a.member = m
            GROUP BY m ORDER BY count DESC
    """)
    List<MemberStats> getMemberStats();

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.age = :age")
    int setMemberAge(Integer age);

    @Query(value="""
        SELECT m.name, m.email, count(a.id) AS count
            FROM member m LEFT JOIN article a ON m.id = a.member_id
            GROUP BY m.id ORDER BY count DESC
    """, nativeQuery = true)
    List<Object[]> getMemberStatsNativeObjects();

    @Query(value="""
        SELECT m.name, m.email, count(a.id) AS count
            FROM member m LEFT JOIN article a ON m.id = a.member_id
            GROUP BY m.id ORDER BY count DESC
    """, nativeQuery = true)
    List<MemberStatsNative> getMemberStatsNative();
}
