package com.krafton.stamp.config;

import com.krafton.stamp.domain.Rarity;
import com.krafton.stamp.domain.Stamp;
import com.krafton.stamp.repository.StampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

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
                "깃 커밋을 쌓는 당신을 위한 개발자 필수 우표"
        );

        addStampIfNotExists(
                "Spring Initializr Stamp",
                "https://start.spring.io/images/icon-spring-initializr.svg",
                "start.spring.io",
                Rarity.COMMON,
                "스프링 프로젝트를 시작한 개발자에게"
        );

        addStampIfNotExists(
                "Docker Stamp",
                "https://www.docker.com/wp-content/uploads/2022/03/Moby-logo.png",
                "docker.com",
                Rarity.RARE,
                "모든 환경에서 돌아가는 그대의 컨테이너에게"
        );

        addStampIfNotExists(
                "AWS Stamp",
                "https://a0.awsstatic.com/libra-css/images/logos/aws_logo_smile_1200x630.png",
                "aws.amazon.com",
                Rarity.RARE,
                "클라우드 위에 당신의 앱을 띄웠다면 이 우표를!"
        );

        addStampIfNotExists(
                "Stack Overflow Stamp",
                "https://cdn.sstatic.net/Sites/stackoverflow/company/img/logos/so/so-icon.svg",
                "stackoverflow.com",
                Rarity.COMMON,
                "질문과 답변으로 성장한 당신에게"
        );

        addStampIfNotExists(
                "Maven Central Stamp",
                "https://upload.wikimedia.org/wikipedia/commons/4/4f/Maven_logo.png",
                "search.maven.org",
                Rarity.COMMON,
                "의존성을 사랑하는 자바 개발자를 위한 우표"
        );

        addStampIfNotExists(
                "Render Stamp",
                "https://dashboard.render.com/static/media/render-logo.2cdb46ed.svg",
                "render.com",
                Rarity.RARE,
                "쉽고 빠른 배포, Render 유저 전용 스탬프"
        );

        addStampIfNotExists(
                "Postman Stamp",
                "https://www.postman.com/_gatsby/image/6d19b58c7a58c4f08dffb5198d7b26e1/postman-logo-stacked.svg",
                "postman.com",
                Rarity.COMMON,
                "API 테스트를 즐기는 이들에게"
        );

        addStampIfNotExists(
                "GitLab Stamp",
                "https://about.gitlab.com/images/press/logo/png/gitlab-icon-rgb.png",
                "gitlab.com",
                Rarity.RARE,
                "DevOps 풀코스를 달리는 당신을 위한 증표"
        );

        addStampIfNotExists(
                "OpenAI Stamp",
                "https://openai.com/content/images/2022/05/openai-avatar.png",
                "openai.com",
                Rarity.LEGENDARY,
                "AI와 함께하는 개발자에게 주어지는 전설의 우표"
        );

    }

    private void addStampIfNotExists(String name, String imageUrl, String siteUrl, Rarity rarity, String description) {
        stampRepository.findByName(name)
                .orElseGet(() -> stampRepository.save(
                        Stamp.builder()
                                .name(name)
                                .imageUrl(imageUrl)
                                .siteUrl(siteUrl) // ✅ siteUrl 추가
                                .rarity(rarity)
                                .description(description)
                                .build()
                ));
    }

}
