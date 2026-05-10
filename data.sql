INSERT INTO role (id, role_code, role_name, description) VALUES
(1, 'ADMIN', '管理员', '后台管理权限'),
(2, 'USER', '普通用户', '前台交易权限');

INSERT INTO user_account (id, username, password, nickname, phone, email, avatar, status) VALUES
(1, 'admin', '{noop}123456', '系统管理员', '13800000001', 'admin@dsmall.local', NULL, 1),
(2, 'user', '{noop}123456', '测试用户', '13800000002', 'user@dsmall.local', NULL, 1);

INSERT INTO user_role (user_id, role_id) VALUES
(1, 1),
(2, 2);

INSERT INTO product_category (id, parent_id, name, path, level, sort_order, enabled, icon_url) VALUES
(1, 0, '手机数码', '/1', 1, 1, 1, NULL),
(2, 0, '电脑办公', '/2', 1, 2, 1, NULL),
(3, 0, '服饰鞋包', '/3', 1, 3, 1, NULL),
(4, 0, '食品生鲜', '/4', 1, 4, 1, NULL);

INSERT INTO product_brand (id, name, logo_url, description, enabled) VALUES
(1, 'Apple', NULL, '消费电子品牌', 1),
(2, 'Huawei', NULL, '智能终端品牌', 1),
(3, 'Xiaomi', NULL, '智能硬件品牌', 1),
(4, 'Lenovo', NULL, '电脑办公品牌', 1);

INSERT INTO product_spu (id, spu_code, name, subtitle, category_id, brand_id, main_image, min_price, sale_status, audit_status, description) VALUES
(1, 'SPU-IP15', 'iPhone 15', '灵动岛全面屏手机', 1, 1, 'https://example.com/images/iphone15.png', 5999.00, 'ON_SALE', 'APPROVED', '适合日常拍摄、游戏和移动办公。'),
(2, 'SPU-MATE60', 'Huawei Mate 60', '旗舰影像手机', 1, 2, 'https://example.com/images/mate60.png', 5499.00, 'ON_SALE', 'APPROVED', '旗舰性能与通信能力。'),
(3, 'SPU-THINKPAD-X1', 'ThinkPad X1 Carbon', '轻薄商务笔记本', 2, 4, 'https://example.com/images/thinkpad-x1.png', 8999.00, 'ON_SALE', 'APPROVED', '轻薄高性能商务办公设备。');

INSERT INTO product_sku (id, spu_id, sku_code, name, sale_price, market_price, stock, locked_stock, spec_json, enabled, version) VALUES
(1, 1, 'SKU-IP15-128-BLACK', 'iPhone 15 128G 黑色', 5999.00, 6299.00, 100, 0, JSON_OBJECT('storage','128G','color','黑色'), 1, 0),
(2, 1, 'SKU-IP15-256-BLUE', 'iPhone 15 256G 蓝色', 6999.00, 7299.00, 80, 0, JSON_OBJECT('storage','256G','color','蓝色'), 1, 0),
(3, 2, 'SKU-MATE60-256-BLACK', 'Huawei Mate 60 256G 雅黑', 5499.00, 5799.00, 90, 0, JSON_OBJECT('storage','256G','color','雅黑'), 1, 0),
(4, 2, 'SKU-MATE60-512-GREEN', 'Huawei Mate 60 512G 雅绿', 6499.00, 6799.00, 60, 0, JSON_OBJECT('storage','512G','color','雅绿'), 1, 0),
(5, 3, 'SKU-X1-I5-16G', 'ThinkPad X1 i5 16G 512G', 8999.00, 9299.00, 40, 0, JSON_OBJECT('cpu','i5','memory','16G','disk','512G'), 1, 0),
(6, 3, 'SKU-X1-I7-32G', 'ThinkPad X1 i7 32G 1T', 11999.00, 12999.00, 30, 0, JSON_OBJECT('cpu','i7','memory','32G','disk','1T'), 1, 0);

INSERT INTO product_attribute_definition (category_id, attr_name, input_type, `options`, `required`, sort_order) VALUES
(1, '存储容量', 'SELECT', '128G,256G,512G,1T', 1, 1),
(1, '颜色', 'SELECT', '黑色,蓝色,白色,绿色', 1, 2),
(2, '处理器', 'INPUT', NULL, 1, 1),
(2, '内存', 'SELECT', '16G,32G,64G', 1, 2);
