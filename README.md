# Share Anything

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0.2-6DB33F?style=flat-square&logo=spring&logoColor=white)
![React](https://img.shields.io/badge/React-18-61DAFB?style=flat-square&logo=react&logoColor=black)
![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=flat-square&logo=mariadb&logoColor=white)
![WebSocket & Stomp](https://img.shields.io/badge/WebSocket_&_Stomp-000000?style=flat-square)

**Share Anything**은 사용자들이 다양한 정보를 공유하고 소통할 수 있는 커뮤니티 플랫폼입니다. Spring Boot와 React를 기반으로 구축되었으며, 게시판부터 실시간 채팅까지 다채로운 커뮤니케이션 기능을 제공합니다.

## ✨ 현재 구현 기능
- **게시판 기능**: 유저는 카테고리별로 게시글을 작성하고 분류할 수 있습니다.
- **상호작용**: 각 게시글에 댓글을 작성하고, '좋아요'를 누를 수 있습니다.
- **소셜 네트워킹**: 다른 유저를 팔로우하고, 팔로우한 유저들이 작성한 글을 우선적으로 확인할 수 있습니다.
- **실시간 소통**: 실시간 채팅방 기능을 제공하여 다른 유저들과 자유롭게 대화할 수 있습니다.
- **채팅 보관**: 사용자가 구독한 채팅방의 기존 대화 기록을 효율적으로 불러옵니다.

## 🛠 사용한 기술
- **Backend**: Spring boot 3.0.2 (Java 17), Spring Data JPA, QueryDSL, Spring Security (JWT), WebSocket (Stomp)
- **Database**: MariaDB, H2 Database (테스트 환경)
- **Frontend**: React (TypeScript)
- **API Docs**: Swagger (Springdoc)

## 🗄️ 프로젝트 기능 및 구조

### ERD 다이어그램
<img width="1128" alt="image" src="https://user-images.githubusercontent.com/51548333/230779783-e1bd30e8-e77f-40b2-9f89-a76cfda1f591.png">

### 트러블 슈팅 및 관련 문제 경험
- **[JPA]** [QueryDsl] JPA N+1 문제 해결 및 최적화
- **[WebSocket]** [Stomp] 구독한 채팅방에 기존 채팅글 불러오는 기능 구현 문제 해결
- **[Security]** [Stomp] 소켓 통신 시의 JWT 토큰 처리 방법 및 인증 아키텍처
- **[Spring]** 전역 예외 처리 체계 구축
- **[Architecture]** 테스트용 데이터베이스와 서버용 데이터베이스 환경 분리
- **[Network]** Cors Origin 문제 도출 및 해결
