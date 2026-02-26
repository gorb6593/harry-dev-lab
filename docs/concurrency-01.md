# Concurrency 01 - Lost Update 재현 (Unsafe)

작성일: 2026-02-26

## 문제 상황
- 재고 차감 로직이 `read -> modify -> write` 패턴으로 구현되어 있고, 동시성 제어가 없다.

## 가설
- 여러 스레드가 동시에 같은 재고를 읽고 저장하면 `Lost Update`가 발생해 최종 재고 정합성이 깨진다.

## 재현 코드
- 대상: [UnsafeStockService.java](/Users/kiwoong/Downloads/rab/src/main/java/harry/backend/rab/concurrency/step02/UnsafeStockService.java)
- 웹 API: [UnsafeStockController.java](/Users/kiwoong/Downloads/rab/src/main/java/harry/backend/rab/concurrency/step02/UnsafeStockController.java)

## 재현 조건 (웹에서 수동 확인)
- 초기 재고: `100`
- 동시 요청 수: `100` (브라우저 또는 도구로 동시에 `decrease` 호출)
- 요청 동작: `1씩 차감`

## 기대 vs 실제 (예상)
- 기대 최종값: `0`
- 실제 최종값: `0보다 큰 값`이 남을 수 있음 (Lost Update)

## 해석
- `read -> modify -> write`가 원자적이지 않아서, 동시에 요청이 들어오면 마지막 쓰기가 이전 쓰기를 덮어쓴다.
- 즉, 요청은 여러 번 성공했는데 최종 재고는 기대보다 덜 감소하는 정합성 문제가 발생한다.

## 다음 액션
- Step 3에서 `synchronized` 또는 `ReentrantLock`으로 1차 해결 후 동일 테스트를 통과시키기.
