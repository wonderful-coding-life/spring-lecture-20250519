package com.example.demo;

import com.example.demo.model.Article;
import com.example.demo.model.Member;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class SpringJdbcApplication implements ApplicationRunner {
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        testMember();
        testArticle();
    }

    public void testMember() {
        memberRepository.save(Member.builder()
                .name("정수빈")
                .email("SubinJung@hanbit.co.kr")
                .age(10).build());
        memberRepository.save(Member.builder()
                .name("윤지웅")
                .email("JieungYoon@hanbit.co.kr")
                .age(10).build());
        var members = memberRepository.findAll();
        log.info("{}", members);

        long count = memberRepository.count();
        log.info("count = {}", count);

        Member member = memberRepository.findByName("윤서준").getFirst();
        log.info("member = {}", member);

        member = memberRepository.findByNameOrEmail("윤서준", "SubinJung@hanbit.co.kr").getFirst();
        log.info("member = {}", member);

        member = memberRepository.findById(2L).orElse(null);
        log.info("member = {}", member);

        members = memberRepository.findTeenAge();
        log.info("members = {}", members);

        members = memberRepository.findByAgeRange(10, 15);
        log.info("members = {}", members);

        // update
        member.setAge(11);
        log.info("update {}", memberRepository.save(member));
        log.info("updated member = {}", memberRepository.findById(member.getId()));

        // delete
        memberRepository.deleteById(member.getId());
        log.info("deleted member = {}", memberRepository.findById(member.getId()).orElse(null));
    }

    public void testArticle() {
        var member = memberRepository.findByName("윤서준").getFirst();
        var article = Article.builder()
                .title("서울의 숨겨진 보석, 북촌 한옥마을")
                .description("북촌 한옥마을은 서울의 전통적인 한옥이 밀집된 지역으로, 한국의 전통문화를 체험할 수 있는 최고의 장소 중 하나입니다. 좁은 골목길을 따라 걸으며 한옥의 아름다움을 감상하고, 전통 찻집에서 차를 마시며 여유를 즐길 수 있습니다.")
                .created(new Date())
                .updated(new Date())
                .memberId(member.getId())
                .build();
        articleRepository.save(article);
        log.info("saved {}", article);

        var articles = articleRepository.findByMemberId(member.getId());
        log.info("articles {}", articles);

        articles = articleRepository.findByTitleContaining("북촌");
        log.info("articles {}", articles);
    }
}
