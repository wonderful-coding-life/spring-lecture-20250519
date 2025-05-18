package com.example.demo.repository;

import com.example.demo.model.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Direction.DESC;

@SpringBootTest
@Slf4j
public class MemberRepositoryTests {
    @Autowired
    private MemberRepository memberRepository;
    private Member seojun;

    @BeforeEach
    public void doBeforeEach() {
        seojun = Member.builder().name("윤서준").email("SeojunYoon@hanbit.co.kr").age(10).build();
        memberRepository.save(seojun);
        memberRepository.save(Member.builder().name("윤광철").email("KwangcheolYoon@hanbit.co.kr").age(43).build());
        memberRepository.save(Member.builder().name("공미영").email("MiyeongKong@hanbit.co.kr").age(26).build());
        memberRepository.save(Member.builder().name("김도윤").email("DoyunKim@hanbit.co.kr").age(10).build());
    }

    @AfterEach
    public void doAfterEach() {
        memberRepository.deleteAll();
    }

    @Test
    public void findAll() {
        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(4);
    }

    @Test
    public void findById() {
        var member = memberRepository.findById(seojun.getId());
        log.info("{}", member);
    }

    @Test
    public void findByName() {
        log.info("#1 {}", memberRepository.findByName("윤서준"));
        log.info("#2 {}", memberRepository.findByNameIs("윤광철"));
        log.info("#3 {}", memberRepository.findByNameEquals("공미영"));
        log.info("#4 {}", memberRepository.getByName("김도윤"));
        log.info("#5 {}", memberRepository.findByNameAndEmail("윤서준", "SeojunYoon@hanbit.co.kr"));
        log.info("#6 {}", memberRepository.findByNameOrEmail("윤서준", "KwangcheolYoon@hanbit.co.kr"));
        log.info("#7 {}", memberRepository.findByNameStartingWith("공"));
        log.info("#8 {}", memberRepository.findByNameEndingWith("철"));
        log.info("#9 {}", memberRepository.findByNameContaining("윤"));
        log.info("#10 {}", memberRepository.findByNameLike("%윤%"));
    }

    @Test
    public void findByAge() {
        log.info("#1 {}", memberRepository.findByAgeIsNull());
        log.info("#2 {}", memberRepository.findByAgeIsNotNull());
        log.info("#3 {}", memberRepository.findByAgeGreaterThan(10));
        log.info("#4 {}", memberRepository.findByAgeAfter(10));
        log.info("#5 {}", memberRepository.findByAgeGreaterThanEqual(10));
        log.info("#6 {}", memberRepository.findByAgeLessThan(10));
        log.info("#7 {}", memberRepository.findByAgeBefore(10));
        log.info("#8 {}", memberRepository.findByAgeLessThanEqual(10));
        log.info("#9 {}", memberRepository.findByAgeBetween(10, 23));
    }

    @Test
    public void findByOrderBy() {
        log.info("#1 {}", memberRepository.findByOrderByNameAsc());
        log.info("#2 {}", memberRepository.findByOrderByNameDesc());
        log.info("#3 {}", memberRepository.findByOrderByNameAscAgeDesc());
        Sort sort = Sort.by(Sort.Order.asc("name"), Sort.Order.desc("age"));
        log.info("#4 {}", memberRepository.findByNameContaining("윤", sort));
        Pageable pageable = PageRequest.of(0, 2, Sort.by(DESC, "name"));
        Page<Member> page = memberRepository.findByNameContaining("윤", pageable);
        log.info("#5 {} {}", page, page.toList());
        log.info("#6 {}", memberRepository.findByNameContainingOrderByNameAsc("윤"));
    }

    @Test
    public void findTopBy() {
        log.info("#1 {}", memberRepository.findFirstByOrderByAgeAsc());
        log.info("#2 {}", memberRepository.findFirst2ByOrderByAgeAsc());
        log.info("#3 {}", memberRepository.findTopByOrderByAgeAsc());
        log.info("#4 {}", memberRepository.findTop2ByOrderByAgeAsc());
    }

    // Example 검색은 전달된 객체 프로퍼티 중에 null 이 아닌 값만 검색 조건으로 사용하며, ExampleMatcher 를 통해 검색 조건을 설정할 수 있다.
    // ExampleMatcher 는 검색 조건을 설정하는 클래스로, matchingAll() 은 모든 조건을 만족하는 데이터를 검색하고, matchingAny() 는 하나라도 조건을 만족하는 데이터를 검색한다.
    // Example 을 사용하면 동적 쿼리를 작성할 수 있으며, 검색 조건이 많아지더라도 코드가 복잡해지지 않는 장점이 있다.

    @Test
    public void findAllByExampleMatchingAll() {
        Example<Member> example = Example.of(
                Member.builder().name("윤서준").age(10).build(),
                ExampleMatcher.matchingAll());
        List<Member> members = memberRepository.findAll(example);
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    public void findAllByExampleMatchingAny() {
        Example<Member> example = Example.of(
                Member.builder().name("윤서준").age(10).build(),
                ExampleMatcher.matchingAny());
        List<Member> members = memberRepository.findAll(example);
        assertThat(members.size()).isEqualTo(2);
    }

    @Test
    public void findAllByPage() {
        // 정렬 방법 여러개 설정시 Sort sort = Sort.by(Sort.Order.asc("name"), Sort.Order.desc("age"));
        Sort sort  = Sort.by(Sort.Direction.ASC, "name");
        Pageable pageable = PageRequest.of(0, 2, sort);
        Page<Member> page = memberRepository.findAll(pageable);
        log.info("{}", page);
        log.info("total elements={} number={} totalPages={}",
                page.getTotalElements(),
                page.getNumber(), // current page number starting from 0
                page.getTotalPages()
        );
        for (Member member : page.toList()) {
            log.info("{}", member);
        }
    }

    @Test
    public void update() {
        Member member = memberRepository.findById(seojun.getId()).orElseThrow();
        member.setAge(11);
        memberRepository.save(member);
        log.info("{}", memberRepository.findAll());
    }

    @Test
    public void delete() {
        Member member = memberRepository.findById(seojun.getId()).orElseThrow();
        memberRepository.delete(member);
        memberRepository.deleteById(seojun.getId());
        log.info("{}", memberRepository.findAll());
    }

    @Test
    @Transactional
    public void deleteBy() {
        log.info("#1 {}", memberRepository.deleteByName("윤서준"));
        log.info("#2 {}", memberRepository.deleteByEmail("KwangcheolYoon@hanbit.co.kr"));
        log.info("#3 {}", memberRepository.count());
    }
}
