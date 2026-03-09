# JPA_TO_QUERYDSL_PLAN.md

작성일: 2026-03-09

## 목적
- Java 21, Spring Boot 3.x 이상 환경에서 JPA의 원리부터 Querydsl의 타입 세이프 쿼리까지 단계적으로 학습한다.
- 모든 단계는 "왜 이 기능이 필요한가", "어떤 SQL이 나가는가", "실무에서 어디서 깨지는가"를 함께 다룬다.
- 공식 문서를 기준으로 학습하고, 프로젝트 코드와 문서에 관찰 결과를 남긴다.

## 공식 출처
- Jakarta Persistence 3.2: [jakarta.ee/specifications/persistence/3.2](https://jakarta.ee/specifications/persistence/3.2/)
- Jakarta Persistence 3.2 Specification: [jakarta-persistence-spec-3.2](https://jakarta.ee/specifications/persistence/3.2/jakarta-persistence-spec-3.2)
- Spring Data JPA Project: [spring.io/projects/spring-data-jpa](https://spring.io/projects/spring-data-jpa/)
- Spring Data JPA Reference: [docs.enterprise.spring.io Spring Data JPA 3.1.13](https://docs.enterprise.spring.io/spring-data-jpa-distribution/docs/3.1.13/reference/html/index.html)
- Querydsl Reference Guide: [querydsl.com/static/querydsl/latest/reference/html](https://querydsl.com/static/querydsl/latest/reference/html/)

## 학습 방식
1. 개념 학습
2. 최소 코드 구현
3. SQL 로그 관찰
4. 문서 기록
5. 리뷰 후 다음 단계 이동

## Stage 0. Environment Baseline
- 목표: 학습 환경과 기준선을 고정한다.
- 다룰 것:
  - MySQL 연결
  - JPA/Hibernate 로그 확인
  - 패키지 구조 확인
  - 첫 도메인(`Product`) 확인
- 산출물:
  - 실행 로그
  - Stage 00 문서 체크 완료

## Stage 1. Entity and Persistence Context
- 목표: 엔티티와 영속성 컨텍스트를 이해한다.
- 다룰 것:
  - `@Entity`, `@Id`, `@GeneratedValue`
  - `persist`, `find`
  - 1차 캐시
- 확인 질문:
  - 엔티티와 DTO의 차이는 무엇인가?
  - 같은 트랜잭션에서 왜 같은 객체처럼 보이는가?

## Stage 2. Dirty Checking and Flush
- 목표: JPA가 언제 UPDATE SQL을 만드는지 이해한다.
- 다룰 것:
  - Dirty Checking
  - flush vs commit
  - 트랜잭션 종료 시점 SQL
- 확인 질문:
  - 왜 `save()` 없이 변경이 반영되는가?

## Stage 3. Repository and Spring Data JPA
- 목표: JPA와 Spring Data JPA의 경계를 구분한다.
- 다룰 것:
  - `JpaRepository`
  - save/findById/findAll
  - EntityManager와 Repository의 관계
- 확인 질문:
  - Spring Data JPA는 무엇을 대신해 주는가?

## Stage 4. Transaction Boundary
- 목표: 서비스 계층에서 트랜잭션을 어떻게 설계하는지 이해한다.
- 다룰 것:
  - `@Transactional`
  - readOnly
  - OSIV off 전제
- 확인 질문:
  - 왜 Controller에 트랜잭션을 두면 안 되는가?

## Stage 5. Relationship Mapping
- 목표: 연관관계와 연관관계 주인을 이해한다.
- 다룰 것:
  - `@ManyToOne`
  - `@OneToMany`
  - 주인/비주인
  - 편의 메서드
- 도메인 확장:
  - `Product`
  - `Stock` 또는 `OrderItem`

## Stage 6. Fetch Strategy and N+1
- 목표: LAZY/EAGER와 N+1 문제를 직접 본다.
- 다룰 것:
  - LAZY 로딩
  - N+1 재현
  - fetch join
  - entity graph 비교
- 확인 질문:
  - N+1은 왜 "조회 설계" 문제인가?

## Stage 7. JPQL
- 목표: 객체 지향 쿼리 언어의 역할을 이해한다.
- 다룰 것:
  - JPQL 기본 문법
  - select/from/where/join
  - DTO projection
- 확인 질문:
  - SQL 대신 JPQL을 쓰는 이유는 무엇인가?

## Stage 8. Paging, Sorting, Projection
- 목표: 실무 조회 기능에 필요한 조회 패턴을 익힌다.
- 다룰 것:
  - Pageable
  - Slice/Page
  - interface/class projection
  - count query 비용
- 확인 질문:
  - Page와 Slice를 언제 구분해야 하는가?

## Stage 9. Locking and Concurrency
- 목표: JPA 락과 동시성 제어를 프로젝트 주제와 연결한다.
- 다룰 것:
  - `@Version`
  - optimistic locking
  - pessimistic locking
  - 재시도 전략
- 확인 질문:
  - 앱 락과 DB 락은 어디서 갈리는가?

## Stage 10. Querydsl Setup
- 목표: Querydsl이 왜 필요한지 이해하고 프로젝트에 연결한다.
- 다룰 것:
  - Querydsl 개념
  - Q 클래스 생성
  - build 설정
  - JPAQueryFactory 연결
- 확인 질문:
  - JPQL 문자열 대비 Querydsl의 장점은 무엇인가?

## Stage 11. Querydsl Basic Queries
- 목표: Querydsl 기본 조회를 쓸 수 있게 만든다.
- 다룰 것:
  - select/from/where
  - predicate 조합
  - 정렬/페이징
  - 단건/목록 조회
- 확인 질문:
  - Querydsl이 타입 세이프하다는 뜻은 무엇인가?

## Stage 12. Querydsl Dynamic Queries
- 목표: 실무형 조건 검색을 구현한다.
- 다룰 것:
  - BooleanBuilder
  - where 파라미터 조합
  - null-safe 조건 결합
- 확인 질문:
  - 동적 검색에서 메서드 분리가 왜 중요한가?

## Stage 13. Querydsl Join and Projection
- 목표: 복잡한 조회를 구조적으로 다룬다.
- 다룰 것:
  - join / leftJoin
  - fetchJoin
  - DTO projection
  - constructor/fields/bean projection 차이
- 확인 질문:
  - 엔티티 조회와 DTO 조회를 언제 분리해야 하는가?

## Stage 14. Querydsl Repository Customization
- 목표: Spring Data JPA와 Querydsl을 함께 쓸 수 있게 정리한다.
- 다룰 것:
  - custom repository
  - query repository 분리
  - 명령/조회 책임 분리
- 확인 질문:
  - 왜 모든 쿼리를 JpaRepository 하나에 몰아넣으면 안 되는가?

## Stage 15. Performance Review
- 목표: JPA/Querydsl 코드가 운영 가능한지 점검한다.
- 다룰 것:
  - SQL 로그 검토
  - 인덱스 관점 점검
  - count query 비용
  - batch/cascade/orphanRemoval 리뷰
- 확인 질문:
  - "작동하는 코드"와 "운영 가능한 코드"의 차이는 무엇인가?

## Stage 16. Final Consolidation
- 목표: 학습 결과를 공유 가능한 자산으로 정리한다.
- 다룰 것:
  - Stage별 요약
  - 공식 출처 링크 정리
  - 실수/안티패턴 목록
  - 블로그 초안

## 추천 진행 순서
1. Stage 0
2. Stage 1
3. Stage 2
4. Stage 3
5. Stage 4
6. Stage 5
7. Stage 6
8. Stage 7
9. Stage 8
10. Stage 9
11. Stage 10
12. Stage 11
13. Stage 12
14. Stage 13
15. Stage 14
16. Stage 15
17. Stage 16

## 현재 바로 할 일
1. `Product` 엔티티와 생성/조회 흐름을 먼저 실행한다.
2. `stage-01-persistence-context.md`에 SQL 로그와 관찰 내용을 기록한다.
3. Stage 2 전에는 Dirty Checking을 설명할 수 있어야 한다.
4. Querydsl은 Stage 10 전까지 도입하지 않는다.
