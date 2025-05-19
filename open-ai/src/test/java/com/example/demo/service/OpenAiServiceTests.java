package com.example.demo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OpenAiServiceTests {
    @Autowired
    private OpenAiService openAiService;

    @Test
    public void testChatModelSimpleAsync() {
        openAiService.answer("신입 개발자를 채용하기 위해 AI 관련 질문지를 만들어 주세요.");
        System.out.println("called asynchronously");
    }
}
