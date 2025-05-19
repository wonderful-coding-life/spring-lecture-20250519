package com.example.demo.controller;

import com.example.demo.dto.ArticleRequest;
import com.example.demo.dto.ArticleResponse;
import com.example.demo.dto.MemberRequest;
import com.example.demo.dto.MemberResponse;
import com.example.demo.service.ArticleService;
import com.example.demo.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final ArticleService articleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse post(@RequestBody MemberRequest memberRequest) {
        return memberService.create(memberRequest);
    }

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

    @PostMapping("/{id}/articles")
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleResponse postArticle(@PathVariable("id") Long id, @RequestBody ArticleRequest articleRequest) {
        return articleService.create(id, articleRequest);
    }

    @GetMapping("/{id}/articles")
    public void getArticle(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 포워드를 하면 서버 내부에서 새로운 URI에 대한 처리를 하고 결과를 전달한다. 클라이언트 입장에서는 URL 변화도 없고 이러한 내용을 알 수 없다.
        request.getSession().getServletContext().getRequestDispatcher("/api/articles?memberId=" + id).forward(request, response);
    }

    @GetMapping("/{id}/articles/duplicated")
    public List<ArticleResponse> getArticleDuplicated(@PathVariable("id") Long id) {
        // 아래와 같이 리턴해도 된다. 하지만 비즈니스 로직을 한군데서 관리하려면 forward 하는 것이 바람직함
        return articleService.findByMemberId(id);
    }

    @GetMapping("/{id}/articles/redirect")
    public void getArticleRedirect(@PathVariable("id") Long id, HttpServletResponse response) throws ServletException, IOException {
        // 리다이렉트를 하면 클라이언트에게 새로운 URL이 전달되어 클라이언트가 다시 요청을 하게 된다.
        // 이 경우 클라이언트와 서버간 통신이 두번 일어나게 되고 만약 필터가 있다면 필터 역시 두번씩 거치는 등 리소스 낭비가 발생한다.
        response.sendRedirect("/api/articles?memberId=" + id);
    }
}
