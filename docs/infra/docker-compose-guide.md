# Docker Compose Guide (MySQL + Redis Option)

작성일: 2026-02-27

## 1) 핵심
- 이 프로젝트는 `.env` 없이 바로 실행되도록 고정값으로 구성했다.
- 기본 명령은 아래 한 줄이다.

```bash
docker compose up -d mysql
```

## 2) 명령 해석 (`docker compose up -d mysql`)
- `docker compose`: 현재 폴더의 `docker-compose.yml` 사용
- `up`: 서비스 생성/시작
- `-d`: 백그라운드 실행(detached)
- `mysql`: 서비스 이름 선택자 (해당 서비스만 실행)

중요:
- `mysql`은 **Compose 내부 서비스 이름**이다.
- 다른 사람 PC에 "mysql이라는 이름의 프로그램"이 있어도 이 이름과는 무관하다.

## 3) 빠른 시작 순서
1. MySQL 실행
```bash
docker compose up -d mysql
```
2. 상태 확인
```bash
docker compose ps
```
3. 앱 실행
```bash
./gradlew bootRun
```
4. API 확인
```bash
curl http://localhost:25000/api/concurrency/unsafe/1
```

## 4) 실제 설정이 어떻게 적용됐는지 확인하는 법
1. 최종 Compose 설정 확인(가장 정확)
```bash
docker compose config
```

2. 포트 매핑 확인
```bash
docker compose ps
docker port $(docker compose ps -q mysql)
```

3. 로그 확인
```bash
docker compose logs -f mysql
```

## 5) 기본 고정값
### MySQL
- Image: `mysql:9.6.0`
- Host Port: `13306`
- Container Port: `3306`
- DB: `rab`
- User: `rab`
- Password: `rab1234`
- Root Password: `root1234`

JDBC URL:
```text
jdbc:mysql://localhost:13306/rab?useSSL=false&allowPublicKeyRetrieval=true&connectionTimeZone=Asia/Seoul&forceConnectionTimeZoneToSession=true
```

### Application
- Server Port: `25000`
- 실행 명령:
```bash
./gradlew bootRun
```

## 6) 에러가 나는 대표 원인
1. `port is already allocated`
- 원인: 로컬에서 13306 또는 6379를 이미 사용 중
- 해결: `docker-compose.yml`의 host port를 다른 값으로 변경

2. `docker compose` 명령 없음
- Docker Desktop/CLI 설치 필요

3. `Access denied` 또는 연결 실패
- 컨테이너 상태 확인: `docker compose ps`
- 필요 시 초기화: `docker compose down -v && docker compose up -d mysql`

## 7) 팀 공통 유의사항
1. 서비스 이름(`mysql`, `redis`)은 임의 변경하지 않는다.
2. 포트 변경 시 README/가이드를 같이 수정한다.
3. 실험 데이터 영향이 있으면 `down -v`로 초기화 후 재실행한다.
4. 실행 전후 `docker compose config`로 최종 설정을 확인한다.

## 참고
- https://docs.docker.com/compose/
- https://hub.docker.com/_/mysql
- https://hub.docker.com/_/redis
