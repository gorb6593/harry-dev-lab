# Day 2 총정리: Bean, 의존성 주입, 생명주기, 프록시

이 문서는 코드를 직접 찍어가며 Day 2 내용을 복습하기 위한 순서다.

## 1. API 입구: Controller도 Bean이다

파일: `api/BeanLearningController.java`

```java
@RestController
@RequestMapping("/api/day2/beans")
@RequiredArgsConstructor
public class BeanLearningController {

	private final BeanLearningService beanLearningService;
}
```

확인할 것:

- `@RestController`는 `@Controller`와 `@ResponseBody` 성격을 가진다.
- `@RestController`는 Component Scan 대상이므로 Controller가 Spring Bean으로 등록된다.
- `@RequiredArgsConstructor`는 Spring 기능이 아니라 Lombok 기능이다.
- Lombok이 `final` 필드를 받는 생성자를 만든다.
- Spring은 생성자 파라미터 타입을 보고 `BeanLearningService` Bean을 주입한다.

정리 문장:

```text
Controller는 내가 new로 만든 객체가 아니라 Spring IoC 컨테이너가 생성하고 관리하는 Bean이다.
```

## 2. Service Bean과 생성자 주입

파일: `service/BeanLearningService.java`

```java
@Service
@RequiredArgsConstructor
public class BeanLearningService {

	private final ApplicationContext applicationContext;
	private final Day2ProductRepository productRepository;
	private final DiscountPolicy discountPolicy;
	private final TransactionProbeService transactionProbeService;
	private final LifecycleTraceBean lifecycleTraceBean;
	private final TransactionSelfInvocationService transactionSelfInvocationService;
}
```

확인할 것:

- `@Service`는 `@Component`의 특수화다.
- Service도 Component Scan으로 Bean이 된다.
- 생성자 주입은 필수 의존성을 객체 생성 시점에 보장한다.
- `final` 필드와 생성자 주입을 같이 쓰면 의존성이 바뀌지 않는 구조가 된다.

정리 문장:

```text
Spring은 Bean을 만들 때 생성자 파라미터 타입을 기준으로 필요한 Bean을 찾아 연결한다.
```

## 3. Bean 등록 여부 확인

파일: `service/BeanLearningService.java`

```java
private boolean beanExists(String beanName) {
	return applicationContext.containsBean(beanName);
}
```

실행:

```http
GET /api/day2/beans/report
```

응답 예:

```json
{
  "controllerBeanExists": true,
  "serviceBeanExists": true,
  "repositoryBeanExists": true,
  "discountPolicyBeanExists": true
}
```

확인할 것:

- `ApplicationContext`는 Spring IoC 컨테이너의 대표 인터페이스다.
- `containsBean()`으로 특정 이름의 Bean이 등록되어 있는지 확인할 수 있다.
- 실무 비즈니스 코드에서 `ApplicationContext`를 직접 자주 쓰는 것은 권장하지 않는다. 여기서는 학습용이다.

정리 문장:

```text
Bean은 Spring 컨테이너 안에 이름과 타입으로 등록되어 관리된다.
```

## 4. 인터페이스 주입

파일: `service/DiscountPolicy.java`

```java
public interface DiscountPolicy {

	int discount(int originalPrice);
}
```

파일: `service/FixedDiscountPolicy.java`

```java
@Component
public class FixedDiscountPolicy implements DiscountPolicy {
}
```

파일: `service/BeanLearningService.java`

```java
private final DiscountPolicy discountPolicy;
```

실행:

```http
GET /api/day2/beans/price?originalPrice=10000
```

응답 예:

```json
{
  "originalPrice": 10000,
  "discountedPrice": 9000,
  "discountPolicy": "FixedDiscountPolicy"
}
```

확인할 것:

- `BeanLearningService`는 구체 클래스가 아니라 `DiscountPolicy` 인터페이스에 의존한다.
- `FixedDiscountPolicy`는 `DiscountPolicy` 구현체이고 `@Component`로 Bean 등록되어 있다.
- 현재 `DiscountPolicy` 타입 Bean이 하나뿐이므로 Spring이 자동 주입한다.
- 구현체가 둘 이상이면 `NoUniqueBeanDefinitionException`이 발생할 수 있다.
- 그때 `@Primary` 또는 `@Qualifier`로 선택 기준을 명시한다.

정리 문장:

```text
Spring은 주입 지점의 타입에 맞는 Bean을 찾고, 후보가 하나면 자동으로 주입한다.
```

## 5. Bean 생명주기

파일: `service/LifecycleTraceBean.java`

```java
@Component
public class LifecycleTraceBean {

	public LifecycleTraceBean() {
		recordEvent("constructor");
	}

	@PostConstruct
	public void initialize() {
		recordEvent("postConstruct");
	}

	@PreDestroy
	public void destroy() {
		recordEvent("preDestroy");
	}
}
```

실행:

```http
GET /api/day2/beans/lifecycle
```

응답 예:

```json
{
  "lifecycleBeanIsProxy": false,
  "events": [
    {"phase": "constructor"},
    {"phase": "postConstruct"}
  ]
}
```

서버 시작 로그:

```text
Day2 lifecycle event phase=constructor
Day2 lifecycle event phase=postConstruct
```

서버 종료 로그:

```text
Day2 lifecycle event phase=preDestroy
```

확인할 것:

- 생성자는 Bean 객체가 만들어질 때 실행된다.
- `@PostConstruct`는 의존성 주입이 끝난 뒤 초기화 단계에서 실행된다.
- `@PreDestroy`는 애플리케이션 종료 시점에 실행된다.
- `LifecycleTraceBean`에는 트랜잭션 같은 부가 기능이 없으므로 프록시가 아니다.

정리 문장:

```text
Bean은 생성, 의존성 주입, 초기화, 사용, 소멸의 생명주기를 가진다.
```

## 6. 프록시 Bean 확인

파일: `service/TransactionProbeService.java`

```java
@Service
public class TransactionProbeService {

	@Transactional(readOnly = true)
	public String currentTransactionMode() {
		return "readOnly";
	}
}
```

파일: `service/BeanLearningService.java`

```java
AopUtils.isAopProxy(transactionProbeService)
AopUtils.getTargetClass(transactionProbeService)
```

실행:

```http
GET /api/day2/beans/report
```

응답 예:

```json
{
  "transactionProbeRuntimeClass": "TransactionProbeService$$SpringCGLIB$$0",
  "transactionProbeIsProxy": true,
  "transactionProbeTargetClass": "TransactionProbeService"
}
```

확인할 것:

- 런타임 클래스는 실제 주입된 객체의 클래스다.
- 타깃 클래스는 프록시가 감싸는 원본 클래스다.
- `$$SpringCGLIB$$`가 보이면 Spring이 만든 CGLIB 프록시라는 뜻이다.
- `@Transactional` 같은 부가 기능은 프록시를 통해 적용된다.

정리 문장:

```text
Spring은 트랜잭션 같은 부가 기능을 적용하기 위해 원본 Bean을 프록시로 감쌀 수 있다.
```

## 7. `@Transactional`과 self-invocation

파일: `service/TransactionSelfInvocationService.java`

```java
@Service
public class TransactionSelfInvocationService {

	public TransactionProbeResponse compareExternalAndInternalCall() {
		TransactionState selfInvocationState = transactionalReadOnlyState();
		...
	}

	@Transactional(readOnly = true)
	public TransactionState transactionalReadOnlyState() {
		return new TransactionState(
			TransactionSynchronizationManager.isActualTransactionActive(),
			TransactionSynchronizationManager.isCurrentTransactionReadOnly()
		);
	}
}
```

파일: `service/BeanLearningService.java`

```java
TransactionSelfInvocationService.TransactionState externalCallState =
	transactionSelfInvocationService.transactionalReadOnlyState();

TransactionProbeResponse selfInvocationReport =
	transactionSelfInvocationService.compareExternalAndInternalCall();
```

실행:

```http
GET /api/day2/beans/transaction-proxy
```

응답 예:

```json
{
  "externalCallTransactionActive": true,
  "externalCallReadOnly": true,
  "selfInvocationTransactionActive": false,
  "selfInvocationReadOnly": false
}
```

확인할 것:

- 다른 Bean에서 `@Transactional` 메서드를 호출하면 프록시를 통과한다.
- 프록시를 통과하면 트랜잭션이 시작된다.
- 같은 클래스 내부에서 자기 자신의 메서드를 호출하면 `this.method()` 호출이므로 프록시를 통과하지 않는다.
- 프록시를 통과하지 않으면 `@Transactional`이 적용되지 않는다.

정리 문장:

```text
@Transactional은 프록시를 통과하는 외부 호출에서 적용된다.
같은 클래스 내부 호출은 프록시를 우회하므로 트랜잭션이 적용되지 않을 수 있다.
```

## 8. 지금까지의 핵심 모범답안

```text
Bean은 Spring IoC 컨테이너가 생성하고 관리하는 객체다.
@Component 계열 어노테이션이 붙은 클래스는 Component Scan 범위 안에 있으면 Bean으로 등록된다.

Spring은 Bean을 만들 때 생성자 파라미터 타입을 기준으로 필요한 Bean을 찾아 주입한다.
@RequiredArgsConstructor는 Lombok이 final 필드를 대상으로 생성자를 만들어주는 기능이다.

인터페이스 타입으로 의존하면 Spring은 해당 타입에 주입 가능한 구현체 Bean을 찾는다.
후보가 하나면 자동 주입하고, 둘 이상이면 @Primary 또는 @Qualifier가 필요하다.

Bean 생명주기는 생성자 호출, 의존성 주입, @PostConstruct, 사용, @PreDestroy 순서로 진행된다.

Spring은 @Transactional 같은 부가 기능을 적용하기 위해 Bean을 프록시로 감쌀 수 있다.
@Transactional은 프록시를 통과하는 호출에서 적용되며, 같은 클래스 내부 self-invocation에서는 적용되지 않을 수 있다.
```

## 9. Day 3 예고

Day 3는 Day 1의 재고 차감 코드와 Day 2의 프록시 개념을 연결한다.

주제:

```text
트랜잭션 경계, 영속성 컨텍스트, 변경 감지
```

핵심 질문:

1. `@Transactional`은 정확히 어디에 붙어야 하는가?
2. Repository에서 조회한 엔티티는 언제 영속 상태가 되는가?
3. `save()`를 다시 호출하지 않아도 update SQL이 나가는 이유는 무엇인가?
4. 트랜잭션이 끝나면 영속성 컨텍스트는 어떻게 되는가?
5. readOnly 트랜잭션은 어떤 의미가 있는가?
