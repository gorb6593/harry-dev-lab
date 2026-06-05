# Day 3: 트랜잭션 경계, 영속성 컨텍스트, 변경 감지

## 목표

Day 3는 Day 1의 재고 변경 코드와 Day 2의 프록시 개념을 JPA 트랜잭션으로 연결한다.

오늘 확인할 역량은 다음과 같다.

- `@Transactional`이 붙은 Service 메서드가 트랜잭션 경계가 되는 이유를 설명한다.
- Repository에서 조회한 엔티티가 영속 상태가 되는 시점을 설명한다.
- `save()`를 다시 호출하지 않아도 update SQL이 실행되는 이유를 설명한다.
- `EntityManager.contains()`로 영속 상태를 관찰한다.
- `flush`, `clear`, dirty checking의 의미를 설명한다.
- `readOnly` 트랜잭션의 의미와 한계를 설명한다.

## 제공된 구조

```text
day3_260604
├── api
│   ├── Day3ProductController
│   ├── Day3ProductCreateRequest
│   ├── Day3ProductPriceChangeRequest
│   └── Day3ProductResponse
├── domain
│   └── Day3Product
├── repository
│   └── Day3ProductRepository
└── service
    ├── Day3ProductNotFoundException
    ├── PersistenceObservationResponse
    └── ProductPersistenceService
```

## 실행

```bash
./gradlew bootRun
```

`day3.http`를 위에서부터 실행한다.

## 과제 1. 트랜잭션 경계 찾기

파일: `service/ProductPersistenceService.java`

확인할 메서드:

```java
@Transactional
public PersistenceObservationResponse changePriceWithDirtyChecking(...)
```

설명할 내용:

1. Controller가 아니라 Service에 `@Transactional`을 둔 이유는 무엇인가?
2. 이 메서드가 시작될 때 어떤 트랜잭션이 열리는가?
3. 메서드가 정상 종료되면 어떤 일이 발생하는가?
4. 예외가 발생하면 어떤 일이 발생하는가?

## 과제 2. 영속 상태 관찰

파일: `service/ProductPersistenceService.java`

확인할 코드:

```java
Day3Product product = getProduct(productId);
boolean containsAfterFind = entityManager.contains(product);
```

응답에서 확인할 값:

```json
{
  "containsAfterFind": true,
  "containsAfterChange": true
}
```

설명할 내용:

1. Repository에서 조회한 엔티티는 왜 영속 상태인가?
2. `EntityManager.contains(product)`가 `true`라는 것은 무엇을 의미하는가?
3. 트랜잭션이 끝난 뒤 이 객체는 계속 영속 상태인가?

## 과제 3. 변경 감지 관찰

실행:

```http
POST /api/day3/products/{productId}/price/dirty-checking
```

확인할 코드:

```java
product.changePrice(newPrice);
entityManager.flush();
```

설명할 내용:

1. `productRepository.save(product)`를 호출하지 않았는데 왜 update SQL이 실행되는가?
2. JPA는 언제 최초 상태와 현재 상태를 비교하는가?
3. `flush()`는 commit인가?
4. `clear()`는 왜 호출했는가?

## 과제 4. readOnly 트랜잭션 관찰

실행:

```http
POST /api/day3/products/{productId}/price/read-only
```

확인할 코드:

```java
@Transactional(readOnly = true)
public PersistenceObservationResponse changePriceInsideReadOnlyTransaction(...)
```

설명할 내용:

1. `readOnly = true`는 DB write를 절대 막는가?
2. 객체 필드는 바뀔 수 있는가?
3. 실제 DB 값은 바뀌었는가?
4. 이 실험 결과가 DB와 JPA 구현체에 따라 어떻게 달라질 수 있는가?

## 채점 기준

총점은 100점이다.

| 항목 | 배점 | 확인 기준 |
| --- | ---: | --- |
| 트랜잭션 경계 이해 | 20 | Service 트랜잭션 경계와 프록시 호출 관계를 설명한다. |
| 영속성 컨텍스트 이해 | 25 | 영속 상태, 준영속 상태, `contains()` 의미를 설명한다. |
| 변경 감지 이해 | 25 | `save()` 없이 update SQL이 실행되는 이유를 설명한다. |
| flush/clear 이해 | 15 | flush와 commit의 차이, clear의 의미를 설명한다. |
| readOnly 이해 | 15 | readOnly 트랜잭션의 최적화 성격과 한계를 설명한다. |

80점 이상이면 Day 3 완료로 판단한다.

## 생각해 볼 질문

1. `@Transactional` 메서드가 같은 클래스 내부 호출이면 Day 2의 self-invocation 문제가 다시 발생하는가?
2. `findById()`로 조회한 엔티티와 `new Day3Product()`로 직접 만든 객체의 상태는 어떻게 다른가?
3. `flush()`가 실행되어도 트랜잭션이 롤백되면 DB 변경은 어떻게 되는가?
4. `open-in-view: false`일 때 Controller에서 지연 로딩을 접근하면 어떤 문제가 생길 수 있는가?
5. readOnly 트랜잭션 안에서 엔티티를 변경하는 코드는 왜 위험한가?
