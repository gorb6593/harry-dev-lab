#!/usr/bin/env bash
# Spring Basic 3 전체 시나리오. 서버(25000)가 떠 있어야 한다.
#   bash src/main/java/harry/backend/rab/study/springbasic3/curl-examples.sh
set -u
BASE="${BASE:-http://localhost:25000}/study/spring-basic3/memos"
J='Content-Type: application/json'

step() { printf '\n\033[1;36m### %s\033[0m\n' "$1"; }

step "1. 생성 → 201 Created + Location"
curl -i -s -X POST "$BASE" -H "$J" -d '{"content":"Spring 공부"}'; echo

step "2. 목록 조회 → 200"
curl -i -s "$BASE"; echo

step "3. 단건 조회 → 200"
curl -i -s "$BASE/1"; echo

step "4. 수정 (POST + /update) → 200"
curl -i -s -X POST "$BASE/1/update" -H "$J" -d '{"content":"Spring 공부 (수정)"}'; echo

step "5. 없는 id 조회 → 404 problem+json"
curl -i -s "$BASE/999"; echo

step "6. 검증 실패 (빈 content) → 400 problem+json"
curl -i -s -X POST "$BASE" -H "$J" -d '{"content":""}'; echo

step "7. 깨진 JSON → 400 problem+json"
curl -i -s -X POST "$BASE" -H "$J" -d '{"content": '; echo

step "8. Content-Type 누락 → 415"
curl -i -s -X POST "$BASE" -d '{"content":"x"}'; echo

step "9. 삭제 (POST + /delete) → 204"
curl -i -s -X POST "$BASE/1/delete"; echo

step "10. 삭제 후 조회 → 404"
curl -i -s "$BASE/1"; echo
