# Step 1 - Java / Spring Boot 버전 비교

작성일: 2026-02-26

## 1) 목적
- 프로젝트 기본 스택을 근거 기반으로 확정한다.
- 동시성/Redis/PubSub 실험에 영향을 주는 버전 차이를 먼저 정리한다.

## 2) 공식 근거 요약

### Java 8 vs 17 vs 21
| 항목 | Java 8 | Java 17 (LTS) | Java 21 (LTS) |
|---|---|---|---|
| 포지션 | 오래된 기준선(레거시 다수) | 안정적 LTS 전환 지점 | 최신 LTS (현재 실무 기준선) |
| 핵심 변화 | Lambda, Stream, `java.time`, `CompletableFuture` | 장기 안정성, 언어 개선 누적 | Virtual Threads, Sequenced Collections, 패턴 매칭 고도화 |
| 동시성 관점 | 전통 스레드/풀 중심 | 구조는 유사, JVM/GC/언어 개선 누적 | 고동시성 I/O에서 Virtual Threads 실험/적용 가치 큼 |
| 이 프로젝트 적용성 | 비교 기준선으로만 사용 | 보수적 선택 시 유효 | 실험 프로젝트 기본 JDK로 가장 적합 |

참고(공식):
- [What\'s New in JDK 8 (Oracle)](https://www.oracle.com/java/technologies/javase/8-whats-new.html)
- [JDK 17 Release Notes (OpenJDK Wiki)](https://openjdk.org/projects/jdk/17/)
- [JDK 21 Release Notes (OpenJDK Wiki)](https://openjdk.org/projects/jdk/21/)
- [Java SE 8 Release (OpenJDK Archive: 2014-03)](https://jdk.java.net/java-se-ri/8-MR6)

### Spring Boot 3 vs 4
| 항목 | Spring Boot 3.0 | Spring Boot 4.0 |
|---|---|---|
| 공개 시점 | 2022-11-24 | 2025-11-20 |
| 최소 Java | 17+ | 17+ |
| Spring Framework | 6.x | 7.x |
| Jakarta/Servlet 기준선 | Jakarta EE 전환, Servlet 6.0 | Jakarta EE 11, Servlet 6.1 |
| 마이그레이션 핵심 | 2.x -> 3.x 네임스페이스/호환성 이슈 큼 | 3.5.x 선행 업그레이드 권장, 3.x deprecated 제거 |

참고(공식):
- [Spring Boot 3.0 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Release-Notes)
- [Spring Boot 4.0 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Release-Notes)
- [Spring Boot 4.0 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide)
- [Spring Boot v3.0.0 Release (GitHub)](https://github.com/spring-projects/spring-boot/releases/tag/v3.0.0)
- [Spring Boot v4.0.0 Release (GitHub)](https://github.com/spring-projects/spring-boot/releases/tag/v4.0.0)

## 3) 블로그/실무 해설 참고 링크 (보조)
- [Inside Java - Java 21 API additions](https://inside.java/2023/11/01/case-for-sequenced-collections/)
- [BellSoft Blog - Java 8 vs Java 17 vs Java 21](https://bell-sw.com/blog/java-8-vs-java-17-vs-java-21/)
- [Baeldung - Virtual Threads in Java](https://www.baeldung.com/java-virtual-threads)
- [Spring Blog - Spring Boot 4.0.0 available now](https://spring.io/blog/2025/11/20/spring-boot-4-0-0-available-now)

## 4) 1차 결론 (이 프로젝트 기준)
- 기본 런타임: **Java 21**
- 프레임워크: **Spring Boot 4.x**
- 이유:
  - 동시성 실험에서 Virtual Threads를 비교군으로 활용 가능
  - Boot 4 기준선(Framework 7, Jakarta EE 11, Servlet 6.1)을 조기에 체득 가능
  - 업그레이드 관점 학습 목표(3 -> 4)를 같은 레포에서 재현 가능

## 5) 리스크 및 대응
- 리스크: Boot 4 신규 기준선으로 인해 라이브러리 호환성 이슈 발생 가능
- 대응:
  - Step 0에서 의존성 호환성 스냅샷 작성
  - 문제가 나는 라이브러리는 "대체안/버전고정/제거" 중 하나로 명시
  - 3.5.x 기준 재현 브랜치(비교 브랜치) 유지

## 6) Step 1 완료 조건
- [ ] 공식 출처 5개 이상 반영
- [ ] 블로그/해설 출처 3개 이상 반영
- [ ] 프로젝트 기본 스택(Java/Boot) 확정
- [ ] Step 2로 넘길 기술 부채/리스크 목록 작성
