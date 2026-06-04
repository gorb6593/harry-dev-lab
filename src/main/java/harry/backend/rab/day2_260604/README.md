# Day 2: Spring Bean, 의존성 주입, 프록시

## 목표

Day 2는 API 흐름을 따라가면서 Spring Bean의 실제 동작을 확인하는 과제다.

오늘 확인할 역량은 다음과 같다.

- Controller, Service, Repository가 어떻게 Bean으로 등록되는지 설명한다.
- 생성자 주입과 `@RequiredArgsConstructor`의 관계를 설명한다.
- 인터페이스 타입 주입이 실제 구현체와 어떻게 연결되는지 확인한다.
- `@Transactional`이 붙은 Bean이 프록시로 감싸지는 이유를 설명한다.
- Bean 이름, 런타임 클래스, 타깃 클래스를 구분한다.

## 제공된 구조

```text
day2_260604
├── api
│   └── BeanLearningController
├── domain
│   └── Day2Product
├── repository
│   ├── Day2ProductRepository
│   └── Day2MemoryProductRepository
└── service
    ├── BeanLearningService
    ├── BeanReportResponse
    ├── DiscountPolicy
    ├── FixedDiscountPolicy
    ├── PriceCalculationResponse
    └── TransactionProbeService
```

## 실행

애플리케이션을 실행한다.

```bash
./gradlew bootRun
```

`day2.http`를 실행하거나 아래 요청을 직접 호출한다.

```http
GET http://localhost:25000/api/day2/beans/report
GET http://localhost:25000/api/day2/beans/price?originalPrice=10000
```

## 과제 1. Bean 등록 방식 분석

아래 클래스들이 왜 Bean이 되는지 설명한다.

| 클래스 | 어노테이션 | Bean 등록 이유 |
| --- | --- | --- |
| `BeanLearningController` | `@RestController` | |
| `BeanLearningService` | `@Service` | |
| `Day2MemoryProductRepository` | `@Repository` | |
| `FixedDiscountPolicy` | `@Component` | |
| `TransactionProbeService` | `@Service` | |

주의할 점:

- `@RestController`, `@Service`, `@Repository`는 모두 Component Scan의 대상이다.
- `@Service`, `@Repository`는 기술적으로 `@Component`의 특수화다.
- `@Repository`는 예외 변환 같은 부가 의미가 있다.

## 과제 2. 생성자 주입 분석

`BeanLearningController`와 `BeanLearningService`는 `@RequiredArgsConstructor`를 사용한다.

설명할 내용:

1. `@RequiredArgsConstructor`는 Spring 기능인가, Lombok 기능인가?
2. 어떤 필드가 생성자 파라미터가 되는가?
3. 생성자가 하나뿐이면 왜 `@Autowired`를 생략할 수 있는가?
4. 필드 주입보다 생성자 주입이 나은 이유는 무엇인가?

## 과제 3. 인터페이스 주입 분석

`BeanLearningService`는 구체 클래스가 아니라 인터페이스 타입을 주입받는다.

```java
private final Day2ProductRepository productRepository;
private final DiscountPolicy discountPolicy;
```

설명할 내용:

1. 실제 주입되는 구현체는 무엇인가?
2. 구현체가 하나일 때 Spring은 어떻게 선택하는가?
3. 구현체가 두 개 이상이면 어떤 오류가 발생할 수 있는가?
4. 그때 `@Qualifier`, `@Primary`는 어떻게 다르게 해결하는가?

## 과제 4. 프록시 Bean 분석

`TransactionProbeService`에는 `@Transactional(readOnly = true)` 메서드가 있다.

`GET /api/day2/beans/report` 응답에서 아래 값을 확인한다.

- `transactionProbeRuntimeClass`
- `transactionProbeIsProxy`
- `transactionProbeTargetClass`

설명할 내용:

1. 런타임 클래스와 타깃 클래스가 왜 다를 수 있는가?
2. `@Transactional`은 왜 프록시가 필요한가?
3. 같은 클래스 내부에서 자기 자신의 `@Transactional` 메서드를 호출하면 어떤 문제가 생길 수 있는가?

## 과제 5. Bean 생명주기 분석

아래 요청을 실행한다.

```http
GET /api/day2/beans/lifecycle
```

응답에서 `events`를 확인한다.

- `constructor`
- `postConstruct`

`preDestroy`는 애플리케이션 종료 시 실행되므로 HTTP 응답에는 보통 보이지 않는다. 종료 로그나 디버깅으로 확인한다.

설명할 내용:

1. 생성자는 언제 호출되는가?
2. `@PostConstruct`는 생성자보다 왜 나중에 실행되는가?
3. `@PreDestroy`는 언제 호출되는가?
4. 이 Bean은 프록시인가, 원본 객체인가?

## 과제 6. 프록시와 self-invocation 분석

아래 요청을 실행한다.

```http
GET /api/day2/beans/transaction-proxy
```

기대 응답의 의미는 다음과 같다.

```json
{
  "externalCallTransactionActive": true,
  "externalCallReadOnly": true,
  "selfInvocationTransactionActive": false,
  "selfInvocationReadOnly": false
}
```

`externalCall...` 값은 다른 Bean이 `@Transactional` 메서드를 호출한 결과다. 프록시를 통과하므로 트랜잭션이 활성화된다.

`selfInvocation...` 값은 같은 클래스 내부에서 자기 자신의 `@Transactional` 메서드를 호출한 결과다. 프록시를 통과하지 않으므로 트랜잭션이 활성화되지 않는다.

설명할 내용:

1. 왜 외부 호출은 트랜잭션이 적용되는가?
2. 왜 내부 호출은 트랜잭션이 적용되지 않는가?
3. 이 문제가 실무에서 어떤 버그로 이어질 수 있는가?
4. 해결 방법은 무엇인가?

## 채점 기준

총점은 100점이다.

| 항목 | 배점 | 확인 기준 |
| --- | ---: | --- |
| Bean 등록 이해 | 20 | stereotype 어노테이션과 component scan을 설명한다. |
| 생성자 주입 이해 | 20 | Lombok, 생성자 주입, `@Autowired` 생략 조건을 설명한다. |
| 인터페이스 주입 이해 | 20 | 구현체 선택, 중복 Bean 오류, 해결 방식을 설명한다. |
| Bean 생명주기 이해 | 15 | 생성자, `@PostConstruct`, `@PreDestroy` 실행 시점을 설명한다. |
| 프록시 이해 | 20 | `@Transactional`, 프록시, self-invocation의 관계를 설명한다. |
| 코드 흐름 설명 | 10 | Controller에서 응답까지 호출 흐름을 정확히 설명한다. |

80점 이상이면 Day 2 완료로 판단한다.

## 생각해 볼 질문

1. `BeanLearningController`를 `new`로 직접 만들면 어떤 문제가 생기는가?
2. `DiscountPolicy` 구현체가 2개가 되면 애플리케이션은 어떻게 반응하는가?
3. `@Repository`와 `@Component`는 실무에서 어떤 의미 차이가 있는가?
4. `ApplicationContext`를 비즈니스 코드에서 직접 사용하는 것은 언제 문제가 되는가?
5. Bean이 프록시인지 확인하는 코드가 운영 비즈니스 로직에 들어가도 되는가?

## 선택 과제

- `RateDiscountPolicy`를 추가해서 중복 Bean 오류를 재현한다.
- `@Qualifier`로 특정 구현체를 선택한다.
- `@Primary`로 기본 구현체를 지정한다.
- 같은 클래스 내부 self-invocation으로 `@Transactional`이 적용되지 않는 예제를 만든다.
