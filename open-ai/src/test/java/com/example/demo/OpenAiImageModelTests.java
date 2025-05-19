package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OpenAiImageModelTests {

    @Autowired
    private OpenAiImageModel imageModel;

    // ImageModel.call()에는 ImagePrompt만 파라미터로 전달받을 수 있다.
    // 가로 세로 크기는 필수
    // '256x256', '512x512', '1024x1024', '1024x1792', '1792x1024'
    @Test
    public void testImageModelOptions() {
        String message = """
            수채화 스타일로 그린 화성 탐사 로버 그림이 필요해.
            2족 보행 로봇이 함께 탐사하는 모습으로 해 줘.
            붓으로 그린 듯한 부드러운 필치와 여백의 미를 살려서 표현해 줘.
            """;
        ImageResponse response = imageModel.call(
                new ImagePrompt(message, OpenAiImageOptions.builder()
                    .model("dall-e-3") // model = 'dall-e-2', 'dall-e-3'
                    .quality("hd") // quality = 'hd', 'standard'
                    .style("natural") // style = 'vivid', 'natural'
                    .width(1024)
                    .height(1024).build()));

        System.out.println("result = " + response.getResult().getOutput().getUrl());
        // result = https://oaidalleapiprodscus.blob.core.windows.net/private/org-mVOWXNeCVfYGDLx05waWhk1J/user-Y6YkEQhQL816twkubJksyZh7/img-dMwKpD2iGUGbWYgy3Nv0SDhb.png?st=2025-02-14T04%3A45%3A59Z&se=2025-02-14T06%3A45%3A59Z&sp=r&sv=2024-08-04&sr=b&rscd=inline&rsct=image/png&skoid=d505667d-d6c1-4a0a-bac7-5c84a87759f8&sktid=a48cca56-e6da-484e-a814-9c849652bcb3&skt=2025-02-13T08%3A12%3A08Z&ske=2025-02-14T08%3A12%3A08Z&sks=b&skv=2024-08-04&sig=EgQNGS9YMs4vGQXupCfklL8Qg/BfEUMGQdjpa4z%2BhwY%3D
    }

    // String message = "토끼와 거북이 동화에 삽입할 삽화가 필요해. 거북이가 느리지만 열심히 기어가는 모습을 그려줘. 거북이 바로 뒤에는 열심히 뛰어 오는 토끼를 그려 줘. 전체 분위기는 숲속이고 나무 아래를 지나는 모습이면 좋겠어";
    // org.springframework.ai.retry.NonTransientAiException: 400 - {
    //  "error": {
    //    "code": "content_policy_violation",
    //    "message": "This request has been blocked by our content filters.",
    //    "param": null,
    //    "type": "invalid_request_error"
    //  }
    // }
}
