package com.krafton.stamp.config;

import com.krafton.stamp.domain.Rarity;
import com.krafton.stamp.domain.Stamp;
import com.krafton.stamp.repository.StampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import com.krafton.stamp.domain.Category;

@Component
//@Profile("dev")
@RequiredArgsConstructor
public class StampSeeder implements CommandLineRunner {

    private final StampRepository stampRepository;

    @Override
    public void run(String... args) {
        addStampIfNotExists("GitHub Stamp", "https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png", "github.com", Rarity.COMMON, Category.BACKEND, "커밋 하나에도 의미를 담는 당신에게");
        addStampIfNotExists("GitHub Pro Stamp", "https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png", "github.com", Rarity.RARE, Category.BACKEND, "오픈소스 기여까지 하는 당신은 이미 고수!");
        addStampIfNotExists("GitHub Epic Coder Stamp", "https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png", "github.com", Rarity.EPIC, Category.BACKEND, "수많은 레포를 관리하는 전설 직전의 실력자");
        addStampIfNotExists("GitHub Legend Stamp", "https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png", "github.com", Rarity.LEGENDARY, Category.BACKEND, "별이 빛나는 GitHub의 전설, 바로 당신");

        // ✅ Spring Initializr Stamp
        addStampIfNotExists("Spring Stamp", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQwsq-7f5BWyog4cdeT1sQaYLVzhJ0o37Up8TjHvVU08WUgfyyMMRMHTVwJ5XReSjyhZa0&usqp=CAU", "start.spring.io", Rarity.COMMON, Category.BACKEND, "스프링 프로젝트의 시작은 여기서부터");
        addStampIfNotExists("Spring Developer Stamp", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQwsq-7f5BWyog4cdeT1sQaYLVzhJ0o37Up8TjHvVU08WUgfyyMMRMHTVwJ5XReSjyhZa0&usqp=CAU", "start.spring.io", Rarity.RARE, Category.BACKEND, "Spring Security, JPA도 자유자재!");
        addStampIfNotExists("Spring Epic Engineer Stamp", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQwsq-7f5BWyog4cdeT1sQaYLVzhJ0o37Up8TjHvVU08WUgfyyMMRMHTVwJ5XReSjyhZa0&usqp=CAU", "start.spring.io", Rarity.EPIC, Category.BACKEND, "스프링 부트와 클라우드를 넘나드는 실력자");
        addStampIfNotExists("Spring Master Stamp", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQwsq-7f5BWyog4cdeT1sQaYLVzhJ0o37Up8TjHvVU08WUgfyyMMRMHTVwJ5XReSjyhZa0&usqp=CAU", "start.spring.io", Rarity.LEGENDARY, Category.BACKEND, "Spring 생태계를 정복한 자에게 수여됨");

        // ✅ VS Code Stamp
        addStampIfNotExists("VS Code Stamp", "https://code.visualstudio.com/assets/images/code-stable.png", "code.visualstudio.com", Rarity.COMMON, Category.TOOL, "가볍고 빠른 개발 시작의 동반자");
        addStampIfNotExists("VS Code Hacker Stamp", "https://code.visualstudio.com/assets/images/code-stable.png", "code.visualstudio.com", Rarity.RARE, Category.TOOL, "단축키 마스터한 당신의 에디팅은 예술");
        addStampIfNotExists("VS Code Epic Coder Stamp", "https://code.visualstudio.com/assets/images/code-stable.png", "code.visualstudio.com", Rarity.EPIC, Category.TOOL, "플러그인과 확장팩을 자유자재로 활용하는 고수");
        addStampIfNotExists("VS Code Overlord Stamp", "https://code.visualstudio.com/assets/images/code-stable.png", "code.visualstudio.com", Rarity.LEGENDARY, Category.TOOL, "에디터를 무기로 삼은 전설적인 개발자");

        // ✅ ChatGPT Stamp
        addStampIfNotExists("ChatGPT Stamp", "https://upload.wikimedia.org/wikipedia/commons/0/04/ChatGPT_logo.svg", "chat.openai.com", Rarity.COMMON, Category.AI, "궁금할 때마다 찾아오는 AI 친구");
        addStampIfNotExists("ChatGPT Power User Stamp", "https://upload.wikimedia.org/wikipedia/commons/0/04/ChatGPT_logo.svg", "chat.openai.com", Rarity.RARE, Category.AI, "프롬프트의 장인, 생산성 폭발!");
        addStampIfNotExists("ChatGPT Epic Thinker Stamp", "https://upload.wikimedia.org/wikipedia/commons/0/04/ChatGPT_logo.svg", "chat.openai.com", Rarity.EPIC, Category.AI, "AI를 팀원처럼 다루는 창의적 문제 해결자");
        addStampIfNotExists("ChatGPT Architect Stamp", "https://upload.wikimedia.org/wikipedia/commons/0/04/ChatGPT_logo.svg", "chat.openai.com", Rarity.LEGENDARY, Category.AI, "AI를 완벽히 도구화한 미래형 인간");

        // ✅ Tailwind CSS Stamp
        addStampIfNotExists("Tailwind Stamp", "https://tailwindcss.com/favicons/favicon-32x32.png", "tailwindcss.com", Rarity.COMMON, Category.FRONTEND, "빠르게 UI를 그리는 데일리 툴");
        addStampIfNotExists("Tailwind Wizard Stamp", "https://tailwindcss.com/favicons/favicon-32x32.png", "tailwindcss.com", Rarity.RARE, Category.FRONTEND, "디자인과 코드의 경계를 넘나드는 마법사");
        addStampIfNotExists("Tailwind Epic Designer Stamp", "https://tailwindcss.com/favicons/favicon-32x32.png", "tailwindcss.com", Rarity.EPIC, Category.FRONTEND, "UI/UX와 퍼포먼스를 모두 챙기는 달인");
        addStampIfNotExists("Tailwind Virtuoso Stamp", "https://tailwindcss.com/favicons/favicon-32x32.png", "tailwindcss.com", Rarity.LEGENDARY, Category.FRONTEND, "클래스 이름만으로 UI를 지휘하는 지휘자");

        // ✅ React Stamp
        addStampIfNotExists("React Stamp", "https://upload.wikimedia.org/wikipedia/commons/a/a7/React-icon.svg", "reactjs.org", Rarity.COMMON, Category.FRONTEND, "컴포넌트를 처음 만들었다면 이 우표를");
        addStampIfNotExists("React Developer Stamp", "https://upload.wikimedia.org/wikipedia/commons/a/a7/React-icon.svg", "reactjs.org", Rarity.RARE, Category.FRONTEND, "Hooks와 상태 관리를 즐기는 개발자");
        addStampIfNotExists("React Epic Builder Stamp", "https://upload.wikimedia.org/wikipedia/commons/a/a7/React-icon.svg", "reactjs.org", Rarity.EPIC, Category.FRONTEND, "리액트와 Next.js까지 다루는 고급 개발자");
        addStampIfNotExists("React Architect Stamp", "https://upload.wikimedia.org/wikipedia/commons/a/a7/React-icon.svg", "reactjs.org", Rarity.LEGENDARY, Category.FRONTEND, "React 생태계를 설계하는 프론트엔드의 신");

        // ✅ LeetCode Stamp
        addStampIfNotExists("LeetCode Stamp", "https://upload.wikimedia.org/wikipedia/commons/1/19/LeetCode_logo_black.png", "leetcode.com", Rarity.COMMON, Category.LEARNING, "한 문제씩 푸는 당신의 성실함에");
        addStampIfNotExists("LeetCode Warrior Stamp", "https://upload.wikimedia.org/wikipedia/commons/1/19/LeetCode_logo_black.png", "leetcode.com", Rarity.RARE, Category.LEARNING, "일일 3문제? 취업 준비 끝판왕");
        addStampIfNotExists("LeetCode Epic Solver Stamp", "https://upload.wikimedia.org/wikipedia/commons/1/19/LeetCode_logo_black.png", "leetcode.com", Rarity.EPIC, Category.LEARNING, "중급 난이도 문제를 무난히 해결하는 강자");
        addStampIfNotExists("LeetCode Champion Stamp", "https://upload.wikimedia.org/wikipedia/commons/1/19/LeetCode_logo_black.png", "leetcode.com", Rarity.LEGENDARY, Category.LEARNING, "모든 문제를 풀고 나면 보이는 전설의 우표");

        // ✅ Notion Stamp
        addStampIfNotExists("Notion Stamp", "https://upload.wikimedia.org/wikipedia/commons/4/45/Notion_app_logo.png", "notion.so", Rarity.COMMON, Category.TOOL, "기본 정리는 노션으로 시작!");
        addStampIfNotExists("Notion Organizer Stamp", "https://upload.wikimedia.org/wikipedia/commons/4/45/Notion_app_logo.png", "notion.so", Rarity.RARE, Category.TOOL, "템플릿을 직접 만들 줄 아는 정리왕");
        addStampIfNotExists("Notion Epic Creator Stamp", "https://upload.wikimedia.org/wikipedia/commons/4/45/Notion_app_logo.png", "notion.so", Rarity.EPIC, Category.TOOL, "워크스페이스를 예술로 승화시키는 설계자");
        addStampIfNotExists("Notion Guru Stamp", "https://upload.wikimedia.org/wikipedia/commons/4/45/Notion_app_logo.png", "notion.so", Rarity.LEGENDARY, Category.TOOL, "모든 생각을 구조화하는 노션의 달인");

        // ✅ Kubernetes Stamp
        addStampIfNotExists("Kubernetes Stamp", "https://upload.wikimedia.org/wikipedia/commons/3/39/Kubernetes_logo_without_workmark.svg", "kubernetes.io", Rarity.COMMON, Category.DEVOPS, "컨테이너 오케스트레이션 입문자에게");
        addStampIfNotExists("K8s Operator Stamp", "https://upload.wikimedia.org/wikipedia/commons/3/39/Kubernetes_logo_without_workmark.svg", "kubernetes.io", Rarity.RARE, Category.DEVOPS, "배포 자동화와 스케일링까지 자유자재");
        addStampIfNotExists("Kubernetes Epic Engineer Stamp", "https://upload.wikimedia.org/wikipedia/commons/3/39/Kubernetes_logo_without_workmark.svg", "kubernetes.io", Rarity.EPIC, Category.DEVOPS, "Helm과 Istio까지 다루는 고급 DevOps");
        addStampIfNotExists("Kubernetes Sage Stamp", "https://upload.wikimedia.org/wikipedia/commons/3/39/Kubernetes_logo_without_workmark.svg", "kubernetes.io", Rarity.LEGENDARY, Category.DEVOPS, "클러스터를 다스리는 자, 전설로 남다");

        // ✅ Baekjoon Stamp
        addStampIfNotExists("Baekjoon Stamp", "https://buly.kr/6BxKete", "acmicpc.net", Rarity.COMMON, Category.LEARNING, "처음으로 입출력 문제를 푼 당신에게");
        addStampIfNotExists("Baekjoon Solver Stamp", "https://buly.kr/6BxKete", "acmicpc.net", Rarity.RARE, Category.LEARNING, "실버 문제들을 하나씩 풀어내는 성장형 코더");
        addStampIfNotExists("Baekjoon Epic Coder Stamp", "https://buly.kr/6BxKete", "acmicpc.net", Rarity.EPIC, Category.LEARNING, "골드 문제도 이제는 익숙한 당신");
        addStampIfNotExists("Baekjoon Grandmaster Stamp", "https://buly.kr/6BxKete", "acmicpc.net", Rarity.LEGENDARY, Category.LEARNING, "Platinum 이상, 알고리즘의 전설에게 수여됩니다");

        // ✅ Programmers Stamp
        addStampIfNotExists("Programmers Stamp", "https://buly.kr/CWubyHp", "programmers.co.kr", Rarity.COMMON, Category.LEARNING, "코딩 테스트 연습을 시작했다면");
        addStampIfNotExists("Programmers Challenger Stamp", "https://buly.kr/CWubyHp", "programmers.co.kr", Rarity.RARE, Category.LEARNING, "레벨 2도 이젠 익숙한 당신");
        addStampIfNotExists("Programmers Epic Solver Stamp", "https://buly.kr/CWubyHp", "programmers.co.kr", Rarity.EPIC, Category.LEARNING, "레벨 3 문제도 완주한 중급 실력자");
        addStampIfNotExists("Programmers Mastermind Stamp", "https://buly.kr/CWubyHp", "programmers.co.kr", Rarity.LEGENDARY, Category.LEARNING, "채용 연계형 코딩테스트도 통과하는 실력자");


        // ✅ Render Stamp
        addStampIfNotExists("Render Stamp", "https://buly.kr/G3DladO", "render.com", Rarity.COMMON, Category.DEVOPS, "처음으로 Render로 배포해봤다면");
        addStampIfNotExists("Render Deployer Stamp", "https://buly.kr/G3DladO", "render.com", Rarity.RARE, Category.DEVOPS, "자동 배포와 환경 설정까지 마친 배포러");
        addStampIfNotExists("Render Epic Engineer Stamp", "https://buly.kr/G3DladO", "render.com", Rarity.EPIC, Category.DEVOPS, "서브도메인, 백엔드/프론트 멀티 배포까지 섭렵한 당신");
        addStampIfNotExists("Render Architect Stamp", "https://buly.kr/G3DladO", "render.com", Rarity.LEGENDARY, Category.DEVOPS, "빌드, 배포, 캐시, DB까지 모두 자동화한 마스터");


        // ✅ AWS Stamp
        // 긴 URL: https://a0.awsstatic.com/libra-css/images/logos/aws_logo_smile_1200x630.png
        addStampIfNotExists("AWS Stamp", "https://buly.kr/58SnBO4", "aws.amazon.com", Rarity.COMMON, Category.DEVOPS, "처음으로 EC2를 써본 당신에게");
        addStampIfNotExists("AWS Builder Stamp", "https://buly.kr/58SnBO4", "aws.amazon.com", Rarity.RARE, Category.DEVOPS, "S3, RDS, Route53을 다루는 실전 유저");
        addStampIfNotExists("AWS Epic Engineer Stamp", "https://buly.kr/58SnBO4", "aws.amazon.com", Rarity.EPIC, Category.DEVOPS, "IAM, VPC, CloudWatch까지 다룰 줄 안다면");
        addStampIfNotExists("AWS Cloud Architect Stamp", "https://buly.kr/58SnBO4", "aws.amazon.com", Rarity.LEGENDARY, Category.DEVOPS, "인프라를 코드로 관리하는 AWS 마스터");


        // ✅ Gemini Stamp
        addStampIfNotExists("Gemini Stamp", "https://gemini.google.com/favicon.ico", "gemini.google.com", Rarity.COMMON, Category.AI, "새로운 지식을 탐험하는 AI 동반자");
        addStampIfNotExists("Gemini Power User Stamp", "https://gemini.google.com/favicon.ico", "gemini.google.com", Rarity.RARE, Category.AI, "프롬프트로 세상을 확장하는 탐험가");
        addStampIfNotExists("Gemini Epic Innovator Stamp", "https://gemini.google.com/favicon.ico", "gemini.google.com", Rarity.EPIC, Category.AI, "AI와 함께 창의성을 현실로 만드는 발명가");
        addStampIfNotExists("Gemini Visionary Stamp", "https://gemini.google.com/favicon.ico", "gemini.google.com", Rarity.LEGENDARY, Category.AI, "미래를 내다보는 AI 비전가");


        // ✅ Perplexity Stamp
        addStampIfNotExists("Perplexity Stamp (Common)", "https://www.perplexity.ai/favicon.ico", "perplexity.ai", Rarity.COMMON, Category.AI, "최신 AI 트렌드를 탐험하는 당신은 AI 리더!");
        addStampIfNotExists("Perplexity Stamp (Rare)", "https://www.perplexity.ai/favicon.ico", "perplexity.ai", Rarity.RARE, Category.AI, "최신 AI 트렌드를 탐험하는 당신은 AI 리더!");
        addStampIfNotExists("Perplexity Stamp (Epic)", "https://www.perplexity.ai/favicon.ico", "perplexity.ai", Rarity.EPIC, Category.AI, "최신 AI 트렌드를 탐험하는 당신은 AI 리더!");
        addStampIfNotExists("Perplexity Stamp (Legendary)", "https://www.perplexity.ai/favicon.ico", "perplexity.ai", Rarity.LEGENDARY, Category.AI, "최신 AI 트렌드를 탐험하는 당신은 AI 리더!");

        addStampIfNotExists("자랑스러운 경기인 Stamp", "https://buly.kr/B7aYW6j", "kyonggi.ac.kr", Rarity.COMMON, Category.LEARNING, "지식의 전당에서 꾸준히 배움을 이어가는 자랑스러운 경기인!");
        addStampIfNotExists("자랑스러운 한밭인 Stamp", "https://www.hanbat.ac.kr/images/kor/sub01/sub01_010301_img03.png", "hanbat.ac.kr", Rarity.COMMON, Category.LEARNING, "학문에 대한 열정으로 가득한 자랑스러운 한밭인!");
        addStampIfNotExists("자랑스러운 아주인 Stamp", "https://www.ajou.ac.kr/_res/ajou/kr/img/intro/img-symbol.png", "ajou.ac.kr", Rarity.COMMON, Category.LEARNING, "변화를 주도하는 지성, 자랑스러운 아주인!");
        addStampIfNotExists("자랑스러운 한신인 Stamp", "https://i.namu.wiki/i/9-rju60Q09IbjvdtbMdbKlxaJ9UbWrOBLFprjWf4xHwlHCkH1_yp-z2IprzyPvlbHy_9pw1k8O43ItzQ93uzsQ.svg", "hs.ac.kr", Rarity.COMMON, Category.LEARNING, "학문의 길을 걷는 따뜻한 지성, 자랑스러운 한신인!");
        addStampIfNotExists("자랑스러운 경희인 Stamp", "https://buly.kr/5fD46pe", "khu.ac.kr", Rarity.COMMON, Category.LEARNING, "학문의 길을 걷는 따뜻한 지성, 자랑스러운 한신인!");

        // ✅ Slack Stamp
        addStampIfNotExists("Slack Stamp (Common)", "https://a.slack-edge.com/80588/marketing/img/meta/favicon-32.png", "slack.com", Rarity.COMMON, Category.TOOL, "팀워크의 중심! 소통의 달인 Slack 유저!");
        addStampIfNotExists("Slack Stamp (Rare)", "https://a.slack-edge.com/80588/marketing/img/meta/favicon-32.png", "slack.com", Rarity.RARE, Category.TOOL, "팀워크의 중심! 소통의 달인 Slack 유저!");
        addStampIfNotExists("Slack Stamp (Epic)", "https://a.slack-edge.com/80588/marketing/img/meta/favicon-32.png", "slack.com", Rarity.EPIC, Category.TOOL, "팀워크의 중심! 소통의 달인 Slack 유저!");
        addStampIfNotExists("Slack Stamp (Legendary)", "https://a.slack-edge.com/80588/marketing/img/meta/favicon-32.png", "slack.com", Rarity.LEGENDARY, Category.TOOL, "팀워크의 중심! 소통의 달인 Slack 유저!");


        // ✅ Swagger Stamp
        addStampIfNotExists("Swagger Stamp (Common)", "https://media.trustradius.com/product-logos/hn/mI/7LD6S38HPGTZ.PNG", "swagger.io", Rarity.COMMON, Category.TOOL, "API 문서화의 정석! Swagger와 함께하는 개발자!");
        addStampIfNotExists("Swagger Stamp (Rare)", "https://media.trustradius.com/product-logos/hn/mI/7LD6S38HPGTZ.PNG", "swagger.io", Rarity.RARE, Category.TOOL, "API 문서화의 정석! Swagger와 함께하는 개발자!");
        addStampIfNotExists("Swagger Stamp (Epic)", "https://media.trustradius.com/product-logos/hn/mI/7LD6S38HPGTZ.PNG", "swagger.io", Rarity.EPIC, Category.TOOL, "API 문서화의 정석! Swagger와 함께하는 개발자!");
        addStampIfNotExists("Swagger Stamp (Legendary)", "https://media.trustradius.com/product-logos/hn/mI/7LD6S38HPGTZ.PNG", "swagger.io", Rarity.LEGENDARY, Category.TOOL, "API 문서화의 정석! Swagger와 함께하는 개발자!");


        // ✅ IntelliJ Stamp
        addStampIfNotExists("IntelliJ Stamp (Common)", "https://resources.jetbrains.com/storage/products/intellij-idea/img/meta/intellij-idea_logo_300x300.png", "jetbrains.com", Rarity.COMMON, Category.TOOL, "생산성을 극대화하는 당신의 IDE, IntelliJ!");
        addStampIfNotExists("IntelliJ Stamp (Rare)", "https://resources.jetbrains.com/storage/products/intellij-idea/img/meta/intellij-idea_logo_300x300.png", "jetbrains.com", Rarity.RARE, Category.TOOL, "생산성을 극대화하는 당신의 IDE, IntelliJ!");
        addStampIfNotExists("IntelliJ Stamp (Epic)", "https://resources.jetbrains.com/storage/products/intellij-idea/img/meta/intellij-idea_logo_300x300.png", "jetbrains.com", Rarity.EPIC, Category.TOOL, "생산성을 극대화하는 당신의 IDE, IntelliJ!");
        addStampIfNotExists("IntelliJ Stamp (Legendary)", "https://resources.jetbrains.com/storage/products/intellij-idea/img/meta/intellij-idea_logo_300x300.png", "jetbrains.com", Rarity.LEGENDARY, Category.TOOL, "생산성을 극대화하는 당신의 IDE, IntelliJ!");


        // ✅ Visual Studio Stamp
        // 긴 URL: https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Visual_Studio_Icon_2022.svg/1200px-Visual_Studio_Icon_2022.svg.png
        addStampIfNotExists("Visual Studio Stamp (Common)", "https://buly.kr/FhOG2KH", "visualstudio.microsoft.com", Rarity.COMMON, Category.TOOL, "강력한 도구로 개발 세계를 탐험하는 당신!");
        addStampIfNotExists("Visual Studio Stamp (Rare)", "https://buly.kr/FhOG2KH", "visualstudio.microsoft.com", Rarity.RARE, Category.TOOL, "강력한 도구로 개발 세계를 탐험하는 당신!");
        addStampIfNotExists("Visual Studio Stamp (Epic)", "https://buly.kr/FhOG2KH", "visualstudio.microsoft.com", Rarity.EPIC, Category.TOOL, "강력한 도구로 개발 세계를 탐험하는 당신!");
        addStampIfNotExists("Visual Studio Stamp (Legendary)", "https://buly.kr/FhOG2KH", "visualstudio.microsoft.com", Rarity.LEGENDARY, Category.TOOL, "강력한 도구로 개발 세계를 탐험하는 당신!");

        addStampIfNotExists("크래프톤 정글 Stamp", "https://cdn.sayun.studio/boottent/files/images/events/jiymlab/103c5545-4070-4aae-8217-703ef8b6775f.png", "jungle-compass.krafton.com", Rarity.EPIC, Category.TOOL, "정글을 헤치고 나아가는 성장형 개발자!");

        addStampIfNotExists("Python Stamp", "https://images.icon-icons.com/1508/PNG/512/python_104451.png", "python.org", Rarity.COMMON, Category.BACKEND, "간결하고 우아한 코드를 지향하는 파이썬 유저!");
        addStampIfNotExists("Java Stamp", "https://images.icon-icons.com/2699/PNG/512/java_logo_icon_169577.png", "oracle.com", Rarity.COMMON, Category.BACKEND, "한 줄 한 줄 성실하게 쌓아올리는 자바 개발자!");
        addStampIfNotExists("Kotlin Stamp", "https://kotlinlang.org/assets/images/favicon/apple-touch-icon-120x120.png", "kotlinlang.org", Rarity.COMMON, Category.BACKEND, "모던한 백엔드를 지향하는 코틀린 마스터!");
        addStampIfNotExists("Flask Stamp", "https://images.icon-icons.com/3913/PNG/512/flask_logo_icon_248540.png", "flask.palletsprojects.com", Rarity.COMMON, Category.BACKEND, "가볍고 유연한 백엔드를 사랑하는 플라스크 유저!");
        addStampIfNotExists("Django Stamp", "https://upload.wikimedia.org/wikipedia/commons/thumb/7/75/Django_logo_2017.svg/1200px-Django_logo_2017.svg.png", "djangoproject.com", Rarity.COMMON, Category.BACKEND, "빠르게 강력한 웹앱을 만드는 장고 장인!");

        addStampIfNotExists("MongoDB Stamp", "https://www.vectorlogo.zone/logos/mongodb/mongodb-icon.svg", "mongodb.com", Rarity.COMMON, Category.DATABASE, "NoSQL 세계를 이끄는 몽고 마스터!");
        addStampIfNotExists("Redis Stamp", "https://redis.io/images/redis-logo.svg", "redis.io", Rarity.COMMON, Category.DATABASE, "데이터 캐시의 정점, 레디스를 지배하는 자!");
        addStampIfNotExists("MySQL Stamp", "https://www.mysql.com/common/logos/mysql-logo.svg", "mysql.com", Rarity.COMMON, Category.DATABASE, "전통을 지켜온 관계형 DB의 수호자!");
        addStampIfNotExists("PostgreSQL Stamp", "https://upload.wikimedia.org/wikipedia/commons/2/29/Postgresql_elephant.svg", "postgresql.org", Rarity.COMMON, Category.DATABASE, "정확함과 확장성의 조화, 포스트그레 유저!");


        addStampIfNotExists("Node.js Stamp", "https://nodejs.org/static/images/favicons/favicon.png", "nodejs.org", Rarity.COMMON, Category.BACKEND, "자바스크립트로 웹 서버를 구축하는 백엔드 개발자의 필수 도구!");
        addStampIfNotExists("HTML Stamp", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/61/HTML5_logo_and_wordmark.svg/800px-HTML5_logo_and_wordmark.svg.png", "developer.mozilla.org", Rarity.COMMON, Category.FRONTEND, "웹의 뼈대를 설계하는 마크업 마스터!");
        addStampIfNotExists("Bootstrap Stamp", "https://getbootstrap.com/docs/5.3/assets/img/favicons/favicon-32x32.png", "getbootstrap.com", Rarity.COMMON, Category.FRONTEND, "빠르고 예쁜 UI를 사랑하는 당신에게!");
        addStampIfNotExists("Vue.js Stamp", "https://vuejs.org/logo.svg", "vuejs.org", Rarity.COMMON, Category.FRONTEND, "선언형 UI의 매력에 빠진 Vue 장인!");


    }


    private void addStampIfNotExists(
            String name,
            String imageUrl,
            String siteUrl,
            Rarity rarity,
            Category category,
            String description
    ) {
        stampRepository.findByName(name)
                .orElseGet(() -> stampRepository.save(
                        Stamp.builder()
                                .name(name)
                                .imageUrl(imageUrl)
                                .siteUrl(siteUrl)
                                .rarity(rarity)
                                .category(category)
                                .description(description)
                                .build()
                ));
    }
}