package com.example.demo.service;

import com.example.demo.dto.MemberDto;
import com.example.demo.dto.MemberForm;
import com.example.demo.model.Member;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberDto create(MemberForm memberForm) {
        Member member =  Member.builder()
                .name(memberForm.getName())
                .password(passwordEncoder.encode(memberForm.getPassword()))
                .email(memberForm.getEmail()).build();
        memberRepository.save(member);
        return mapToMemberDto(member);
    }

    public Optional<MemberDto> findByEmail(String email) {
        return memberRepository.findByEmail(email).map(this::mapToMemberDto);
    }

    public boolean checkPassword(Long id, String password) {
        Member member = memberRepository.findById(id).orElseThrow();
        return passwordEncoder.matches(password, member.getPassword());
    }

    public void updatePassword(Long id, String password) {
        Member member = memberRepository.findById(id).orElseThrow();
        member.setPassword(passwordEncoder.encode(password));
        memberRepository.save(member);
    }

    private MemberDto mapToMemberDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }
}
