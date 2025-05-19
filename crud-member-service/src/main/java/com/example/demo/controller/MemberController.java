package com.example.demo.controller;

import com.example.demo.dto.MemberRequest;
import com.example.demo.dto.MemberResponse;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 한명의 사용자를 생성
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse post(@RequestBody MemberRequest memberRequest) {
        return memberService.create(memberRequest);
    }

    // 여러명의 사용자를 생성
    @PostMapping("/batch")
    public List<MemberResponse> postBatch(@RequestBody List<MemberRequest> memberRequests) {
        return memberService.createList(memberRequests);
    }

    // 전체 사용자 목록 조회
//    @GetMapping
//    public List<UserResponse> getAll() {
//        return userService.findAll();
//    }

    // 이름에 매개변수로 전달된 스트링이 포함된 사용자 조회
    @GetMapping
    public List<MemberResponse> getAll(@RequestParam(name= "name", required=false) String name) {
        return memberService.findAll(name);
    }

    @GetMapping("/{id}")
    public MemberResponse get(@PathVariable("id") Long id) {
        return memberService.findById(id);
    }

    @PutMapping("/{id}")
    public MemberResponse put(@PathVariable("id") Long id, @RequestBody MemberRequest memberRequest) {
        return memberService.update(id, memberRequest);
    }

    @PatchMapping("/{id}")
    public MemberResponse patch(@PathVariable("id") Long id, @RequestBody MemberRequest memberRequest) {
        return memberService.patch(id, memberRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        memberService.deleteById(id);
    }
}
