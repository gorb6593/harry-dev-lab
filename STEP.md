# STEP.md

## 목표
- Java 21+ / Spring Boot 4 기반으로 동시성 이슈를 **재현하고 해결**한다.
- Redis 도입 전/후를 비교하고, 왜 필요한지 코드와 지표로 설명한다.
- Java 8/17/21, Spring Boot 3/4 차이를 실습 맥락에서 먼저 정리한다.
- 간단한 화면(UI)에서 동시 요청을 발생시켜 눈으로 확인한다.

## 필수 참조
- 프로젝트 진행 전/중/후 항상 [PROJECT_RULES.md](./PROJECT_RULES.md)를 확인한다.

## 진행 원칙
- 매 단계마다 `문제 상황 -> 재현 코드 -> 실패 테스트 -> 해결 코드 -> 통과 테스트 -> 회고` 순서로 진행
- 변경 사항은 작은 단위로 커밋
- 측정 가능한 지표(성공/실패 수, 처리 시간, 정합성) 기록

---

## Step 0. 기본 세팅
- [ ] 프로젝트 환경 점검
  - [ ] Java 21 이상 확인
  - [ ] Spring Boot 버전 확인(4 사용 가능 여부, 필요 시 3.x로 시작 후 업그레이드)
  - [ ] Gradle Wrapper/테스트 실행 확인
- [ ] 공통 패키지 구조 설계 (`domain`, `service`, `api`, `infra`, `testsupport`)
- [x] `README.md`에 실험 목표/실행 방법 추가
- [x] Docker Compose 인프라 문서화 (`docs/infra/docker-compose-guide.md`)

## Step 1. 버전 비교 정리 (선행)
- [x] `docs/step-01-version-comparison.md` 작성
- [x] 블로그 초안 작성 (`docs/blog/step-01-java-spring-version-comparison-draft.md`)
- [x] Java 8 vs 17 vs 21 비교
  - [x] 언어/라이브러리/동시성 관점 핵심 차이
  - [x] 이 프로젝트에서 실무적으로 중요한 항목만 선별
- [x] Spring Boot 3 vs 4 비교
  - [x] 시스템 요구사항/의존성 기준선 차이
  - [x] 마이그레이션 리스크(깨질 수 있는 포인트) 정리
- [x] 공식 문서 + 블로그 참고 링크 기록
- [x] 최종 결론: 이 프로젝트의 기본 스택 확정

## Step 2. 동시성 문제 첫 재현 (기초)
- [x] 시나리오 선정: `재고 감소` 또는 `포인트 차감`
- [x] 의도적으로 취약한 코드 작성
  - [x] 단순 `read -> modify -> write`
  - [x] 동기화/락 없음
- [x] 웹 확인용 API 작성
  - [x] `init / decrease / current` API
  - [x] 나중에 UI에서 호출 가능한 형태로 구성
- [ ] 동시성 테스트 작성
  - [ ] `ExecutorService` + `CountDownLatch`
  - [ ] 예상 결과와 실제 결과 불일치 확인
- [x] 왜 문제가 발생하는지 설명 문서화
  - [x] Race Condition
  - [x] Lost Update

## Step 3. 해결책 1 - 애플리케이션 레벨 락
- [ ] `synchronized` 또는 `ReentrantLock` 적용
- [ ] 단일 인스턴스 기준 문제 해결 확인
- [ ] 한계 정리
  - [ ] 멀티 인스턴스 환경에서 무력함
  - [ ] 처리량 저하 가능성

## Step 4. 해결책 2 - DB 레벨 제어
- [ ] 낙관적 락(`@Version`) 적용
- [ ] 비관적 락(`PESSIMISTIC_WRITE`) 비교
- [ ] 재시도 로직 적용 여부 실험
- [ ] 장단점 비교표 작성

## Step 5. Redis 도입 (분산 락)
- [ ] Redis 연동 구성
- [ ] 분산 락 구현 (예: Redisson)
- [ ] 멀티 인스턴스 가정 테스트
- [ ] 왜 Redis가 필요한지 정리
  - [ ] 인스턴스 간 상호 배제
  - [ ] 락 획득/해제 실패 시나리오

## Step 6. Pub/Sub 실습
- [ ] Redis Pub/Sub 기본 예제 구현
- [ ] 적용 시나리오
  - [ ] 이벤트 알림
  - [ ] 캐시 무효화 신호
- [ ] 메시지 유실/순서/재처리 한계 정리
- [ ] Stream/Kafka와의 차이 간단 비교

## Step 7. 화면(UI) 만들기
- [ ] 최소 기능 화면
  - [ ] 상품/포인트 현재 값 조회
  - [ ] 동시 요청 수 입력
  - [ ] `동시 차감 실행` 버튼
- [ ] 실행 결과 표시
  - [ ] 요청 성공 수
  - [ ] 실패 수
  - [ ] 최종 값
  - [ ] 소요 시간
- [ ] 해결 방식 선택 드롭다운
  - [ ] 취약 코드
  - [ ] 앱 락
  - [ ] DB 락
  - [ ] Redis 락

## Step 8. 통합 리포트
- [ ] 각 방식별 결과를 표로 정리
  - [ ] 정합성
  - [ ] 처리량
  - [ ] 구현 난이도
  - [ ] 운영 난이도
- [ ] 최종 아키텍처 제안
- [ ] 다음 확장 과제 정의
  - [ ] JMeter/Gatling 부하 테스트
  - [ ] 관측성(Micrometer/Prometheus/Grafana)
  - [ ] 장애 주입 테스트

---

## 지금 바로 할 일
- [x] `Step 1` 문서 작성 시작 (`docs/step-01-version-comparison.md`)
- [x] Java 21 + Spring Boot 4 조합의 리스크/혜택 1차 결론 작성
- [x] 결론 확정 후 Step 2(동시성 재현 코드)로 이동
