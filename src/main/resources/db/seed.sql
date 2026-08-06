-- ============================================================
-- ARCMETHOD — 시드 데이터 (개념 데모용)
-- 상품명·가격은 라이브 사이트 기준, 이미지는 플레이스홀더.
-- 실제 상품컷은 친구 동의하에 교체 예정.
-- 실행: psql -d arcmethod -f seed.sql  (schema.sql 적용 후)
-- ============================================================

TRUNCATE TABLE product_measurement, product_variant, product_image,
               order_item, orders, cart_item, wishlist,
               review_image, review, product, category, color, size,
               community_comment, community_post, member
        RESTART IDENTITY CASCADE;

-- ------------------------------------------------------------
-- 색상 / 사이즈
-- ------------------------------------------------------------
INSERT INTO color (name, hex, sort_order) VALUES
    ('Black', '#1a1a1a', 1),
    ('Grey',  '#8f8f8f', 2),
    ('White', '#efece6', 3),
    ('Beige', '#d8ccb8', 4),
    ('Blue',  '#9db2c6', 5);

INSERT INTO size (name, sort_order) VALUES
    ('S', 1), ('M', 2), ('L', 3), ('FREE', 4);

-- ------------------------------------------------------------
-- 카테고리
-- ------------------------------------------------------------
INSERT INTO category (name, slug, sort_order) VALUES
    ('Outer',  'outer',  1),
    ('Top',    'top',    2),
    ('Bottom', 'bottom', 3);

-- ------------------------------------------------------------
-- 상품 (§0-⑤: 소재·핏·모델 정보를 처음부터 채운다)
-- ------------------------------------------------------------
INSERT INTO product (
    category_id, name, slug, description, price, discount_rate, status,
    is_new, is_best, preorder_ship_date,
    material, care_instructions, thickness, elasticity, transparency, lining, season,
    model_height_cm, model_weight_kg, model_size_worn
) VALUES
-- 1. 목넥 립 슬리브리스
((SELECT id FROM category WHERE slug = 'top'),
 'Mock Neck Rib Sleeveless Top', 'mock-neck-rib-sleeveless-top',
 '몸의 라인을 따라 흐르는 립 조직의 슬리브리스 탑. 목선을 감싸는 모크넥으로 단정함을 더했습니다.',
 61000, 31, 'ON_SALE', TRUE, FALSE, NULL,
 '폴리에스터 68% 레이온 30% 폴리우레탄 2%', '찬물 손세탁 · 건조기 사용 금지 · 다림질 금지',
 '보통', '있음', '약간', '안감 없음', '26 SS', 172, 50, 'FREE'),

-- 2. 카고 포켓 쇼츠
((SELECT id FROM category WHERE slug = 'bottom'),
 'Cargo Pocket Shorts', 'cargo-pocket-shorts',
 '입체 포켓으로 실용성을 더한 미니 쇼츠. 탄탄한 조직감으로 실루엣이 무너지지 않습니다.',
 68000, 0, 'ON_SALE', TRUE, FALSE, NULL,
 '폴리에스터 92% 폴리우레탄 8%', '드라이클리닝 권장 · 단독 세탁',
 '보통', '약간', '없음', '안감 있음', '26 SS', 172, 50, 'S'),

-- 3. 립 니트 홀터 탑
((SELECT id FROM category WHERE slug = 'top'),
 'Rib Knit Halter Top', 'rib-knit-halter-top',
 '어깨선을 드러내는 홀터넥 립 니트. 냉감 소재로 여름까지 이어 입을 수 있습니다.',
 54000, 0, 'ON_SALE', TRUE, FALSE, NULL,
 '레이온 60% 나일론 38% 폴리우레탄 2%', '찬물 손세탁 · 뉘어서 건조',
 '얇음', '있음', '약간', '안감 없음', '26 SS', 170, 48, 'FREE'),

-- 4. 버튼 플리츠 립 밴딩 미니스커트
((SELECT id FROM category WHERE slug = 'bottom'),
 'Button Pleated Rib Banding Mini Skirt', 'button-pleated-rib-banding-mini-skirt',
 '립 밴딩 요크에 플리츠를 이어 붙인 미니스커트. 버튼 디테일로 클래식한 인상을 남깁니다.',
 99000, 15, 'ON_SALE', FALSE, TRUE, NULL,
 '폴리에스터 100%', '드라이클리닝 권장 · 플리츠 유지를 위해 걸어서 보관',
 '보통', '허리 밴딩 신축', '없음', '안감 있음', '26 SS', 172, 50, 'S'),

-- 5. 레이어드 카라 반팔 티 (품절)
((SELECT id FROM category WHERE slug = 'top'),
 'Layered Collar Short Sleeve Tee', 'layered-collar-short-sleeve-tee',
 '대조되는 카라를 덧댄 레이어드 반팔 티셔츠. 한 장으로 완성되는 스타일링.',
 89000, 10, 'SOLDOUT', FALSE, TRUE, NULL,
 '면 95% 폴리우레탄 5%', '30도 이하 세탁 · 표백 금지',
 '보통', '있음', '없음', '안감 없음', '26 SS', 172, 50, 'S'),

-- 6. 레이어드 슬리브리스 & 볼레로 세트 (예약배송)
((SELECT id FROM category WHERE slug = 'top'),
 'Layered Sleeveless Top & Bolero Set', 'layered-sleeveless-top-bolero-set',
 '슬리브리스 탑과 크롭 볼레로의 2피스 세트. 따로 또 같이 착용할 수 있습니다.',
 129000, 20, 'PREORDER', FALSE, TRUE, DATE '2026-07-31',
 '폴리에스터 70% 레이온 28% 폴리우레탄 2%', '찬물 손세탁 · 건조기 사용 금지',
 '보통', '있음', '약간', '안감 없음', '26 SS', 172, 50, 'FREE'),

-- 7. 레이어드 타이 셔츠
((SELECT id FROM category WHERE slug = 'top'),
 'Layered Tie Shirt', 'layered-tie-shirt',
 '스트라이프 카라와 타이 디테일을 레이어드한 셔츠. 리본은 탈부착 가능합니다.',
 92000, 10, 'ON_SALE', FALSE, TRUE, NULL,
 '폴리에스터 65% 면 33% 폴리우레탄 2%', '단독 손세탁 · 낮은 온도 다림질',
 '얇음', '약간', '약간', '안감 없음', '26 SS', 170, 48, 'S');

-- ------------------------------------------------------------
-- 이미지 (§0-④: MAIN + HOVER 대체컷)
-- picsum 플레이스홀더 — 실제 상품컷으로 교체 예정
-- ------------------------------------------------------------
INSERT INTO product_image (product_id, color_id, url, alt, image_type, sort_order)
SELECT p.id, NULL,
       'https://picsum.photos/seed/' || p.slug || '-' || t.n || '/900/1200',
       p.name, t.kind, t.n
FROM product p
CROSS JOIN (VALUES (1, 'MAIN'), (2, 'HOVER'), (3, 'DETAIL'), (4, 'DETAIL')) AS t(n, kind);

-- ------------------------------------------------------------
-- 변형 (색 × 사이즈) — §0-④ 필터의 원천
-- ------------------------------------------------------------
INSERT INTO product_variant (product_id, color_id, size_id, sku, stock_qty, additional_price)
SELECT p.id, c.id, s.id,
       upper(replace(p.slug, '-', '')) || '-' || upper(c.name) || '-' || s.name,
       CASE WHEN p.status = 'SOLDOUT' THEN 0 ELSE 12 END,
       0
FROM product p
JOIN color c ON c.name = ANY (
    CASE p.slug
        WHEN 'mock-neck-rib-sleeveless-top'          THEN ARRAY['White','Beige','Blue','Black']
        WHEN 'cargo-pocket-shorts'                   THEN ARRAY['White','Black']
        WHEN 'rib-knit-halter-top'                   THEN ARRAY['Beige','White']
        WHEN 'button-pleated-rib-banding-mini-skirt' THEN ARRAY['Grey','Black']
        WHEN 'layered-collar-short-sleeve-tee'       THEN ARRAY['Black','White']
        WHEN 'layered-sleeveless-top-bolero-set'     THEN ARRAY['Black','Grey']
        WHEN 'layered-tie-shirt'                     THEN ARRAY['Blue','Black']
    END)
JOIN size s ON s.name = ANY (
    CASE WHEN p.slug IN ('mock-neck-rib-sleeveless-top', 'rib-knit-halter-top',
                         'layered-sleeveless-top-bolero-set')
         THEN ARRAY['FREE']
         ELSE ARRAY['S','M','L']
    END);

-- ------------------------------------------------------------
-- 실측 사이즈표 (§0-⑤: 상세페이지 정보 부족 해결)
-- ------------------------------------------------------------
-- 상의(총장/어깨너비/가슴단면)
INSERT INTO product_measurement (product_id, size_id, item_key, value_cm)
SELECT p.id, s.id, m.item_key,
       m.base + (CASE s.name WHEN 'S' THEN 0 WHEN 'M' THEN 2 WHEN 'L' THEN 4 ELSE 1 END)
FROM product p
JOIN product_variant v ON v.product_id = p.id
JOIN size s ON s.id = v.size_id
CROSS JOIN (VALUES ('총장', 52.0), ('어깨너비', 34.0), ('가슴단면', 40.0)) AS m(item_key, base)
WHERE p.category_id = (SELECT id FROM category WHERE slug = 'top')
GROUP BY p.id, s.id, s.name, m.item_key, m.base;

-- 하의(총장/허리단면/밑단단면)
INSERT INTO product_measurement (product_id, size_id, item_key, value_cm)
SELECT p.id, s.id, m.item_key,
       m.base + (CASE s.name WHEN 'S' THEN 0 WHEN 'M' THEN 2 WHEN 'L' THEN 4 ELSE 1 END)
FROM product p
JOIN product_variant v ON v.product_id = p.id
JOIN size s ON s.id = v.size_id
CROSS JOIN (VALUES ('총장', 33.0), ('허리단면', 32.0), ('밑단단면', 46.0)) AS m(item_key, base)
WHERE p.category_id = (SELECT id FROM category WHERE slug = 'bottom')
GROUP BY p.id, s.id, s.name, m.item_key, m.base;

-- ------------------------------------------------------------
-- 회원 + 리뷰 (목업 · 착용 참고 정보)
-- password_hash는 더미. 실제 인증 붙일 때 교체.
-- ------------------------------------------------------------
INSERT INTO member (email, password_hash, name, role) VALUES
    ('demo1@example.com', '{noop}demo', '김**', 'USER'),
    ('demo2@example.com', '{noop}demo', '이**', 'USER'),
    ('demo3@example.com', '{noop}demo', '박**', 'USER');

INSERT INTO review (product_id, member_id, rating, content, reviewer_height, reviewer_weight, size_purchased)
SELECT p.id, m.id, r.rating, r.content, r.h, r.w, r.sz
FROM (VALUES
    ('mock-neck-rib-sleeveless-top', 'demo1@example.com', 5,
     '핏이 정말 예뻐요. 립 조직이 탄탄해서 비침도 거의 없습니다.', 165, 50, 'FREE'),
    ('mock-neck-rib-sleeveless-top', 'demo2@example.com', 4,
     '색감이 사진과 동일해요. 다만 세탁 후 살짝 줄어드는 느낌.', 158, 46, 'FREE'),
    ('button-pleated-rib-banding-mini-skirt', 'demo3@example.com', 5,
     '허리 밴딩이라 편하고 플리츠가 잘 살아있어요.', 167, 52, 'S'),
    ('layered-tie-shirt', 'demo1@example.com', 4,
     '리본 탈부착이 되어서 다양하게 입기 좋아요.', 165, 50, 'M')
) AS r(slug, email, rating, content, h, w, sz)
JOIN product p ON p.slug = r.slug
JOIN member m ON m.email = r.email;

-- ------------------------------------------------------------
-- 커뮤니티 (목업)
-- ------------------------------------------------------------
INSERT INTO community_post (member_id, board_type, title, content)
SELECT m.id, b.board, b.title, b.content
FROM (VALUES
    ('demo1@example.com', 'NOTICE', '26 SS 컬렉션 배송 안내',
     '예약 배송 상품은 7월 31일부터 순차 발송됩니다.'),
    ('demo2@example.com', 'STYLE', '립 슬리브리스 데일리 코디',
     '데님과 매치했는데 잘 어울려요.'),
    ('demo3@example.com', 'QNA', '사이즈 문의드립니다',
     '165cm / 50kg인데 FREE 사이즈 괜찮을까요?')
) AS b(email, board, title, content)
JOIN member m ON m.email = b.email;
