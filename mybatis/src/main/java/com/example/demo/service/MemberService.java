package com.example.demo.service;

import com.example.demo.mapper.ArticleMapper;
import com.example.demo.mapper.MemberMapper;
import com.example.demo.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper memberMapper;
    private final ArticleMapper articleMapper;

    public int unsubscribe(Long id) {
        Member member = memberMapper.selectById(id).orElseThrow();
        articleMapper.deleteByMemberId(member.getId());
        return memberMapper.deleteById(member.getId());
    }

    @Transactional
    public void subscribeBatch(List<Member> members) {
        for (Member member : members) {
            memberMapper.insert(member);
        }
    }
}
