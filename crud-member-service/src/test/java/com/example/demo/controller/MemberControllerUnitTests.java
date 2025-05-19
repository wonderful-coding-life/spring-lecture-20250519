package com.example.demo.controller;

import com.example.demo.dto.MemberRequest;
import com.example.demo.dto.MemberResponse;
import com.example.demo.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerUnitTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private MemberService memberService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void post() throws Exception {
        List<MemberRequest> memberRequests = List.of(
                MemberRequest.builder()
                        .name("윤서준")
                        .email("Seojun-Yoon@hanbit.co.kr")
                        .age(10).build(),
                MemberRequest.builder()
                        .name("윤광철")
                        .email("Kwangcheol-Yoon@hanbit.co.kr")
                        .age(43).build());
        String userRequestString = objectMapper.writeValueAsString(memberRequests);

        when(memberService.createList(memberRequests)).thenReturn(List.of(
                MemberResponse.builder()
                        .id(1L)
                        .name("윤서준")
                        .email("Seojun-Yoon@hanbit.co.kr")
                        .age(10).build(),
                MemberResponse.builder()
                        .id(2L)
                        .name("윤광철")
                        .email("Kwangcheol-Yoon@hanbit.co.kr")
                        .age(43).build()));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/members/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestString))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("윤서준"))
                .andExpect(jsonPath("$[0].email").value("Seojun-Yoon@hanbit.co.kr"))
                .andExpect(jsonPath("$[0].age").value(10))
                .andExpect(jsonPath("$[1]").exists());
    }
}
