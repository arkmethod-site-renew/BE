-- ============================================================
-- ARCMETHOD — 주문 시드 (개념 데모 · 실제 결제 없음)
-- schema.sql + seed.sql 이후에 실행
-- ============================================================

TRUNCATE TABLE order_item, orders RESTART IDENTITY CASCADE;

-- 주문 5건 (상태별로 하나씩 + 완료 2건)
INSERT INTO orders (member_id, order_no, status, total_amount, receiver_name, receiver_phone, address, created_at)
SELECT m.id, o.order_no, o.status, o.total, o.receiver, o.phone, o.addr, o.created
FROM (VALUES
    ('demo1@example.com', 'ARC-20260715-0001', 'DONE',      126240, '김**', '010-1234-5678', '서울 강남구 테헤란로 1', TIMESTAMPTZ '2026-07-15 10:24+09'),
    ('demo2@example.com', 'ARC-20260720-0002', 'SHIPPED',    84150, '이**', '010-2345-6789', '경기 성남시 분당구 판교로 2', TIMESTAMPTZ '2026-07-20 15:02+09'),
    ('demo3@example.com', 'ARC-20260725-0003', 'PAID',      145290, '박**', '010-3456-7890', '부산 해운대구 마린시티 3', TIMESTAMPTZ '2026-07-25 09:41+09'),
    ('demo1@example.com', 'ARC-20260728-0004', 'PENDING',    68000, '김**', '010-1234-5678', '서울 강남구 테헤란로 1', TIMESTAMPTZ '2026-07-28 18:33+09'),
    ('demo2@example.com', 'ARC-20260729-0005', 'CANCELLED',  42090, '이**', '010-2345-6789', '경기 성남시 분당구 판교로 2', TIMESTAMPTZ '2026-07-29 11:07+09')
) AS o(email, order_no, status, total, receiver, phone, addr, created)
JOIN member m ON m.email = o.email;

-- 주문 상세 — 각 주문의 변형을 실제 데이터에서 가져온다
-- 1) DONE: 미니스커트 + 슬리브리스탑
INSERT INTO order_item (order_id, variant_id, product_name, color_name, size_name, unit_price, quantity)
SELECT o.id, v.id, p.name, c.name, s.name,
       round(p.price * (100 - p.discount_rate) / 100.0), 1
FROM orders o
JOIN product p ON p.slug IN ('button-pleated-rib-banding-mini-skirt', 'mock-neck-rib-sleeveless-top')
JOIN product_variant v ON v.product_id = p.id
JOIN color c ON c.id = v.color_id
JOIN size s ON s.id = v.size_id
WHERE o.order_no = 'ARC-20260715-0001'
  AND v.id = (SELECT MIN(v2.id) FROM product_variant v2 WHERE v2.product_id = p.id);

-- 2) SHIPPED: 미니스커트 1
INSERT INTO order_item (order_id, variant_id, product_name, color_name, size_name, unit_price, quantity)
SELECT o.id, v.id, p.name, c.name, s.name,
       round(p.price * (100 - p.discount_rate) / 100.0), 1
FROM orders o
JOIN product p ON p.slug = 'button-pleated-rib-banding-mini-skirt'
JOIN product_variant v ON v.id = (SELECT MIN(v2.id) FROM product_variant v2 WHERE v2.product_id = p.id)
JOIN color c ON c.id = v.color_id
JOIN size s ON s.id = v.size_id
WHERE o.order_no = 'ARC-20260720-0002';

-- 3) PAID: 타이셔츠 + 쇼츠
INSERT INTO order_item (order_id, variant_id, product_name, color_name, size_name, unit_price, quantity)
SELECT o.id, v.id, p.name, c.name, s.name,
       round(p.price * (100 - p.discount_rate) / 100.0), 1
FROM orders o
JOIN product p ON p.slug IN ('layered-tie-shirt', 'cargo-pocket-shorts')
JOIN product_variant v ON v.id = (SELECT MIN(v2.id) FROM product_variant v2 WHERE v2.product_id = p.id)
JOIN color c ON c.id = v.color_id
JOIN size s ON s.id = v.size_id
WHERE o.order_no = 'ARC-20260725-0003';

-- 4) PENDING: 쇼츠 1
INSERT INTO order_item (order_id, variant_id, product_name, color_name, size_name, unit_price, quantity)
SELECT o.id, v.id, p.name, c.name, s.name, p.price, 1
FROM orders o
JOIN product p ON p.slug = 'cargo-pocket-shorts'
JOIN product_variant v ON v.id = (SELECT MIN(v2.id) FROM product_variant v2 WHERE v2.product_id = p.id)
JOIN color c ON c.id = v.color_id
JOIN size s ON s.id = v.size_id
WHERE o.order_no = 'ARC-20260728-0004';

-- 5) CANCELLED: 슬리브리스탑 1
INSERT INTO order_item (order_id, variant_id, product_name, color_name, size_name, unit_price, quantity)
SELECT o.id, v.id, p.name, c.name, s.name,
       round(p.price * (100 - p.discount_rate) / 100.0), 1
FROM orders o
JOIN product p ON p.slug = 'mock-neck-rib-sleeveless-top'
JOIN product_variant v ON v.id = (SELECT MIN(v2.id) FROM product_variant v2 WHERE v2.product_id = p.id)
JOIN color c ON c.id = v.color_id
JOIN size s ON s.id = v.size_id
WHERE o.order_no = 'ARC-20260729-0005';

-- 합계를 실제 품목 기준으로 재계산 (데이터 정합성)
UPDATE orders o
SET total_amount = COALESCE((
    SELECT SUM(i.unit_price * i.quantity) FROM order_item i WHERE i.order_id = o.id
), 0);
