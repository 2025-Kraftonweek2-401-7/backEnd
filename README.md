# 🧭 Developer Stamp Backend

개발자 사이트에서 활동한 흔적을 스탬프로 모아보세요!  
개발자 전용 디지털 스탬프 수집 확장 프로그램의 **백엔드 서버**입니다.

<p>
  <img src="https://img.shields.io/badge/Spring%20Boot-2.7.5-green?style=flat&logo=Spring-Boot"/>
  <img src="https://img.shields.io/badge/JPA-Hibernate-informational?style=flat&logo=hibernate"/>
  <img src="https://img.shields.io/badge/PostgreSQL-15-blue?style=flat&logo=PostgreSQL"/>
  <img src="https://img.shields.io/badge/Render-Cloud-blueviolet?style=flat&logo=Render"/>
</p>

---

## 🚀 프로젝트 소개

Developer Stamp는 사용자의 사이트 방문 기록을 기반으로 디지털 스탬프를 수집하고, 이를 시각적으로 보여주는 크롬 확장 프로그램입니다.  
이 저장소는 그 백엔드 API 서버를 담당합니다.

---

## 🛠️ 기술 스택

| 구분 | 스택 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot |
| ORM | Spring Data JPA |
| Database | PostgreSQL |
| Build Tool | Gradle |
| Deployment | Render |
| Documentation | Swagger (springdoc-openapi) |

---

## 📁 프로젝트 구조
src/
├── main/
│ ├── java/com/example/stamp/
│ │ ├── controller/ # REST API 컨트롤러
│ │ ├── domain/ # JPA 엔티티
│ │ ├── repository/ # JpaRepository 인터페이스
│ │ ├── service/ # 비즈니스 로직
│ │ └── config/ # 인증, CORS, 시드 등 설정
│ └── resources/
│ ├── application.yml
│ └── static/
└── test/ # 단위 테스트

---

---

## 📦 주요 기능

- ✅ 사용자 인증 (Google OAuth2)
- 🧭 스탬프 수집 및 조회
- 🌟 카테고리별/희귀도별 스탬프 필터링
- 🏅 스탬프 조건 충족 시 칭호 부여
- 🔐 JWT 기반 인증 및 권한 처리
- 🔄 배포 자동화 (Render)

---

## 🐳 배포 환경

- **Render**를 통한 CI/CD 구성
- 환경변수 기반 설정 (`application-prod.yml`)
- PostgreSQL 데이터베이스 연결

---

## ⚙️ 로컬 실행 방법

### 1. 환경 변수 설정

`.env` 또는 Render의 환경변수 설정에서 다음 값을 등록합니다:
DB_HOST=
DB_PORT=
DB_NAME=
DB_USER=
DB_PASS=
jwt.secret=
GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=

---
### 2. PostgreSQL 실행

```bash
# 예시 (도커로 실행)
docker run --name stamp-db -e POSTGRES_DB=stamp \
  -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=yourpassword \
  -p 5432:5432 -d postgres
---
### 3. 서버 실행
./gradlew build

### API 문서 
https://developer-stamp-lckq.onrender.com/oauth2/authorization/google (로그인 후 토큰 발급) 
https://developer-stamp-lckq.onrender.com/swagger-ui/index.html (스웨거)

🧑‍💻 개발자
| 이름           | 역할                      |
| ------------ | ----------------------- |
| 오주영 (OH JUYEONG) | Backend 개발, 아키텍처 설계, 배포 |

