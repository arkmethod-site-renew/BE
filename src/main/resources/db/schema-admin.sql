-- ============================================================
-- ARCMETHOD — 관리자(Admin) 스키마 추가분
-- Engine: PostgreSQL 15+
-- schema.sql 이후에 실행한다.
--   psql -d arcmethod -f schema-admin.sql
--
-- 공통 원칙: 노출 제어가 필요한 모든 것은 (is_active + starts_at + ends_at) 3종 세트를 갖는다.
--   - starts_at/ends_at 이 NULL이면 "제한 없음"
--   - 노출 조건: is_active AND (starts_at IS NULL OR now() >= starts_at)
--                          AND (ends_at   IS NULL OR now() <  ends_at)
-- ============================================================

-- ------------------------------------------------------------
-- 시즌 (26 SS 등) — 상품을 묶는 단위
-- ------------------------------------------------------------
CREATE TABLE season (
    id         BIGINT      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code       VARCHAR(20) NOT NULL UNIQUE,        -- 26SS, 26FW
    name       VARCHAR(100) NOT NULL,              -- "26 SS — Sleek Modest"
    concept    VARCHAR(300),                       -- 시즌 컨셉 카피
    is_active  BOOLEAN     NOT NULL DEFAULT TRUE,
    starts_at  TIMESTAMPTZ,
    ends_at    TIMESTAMPTZ,
    sort_order INT         NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT ck_season_period CHECK (ends_at IS NULL OR starts_at IS NULL OR ends_at > starts_at)
);
CREATE TRIGGER trg_season_updated BEFORE UPDATE ON season
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- 기존 product.season(varchar)은 표시용으로 두고, 관계는 FK로 관리
ALTER TABLE product ADD COLUMN season_id BIGINT REFERENCES season (id);
CREATE INDEX idx_product_season ON product (season_id);

-- ------------------------------------------------------------
-- 배너 (메인 히어로 / 띠배너 등) — 기간 예약 노출
-- ------------------------------------------------------------
CREATE TABLE banner (
    id          BIGINT       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    position    VARCHAR(30)  NOT NULL,             -- MAIN_HERO / MAIN_STRIP / SHOP_TOP / POPUP
    title       VARCHAR(200) NOT NULL,
    subtitle    VARCHAR(300),
    image_url   VARCHAR(500) NOT NULL,
    mobile_image_url VARCHAR(500),                 -- 모바일 전용 컷 (없으면 image_url 사용)
    link_url    VARCHAR(500),
    text_color  VARCHAR(20)  NOT NULL DEFAULT 'LIGHT', -- LIGHT / DARK (배경 대비용)
    is_active   BOOLEAN      NOT NULL DEFAULT TRUE,
    starts_at   TIMESTAMPTZ,
    ends_at     TIMESTAMPTZ,
    sort_order  INT          NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT ck_banner_period CHECK (ends_at IS NULL OR starts_at IS NULL OR ends_at > starts_at)
);
CREATE INDEX idx_banner_live ON banner (position, is_active, starts_at, ends_at);
CREATE TRIGGER trg_banner_updated BEFORE UPDATE ON banner
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- ------------------------------------------------------------
-- 프로모션 (기간 한정 할인) — product.discount_rate(상시)와 별개
-- 우선순위: 유효한 프로모션이 있으면 그 할인율이 상시 할인율을 덮어쓴다.
-- ------------------------------------------------------------
CREATE TABLE promotion (
    id            BIGINT       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name          VARCHAR(200) NOT NULL,           -- "여름 시즌오프"
    discount_rate SMALLINT     NOT NULL CHECK (discount_rate BETWEEN 0 AND 100),
    scope         VARCHAR(20)  NOT NULL,           -- ALL / CATEGORY / PRODUCT
    category_id   BIGINT       REFERENCES category (id),
    is_active     BOOLEAN      NOT NULL DEFAULT TRUE,
    starts_at     TIMESTAMPTZ,
    ends_at       TIMESTAMPTZ,
    priority      INT          NOT NULL DEFAULT 0, -- 높을수록 우선
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT ck_promo_period CHECK (ends_at IS NULL OR starts_at IS NULL OR ends_at > starts_at)
);
CREATE INDEX idx_promotion_live ON promotion (is_active, starts_at, ends_at);
CREATE TRIGGER trg_promotion_updated BEFORE UPDATE ON promotion
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- scope = PRODUCT 일 때 대상 상품들
CREATE TABLE promotion_product (
    promotion_id BIGINT NOT NULL REFERENCES promotion (id) ON DELETE CASCADE,
    product_id   BIGINT NOT NULL REFERENCES product (id) ON DELETE CASCADE,
    PRIMARY KEY (promotion_id, product_id)
);

-- ------------------------------------------------------------
-- 공지사항 — community_post와 분리(운영 공지는 노출 기간·상단고정이 필요)
-- ------------------------------------------------------------
CREATE TABLE notice (
    id         BIGINT       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title      VARCHAR(200) NOT NULL,
    content    TEXT         NOT NULL,
    category   VARCHAR(30)  NOT NULL DEFAULT 'GENERAL', -- GENERAL / SHIPPING / RESTOCK / EVENT
    is_pinned  BOOLEAN      NOT NULL DEFAULT FALSE,
    is_active  BOOLEAN      NOT NULL DEFAULT TRUE,
    starts_at  TIMESTAMPTZ,
    ends_at    TIMESTAMPTZ,
    view_count INT          NOT NULL DEFAULT 0,
    author_id  BIGINT       REFERENCES member (id),
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT ck_notice_period CHECK (ends_at IS NULL OR starts_at IS NULL OR ends_at > starts_at)
);
CREATE INDEX idx_notice_live ON notice (is_active, is_pinned, starts_at, ends_at);
CREATE TRIGGER trg_notice_updated BEFORE UPDATE ON notice
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- ------------------------------------------------------------
-- 관리자 활동 로그 (누가 무엇을 바꿨는지)
-- ------------------------------------------------------------
CREATE TABLE admin_log (
    id          BIGINT       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    member_id   BIGINT       REFERENCES member (id),
    action      VARCHAR(20)  NOT NULL,             -- CREATE / UPDATE / DELETE
    entity_type VARCHAR(40)  NOT NULL,             -- PRODUCT / BANNER / NOTICE …
    entity_id   BIGINT,
    summary     VARCHAR(300),
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);
CREATE INDEX idx_admin_log_created ON admin_log (created_at DESC);

-- ------------------------------------------------------------
-- 회원 관리 보강 (계정관리 화면용)
-- ------------------------------------------------------------
ALTER TABLE member ADD COLUMN last_login_at TIMESTAMPTZ;
ALTER TABLE member ADD COLUMN memo VARCHAR(300);   -- 운영자 메모

-- ------------------------------------------------------------
-- 시드: 기본 시즌 + 배너 + 공지
-- ------------------------------------------------------------
INSERT INTO season (code, name, concept, is_active, starts_at, ends_at, sort_order) VALUES
    ('26SS', '26 SS — Sleek Modest', '정제된 실루엣과 냉감 소재', TRUE,
     TIMESTAMPTZ '2026-02-01 00:00+09', TIMESTAMPTZ '2026-08-31 23:59+09', 1),
    ('26FW', '26 FW', '준비 중', FALSE, NULL, NULL, 2);

UPDATE product SET season_id = (SELECT id FROM season WHERE code = '26SS');

INSERT INTO banner (position, title, subtitle, image_url, link_url, text_color, is_active, starts_at, ends_at, sort_order) VALUES
    ('MAIN_HERO', 'Sleek Modest', '26 SS — Latest Release',
     'https://picsum.photos/seed/arc-hero-1/1600/2000', '/shop?flag=new', 'LIGHT', TRUE,
     TIMESTAMPTZ '2026-02-01 00:00+09', TIMESTAMPTZ '2026-08-31 23:59+09', 1),
    ('MAIN_HERO', 'Layered Ease', '26 SS — Apex Collection',
     'https://picsum.photos/seed/arc-hero-2/1600/2000', '/shop', 'LIGHT', TRUE, NULL, NULL, 2),
    ('MAIN_STRIP', '20,000원 이상 무료배송', '사이즈 교환 가능',
     '', NULL, 'DARK', TRUE, NULL, NULL, 1);

INSERT INTO notice (title, content, category, is_pinned, is_active) VALUES
    ('26 SS 컬렉션 배송 안내',
     '예약 배송 상품은 7월 31일부터 순차 발송됩니다.', 'SHIPPING', TRUE, TRUE),
    ('사이즈 교환 정책 안내',
     '착용 흔적이 없는 경우 수령 후 7일 이내 교환 가능합니다.', 'GENERAL', FALSE, TRUE);
