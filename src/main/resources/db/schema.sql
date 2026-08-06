-- ============================================================
-- ARCMETHOD Renewal Concept — Database Schema
-- Engine: PostgreSQL 15+
-- 개념 데모: 실제 결제 연동 없음. 주문은 목업.
-- 통화: 원화 정수(KRW, 소수 없음, CHECK >= 0)
-- ============================================================

-- updated_at 자동 갱신 트리거 함수
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- ------------------------------------------------------------
-- 회원
-- ------------------------------------------------------------
CREATE TABLE member (
    id            BIGINT       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    name          VARCHAR(50)  NOT NULL,
    phone         VARCHAR(20),
    role          VARCHAR(20)  NOT NULL DEFAULT 'USER',   -- USER / ADMIN
    status        VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE', -- ACTIVE / DORMANT / WITHDRAWN
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT now()
);
CREATE TRIGGER trg_member_updated BEFORE UPDATE ON member
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- ------------------------------------------------------------
-- 카테고리 (Outer / Top / Bottom …). New/Sale는 product 플래그로 분리.
-- ------------------------------------------------------------
CREATE TABLE category (
    id         BIGINT      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    parent_id  BIGINT      REFERENCES category (id),
    name       VARCHAR(50) NOT NULL,
    slug       VARCHAR(80) NOT NULL UNIQUE,
    sort_order INT         NOT NULL DEFAULT 0
);
CREATE INDEX idx_category_parent ON category (parent_id);

-- ------------------------------------------------------------
-- 색상 / 사이즈 마스터 (필터의 원천)
-- ------------------------------------------------------------
CREATE TABLE color (
    id         BIGINT      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR(40) NOT NULL UNIQUE,   -- Black, Grey, Blue, White, Beige …
    hex        VARCHAR(7)  NOT NULL,          -- #RRGGBB (스와치 표시용). CHAR면 JPA validate가 거부.
    sort_order INT         NOT NULL DEFAULT 0
);

CREATE TABLE size (
    id         BIGINT      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR(20) NOT NULL UNIQUE,   -- XS, S, M, L, FREE
    sort_order INT         NOT NULL DEFAULT 0
);

-- ------------------------------------------------------------
-- 상품 (코어 + PDP 정보 보강: 소재·관리법·모델·핏 속성)
-- ------------------------------------------------------------
CREATE TABLE product (
    id                 BIGINT       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    category_id        BIGINT       NOT NULL REFERENCES category (id),
    name               VARCHAR(200) NOT NULL,             -- 운영 메모((2차)/(예약배송)) 섞지 않음
    slug               VARCHAR(220) NOT NULL UNIQUE,
    description        TEXT,
    price              INTEGER      NOT NULL CHECK (price >= 0),        -- 원화 정가(원)
    discount_rate      SMALLINT     NOT NULL DEFAULT 0 CHECK (discount_rate BETWEEN 0 AND 100), -- %
    status             VARCHAR(20)  NOT NULL DEFAULT 'ON_SALE',        -- ON_SALE / SOLDOUT / PREORDER / HIDDEN
    is_new             BOOLEAN      NOT NULL DEFAULT FALSE,
    is_best            BOOLEAN      NOT NULL DEFAULT FALSE,
    preorder_ship_date DATE,                                            -- 예약배송일 (상품명에서 분리)
    -- 소재/핏 정보 (§0-⑤: PDP 정보 부족 해결)
    material           VARCHAR(255),                                    -- 예: 폴리 68% 레이온 30% 폴리우레탄 2%
    care_instructions  VARCHAR(500),
    thickness          VARCHAR(20),                                     -- 두꺼움 / 보통 / 얇음
    elasticity         VARCHAR(20),                                     -- 있음 / 약간 / 없음
    transparency       VARCHAR(20),                                     -- 비침 있음 / 약간 / 없음
    lining             VARCHAR(20),                                     -- 안감 있음 / 없음
    season             VARCHAR(20),
    -- 모델 착용 정보
    model_height_cm    SMALLINT,
    model_weight_kg    SMALLINT,
    model_size_worn    VARCHAR(20),
    created_at         TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at         TIMESTAMPTZ  NOT NULL DEFAULT now()
);
CREATE INDEX idx_product_category ON product (category_id);
CREATE INDEX idx_product_flags    ON product (is_new, is_best, status);
CREATE TRIGGER trg_product_updated BEFORE UPDATE ON product
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- ------------------------------------------------------------
-- 상품 이미지 (색상별 · MAIN/HOVER/DETAIL → §0-④ 호버 대체컷)
-- ------------------------------------------------------------
CREATE TABLE product_image (
    id         BIGINT       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id BIGINT       NOT NULL REFERENCES product (id) ON DELETE CASCADE,
    color_id   BIGINT       REFERENCES color (id),          -- 특정 색 컷이면 지정, 공용이면 NULL
    url        VARCHAR(500) NOT NULL,
    alt        VARCHAR(200),
    image_type VARCHAR(20)  NOT NULL DEFAULT 'MAIN',        -- MAIN / HOVER / DETAIL
    sort_order INT          NOT NULL DEFAULT 0
);
CREATE INDEX idx_image_product ON product_image (product_id, sort_order);
CREATE INDEX idx_image_color   ON product_image (color_id);

-- ------------------------------------------------------------
-- 상품 변형 = 색 × 사이즈 = 판매단위(SKU·재고) → §0-④ 필터/품절
-- ------------------------------------------------------------
CREATE TABLE product_variant (
    id               BIGINT      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id       BIGINT      NOT NULL REFERENCES product (id) ON DELETE CASCADE,
    color_id         BIGINT      NOT NULL REFERENCES color (id),
    size_id          BIGINT      NOT NULL REFERENCES size (id),
    sku              VARCHAR(60) NOT NULL UNIQUE,
    stock_qty        INT         NOT NULL DEFAULT 0 CHECK (stock_qty >= 0),
    additional_price INT         NOT NULL DEFAULT 0,        -- 옵션 추가금(원)
    UNIQUE (product_id, color_id, size_id)
);
CREATE INDEX idx_variant_color ON product_variant (color_id);
CREATE INDEX idx_variant_size  ON product_variant (size_id);

-- ------------------------------------------------------------
-- 사이즈별 실측 (key-value: 총장/어깨/가슴/소매 …) → §0-⑤ 실측표
-- ------------------------------------------------------------
CREATE TABLE product_measurement (
    id         BIGINT       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id BIGINT       NOT NULL REFERENCES product (id) ON DELETE CASCADE,
    size_id    BIGINT       NOT NULL REFERENCES size (id),
    item_key   VARCHAR(40)  NOT NULL,     -- 총장 / 어깨너비 / 가슴단면 / 소매길이 / 밑단
    value_cm   NUMERIC(5,1) NOT NULL,
    UNIQUE (product_id, size_id, item_key)
);

-- ------------------------------------------------------------
-- 리뷰 (착용 참고: 키/몸무게/구매사이즈) — 목업
-- ------------------------------------------------------------
CREATE TABLE review (
    id              BIGINT   GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id      BIGINT   NOT NULL REFERENCES product (id) ON DELETE CASCADE,
    member_id       BIGINT   NOT NULL REFERENCES member (id),
    rating          SMALLINT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    content         TEXT,
    reviewer_height SMALLINT,
    reviewer_weight SMALLINT,
    size_purchased  VARCHAR(20),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_review_product ON review (product_id, created_at);

CREATE TABLE review_image (
    id         BIGINT       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    review_id  BIGINT       NOT NULL REFERENCES review (id) ON DELETE CASCADE,
    url        VARCHAR(500) NOT NULL,
    sort_order INT          NOT NULL DEFAULT 0
);
CREATE INDEX idx_review_image ON review_image (review_id);

-- ------------------------------------------------------------
-- 찜 (♡)
-- ------------------------------------------------------------
CREATE TABLE wishlist (
    id         BIGINT      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    member_id  BIGINT      NOT NULL REFERENCES member (id) ON DELETE CASCADE,
    product_id BIGINT      NOT NULL REFERENCES product (id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (member_id, product_id)
);

-- ------------------------------------------------------------
-- 장바구니 (회원 or 게스트 세션)
-- ------------------------------------------------------------
CREATE TABLE cart_item (
    id         BIGINT      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    member_id  BIGINT      REFERENCES member (id) ON DELETE CASCADE,  -- 게스트면 NULL + session_id
    session_id VARCHAR(64),
    variant_id BIGINT      NOT NULL REFERENCES product_variant (id),
    quantity   INT         NOT NULL DEFAULT 1 CHECK (quantity > 0),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_cart_member  ON cart_item (member_id);
CREATE INDEX idx_cart_session ON cart_item (session_id);

-- ------------------------------------------------------------
-- 주문 (결제는 목업 · 상품명/가격 스냅샷 보존)
-- ------------------------------------------------------------
CREATE TABLE orders (
    id             BIGINT      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    member_id      BIGINT      NOT NULL REFERENCES member (id),
    order_no       VARCHAR(30) NOT NULL UNIQUE,          -- 표시용 주문번호
    status         VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- PENDING/PAID/SHIPPED/DONE/CANCELLED (목업)
    total_amount   INTEGER     NOT NULL CHECK (total_amount >= 0),
    receiver_name  VARCHAR(50),
    receiver_phone VARCHAR(20),
    address        VARCHAR(300),
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_order_member ON orders (member_id, created_at);

CREATE TABLE order_item (
    id           BIGINT       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_id     BIGINT       NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    variant_id   BIGINT       NOT NULL REFERENCES product_variant (id),
    product_name VARCHAR(200) NOT NULL,   -- 스냅샷
    color_name   VARCHAR(40)  NOT NULL,   -- 스냅샷
    size_name    VARCHAR(20)  NOT NULL,   -- 스냅샷
    unit_price   INTEGER      NOT NULL CHECK (unit_price >= 0),   -- 스냅샷(구매시점 판매가)
    quantity     INT          NOT NULL CHECK (quantity > 0)
);
CREATE INDEX idx_order_item_order ON order_item (order_id);

-- ------------------------------------------------------------
-- 커뮤니티 (Community 메뉴)
-- ------------------------------------------------------------
CREATE TABLE community_post (
    id         BIGINT       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    member_id  BIGINT       NOT NULL REFERENCES member (id),
    board_type VARCHAR(20)  NOT NULL DEFAULT 'STYLE',    -- NOTICE / STYLE / QNA
    title      VARCHAR(200) NOT NULL,
    content    TEXT         NOT NULL,
    view_count INT          NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT now()
);
CREATE INDEX idx_post_board ON community_post (board_type, created_at);
CREATE TRIGGER trg_post_updated BEFORE UPDATE ON community_post
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TABLE community_comment (
    id         BIGINT      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    post_id    BIGINT      NOT NULL REFERENCES community_post (id) ON DELETE CASCADE,
    member_id  BIGINT      NOT NULL REFERENCES member (id),
    content    TEXT        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_comment_post ON community_comment (post_id, created_at);
