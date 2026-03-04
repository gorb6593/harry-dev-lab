# Stage 01 - Persistence Context

작성일: 2026-03-04

## 목표
- Entity, Identifier, Persistence Context의 최소 동작 원리를 이해한다.

## 학습 질문
- 엔티티는 왜 일반 DTO와 다른가?
- 영속성 컨텍스트는 왜 필요한가?
- 같은 트랜잭션 안에서 동일 엔티티를 다시 조회하면 왜 같은 객체처럼 보이는가?

## 실습 체크리스트
- [ ] `@Entity` 추가
- [ ] `@Id` 전략 선택
- [ ] `persist` 실습
- [ ] `find` 실습
- [ ] 같은 트랜잭션 내 1차 캐시 확인

## 코드 대상
- Entity: [Product.java](/Users/kiwoong/Downloads/rab/src/main/java/harry/backend/rab/jpa/domain/product/Product.java)
- Service: [ProductService.java](/Users/kiwoong/Downloads/rab/src/main/java/harry/backend/rab/jpa/service/ProductService.java)
- Repository/EntityManager: [ProductRepository.java](/Users/kiwoong/Downloads/rab/src/main/java/harry/backend/rab/jpa/infra/persistence/ProductRepository.java)

## 시작 시나리오
1. `POST /api/jpa/products`로 상품 생성
2. `GET /api/jpa/products/{id}`로 조회
3. 생성/조회 SQL 로그 확인
4. 그 다음 같은 트랜잭션에서 두 번 조회하는 코드를 직접 추가해 1차 캐시를 관찰

## 기록해야 할 것
### 개념 요약

### 실행 코드

### 생성 SQL / 로그

### 관찰 결과

### 흔한 오해
- `save()`를 호출해야만 영속 상태가 되는가?
- DB를 다시 읽었다고 생각했는데 왜 같은 객체처럼 보이는가?

### 실무 주의점

## 다음 Stage 연결
- 변경 감지(Dirty Checking)
- flush / commit 시점 구분
