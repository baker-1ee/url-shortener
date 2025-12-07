## 시스템 구조 요약

본 서비스는 단축 URL의 **고성능 조회 처리**, **DB 부하 최소화**, **트래픽 급증 대응**을 목표로 하여 다음과 같은 계층 구조로 설계되었다.

### 1 계층별 역할 정리

| 계층                              | 주요 역할                                                                                                   |
|---------------------------------|---------------------------------------------------------------------------------------------------------|
| **Caffeine Cache (서버 인메모리 캐시)** | 단축 URL → 원본 URL 매핑 조회를 메모리에서 즉시 처리하여 DB 조회를 최소화함. 최대 100개 엔트리 유지 및 접근 기반 만료(expire-after-access) 정책 적용. |
| **Redis (조회수 카운터 저장소)**         | 단축 URL 조회 시 발생하는 hitCount 증가를 실시간으로 처리하며, HINCRBY 기반 원자적 카운터로 초당 대량 요청을 감당함.                            |
| **DB (MySQL)**                  | shortCode 및 원본 URL 매핑의 영구 저장소 역할 수행. 조회수는 배치 프로세스에 의해 주기적으로 누적 반영됨.                                     |
| **Batch Scheduler**             | Redis에 누적된 delta hitCount를 일정 주기마다 일괄 조회하고, DB에 Write-Behind 방식으로 반영함.                                  |

본 구조는 읽기 중심 트래픽이 많은 단축 URL 서비스의 특성에 최적화되어 있으며,  
DB의 즉시 쓰기(write)를 최소화하여 고부하 상황에서도 안정적인 운영을 가능하게 한다.

---

### 2 캐싱 및 조회 구조

단축 URL 조회 요청 처리 흐름은 다음과 같다.

1. 사용자가 `/{shortCode}` 로 접근한다.
2. 서버는 **Caffeine Cache**에서 원본 URL 조회를 시도한다.
3. 캐시에 존재하지 않는 경우(DB 조회 필요), DB에서 조회 후 캐시에 저장한다.
4. 조회 성공 시 서버는 302 리다이렉트로 원본 URL로 이동시킨다.
5. 조회수(hitCount)는 DB에 즉시 반영하지 않고, **Redis에 HINCRBY로 누적 증가**시킨다.

이를 통해 리다이렉트 동작의 핵심 경로는 모두 캐시와 인메모리 구조에서 처리되며,  
DB 접근은 최소화된다.

---

### 3 Redis 기반 Write-Behind 설계

조회수 증가 연산은 요청마다 DB에 접근할 경우 DB 부하가 급증할 수 있으므로,  
다음과 같은 Write-Behind 구조를 도입하였다.

1. 조회 요청마다 Redis Hash에 `HINCRBY` 방식으로 atomic 증가.
2. 배치 스케줄러가 주기적으로 Redis에서 delta hitCount를 조회(fetch).
3. Lua Script를 사용하여 **fetch + clear(delete)** 작업을 하나의 원자적 연산으로 처리.
4. 조회된 delta 값을 DB에 일괄 반영하여 최종 누적 hitCount를 갱신.

### 4 Lua Script 원자성 보장

Redis는 단일 스레드 기반 명령 실행 모델을 제공하며,  
Lua Script는 “하나의 명령처럼” 실행되므로 아래 연산은 절대 중간에 끼어들기(interleaving) 되지 않는다.

```lua
local key = KEYS[1]
local entries = redis.call('HGETALL', key)
redis.call('DEL', key)
return entries
