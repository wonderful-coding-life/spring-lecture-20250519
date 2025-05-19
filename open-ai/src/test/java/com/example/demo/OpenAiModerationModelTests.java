package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.ai.moderation.*;
import org.springframework.ai.openai.OpenAiModerationModel;
import org.springframework.ai.openai.OpenAiModerationOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OpenAiModerationModelTests {
    @Autowired
    private OpenAiModerationModel openAiModerationModel;

    // https://www.short-story.me/stories/horror-stories/710-serial-killer
    private String message = """
            Serial Killer
            ===============
            Written by Davis
            The Summer of 1979.  I lived in a small town just north of Indianapolis, called Indianola.  Many of the people in this town had lived in Indianola most of their lives.  My family had resided on the same street since before I was born - a tree-lined boulevard with large lots, most of the homes set back at angles from the street.  At age 21, I had known every neighbor on our block for years.
            It was a typical Indiana summer – hot and humid – with frequent nighttime thunderstorms. What made this summer unique, and uniquely frightening, was the presence of a vicious serial killer.  Details about the murders were sketchy, and much of what people “knew” was likely random speculation.  Still, the local press was pretty dogged and we knew the police had confirmed five murders since the beginning of the year.  Most of us figured there were more. Many more.
            What gave me chills were the killer’s methods. First, he used a knife.  Not a kitchen knife, but one of those beastly, foot-long, hunting knives used to skin a deer.  It was apparent the killer rarely completed the job with one stab. He liked to toy with his victims, often inflicting painful flesh wounds before finishing the job. I had heard that some of the victims had died of heart attacks before the killer got around to finishing the job, perhaps mercifully.
            The media had dubbed the killer “The Light Stalker”.  It seemed the killer liked to overpower victims in the dark, and to make it worse, he would control the lights, so that the potential victim would become agitated trying to find a working light source. Then, just before stabbing the victim, he would switch on a light so that the victim could see his face. Likely this effect added to the psychological torture. Pretty sick.
            Many of these details came from an assault on a woman who managed to survive her wounds. She told the police that she had enough knowledge about the legend that when she couldn’t find a working light upon entering her house, it occurred to her it might be the Light Stalker. Still, she chose to disregard her fears, instead telling herself she was silly to think this; that surely the power was out. When a table lamp suddenly switched on behind her, she knew he was there. She tried to run, but at this point was so terrified her feet became glued to the floor.  All the evening news stations ran the same footage, her lying in her hospital bed, head wrapped in gauze and her eyes swollen shut. She described how she had learned somewhere if you are ever attacked by a wild animal, to let yourself go completely limp and play dead, which she did by falling to the floor after he sliced open her scalp and stabbed her in the belly. She said she went lifeless, hoping he would stop the assault, but how she managed to stay still with multiple stab wounds is beyond me. Listening to her description on the news was horrifying. She said it was the worst moment of her life.  I can only imagine.
            At age 21, I still lived at home with my Mother, Stepdad and younger sister, Elizabeth. I was studying biology at the local university, with some vague hopes of attending Veterinary School. For income, I worked as a waiter in a diner near the freeway. Because I was 21, I could legally serve the customers cocktails – mostly beer and wine.  I am pretty sure serving alcohol improved the tips and on some evenings, I came home with over $100 in my pocket. Not bad for someone with questionable ambition at this point in his life.
            Several of my friends from high school were also students at the “U”, and we liked to get together and party it up. Thursday and Saturday night were our favorite gathering times. We partied often, but on these two particular evenings, we liked to ride our motorcycles to a raucous bar near the University called the Lid, listening to music and playing pool or foosball.  Both were designated “Two for One” drinking nights. This meant you could keep two beers going at all times, a very important bonus from our perspective.
            The particular Thursday in question, we had seen the band “Black Oak Arkansas”, a rowdy, blues - rock band with a female lead dressed in tight shorts who could really belt out the tunes. By the time I hopped on my motorcycle to drive home, I was buzzed, horny and vaguely hungry. I briefly considered stopping at White Castle for a few cheeseburgers, but then decided against it. I had to work a long shift tomorrow, and the last thing I needed was to overpower my customers with uncontrollable flatulence.
            It was close to 3:00 am as I pulled into our driveway. Lightning flickered in the west, threatening a storm. I always killed the cycle engine as I turned up the street; usually I had enough momentum to just glide to the top of the driveway. As I dismounted, I noted all the lights were out, including the one above the outside door. Mom usually left that on for me, so I was mildly annoyed to have to stumble through the complete darkness. My perception was just enough off to fear walking into a door jamb. I thought about keeping my helmet on but dismissed the idea quickly. I was too liquored up.
            After fumbling with my front door key, I saw the door was unlocked. Not unusual. The entryway opened to a split level, with six steps up and a dozen down, where both my sister and I had bedrooms. I decided to see what was in the fridge, and did my best not to bang around as I approached the steps. I noticed a curious odor – something that smelled like wet metal. Odd, but I quickly dismissed it as probably something in the sink, thawing out for a meal the next day.
            Thunder rumbled in the distance. The storm was getting closer. I began to feel uncomfortable, as if something in the house were slightly off kilter, but my loaded self said just shut up and see what Mom made for dinner. I salivated as I approached the refrigerator, thinking about chowing on a piece of fried chicken or the like, but my hopes were dashed when I saw that dinner had been eggplant surprise.  Tasted like dirt. Yuck. Skip it.
            I turned to head down the steps, my fantasy now turned to nestling my head on the pillow. Mom and Dad often left their bedroom door open and I could see their motionless shapes on their bed as I descended the steps. Jeez, so still... are they dead? A random thought quickly dismissed. Besides, I was sure I could detect Mom’s soft snore.
            My sister’s room was directly across from mine. Her door was closed, but I thought it might be fun to scare her awake. After all, this was the sister who tormented me endlessly in the same fashion, often jumping from behind the fireplace as I walked by…Boo! I hated it, mostly because she got me every time. I’d show her…don’t get mad, get even. Our family mantra.
            As I opened her door, I vaguely noted her closet door was left open, unusual for her. What I really noticed though was how powerful the odor of metal was I smelled before. It instantly reminded me of the time in shop class when we sheared copper to make letter openers. Pungent, almost thick. Why? In spite of the flickering lightning from the approaching storm, it was still dark as could be. I tried to make out the shape of my sister on her bed, right in front of me, but I couldn’t quite see her. I banged my shin on her bedside and winced, then reached down to grab her shoulder and scream “Wake Up!” This was going to be good.
            Just as I touched her, I felt something incredibly warm, thick and wet, almost like motor oil. I pulled my hand back and in the flash of lightning, I could see dark liquid running towards my wrist. What the hell? Elizabeth was absolutely motionless before me. Thunder rumbled, rattling the windows.
            I reached for her bedside light and after fumbling momentarily, turned the switch. Nothing happened. My instincts were screaming at me, but the fog of alcohol barred me from recognition. I stood up from crouching over the bed, completely baffled. Suddenly, a light went on behind me, from the closet. That’s right! I had forgotten she had a closet light, just an ancient socket with a grimy 40 watt bulb and a pull chain.
            And then recognition came crashing through the haze. Fuck, it was him! My muscles poised to turn and run, but it was too late… A huge, moist hand closed around my throat and I could smell his breathe – rotten and foul. A soft, maniacal giggle reached my ears and an inhuman voice said: “Hi, do you wanna play with me…?”
            And there was no more light, but the ensuing pain was beyond description….
            End
            BIO: Davis III is a middle-aged male living in St. Paul, Minnesota. He has some professional experience in both business writing and more recently, having a portion of his memoirs published in Midlife Collage (now defunct). This individual publication is based loosely on a dream Davis III had many years ago; he hopes you enjoy it.
            """;

    // OpenAI의 Moderation API는 법적 요구사항, 윤리적 가이드라인, 공공의 안전 및 건강을 고려하여 컨텐츠 필터링 정보를 제공합니다.
    @Test
    public void testOpenAiModerationModel() {

        // 현재는 model 선택 이외에 별다른 옵션을 지원하고 있지 않으며, omni-moderation-latest를 사용하더라도 아직까지는 이미지등 멀티 모달을 지원하지 않는다.
        OpenAiModerationOptions moderationOptions = OpenAiModerationOptions.builder()
                .model("text-moderation-latest") // omni-moderation-latest
                .build();
        //ModerationPrompt moderationPrompt = new ModerationPrompt(message, moderationOptions);

        ModerationPrompt moderationPrompt = new ModerationPrompt(message);
        ModerationResponse response = openAiModerationModel.call(moderationPrompt);

        // Access the moderation results
        Moderation moderation = response.getResult().getOutput();

        // Print general information
        System.out.println("Moderation ID: " + moderation.getId());
        System.out.println("Model used: " + moderation.getModel());

        // Access the moderation results (there's usually only one, but it's a list)
        for (ModerationResult result : moderation.getResults()) {
            System.out.println("\nModeration Result:");
            System.out.println("Flagged: " + result.isFlagged());

            // Access categories
            Categories categories = result.getCategories();
            System.out.println("\nCategories:");
            System.out.println("Sexual(성적인 컨텐츠): " + categories.isSexual());
            System.out.println("Hate(증오 또는 혐오): " + categories.isHate());
            System.out.println("Harassment(괴롭힘 또는 희롱): " + categories.isHarassment());
            System.out.println("Self-Harm(자해): " + categories.isSelfHarm());
            System.out.println("Sexual/Minors(미성년자와 관련된 성적인 컨텐츠): " + categories.isSexualMinors());
            System.out.println("Hate/Threatening(위협적인 증오 발언): " + categories.isHateThreatening());
            System.out.println("Violence/Graphic(폭력적이고 잔인한 묘사): " + categories.isViolenceGraphic());
            System.out.println("Self-Harm/Intent(자해 의도): " + categories.isSelfHarmIntent());
            System.out.println("Self-Harm/Instructions(자해 방법에 대한 지침): " + categories.isSelfHarmInstructions());
            System.out.println("Harassment/Threatening(협박성 괴롭힘): " + categories.isHarassmentThreatening());
            System.out.println("Violence(폭력): " + categories.isViolence());

            // Access category scores
            CategoryScores scores = result.getCategoryScores();
            System.out.println("\nCategory Scores:");
            System.out.println("Sexual(괴롭힘 또는 희롱): " + scores.getSexual());
            System.out.println("Hate(증오 또는 혐오): " + scores.getHate());
            System.out.println("Harassment(괴롭힘 또는 희롱): " + scores.getHarassment());
            System.out.println("Self-Harm(자해): " + scores.getSelfHarm());
            System.out.println("Sexual/Minors(미성년자와 관련된 성적인 컨텐츠): " + scores.getSexualMinors());
            System.out.println("Hate/Threatening(위협적인 증오 발언): " + scores.getHateThreatening());
            System.out.println("Violence/Graphic(폭력적이고 잔인한 묘사): " + scores.getViolenceGraphic());
            System.out.println("Self-Harm/Intent(자해 의도): " + scores.getSelfHarmIntent());
            System.out.println("Self-Harm/Instructions(자해 방법에 대한 지침): " + scores.getSelfHarmInstructions());
            System.out.println("Harassment/Threatening(협박성 괴롭힘): " + scores.getHarassmentThreatening());
            System.out.println("Violence(폭력): " + scores.getViolence());

            // Moderation ID: modr-B0rd92TAyJPAzvCv5OKW0aoAMZlut
            // Model used: text-moderation-007
            //
            // Moderation Result:
            // Flagged: true
            //
            // Categories:
            // Sexual(성적인 컨텐츠): false
            // Hate(증오 또는 혐오): false
            // Harassment(괴롭힘 또는 희롱): false
            // Self-Harm(자해): false
            // Sexual/Minors(미성년자와 관련된 성적인 컨텐츠): false
            // Hate/Threatening(위협적인 증오 발언): false
            // Violence/Graphic(폭력적이고 잔인한 묘사): true
            // Self-Harm/Intent(자해 의도): false
            // Self-Harm/Instructions(자해 방법에 대한 지침): false
            // Harassment/Threatening(협박성 괴롭힘): false
            // Violence(폭력): true
            //
            // Category Scores:
            // Sexual(괴롭힘 또는 희롱): 0.1455577164888382
            // Hate(증오 또는 혐오): 0.001020487630739808
            // Harassment(괴롭힘 또는 희롱): 0.01706378348171711
            // Self-Harm(자해): 0.0032634730450809
            // Sexual/Minors(미성년자와 관련된 성적인 컨텐츠): 0.0033874674700200558
            // Hate/Threatening(위협적인 증오 발언): 1.1958227696595713E-4
            // Violence/Graphic(폭력적이고 잔인한 묘사): 0.8245492577552795
            // Self-Harm/Intent(자해 의도): 5.512092029675841E-4
            // Self-Harm/Instructions(자해 방법에 대한 지침): 3.738429586519487E-5
            // Harassment/Threatening(협박성 괴롭힘): 0.010005802847445011
            // Violence(폭력): 0.8769115805625916
        }
    }
}
