# Stage 00 - Preparation

작성일: 2026-03-04

## 목표
- JPA 실습을 시작하기 전에 환경/패키지/도메인 기준을 확정한다.

## 이번 Stage에서 확인할 것
- [ ] MySQL 연결 정상 여부
- [ ] Hibernate SQL 로그 출력 여부
- [ ] 패키지 구조 확정
- [ ] 첫 실습 도메인 확정 (`Product`, `Stock`)

## 확인 포인트
- 애플리케이션이 DB에 정상 연결되는가?
- JPA 설정이 실제로 반영되는가?
- 어디에 Entity/Service/Repository를 둘지 합의되었는가?

## 패키지 구조
- `harry.backend.rab.jpa.domain.product`
- `harry.backend.rab.jpa.service`
- `harry.backend.rab.jpa.api`
- `harry.backend.rab.jpa.infra.persistence`

## 현재 준비된 코드 뼈대
- Entity: [Product.java](/Users/kiwoong/Downloads/rab/src/main/java/harry/backend/rab/jpa/domain/product/Product.java)
- Repository: [ProductRepository.java](/Users/kiwoong/Downloads/rab/src/main/java/harry/backend/rab/jpa/infra/persistence/ProductRepository.java)
- Service: [ProductService.java](/Users/kiwoong/Downloads/rab/src/main/java/harry/backend/rab/jpa/service/ProductService.java)
- API: [ProductController.java](/Users/kiwoong/Downloads/rab/src/main/java/harry/backend/rab/jpa/api/ProductController.java)
- DTO: [ProductCreateRequest.java](/Users/kiwoong/Downloads/rab/src/main/java/harry/backend/rab/jpa/api/ProductCreateRequest.java), [ProductResponse.java](/Users/kiwoong/Downloads/rab/src/main/java/harry/backend/rab/jpa/api/ProductResponse.java)

## 도메인 후보
1. `Product`
- 가장 단순한 엔티티 학습에 적합

2. `Stock`
- 나중에 동시성/락 실험까지 자연스럽게 연결 가능

## 결정 기록
- 선택 도메인:
- 선택 이유:
- 보류 항목:

## 관찰 로그
- 실행 명령:
- 결과:
- SQL 로그:

## 다음 Stage로 넘길 것
- 최소 엔티티 1개
- 식별자 전략 결정
- 영속성 컨텍스트 실습 준비
