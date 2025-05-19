package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@SpringBootTest
public class OpenAiChatModelTests {
    @Autowired
    private OpenAiChatModel chatModel;

    @Test
    public void testChatModelSimple() {
        String result = chatModel.call("서울 올림픽은 몇회 올림픽이야?");
        System.out.println("result = " + result);
        // 서울 올림픽은 제24회 하계 올림픽입니다. 1988년 9월 17일부터 10월 2일까지 서울에서 개최되었습니다.
    }

    @Test
    public void testChatModelMessage() {
        var userMessage = new UserMessage("서울 올림픽에 대해 알려 주세요");
        var systemMessage = new SystemMessage("답변은 간략하게 하고, 마지막에는 실제 뉴스를 참고하라는 말을 해 주세요");
        String result = chatModel.call(userMessage, systemMessage);
        System.out.println("result = " + result);
        // 서울 올림픽은 1988년 9월 17일부터 10월 2일까지 대한민국 서울에서 개최된 제24회 하계 올림픽입니다. 이번 대회는 대한민국이 주최한 첫 올림픽이자, 아시아에서 열린 두 번째 올림픽으로, 160개국에서 약 8,000명의 선수들이 참가했습니다.
        // 서울 올림픽은 문화적으로나 정치적으로 큰 의미를 갖는 대회로, 한반도의 긴장 관계 속에서 개최되어 국제 사회의 관심을 끌었습니다. 이 대회는 한국의 경제 발전과 변화를 알리는 계기가 되었으며, 많은 역대 기념적인 순간들이 있었습니다.
        // 또한, 올림픽은 한국의 스포츠 인프라 확장과 글로벌 지위 향상에 큰 기여를 했습니다. 대회에서는 서울에서 개최된 탁구, 육상, 수영 등의 종목에서 세계 신기록이 나왔고, 특히 레슬링과 양궁 강국으로서의 위상을 보여주었습니다.
        // 더 자세한 정보는 관련 뉴스를 참고하시기 바랍니다.
    }

    // 대화의 히스토리를 함께 전달하여 문맥을 바탕으로 답변을 받을 수 있습니다.
    @Test
    public void testChatModelMessageContext() {
        Message system = new SystemMessage("간략하게 답변해 주세요.");
        Message message1 = new UserMessage("서울 올림픽에 대해 알려 주세요");
        Message assistant1 = new AssistantMessage("서울 올림픽, 공식명칭은 제24회 하계 올림픽대회는 1988년 9월 17일부터 10월 2일까지 대한민국 서울에서 개최되었습니다. 이 대회는 한국에서 처음으로 열린 올림픽 경기로, 총 159개 국가가 참여하였고 23종목의 경기가 진행되었습니다. 서울 올림픽은 성공적인 개최로 평가받았으며, 그로 인해 한국은 국제 스포츠 무대에서의 위상이 크게 향상되었습니다. 또한, 대회 기간 동안 평화와 화합을 강조하며 많은 이들에게 기억에 남는 현장이 되었습니다.");
        Message message2 = new UserMessage("그럼 바로 그 이전 올림픽은 어디야?");
        Message assistant2 = new AssistantMessage("바로 이전 올림픽은 1984년 하계 올림픽으로, 미국 로스앤젤레스에서 개최되었습니다. 이 대회는 7월 28일부터 8월 12일까지 진행되었고, 많은 국가가 참여한 강행된 올림픽 중 하나였습니다.");
        Message message3 = new UserMessage("그럼 그 두개의 올림픽중 참여 국가는 어디가 많아?");
        String result = chatModel.call(system, message1, assistant1, message2, assistant2, message3);
        System.out.println("result = " + result);
        // 1984년 로스앤젤레스 올림픽이 1988년 서울 올림픽보다 더 많은 국가가 참여했습니다. 로스앤젤레스 대회에는 약 140개국이 참가한 반면, 서울 올림픽에는 159개국이였지만, 1984년 대회는 냉전의 영향을 받아 일부 국가가 보이콧에 참여하여 상대적으로 낮은 수의 국가가 실제로 출전했습니다.
    }

    // 세밀한 설정을 위해 메시지와 옵션을 포함한 프롬프트 객체를 만들어 전달할 수 있습니다.
    // 전달하는 옵션에는 GPT 모델 선택, 답변 개수, 온도(temperature) 설정을 할 수 있습니다.
    // ChatGPT의 temperature 설정 범위는 일반적으로 0부터 2까지이며, 설정하지 않을시 Spring AI에서 default 값은 0.8입니다.
    // - 0에 가까울수록: 답변이 더 일관되고 예측 가능하지만 창의성이 줄어듭니다. (예: 코드 생성, 사실 기반 응답)
    // - 1에 가까울수록: 균형 잡힌 창의성과 다양성을 가집니다.
    // - 2에 가까울수록: 답변이 더 창의적이고 예측 불가능하지만, 가끔 비논리적인 결과가 나올 수도 있습니다.
    // 응답으로는 ChatResponse 객체를 통해 AI가 응답한 답변 이외에 질문과 답변에 각각 사용된 토큰수라던가 사용 제한 정보를 포함합니다.
    // 사용 제한은 일정한 시간 내에 몇번 요청할 수 있는지 또는 몇개의 토큰을 사용할 수 있는지를 제한하고 있으며 이것은 계정의 Tier에 따라 달라집니다.
    // Tier에 따른 상세한 사용 제한 정보는 https://platform.openai.com/docs/guides/rate-limits를 참고하세요.
    @Test
    public void testChatGptPrompt() {
        List<Message> messages = List.of(
                new SystemMessage("간략하게 답변해 주세요."),
                new UserMessage("서울 올림픽에 대해 알려 주세요"),
                new AssistantMessage("서울 올림픽, 공식명칭은 제24회 하계 올림픽대회는 1988년 9월 17일부터 10월 2일까지 대한민국 서울에서 개최되었습니다. 이 대회는 한국에서 처음으로 열린 올림픽 경기로, 총 159개 국가가 참여하였고 23종목의 경기가 진행되었습니다. 서울 올림픽은 성공적인 개최로 평가받았으며, 그로 인해 한국은 국제 스포츠 무대에서의 위상이 크게 향상되었습니다. 또한, 대회 기간 동안 평화와 화합을 강조하며 많은 이들에게 기억에 남는 현장이 되었습니다."),
                new UserMessage("그럼 바로 그 이전 올림픽은 어디야?"),
                new AssistantMessage("바로 이전 올림픽은 1984년 하계 올림픽으로, 미국 로스앤젤레스에서 개최되었습니다. 이 대회는 7월 28일부터 8월 12일까지 진행되었고, 많은 국가가 참여한 강행된 올림픽 중 하나였습니다."),
                new UserMessage("그럼 그 두개의 올림픽중 참여 국가는 어디가 많아?")
        );

        var chatOptions = OpenAiChatOptions.builder()
                .model(OpenAiApi.ChatModel.GPT_4_O)
                .N(2)
                .temperature(1.0).build();

        var prompt = new Prompt(messages, chatOptions);
        // 빌더 패턴을 사용할 수도 있다.
        //var prompt = Prompt.builder().messages(messages).chatOptions(chatOptions).build();

        var chatResponse = chatModel.call(prompt);

        // 토큰 사용량
        Usage usage = chatResponse.getMetadata().getUsage();
        System.out.println("promptTokens = " + usage.getPromptTokens() +
                ", completionTokens = " + usage.getCompletionTokens() +
                ", totalTokens = " + usage.getTotalTokens());

        // 요청, 토큰 제한 상세
        RateLimit rateLimit = chatResponse.getMetadata().getRateLimit();
        System.out.println("requestLimit = " + rateLimit.getRequestsLimit() +
                ", requestRemaining = " + rateLimit.getRequestsRemaining() +
                ", requestReset = " + rateLimit.getRequestsReset());
        System.out.println("tokensLimit = " + rateLimit.getTokensLimit() +
                ", tokensRemaining = " + rateLimit.getTokensRemaining() +
                ", tokensReset = " + rateLimit.getTokensReset());

        // 답변 출력
        for (Generation generation : chatResponse.getResults()) {
            System.out.println("response = " + generation.getOutput().getText());
        }
        // promptTokens = 272, generationTokens = 108
        // requestLimit = 500, requestRemaining = 499, requestReset = PT0.12S
        // tokensLimit = 30000, tokensRemaining = 29703, tokensReset = PT0.594S
        // response = 1988년 서울 올림픽의 참여 국가는 159개로, 1984년 로스앤젤레스 올림픽의 140개국보다 더 많았습니다. 그래서 서울 올림픽의 참여 국가가 더 많았습니다.
        // response = 1988년 서울 올림픽에는 159개 국가가 참여했으며, 1984년 로스앤젤레스 올림픽에는 140개 국가가 참여했습니다. 따라서, 서울 올림픽에 참여한 국가가 더 많습니다.
    }

    // 사용자 메시지에 텍스트 뿐만 아니라 그림도 함께 전달할 수 있습니다.
    // 그림은 어플리케이션 리소스에 포함하여 클래스 패스로 리소스 객체를 만들거나 또는 파일 시스템의 경로를 사용하여 리소스 객체를 만들어 전달할 수도 있습니다.
    @Test
    public void testChatModelImage() {
        Resource resource = new ClassPathResource("/Disney_World_1.jpg");
        //Resource resource = new FileSystemResource("D:\\archive\\images\\Disney_World_1.jpg");

        var media = Media.builder()
                .mimeType(MimeTypeUtils.IMAGE_JPEG)
                .data(resource)
                .build();

        Message message = UserMessage.builder()
                .text("사진에 제목을 붙인다면 무엇이 좋을까?")
                .media(media)
                .build();

        String result = chatModel.call(message);
        System.out.println("result = " + result);
        // 이 사진에 적합한 제목으로는 "자연의 경이로움" 또는 "신비로운 구름과 바위"가 좋을 것 같습니다. 또는 "파란 하늘 아래의 폭포"라는 제목도 어울릴 것 같아요. 어떤 느낌을 주고 싶은지에 따라 다양한 제목이 가능하네요!
    }

    @Test
    public void testChatModelImage2() {
        Resource resource = new ClassPathResource("/Disney_World_2.jpg");
        //Resource resource = new FileSystemResource("D:\\archive\\images\\Disney_World_2.jpg");

        var media = Media.builder()
                .mimeType(MimeTypeUtils.IMAGE_JPEG)
                .data(resource)
                .build();

        var message = UserMessage.builder()
                .text("사진 속의 풍경을 멋진 시로 써 주세요")
                .media(media)
                .build();

        String result = chatModel.call(message);
        System.out.println("result = " + result);
        /*
        푸른 하늘의 품에, 높이 솟은 산이,
        순백의 눈으로 나를 바라보네.
        물빛에 드리워진, 그림자 속의 동화,
        바람에 실려오는 자유로운 잔잔한 노래.

        덩굴에 감싸인 배가, 고요히 정박해,
        과거의 이야기들을, 물 위에 속삭여.
        녹음이 우거진 나무들, 생명의 숨결 속에,
        모두의 꿈을 품은, 푸르른 역사를 잇네.

        훌쩍 뛰어넘는 구름 사이로,
        반짝이는 햇빛이 달려와,
        그녀의 미소처럼 밝게, 자연의 축복을 담아,
        하루의 여정 속에, 또 다른 나를 찾아가.
         */
    }

    // 오디오 파일을 사용하여 프롬프트를 작성한다.
    @Test
    public void testChatModelAudioInput() {
        // 오디오 파일을 사용하여 사용자 메시지를 작성한다
        var audioResource = new ClassPathResource("sample_audio.mp3");
        var media = Media.builder()
                .mimeType(MimeTypeUtils.parseMimeType("audio/mp3"))
                .data(audioResource)
                .build();
        var userMessage = UserMessage.builder()
                .text("이 오디오 파일의 내용에 대해 요약해 주세요")
                .media(media)
                .build();

        // 사용자 메시지와 챗옵션을 사용하여 프롬프트를 구성하고 AI 모델을 호출한다
        // GPT-4-O 또는 GPT_4_O_MINI 는 텍스트와 이미지만 처리할 수 있다.
        // 오디오 파일을 처리하려면 GPT_4_O_AUDIO_PREVIEW 모델을 사용해야 한다.
        // 아직은 PREVIEW 모델이고 정식 모델은 나오지 않았다.
        var chatOptions = OpenAiChatOptions.builder()
                .model(OpenAiApi.ChatModel.GPT_4_O_AUDIO_PREVIEW)
                .build();
        var prompt = Prompt.builder()
                .messages(userMessage)
                .chatOptions(chatOptions)
                .build();
        ChatResponse response = chatModel.call(prompt);

        System.out.println("result = " + response.getResult().getOutput().getText());
        // result = 안녕하세요, 스프링 부트와 오픈 AI를 연동하기 위한 강의 내용으로, 이 두 기술의 연계 방법에 대해 설명하는 내용입니다.
    }

    // 프롬프트 내용을 사용하여 오디오를 생성한다.
    @Test
    public void testChatModelAudioOutput() throws IOException {
        ChatResponse response = chatModel.call(new Prompt("스프링부트에 대해 간단하게 설명해 주세요",
                OpenAiChatOptions.builder()
                        .temperature(0.5)
                        .model(OpenAiApi.ChatModel.GPT_4_O_AUDIO_PREVIEW)
                        .outputModalities(List.of("text", "audio"))
                        .outputAudio(new OpenAiApi.ChatCompletionRequest.AudioParameters(
                                OpenAiApi.ChatCompletionRequest.AudioParameters.Voice.NOVA,
                                OpenAiApi.ChatCompletionRequest.AudioParameters.AudioResponseFormat.MP3)
                        ).build()));

        String text = response.getResult().getOutput().getText(); // audio transcript
        System.out.println("result = " + text);

        byte[] audio = response.getResult().getOutput().getMedia().getFirst().getDataAsByteArray(); // audio data
        Files.write(Paths.get("D:\\archive\\audio\\ai_chat_audio.mp3"), audio);
    }

    // 프롬프트 내용을 사용하여 오디오를 생성한다.
    @Test
    public void testChatModelAudioInputOutput() throws IOException {
        var audioResource = new ClassPathResource("sample_audio_ask.mp3");
        var media = Media.builder()
                .mimeType(MimeTypeUtils.parseMimeType("audio/mp3"))
                .data(audioResource)
                .build();
        var userMessage =UserMessage.builder()
                .text("질문에 친절하고 간략하게 답변해 주세요")
                .media(media)
                .build();

        ChatResponse response = chatModel.call(new Prompt(userMessage,
        OpenAiChatOptions.builder()
                .temperature(0.5)
                .model(OpenAiApi.ChatModel.GPT_4_O_AUDIO_PREVIEW)
                .outputModalities(List.of("text", "audio"))
                .outputAudio(new OpenAiApi.ChatCompletionRequest.AudioParameters(
                        OpenAiApi.ChatCompletionRequest.AudioParameters.Voice.NOVA,
                        OpenAiApi.ChatCompletionRequest.AudioParameters.AudioResponseFormat.MP3)
                ).build()));

        String text = response.getResult().getOutput().getText(); // audio transcript
        System.out.println("result = " + text);

        byte[] audio = response.getResult().getOutput().getMedia().getFirst().getDataAsByteArray(); // audio data
        Files.write(Paths.get("D:\\archive\\audio\\ai_chat_audio_answer.mp3"), audio);

        // result = 의존성 주입은 스프링 프레임워크의 핵심 개념 중 하나로, 객체 간의 의존 관계를 외부에서 주입해주는 방식입니다. 이를 통해 객체 간 결합도를 낮추고, 코드의 재사용성과 테스트 용이성을 높여줍니다.
    }
}
