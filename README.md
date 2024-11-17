# springCommerce
의류 상품을 관리하고 주문할 수 있는 커머스 서비스

## 개발환경
- Backend: Spring Boot 3, JDK 17
- Database: H2 (개발 환경)
- ORM: JPA, QueryDSL
- Messaging: Apache Kafka
- Cache: Redis
- Containerization: Docker

## Database
Table Diagram
![shop (5) 복사본](https://github.com/user-attachments/assets/1ea3f6cf-576b-4858-84cd-737e3e1d8307)

## 주요 기능
- 회원 관리
  - 회원 가입 
  - JWT 토큰 기반 인증 : 로그인, 토큰 재발급
  - Gateway 토큰 검증 필터
- 상품 조회
  - 상품 검색 및 필터링 : 커서 기반 페이지네이션 적용
- 주문 관리
  - 상품 테이블 재고 감소 kafka로 비동기 처리
  - 주문 상태 변경 자동화 배치
- 재고 관리
  - Redis를 활용한 재고 확인 및 차감
  - 재고 동기화를 위한 배치 처리
- 알림 서비스
  - Kafka를 이용한 비동기 알림 처리



