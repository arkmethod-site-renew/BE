# arc-back

ARCMETHOD 리디자인 컨셉 — 백엔드 (Spring Boot 3.3 · Java 21 · Gradle · PostgreSQL)

## 사전 준비

1. PostgreSQL에 DB/계정 생성:
   ```sql
   CREATE DATABASE arcmethod;
   CREATE USER arc WITH PASSWORD 'arc';
   GRANT ALL PRIVILEGES ON DATABASE arcmethod TO arc;
   ```
2. 스키마 적용:
   ```bash
   psql -U arc -d arcmethod -f src/main/resources/db/schema.sql
   ```

## 실행

```bash
# 최초 실행 시 gradlew가 Gradle 8.10.2 배포판 + JDK 21(toolchain)을 자동으로 받아온다.
./gradlew bootRun        # http://localhost:8080
./gradlew build          # 빌드/테스트
./gradlew tasks          # (소스 없이) wrapper·toolchain 동작 확인용
```

> 로컬 JDK가 17이어도 됨 — `settings.gradle`의 foojay-resolver가 toolchain으로 JDK 21을 확보한다.

## 구조 (도메인별 5-레이어)

```
src/main/java/com/arcmethod/
├─ ArcBackApplication.java          # (직접 작성)
├─ common/{config,entity,exception}
├─ catalog/{domain,repository,service,controller,dto}
├─ member/  cart/  order/  review/  community/   # 동일 패턴
```

## 역할 분담

- **`.java` 파일** → 직접 작성 (Claude가 채팅으로 출력한 코드를 검수·타이핑).
- **빌드/의존성/설정/wrapper** → 세팅 완료:
  - `build.gradle`, `settings.gradle`, `application.yml`, `.gitignore`
  - `gradlew`, `gradlew.bat`, `gradle/wrapper/*`
  - `src/main/resources/db/schema.sql`

## 설정 메모

- `spring.jpa.hibernate.ddl-auto: validate` — 엔티티가 스키마와 어긋나면 부팅 실패(오타 방어). 먼저 schema.sql을 실행해 둘 것.
- 엔티티 ID는 전부 `GenerationType.IDENTITY` (스키마가 `GENERATED ALWAYS AS IDENTITY`).
- `orders`/`member`/`size` 등 예약어 충돌 테이블은 `@Table(name="...")` 명시.
- 개념 데모: 실제 결제 연동 없음.
# BE
