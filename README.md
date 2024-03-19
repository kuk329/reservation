## 예약 구매 프로젝트

### ❗️ Repository 변경
https://github.com/preorder-shop

### 💡 프로젝트 소개
해당 프로젝트는 크게 두가지 주제로 나누어져 있습니다. 

**첫번째 주제는** Spring Data JPA를 사용한 crud 기능의 게시판 형태의 서비스를 제공합니다.  
사용자는 jwt 토큰 방식의 인증/인가를 통해 프로필, 글, 댓글 crud 등의 기능과  
팔로우 기능 다앙한 조건으로 사용자들의 활동을 조회할수 있는 기능을 제공합니다.

**두번째 주제는** 대용량 트래픽 발생시 일어날 수 있는 문제들을 가정하고 이를 처리하는 내용입니다.  
특정 시간 트래픽이 몰리는 예약 구매 상품 결제를 시뮬레이션하면서 데이터 동시성 문제를 해결하고,  
성능 향상을 위해 캐싱을 도입했습니다.

두 프로젝트 모두 도메인과 주요 기능을 중심으로 마이크로 서비스로 분리를 하였습니다.  
단, 프로젝트 관리상 각 서비스들을 서브 모듈로 구성하였습니다.
---
### ⚙️ 개발환경
- Java 17 , Gradle 8.5 , SpringBoot 3.2.2 , MySQL 8.0 , redis , docker

---
### 📁 디렉토리 구조
- 첫번째 주제
```bash
├── user_service
│   ├── client
│   ├── common
│   ├── controller
│   ├── domain
│   ├── provider
│   ├── service
│   └── repository
├── activity_service
│   ├── client
│   ├── common
│   ├── controller
│   ├── domain
│   ├── provider
│   ├── service
│   └── repository
└── newsfeed_service
    ├── client
    ├── common
    ├── controller
    └── domain
```
- 두번째 주제
```bash
├── product_service
└── payment_service

```
---
### 💻 실행 환경 구축
    docker-compose up -d
- 프로젝트에서 사용하는 mysql 과 redis 를 실행

---
### ⚒️ 프로젝트 아키텍처
사진

---
### 🗂️ ERD 구조

---
### 📜 API docs
