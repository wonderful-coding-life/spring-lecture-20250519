package com.example.demo.service;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class OpenAiService {
    @Autowired
    private OpenAiChatModel chatModel;

    @Async
    public void answer(String message) {
        String result = chatModel.call(message);
        System.out.println("result = " + result);
    }
}
