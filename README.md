# RAB (Real-world Architecture & Backend Concurrency Lab)

여러가지 실전적용을 하면서 학습하는 Spring Boot 프로젝트입니다.

## Goals
- Java 21 + Spring Boot 4 기준으로 동시성 이슈를 재현/해결
- Redis 분산 락, Pub/Sub, DB 락을 비교 실험
- 결과를 코드 + 문서 + 수치로 남겨 재현 가능한 학습 자산화

## Tech Stack (Current)
- Java 21
- Spring Boot 4.0.3
- Gradle 9.x (wrapper)

## Project Documents
- [STEP.md](/Users/kiwoong/Downloads/rab/STEP.md): 단계별 실행 계획
- [PROJECT_RULES.md](/Users/kiwoong/Downloads/rab/PROJECT_RULES.md): 실전형 개발/검증 규칙
- [step-01-version-comparison.md](/Users/kiwoong/Downloads/rab/docs/step-01-version-comparison.md): Java/Spring 버전 비교
- [step-01-java-spring-version-comparison-draft.md](/Users/kiwoong/Downloads/rab/docs/blog/step-01-java-spring-version-comparison-draft.md): 블로그 초안
- [concurrency-01.md](/Users/kiwoong/Downloads/rab/docs/concurrency-01.md): Step 2 동시성 재현 문서

## Run
```bash
./gradlew bootRun
```

기본 주소: `http://localhost:8080`

## Step 2 API (Unsafe)
현재는 인메모리 기반 `UnsafeStockService`로 동시성 문제를 의도적으로 노출합니다.

1. 재고 초기화
```bash
curl -X POST http://localhost:8080/api/concurrency/unsafe/init \
  -H 'Content-Type: application/json' \
  -d '{"productId":1,"quantity":100}'
```

2. 재고 1 감소
```bash
curl -X POST http://localhost:8080/api/concurrency/unsafe/decrease \
  -H 'Content-Type: application/json' \
  -d '{"productId":1,"quantity":1}'
```

3. 현재 재고 조회
```bash
curl http://localhost:8080/api/concurrency/unsafe/1
```

## Why In-memory First?
- 문제를 단순화해서 동시성 버그(Lost Update)를 빠르게 관찰하기 위함
- 이후 DB/Redis로 확장할 때 변경 지점을 명확히 분리하기 위함

## Roadmap (Next)
1. Step 3: `synchronized`/`ReentrantLock` 적용
2. Step 4: DB 낙관/비관 락 적용
3. Step 5: Redis 분산 락(Redisson) 도입
4. Step 6: Redis Pub/Sub 실습
5. Step 7: 웹 UI로 동시 요청 실험 자동화

## Open Source Plan
- 학습 기록이 아니라 재현 가능한 실험 저장소로 운영
- 모든 변화는 문서(`docs/`)와 함께 업데이트
- 주요 설계 결정은 ADR 문서(`docs/adr-xxxx-*.md`)로 기록

## Contributing (Draft)
1. 이슈에서 목적/가설을 먼저 명시
2. 작은 변경 단위로 PR 생성
3. 코드 + 문서 + 검증 결과를 같이 제출
4. `PROJECT_RULES.md`를 기준으로 리뷰

## License
- TBD
