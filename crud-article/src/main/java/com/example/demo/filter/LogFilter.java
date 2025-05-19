package com.example.demo.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@WebFilter(urlPatterns = "/api/*")
@Slf4j
public class LogFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        // 요청이 들어 올 때 해야 할 작업
        long start = System.currentTimeMillis();
        log.info("LogFilter.doFilter() begins for {}", httpServletRequest.getRequestURI());

        // 뭔가 문제가 발견되어 더 이상의 진행을 하지 않고 여기서 바로 클라이언트의 요청을 리턴한다면
        // httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        // return;

        // 다음 필터 처리
        filterChain.doFilter(servletRequest, servletResponse);

        // 요청을 처리하고 나갈 때 해야 할 작업
        long timeElapsed = System.currentTimeMillis() - start;
        log.info("LogFilter.doFilter() returns for {} status {} in {} ms", httpServletRequest.getRequestURI(), httpServletResponse.getStatus(), timeElapsed);
    }
}
