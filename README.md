# 🎬 Megabox Clone Project (영화 예매 서비스)

메가박스(Megabox)의 핵심 기능을 웹으로 구현한 클론 코딩 프로젝트입니다.  
영화 정보 수집부터 예매, 극장 정보, 회원가입/로그인, 관람평 작성까지 실제 운영 가능한 수준의 서비스를 목표로 개발했습니다.

## 🛠 Tech Stack

### Backend
<img src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=Java&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"/> <img src="https://img.shields.io/badge/MyBatis-000000?style=flat-square&logo=MyBatis&logoColor=white"/> <img src="https://img.shields.io/badge/Maven-C71A36?style=flat-square&logo=Apache Maven&logoColor=white"/>

### Frontend
<img src="https://img.shields.io/badge/HTML5-E34F26?style=flat-square&logo=html5&logoColor=white"/> <img src="https://img.shields.io/badge/CSS3-1572B6?style=flat-square&logo=css3&logoColor=white"/> <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat-square&logo=javascript&logoColor=black"/> <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=flat-square&logo=Thymeleaf&logoColor=white"/>

### Database & Tools
<img src="https://img.shields.io/badge/MariaDB-003545?style=flat-square&logo=MariaDB&logoColor=white"/> <img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=flat-square&logo=IntelliJ IDEA&logoColor=white"/> <img src="https://img.shields.io/badge/Git-F05032?style=flat-square&logo=git&logoColor=white"/>

---

## 👥 팀 구성 및 역할 (Team Roles)

총 3명의 팀원이 개발하였으며, 저는 **영화 데이터 수집 및 영화 관련 핵심 로직**을 전담하였습니다.

### 👤 **본인 (Me) - 영화 파트 총괄**
> **"정확한 영화 정보 제공을 위한 데이터 수집 및 상세 서비스 구현"**
- **영화 데이터 자동화 (KOBIS + KMDB API 연동):**
    - 영화진흥위원회(KOBIS) API를 활용해 박스오피스 순위 및 기본 정보를 수집.
    - 한국영화데이터베이스(KMDB) API를 연동하여 고화질 포스터 및 줄거리 매칭.
    - **[Troubleshooting]** 서로 다른 두 API 간의 데이터 불일치(제목 띄어쓰기 등)로 인한 포스터 누락 문제를 '공백 제거 및 감독명 교차 검증 로직'으로 해결.
- **영화 상세 페이지 구현:**
    - 영화 정보, 스틸컷, 예고편, 관람 등급 등을 동적으로 렌더링.
- **사용자 인터랙션:**
    - 영화 '좋아요(Like)' 기능 구현 (AJAX 비동기 처리).
    - 실관람객 관람평(Review) 작성, 수정, 삭제 로직 구현.
- **상영 예정작 및 박스오피스 목록 페이지 구현.**

### 👤 **팀원 1 - 회원 및 설계**
- **회원가입 및 로그인:** SMTP 이메일 인증을 포함한 안전한 회원가입 프로세스 구현.
- **DB 설계 (ERD):** 전체 데이터베이스 관계도 설계 및 구축.
- **UI/UX:** 메인 헤더 및 전체적인 레이아웃 디자인 (Fragments 활용).

### 👤 **팀원 2 - 극장 및 예매**
- **극장 페이지:** 지역별 극장 목록 및 상세 정보 페이지 구현.
- **크롤링:** 타 사이트 예매 데이터를 크롤링하여 상영 시간표 데이터 확보.

---

## 📂 Project Structure

MVC 패턴을 기반으로 관심사를 분리하여 설계하였습니다.

```text
src/main
├── java/com/jmk/movie
│   ├── component       # 외부 API 통신 및 스케줄러 (MovieRunner, Movie.java)
│   ├── controllers     # Page 및 API 컨트롤러
│   ├── entities        # DB 테이블과 매핑되는 객체 (DTO/VO)
│   ├── mappers         # MyBatis Interface
│   ├── services        # 핵심 비즈니스 로직
│   └── utils           # 암호화 등 유틸리티
│
└── resources
    ├── mappers         # MyBatis XML Query (SQL)
    ├── static/assets   # 정적 리소스 (CSS, JS, Images)
    └── templates       # Thymeleaf HTML 뷰 파일
        ├── fragments   # Header, Footer 등 공통 모듈
        ├── movie       # 영화 관련 페이지 (list, detail)
        ├── theater     # 극장 관련 페이지
        └── user        # 로그인/회원가입 페이지