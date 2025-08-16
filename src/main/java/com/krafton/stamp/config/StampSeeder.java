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
@Profile("dev")
@RequiredArgsConstructor
public class StampSeeder implements CommandLineRunner {

    private final StampRepository stampRepository;

    @Override
    public void run(String... args) {
        addStampIfNotExists(
                "GitHub Stamp",
                "https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png",
                "github.com",
                Rarity.COMMON,
                Category.BACKEND,
                "깃 커밋을 쌓는 당신을 위한 개발자 필수 우표"
        );

        addStampIfNotExists(
                "Spring Initializr Stamp",
                "https://start.spring.io/images/icon-spring-initializr.svg",
                "start.spring.io",
                Rarity.COMMON,
                Category.BACKEND,
                "스프링 프로젝트를 시작한 개발자에게"
        );

        addStampIfNotExists(
                "VS Code Stamp",
                "https://code.visualstudio.com/assets/images/code-stable.png",
                "code.visualstudio.com",
                Rarity.COMMON,
                Category.TOOL,
                "가장 사랑받는 에디터를 쓰고 있다면 이 우표는 필수!"
        );

        addStampIfNotExists(
                "ChatGPT Stamp",
                "https://upload.wikimedia.org/wikipedia/commons/0/04/ChatGPT_logo.svg",
                "chat.openai.com",
                Rarity.RARE,
                Category.AI,
                "AI와 함께 코딩하는 당신을 위한 특별한 우표"
        );

        addStampIfNotExists(
                "GitHub Actions Stamp",
                "https://github.githubassets.com/images/modules/site/features/actions-icon-actions.svg",
                "github.com/features/actions",
                Rarity.RARE,
                Category.DEVOPS,
                "CI/CD 자동화로 배포를 날려버리는 그대에게"
        );

        addStampIfNotExists(
                "Tailwind CSS Stamp",
                "https://tailwindcss.com/favicons/favicon-32x32.png",
                "tailwindcss.com",
                Rarity.COMMON,
                Category.FRONTEND,
                "빠르고 효율적인 UI 개발을 사랑하는 이에게"
        );

        addStampIfNotExists(
                "React Stamp",
                "https://upload.wikimedia.org/wikipedia/commons/a/a7/React-icon.svg",
                "reactjs.org",
                Rarity.RARE,
                Category.FRONTEND,
                "컴포넌트 지향의 세계에 발을 들였다면"
        );

        addStampIfNotExists(
                "LeetCode Stamp",
                "https://upload.wikimedia.org/wikipedia/commons/1/19/LeetCode_logo_black.png",
                "leetcode.com",
                Rarity.COMMON,
                Category.LEARNING,
                "알고리즘으로 밤을 지새우는 사람이라면 반드시 소장할 우표"
        );

        addStampIfNotExists(
                "Notion Stamp",
                "https://upload.wikimedia.org/wikipedia/commons/4/45/Notion_app_logo.png",
                "notion.so",
                Rarity.COMMON,
                Category.TOOL,
                "모든 걸 정리하는 당신의 두뇌 확장 툴"
        );

        addStampIfNotExists(
                "Kubernetes Stamp",
                "https://upload.wikimedia.org/wikipedia/commons/3/39/Kubernetes_logo_without_workmark.svg",
                "kubernetes.io",
                Rarity.LEGENDARY,
                Category.DEVOPS,
                "쿠버네티스를 다룰 줄 아는 당신은 이미 전설"
        );


        // ... 이하 생략, 각각 적절한 카테고리 추가해줘
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
                                .category(category)  // ✅ 꼭 포함!
                                .description(description)
                                .build()
                ));
    }


}
