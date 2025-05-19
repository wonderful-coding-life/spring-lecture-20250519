package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.ai.audio.transcription.AudioTranscriptionOptions;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@SpringBootTest
public class OpenAiTranscriptionModelTests {

    @Autowired
    private OpenAiAudioTranscriptionModel transcriptionModel;

    @Test
    public void testTranscriptModel() {
        Resource resource = new ClassPathResource("/sample_audio.mp3");
        String result = transcriptionModel.call(resource);
        System.out.println("result = " + result);
        // result = 안녕하세요. 스프링부트와 오픈AI 연동을 위한 강의입니다.
    }

    @Test
    public void testTranscriptModelOptions() {
        AudioTranscriptionOptions options = OpenAiAudioTranscriptionOptions.builder()
                //.withResponseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                //.withTemperature(0.0f) // 너무 높으면 이상하게 받아 쓰기를 한다.
                // 텍스트 출력 언어 - ISO 639-1 코드로 입력 ko, en, ja를 사용한다.
                // 외국인이 말한 내용을 바로 번역해서 볼 수 있는 서비스 개발시 유용
                // 하지만 구글 번역과 비교하면 번역 성능은 좀 떨어지는 듯.
                .language("ja")
                .build();

        Resource resource = new ClassPathResource("/sample_audio.mp3");
        AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(resource, options);
        AudioTranscriptionResponse response = transcriptionModel.call(prompt);
        // 특별히 제공되는 메타 데이터는 없다.
        System.out.println("result = " + response.getResult().getOutput());
        // result = 안녕하세요. 스프링부트와 오픈AI 연동을 위한 강의입니다.
        // result = こんにちは。スプリングブートとオープンAIを繋ぐための講師です。
        // 구글 번역 = こんにちは。スプリングブートとオープンAI連動のための講義です。
    }
}
