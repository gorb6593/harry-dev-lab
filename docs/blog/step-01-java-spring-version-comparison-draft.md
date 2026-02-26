# [초안] Java 8/17/21, Spring Boot 3/4 비교와 업그레이드 판단

작성일: 2026-02-26

## 한 줄 결론
신규 실험 프로젝트 기준으로는 `Java 21 + Spring Boot 4.x`가 가장 타당하다.

## 왜 올려야 하나
- Spring Boot 3.0부터 최소 Java 17이 필요하다.
- Spring Boot 4.0은 Spring Framework 7, Jakarta EE 11, Servlet 6.1 기준선을 요구한다.
- 동시성 실험 관점에서 Java 21의 Virtual Threads(JEP 444)는 직접적인 실험 가치가 있다.

## Java 8 vs 17 vs 21
- Java 8 (GA: 2014-03-18): Lambda/Stream 도입, 현대 Java의 출발점.
- Java 17 (GA: 2021-09-14): 장기 안정 LTS, 최신 Spring 라인의 최소 기준.
- Java 21 (GA: 2023-09-19): 최신 LTS, Virtual Threads 포함.

## Spring Boot 3 vs 4
- Boot 3.0 (출시: 2022-11-24): Java 17+, Spring 6, Jakarta 전환.
- Boot 4.0 (출시: 2025-11-20): Java 17+, Spring 7, Jakarta EE 11 / Servlet 6.1, 3.x deprecated 제거.

## 팀 의견(실전 기준)
- Java 8은 비교군으로만 유지하고 신규 구현 기준으로 채택하지 않는다.
- Boot 4로 먼저 시작하되, 의존성 호환성 이슈는 문서로 관리한다.
- 업그레이드는 선호가 아니라 지원 기준/보안/운영 리스크 관점에서 판단한다.

## 출처
### 공식
- https://openjdk.org/projects/jdk8/
- https://openjdk.org/projects/jdk/17/
- https://openjdk.org/projects/jdk/21/
- https://openjdk.org/jeps/444
- https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Release-Notes
- https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Release-Notes
- https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide
- https://github.com/spring-projects/spring-boot/releases/tag/v3.0.0
- https://github.com/spring-projects/spring-boot/releases/tag/v4.0.0

### 보조(블로그/해설)
- https://www.baeldung.com/java-virtual-threads
- https://bell-sw.com/blog/java-8-vs-java-17-vs-java-21/
