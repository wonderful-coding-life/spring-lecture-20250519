package com.example.demo.filter;

import com.example.demo.model.AccessKey;
import com.example.demo.repository.AccessKeyRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//@WebFilter(urlPatterns = {"/api/*", "/admin/*"})
@RequiredArgsConstructor
public class AccessKeyFilterWithRepository extends OncePerRequestFilter {
    private final AccessKeyRepository accessKeyRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer")) {
            String token = authorization.replace("Bearer", "").trim();
            AccessKey apiKey = accessKeyRepository.findById(token).orElse(null);
            if (apiKey != null && apiKey.getEnabled()) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
