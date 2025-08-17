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
        // ✅ GitHub Stamp
        addStampIfNotExists("GitHub Stamp", "https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png", "github.com", Rarity.COMMON, Category.BACKEND, "커밋 하나에도 의미를 담는 당신에게");
        addStampIfNotExists("GitHub Pro Stamp", "https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png", "github.com", Rarity.RARE, Category.BACKEND, "오픈소스 기여까지 하는 당신은 이미 고수!");
        addStampIfNotExists("GitHub Legend Stamp", "https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png", "github.com", Rarity.LEGENDARY, Category.BACKEND, "별이 빛나는 GitHub의 전설, 바로 당신");


// ✅ Spring Initializr Stamp
        addStampIfNotExists("Spring Stamp", "https://start.spring.io/images/icon-spring-initializr.svg", "start.spring.io", Rarity.COMMON, Category.BACKEND, "스프링 프로젝트의 시작은 여기서부터");
        addStampIfNotExists("Spring Developer Stamp", "https://start.spring.io/images/icon-spring-initializr.svg", "start.spring.io", Rarity.RARE, Category.BACKEND, "Spring Security, JPA도 자유자재!");
        addStampIfNotExists("Spring Master Stamp", "https://start.spring.io/images/icon-spring-initializr.svg", "start.spring.io", Rarity.LEGENDARY, Category.BACKEND, "Spring 생태계를 정복한 자에게 수여됨");


// ✅ VS Code Stamp
        addStampIfNotExists("VS Code Stamp", "https://code.visualstudio.com/assets/images/code-stable.png", "code.visualstudio.com", Rarity.COMMON, Category.TOOL, "가볍고 빠른 개발 시작의 동반자");
        addStampIfNotExists("VS Code Hacker Stamp", "https://code.visualstudio.com/assets/images/code-stable.png", "code.visualstudio.com", Rarity.RARE, Category.TOOL, "단축키 마스터한 당신의 에디팅은 예술");
        addStampIfNotExists("VS Code Overlord Stamp", "https://code.visualstudio.com/assets/images/code-stable.png", "code.visualstudio.com", Rarity.LEGENDARY, Category.TOOL, "에디터를 무기로 삼은 전설적인 개발자");


// ✅ ChatGPT Stamp
        addStampIfNotExists("ChatGPT Stamp", "https://upload.wikimedia.org/wikipedia/commons/0/04/ChatGPT_logo.svg", "chat.openai.com", Rarity.COMMON, Category.AI, "궁금할 때마다 찾아오는 AI 친구");
        addStampIfNotExists("ChatGPT Power User Stamp", "https://upload.wikimedia.org/wikipedia/commons/0/04/ChatGPT_logo.svg", "chat.openai.com", Rarity.RARE, Category.AI, "프롬프트의 장인, 생산성 폭발!");
        addStampIfNotExists("ChatGPT Architect Stamp", "https://upload.wikimedia.org/wikipedia/commons/0/04/ChatGPT_logo.svg", "chat.openai.com", Rarity.LEGENDARY, Category.AI, "AI를 완벽히 도구화한 미래형 인간");


// ✅ GitHub Actions Stamp
        addStampIfNotExists("Actions Stamp", "https://github.githubassets.com/images/modules/site/features/actions-icon-actions.svg", "github.com/features/actions", Rarity.COMMON, Category.DEVOPS, "간단한 테스트 자동화를 시작했다면");
        addStampIfNotExists("CI/CD Engineer Stamp", "https://github.githubassets.com/images/modules/site/features/actions-icon-actions.svg", "github.com/features/actions", Rarity.RARE, Category.DEVOPS, "자동화 파이프라인을 자유롭게 다루는 자");
        addStampIfNotExists("DevOps God Stamp", "https://github.githubassets.com/images/modules/site/features/actions-icon-actions.svg", "github.com/features/actions", Rarity.LEGENDARY, Category.DEVOPS, "CI/CD 시스템을 지배하는 전설의 DevOps");


// ✅ Tailwind CSS Stamp
        addStampIfNotExists("Tailwind Stamp", "https://tailwindcss.com/favicons/favicon-32x32.png", "tailwindcss.com", Rarity.COMMON, Category.FRONTEND, "빠르게 UI를 그리는 데일리 툴");
        addStampIfNotExists("Tailwind Wizard Stamp", "https://tailwindcss.com/favicons/favicon-32x32.png", "tailwindcss.com", Rarity.RARE, Category.FRONTEND, "디자인과 코드의 경계를 넘나드는 마법사");
        addStampIfNotExists("Tailwind Virtuoso Stamp", "https://tailwindcss.com/favicons/favicon-32x32.png", "tailwindcss.com", Rarity.LEGENDARY, Category.FRONTEND, "클래스 이름만으로 UI를 지휘하는 지휘자");


// ✅ React Stamp
        addStampIfNotExists("React Stamp", "https://upload.wikimedia.org/wikipedia/commons/a/a7/React-icon.svg", "reactjs.org", Rarity.COMMON, Category.FRONTEND, "컴포넌트를 처음 만들었다면 이 우표를");
        addStampIfNotExists("React Developer Stamp", "https://upload.wikimedia.org/wikipedia/commons/a/a7/React-icon.svg", "reactjs.org", Rarity.RARE, Category.FRONTEND, "Hooks와 상태 관리를 즐기는 개발자");
        addStampIfNotExists("React Architect Stamp", "https://upload.wikimedia.org/wikipedia/commons/a/a7/React-icon.svg", "reactjs.org", Rarity.LEGENDARY, Category.FRONTEND, "React 생태계를 설계하는 프론트엔드의 신");


// ✅ LeetCode Stamp
        addStampIfNotExists("LeetCode Stamp", "https://upload.wikimedia.org/wikipedia/commons/1/19/LeetCode_logo_black.png", "leetcode.com", Rarity.COMMON, Category.LEARNING, "한 문제씩 푸는 당신의 성실함에");
        addStampIfNotExists("LeetCode Warrior Stamp", "https://upload.wikimedia.org/wikipedia/commons/1/19/LeetCode_logo_black.png", "leetcode.com", Rarity.RARE, Category.LEARNING, "일일 3문제? 취업 준비 끝판왕");
        addStampIfNotExists("LeetCode Champion Stamp", "https://upload.wikimedia.org/wikipedia/commons/1/19/LeetCode_logo_black.png", "leetcode.com", Rarity.LEGENDARY, Category.LEARNING, "모든 문제를 풀고 나면 보이는 전설의 우표");


// ✅ Notion Stamp
        addStampIfNotExists("Notion Stamp", "https://upload.wikimedia.org/wikipedia/commons/4/45/Notion_app_logo.png", "notion.so", Rarity.COMMON, Category.TOOL, "기본 정리는 노션으로 시작!");
        addStampIfNotExists("Notion Organizer Stamp", "https://upload.wikimedia.org/wikipedia/commons/4/45/Notion_app_logo.png", "notion.so", Rarity.RARE, Category.TOOL, "템플릿을 직접 만들 줄 아는 정리왕");
        addStampIfNotExists("Notion Guru Stamp", "https://upload.wikimedia.org/wikipedia/commons/4/45/Notion_app_logo.png", "notion.so", Rarity.LEGENDARY, Category.TOOL, "모든 생각을 구조화하는 노션의 달인");


// ✅ Kubernetes Stamp
        addStampIfNotExists("Kubernetes Stamp", "https://upload.wikimedia.org/wikipedia/commons/3/39/Kubernetes_logo_without_workmark.svg", "kubernetes.io", Rarity.COMMON, Category.DEVOPS, "컨테이너 오케스트레이션 입문자에게");
        addStampIfNotExists("K8s Operator Stamp", "https://upload.wikimedia.org/wikipedia/commons/3/39/Kubernetes_logo_without_workmark.svg", "kubernetes.io", Rarity.RARE, Category.DEVOPS, "배포 자동화와 스케일링까지 자유자재");
        addStampIfNotExists("Kubernetes Sage Stamp", "https://upload.wikimedia.org/wikipedia/commons/3/39/Kubernetes_logo_without_workmark.svg", "kubernetes.io", Rarity.LEGENDARY, Category.DEVOPS, "클러스터를 다스리는 자, 전설로 남다");
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
