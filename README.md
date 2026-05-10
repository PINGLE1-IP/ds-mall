# DS Mall 电商后台管理与交易系统

DS Mall 是一个面向中小型电商业务场景的 Java 后端项目，覆盖用户认证、角色权限、商品分类、品牌管理、SPU/SKU 商品管理、库存管理、订单流转和后台概览等核心能力。项目采用清晰的分层架构，适合用于后端开发学习、接口联调和作品集展示。

## 技术栈

- Java 17
- Spring Boot 3.x
- Spring Web、Spring Validation、Spring Security
- JWT
- MyBatis-Plus
- MySQL 8.x
- Redis
- Lombok
- Maven
- Knife4j / Swagger OpenAPI
- Docker Compose

## 核心功能

- 用户认证：注册、登录、当前用户信息、个人资料修改
- 权限控制：普通用户登录访问，后台接口要求 `ADMIN` 角色
- 分类管理：新增、修改、删除、启停、详情、分类树、分页查询
- 品牌管理：新增、修改、删除、启停、详情、分页查询
- 商品管理：SPU/SKU 新增、修改、删除、详情、分页、上架、下架、审核
- 库存管理：查询库存、增加库存、锁定库存、释放锁定库存、扣减库存
- 订单管理：创建订单、订单详情、用户订单列表、后台订单分页、取消、支付、发货、确认收货
- 后台概览：用户数、商品数、订单数、待支付订单数统计

## 项目结构

```text
com.ds.mall
├── common
│   ├── config
│   ├── exception
│   ├── result
│   ├── security
│   └── util
├── user
│   ├── controller
│   ├── service
│   ├── mapper
│   ├── entity
│   ├── dto
│   └── vo
├── product
│   ├── controller
│   ├── service
│   ├── mapper
│   ├── entity
│   ├── dto
│   └── vo
├── order
│   ├── controller
│   ├── service
│   ├── mapper
│   ├── entity
│   ├── dto
│   └── vo
└── admin
    ├── controller
    ├── service
    ├── dto
    └── vo
```

## 数据库设计说明

项目提供 `schema.sql` 和 `data.sql`，包含用户、角色、商品分类、品牌、SPU、SKU、属性定义、属性值、订单和订单明细表。

核心索引包括：

- `user_account.username` 唯一索引
- `product_spu.spu_code` 唯一索引
- `product_sku.sku_code` 唯一索引
- `order_info.order_no` 唯一索引
- 商品、订单常用查询字段普通索引

## 核心业务流程

下单流程：

1. 校验用户登录状态。
2. 校验 SKU 是否存在、商品是否上架、SKU 是否启用。
3. 使用条件更新锁定库存。
4. 创建订单主表和订单明细。
5. 返回订单号和应付金额。

支付流程：

1. 校验订单存在且属于当前用户。
2. 校验订单处于待支付状态。
3. 扣减锁定库存。
4. 更新支付状态和订单状态。

取消流程：

1. 校验订单存在且状态为待支付。
2. 释放锁定库存。
3. 更新订单状态为已取消。

## 库存设计说明

SKU 库存包含：

- `stock`：总库存
- `locked_stock`：锁定库存
- `availableStock`：可售库存，接口层按 `stock - locked_stock` 计算

库存扣减和锁定使用带条件的 SQL 更新，并放在事务中执行。当可售库存不足时，接口返回库存不足业务错误，避免超卖。

## 权限认证说明

登录成功后返回 `accessToken`，后续请求在 Header 中传入：

```http
Authorization: Bearer <accessToken>
```

访问规则：

- `/api/auth/register`、`/api/auth/login` 可直接访问
- `/api/categories/tree` 可直接访问
- `/api/**` 需要登录
- `/api/admin/**` 需要 `ADMIN` 角色

## Redis 缓存设计

- 分类树缓存：`mall:category:tree`
- 商品详情缓存：`mall:product:spu:{id}`
- 分类新增、修改、删除、启停后删除分类树缓存
- 商品 SPU/SKU 修改后删除对应商品详情缓存

## 本地启动步骤

1. 启动 MySQL 8.x 和 Redis。
2. 创建数据库：

```sql
CREATE DATABASE ds_mall DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. 依次执行 `schema.sql` 和 `data.sql`。
4. 修改 `src/main/resources/application.yml` 中的 MySQL 和 Redis 连接配置。
5. 启动 `com.ds.mall.DsMallApplication`。
6. 浏览器访问接口文档页面。

## Docker Compose 启动方式

```bash
docker compose up -d
```

首次启动 MySQL 容器时会自动执行 `schema.sql` 和 `data.sql`。如果需要重新初始化数据，可以删除对应 Docker volume 后再启动。

## 接口文档地址

启动应用后访问：

- Knife4j：`http://localhost:8080/doc.html`
- OpenAPI JSON：`http://localhost:8080/v3/api-docs`

在 Apifox 或 Postman 中可通过 OpenAPI JSON 地址导入接口。

## 测试账号

| 角色 | 用户名 | 密码 |
| --- | --- | --- |
| 管理员 | admin | 123456 |
| 普通用户 | user | 123456 |

## 常用接口示例

登录：

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "123456"
}
```

创建订单：

```http
POST /api/orders
Authorization: Bearer <accessToken>
Content-Type: application/json

{
  "items": [
    {
      "skuId": 1,
      "quantity": 1
    }
  ],
  "receiverName": "张三",
  "receiverPhone": "13800000000",
  "receiverAddress": "上海市浦东新区示例路 100 号"
}
```

后台商品分页：

```http
GET /api/admin/products/spu/page?current=1&size=10&keyword=iPhone
Authorization: Bearer <accessToken>
```

## 项目截图

后续可在此补充：

- 接口文档首页截图
- 登录接口调试截图
- 商品分页接口截图
- 订单创建与支付流程截图
