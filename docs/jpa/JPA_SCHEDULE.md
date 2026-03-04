# JPA_SCHEDULE.md

작성일: 2026-03-04

## 목적
- Java 21 + Spring Boot 3.x 이상 환경에서 JPA를 "기능 사용법"이 아니라 "동작 원리" 중심으로 학습한다.
- Jakarta Persistence 공식 개념을 기준으로, Spring Data JPA에서 실제로 어떤 코드가 만들어지는지 이해한다.
- 모든 단계는 실습 코드 + 문서 + 관찰 결과를 남긴다.

## 항상 참조
- [JPA_RULES.md](/Users/kiwoong/Downloads/rab/docs/jpa/JPA_RULES.md)
- [Jakarta EE](https://jakarta.ee/)
- [Jakarta Persistence Specification](https://jakarta.ee/specifications/persistence/)
- [Jakarta Persistence 3.2 Specification PDF](https://jakarta.ee/specifications/persistence/3.2/jakarta-persistence-spec-3.2)

## 공식 기준 메모
- Jakarta EE 11 Web Profile에는 Jakarta Persistence 3.2가 포함된다.
- Jakarta Persistence 3.2는 record embeddable, `java.time.Instant`/`Year`, `getSingleResultOrNull()`, `UUID` 생성 관련 명확화 등 학습 가치가 큰 변경을 포함한다.

## 학습 원칙
1. API부터 보지 않고 개념부터 이해한다.
2. EntityManager 관점으로 먼저 이해한 뒤 Spring Data JPA로 연결한다.
3. 각 단계마다 "왜 이렇게 동작하는가"를 문장으로 설명할 수 있어야 한다.
4. 성능/트랜잭션/정합성 관점이 빠진 코드는 완료로 보지 않는다.

## Stage 0. 준비
- 목표: JPA 실습에 필요한 프로젝트 기준선 정리
- 할 일:
  - MySQL 연결 검증
  - JPA/Hibernate 의존성 검증
  - `domain`, `api`, `service`, `infra` 패키지 역할 확정
  - JPA 실습용 도메인 후보 확정 (`Product`, `Stock`, `Order`, `OrderItem` 중 선택)
- 완료 기준:
  - 앱이 MySQL에 정상 연결
  - DDL 생성 여부 확인
  - 패키지 역할 문서화

## Stage 1. JPA의 최소 동작 단위 이해
- 목표: Entity, Identifier, Persistence Context의 의미 이해
- 할 일:
  - `@Entity`, `@Id`, `@GeneratedValue` 실습
  - 동일 트랜잭션 안에서 1차 캐시 동작 확인
  - `persist`, `find`, `remove`의 의미 정리
- 질문:
  - 엔티티는 왜 단순 DTO와 다르게 다뤄야 하는가?
  - 영속성 컨텍스트는 왜 필요한가?
- 완료 기준:
  - 영속/비영속/준영속/삭제 상태를 설명 가능
  - 같은 트랜잭션에서 동일 엔티티 조회가 어떻게 동작하는지 설명 가능

## Stage 2. 변경 감지와 플러시
- 목표: JPA가 언제 SQL을 만들고, 왜 개발자가 setter 한 번으로 UPDATE를 보지 못하는지 이해
- 할 일:
  - Dirty Checking 실습
  - Flush 타이밍 확인
  - Transaction commit 시점 SQL 발생 관찰
- 질문:
  - 왜 save 호출 없이 UPDATE가 반영되는가?
  - flush와 commit은 무엇이 다른가?
- 완료 기준:
  - 변경 감지 메커니즘을 문장으로 설명 가능
  - flush 시점과 SQL 로그를 연결해서 이해 가능

## Stage 3. 연관관계 매핑
- 목표: 단방향/양방향, 연관관계 주인 개념 이해
- 할 일:
  - `@ManyToOne`, `@OneToMany` 실습
  - 연관관계 주인/비주인 정리
  - 편의 메서드 설계
- 질문:
  - 양방향은 왜 두 객체를 모두 맞춰줘야 하는가?
  - 컬렉션 쪽이 아니라 외래 키 가진 쪽이 주인인 이유는 무엇인가?
- 완료 기준:
  - 연관관계 주인을 정확히 설명 가능
  - 편의 메서드가 왜 필요한지 설명 가능

## Stage 4. Fetch 전략과 N+1
- 목표: EAGER/LAZY 차이와 N+1의 본질 이해
- 할 일:
  - LAZY 기본 전략 적용
  - N+1 재현
  - Fetch Join 또는 EntityGraph 해결 비교
- 질문:
  - 왜 EAGER가 문제를 숨길 수 있는가?
  - N+1은 JPA 문제가 아니라 조회 전략 문제라는 뜻은 무엇인가?
- 완료 기준:
  - N+1 재현/해결 로그와 함께 문서화
  - 조회 전용 쿼리와 엔티티 그래프 설계를 분리해 설명 가능

## Stage 5. 값 타입과 임베디드
- 목표: 엔티티와 값 타입을 구분해 모델링
- 할 일:
  - `@Embeddable`, `@Embedded` 실습
  - 값 타입 불변성 설계
  - 컬렉션 값 타입은 왜 조심해야 하는지 정리
- 완료 기준:
  - 엔티티/값 타입 차이를 모델 관점에서 설명 가능
  - 값 타입을 mutable하게 두었을 때 위험 설명 가능

## Stage 6. Repository와 쿼리 전략
- 목표: Spring Data JPA를 쓰더라도 내부가 EntityManager 기반임을 이해
- 할 일:
  - JpaRepository 기본 메서드 사용
  - 메서드명 쿼리와 JPQL 비교
  - 복잡한 조회는 명시 쿼리로 분리
- 완료 기준:
  - "왜 모든 것을 Repository 메서드명으로 해결하면 안 되는가" 설명 가능
  - 조회 성격에 따른 쿼리 전략 기준 수립

## Stage 7. 트랜잭션 경계와 서비스 계층
- 목표: `@Transactional`을 언제 어디에 붙일지 명확히 이해
- 할 일:
  - 서비스 계층 트랜잭션 경계 설계
  - 읽기 전용 트랜잭션과 쓰기 트랜잭션 구분
  - OSIV 끔 상태에서 조회/변경 흐름 정리
- 완료 기준:
  - 컨트롤러에 트랜잭션을 두지 않는 이유 설명 가능
  - 트랜잭션 경계와 영속성 컨텍스트 생명주기 연결 가능

## Stage 8. 락과 동시성
- 목표: JPA 기반 동시성 제어의 실제 해법 학습
- 할 일:
  - 낙관적 락 (`@Version`) 실습
  - 비관적 락 (`PESSIMISTIC_WRITE`) 실습
  - 재시도 전략과 예외 처리 정리
- 완료 기준:
  - 어느 상황에서 낙관/비관 락을 선택하는지 기준 제시 가능
  - 애플리케이션 락/DB 락/Redis 락 차이 연결 가능

## Stage 9. 고급 주제
- 목표: 실무형 품질에 필요한 고급 이슈 이해
- 할 일:
  - 배치 insert/update 전략
  - cascade / orphanRemoval 사용 기준
  - equals/hashCode, proxy 주의사항
  - 페이징/정렬/카운트 쿼리 비용 정리
- 완료 기준:
  - "돌아가는 코드"와 "운영 가능한 코드"의 차이를 설명 가능

## Stage 10. 정리와 블로그화
- 목표: 학습 내용을 외부에 공유 가능한 수준으로 정리
- 할 일:
  - 각 Stage별 핵심 요약 작성
  - 예제 코드 링크 정리
  - 실수 포인트 / 안티패턴 정리
  - 블로그 글 초안 작성
- 완료 기준:
  - 문서만 읽어도 재현 가능
  - 팀원이 동일하게 따라할 수 있음

## 추천 진행 순서
1. Stage 0
2. Stage 1
3. Stage 2
4. Stage 3
5. Stage 4
6. Stage 7
7. Stage 6
8. Stage 5
9. Stage 8
10. Stage 9
11. Stage 10

## 현재 시점의 바로 다음 액션
1. JPA 실습 도메인을 `Product` + `Stock`으로 확정
2. EntityManager 관점의 최소 엔티티 1개부터 시작
3. Stage 0 문서 확인 (`docs/jpa/stages/stage-00-preparation.md`)
4. Stage 1 문서 확인 (`docs/jpa/stages/stage-01-persistence-context.md`)
5. 그 다음에만 Repository 도입
