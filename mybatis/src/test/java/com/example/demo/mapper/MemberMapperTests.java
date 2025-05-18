package com.example.demo.mapper;

import com.example.demo.model.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest
@Slf4j
@Sql(scripts = {"classpath:/test-member.sql"})
public class MemberMapperTests {

    @Autowired
    private MemberMapper memberMapper;

    @Test
    public void selectAll() {
        List<Member> members = memberMapper.selectAll();
        log.info("selectAll size {}", members.size());
        for (Member member : members) {
            log.info("{} ", member);
        }
    }

    @Test
    public void selectBy() {
        // 윤서준
        Member member = memberMapper.selectById(1L).orElseThrow();
        log.info("selectById {}", member);

        member = memberMapper.selectByEmail("MiyeongKong@hanbit.co.kr").orElseThrow();
        log.info("selectByEmail {}", member);
    }

    @Test
    public void selectAllOrderByAgeAsc() {
        List<Member> members = memberMapper.selectAllOrderByAgeAsc();
        log.info("selectAllOrderByAgeAsc size {}", members.size());
        for (Member member : members) {
            log.info("{}", member);
        }
    }

    @Test
    public void selectAllOrderBy() {
        //List<Member> members = memberMapper.selectAllOrderBy("age", "desc; DELETE FROM article; DELETE FROM member;--");
        List<Member> members = memberMapper.selectAllOrderBy("age", "desc");
        log.info("selectAllOrderBy size {}", members.size());
        for (Member member : members) {
            log.info("{}", member);
        }
        //log.info("selectAllCount returns {}", memberMapper.selectAllCount());
    }

    @Test
    public void selectByNameLike() {
        List<Member> members = memberMapper.selectByNameLike("%윤%");
        log.info("selectByNameLike size {}", members.size());
        for (Member member : members) {
            log.info("{}", member);
        }
    }

    @Test
    public void insert() {
        Member member = Member.builder()
                .name("정혁")
                .email("HyeokJung@hanbit.co.kr")
                .age(10).build();
        int insertedCount = memberMapper.insert(member);
        log.info("insert {}", insertedCount);
        log.info("inserted {}", member);
    }

    @Test
    public void update() {
        Member member = memberMapper.selectByEmail("SeojunYoon@hanbit.co.kr").orElseThrow();
        member.setAge(21);
        int updatedCount = memberMapper.update(member);
        log.info("updatedCount {}", updatedCount);
        member = memberMapper.selectByEmail("SeojunYoon@hanbit.co.kr").orElseThrow();
        log.info("updated {}", member);
    }

    @Test
    public void delete() {

        // 윤서준
        memberMapper.deleteById(1L);
        log.info("count after delete {}", memberMapper.selectAllCount());

        Member member = memberMapper.selectByEmail("MiyeongKong@hanbit.co.kr").orElseThrow();
        memberMapper.delete(member);
        log.info("count after delete {}", memberMapper.selectAllCount());

        memberMapper.deleteAll();
        log.info("count after delete {}", memberMapper.selectAllCount());
    }
}
