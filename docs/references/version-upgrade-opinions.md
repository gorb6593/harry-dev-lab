# Version Upgrade Opinions (Senior-level)

작성일: 2026-02-26

## 결론
- 이 프로젝트 기본값은 `Java 21 + Spring Boot 4.x`.
- `Java 17`은 비교/검증 기준선으로 유지.
- `Java 8`은 레거시 이해용 비교군으로만 사용.

## 왜 올려야 하는가
1. 플랫폼 기준선 변화
- Spring Boot 3.0부터 최소 Java 17이 필요하다.
- Spring Boot 4.0은 Spring Framework 7, Jakarta EE 11, Servlet 6.1 기준선을 요구한다.

2. 보안/운영 현실
- 구버전은 최신 보안 패치 흐름에서 멀어질수록 운영 리스크가 증가한다.
- OpenJDK 페이지에서 과거 릴리즈는 최신 보안 취약점 수정이 포함되지 않음을 명확히 경고한다.

3. 동시성 실험 목표 적합성
- Java 21의 Virtual Threads(JEP 444)는 이 프로젝트 핵심 주제(동시성)와 직접 연결된다.
- 실험 프로젝트에서 최신 LTS를 쓰는 것이 학습 가치와 실무 전이성이 높다.

## Java 8 vs 17 vs 21 (핵심만)
- Java 8: Lambda/Stream 도입. 현대 Java의 출발점.
- Java 17: 장기 안정화된 LTS, 최신 Spring의 최소 기준.
- Java 21: 최신 LTS, Virtual Threads 포함.

## Spring Boot 3 vs 4 (핵심만)
- Boot 3.0 (2022-11-24): Java 17+, Spring 6, Jakarta 전환.
- Boot 4.0 (2025-11-20): Java 17+, Spring 7, Jakarta EE 11 / Servlet 6.1, 3.x deprecated 제거.

## 출처
### Official
- https://openjdk.org/projects/jdk8/
- https://openjdk.org/projects/jdk/17/
- https://openjdk.org/projects/jdk/21/
- https://openjdk.org/jeps/444
- https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Release-Notes
- https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Release-Notes
- https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide
- https://github.com/spring-projects/spring-boot/releases/tag/v3.0.0
- https://github.com/spring-projects/spring-boot/releases/tag/v4.0.0
- https://jdk.java.net/21/

### Blog / Explanation
- https://www.baeldung.com/java-virtual-threads
- https://bell-sw.com/blog/java-8-vs-java-17-vs-java-21/
