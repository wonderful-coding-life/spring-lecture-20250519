package com.example.demo.repository;

import com.example.demo.model.Member;
import com.example.demo.model.MemberStats;
import com.example.demo.model.MemberStatsNative;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest
@Slf4j
@Sql(scripts = {"classpath:/test-article.sql"})
//@Transactional
public class MemberRepositoryTests {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void findMember() {
        var members = memberRepository.findMember("윤서준");
        for (Member member : members) {
            log.info("{}", member);
        }

        members = memberRepository.findMember("윤서준", "Seo");
        for (Member member : members) {
            log.info("{}", member);
        }
    }

    @Test
    public void getMemberStatsObjects() {
        List<Object[]> memberStatsList = memberRepository.getMemberStatsObject();
        for (Object[] ob : memberStatsList){
            String name = (String)ob[0];
            String email = (String)ob[1];
            Long count = (Long)ob[2];
            log.info("{} {} {}", name, email, count);
        }
    }

    @Test
    public void getMemberStats() {
        List<MemberStats> memberStatsList = memberRepository.getMemberStats();
        for (MemberStats memberStats : memberStatsList) {
            log.info("{}", memberStats);
        }
    }

    @Test
    public void setMemberAge() {
        int count = memberRepository.setMemberAge(20);
        log.info("count: {}", count);
    }

    @Test
    public void getMemberStatsNativeObject() {
        List<Object[]> memberStatsList = memberRepository.getMemberStatsNativeObjects();
        for (Object[] ob : memberStatsList){
            String name = (String)ob[0];
            String email = (String)ob[1];
            Long count = (Long)ob[2];
            log.info("{} {} {}", name, email, count);
        }
    }

    @Test
    public void getMemberStatsNative() {
        List<MemberStatsNative> memberStatsList = memberRepository.getMemberStatsNative();
        for (MemberStatsNative memberStats : memberStatsList) {
            log.info("{} {} {}", memberStats.getName(), memberStats.getEmail(), memberStats.getCount());
        }
    }
}
