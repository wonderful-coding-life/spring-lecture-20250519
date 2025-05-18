package com.example.demo.service;

import com.example.demo.mapper.MemberMapper;
import com.example.demo.model.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest
@Slf4j
@Sql(scripts = {"classpath:/test-article.sql"})
public class MemberServiceTests {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberMapper memberMapper;

    @Test
    public void unsubscribe() {
        // 윤서준
        int unsubscribed = memberService.unsubscribe(1L);
        log.info("{}", unsubscribed);
    }

    @Test
    public void subscribeBatch() {
        List<Member> members = List.of(
                Member.builder().name("정혁").email("HyeokJung@hanbit.co.kr").age(10).build(),
                // email duplicated
                Member.builder().name("유채린").email("MiyeongKong@hanbit.co.kr").age(23).build(),
                Member.builder().name("로봇").email("Robot@hanbit.co.kr").age(3).build()
        );
        try {
            memberService.subscribeBatch(members);
        } catch (Exception e) {}
        log.info("{}", memberMapper.selectAll());
    }
}
