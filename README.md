# share-anything

Spring + React 이용 프로젝트

## 현재 구현 기능
- 게시판 기능의 정보 공유 플랫폼
- 각 유저는 다른 유저를 팔로우 하여 작성한 글을 볼 수 있다.
- 유저는 게시글을 작성 할 수 있다.
- 게시글별 카테고리를 나눌 수 있다.
- 각 게시글에는 좋아요와 댓글을 달 수 있다.
- 채팅방 기능을 제공하여 다른 사용자들과 자유롭게 채팅 할 수 있다.
- 구독한 채팅방의 내용을 불러올 수 있다.

## 사용한 기술
- Spring boot 3.0.2(java 17)
- Jpa
- mariaDB
- Stomp
- SpringSecurity(jwt)
- React(TypeScript)
- Swagger

## 프로젝트의 기능

### ERD 다이어 그램
<img width="1128" alt="image" src="https://user-images.githubusercontent.com/51548333/230779783-e1bd30e8-e77f-40b2-9f89-a76cfda1f591.png">

### 관련 문제
[Query Dsl]JPA N+1 문제
[Stomp]구독한 채팅방에 기존 채팅글 불러오는 기능 문제
[Stomp]소켓통신시에 JWT 토큰 처리 방법
예외처리
테스트, 서버 데이터 베이스 분리
Cors Origin 문제 
