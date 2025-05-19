package com.example.demo.service;

import com.example.demo.dto.MemberRequest;
import com.example.demo.dto.MemberResponse;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponse create(MemberRequest memberRequest) {
        Member member = Member.builder()
                .name(memberRequest.getName())
                .email(memberRequest.getEmail())
                .age(memberRequest.getAge())
                .enabled(true).build(); // set enabled true for default
        memberRepository.save(member);
        return mapToMemberResponse(member);
    }

    @Transactional
    public List<MemberResponse> createList(List<MemberRequest> memberRequests) {
        List<MemberResponse> memberResponseList = memberRequests.stream().map(this::create).toList();
        return memberResponseList;
    }

    @Transactional(rollbackFor = {Exception.class})
    public List<MemberResponse> createListTestCase1(List<MemberRequest> memberRequests) throws InterruptedException {
        Thread.sleep(1000);
        throw new InterruptedException("interrupted");
    }

    public MemberResponse findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        return mapToMemberResponse(member);
    }

    public List<MemberResponse> findAll() {
        List<MemberResponse> list = new ArrayList<>();
        for (Member member : memberRepository.findAll()) {
            MemberResponse memberResponse = mapToMemberResponse(member);
            list.add(memberResponse);
        }
        return list;
    }

    public List<MemberResponse> findAll(String name) {
        if (name == null || name.isBlank()) {
            return memberRepository
                    .findAll()
                    .stream()
                    .map(this::mapToMemberResponse)
                    .toList();
        } else {
            return memberRepository
                    .findByNameContainingOrderByNameAsc(name)
                    .stream()
                    .map(this::mapToMemberResponse)
                    .toList();
        }
    }

    public MemberResponse update(Long id, MemberRequest memberRequest) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundException::new);
        // password, enabled 필드를 유지해야 하므로 나머지 항목들만 업데이트
        member.setName(memberRequest.getName());
        member.setEmail(memberRequest.getEmail());
        member.setAge(memberRequest.getAge());
        memberRepository.save(member);
        return mapToMemberResponse(member);
    }

    public MemberResponse patch(Long id, MemberRequest memberRequest) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundException::new);
        // 전달된 값이 있는 필드만 업데이트
        if (memberRequest.getName() != null) member.setName(memberRequest.getName());
        if (memberRequest.getEmail() != null) member.setEmail(memberRequest.getEmail());
        if (memberRequest.getAge() != null) member.setAge(memberRequest.getAge());
        memberRepository.save(member);
        return mapToMemberResponse(member);
    }

    public void deleteById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundException::new);
        memberRepository.delete(member);
    }

    private MemberResponse mapToMemberResponse(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .age(member.getAge())
                .build();
    }
}
