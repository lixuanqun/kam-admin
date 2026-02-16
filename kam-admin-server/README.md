# Kamailio Dashboard API - Spring Boot 3

基于 Spring Boot 3 + JDK 21 的 Kamailio 管理后台 API，从 NestJS 迁移而来。

## 技术栈

- **Java**: 21
- **Spring Boot**: 3.3.x
- **Spring Data JPA**: 数据访问
- **Spring WebFlux**: Kamailio RPC 调用
- **MySQL / PostgreSQL**: 存储
- **springdoc-openapi**: Swagger 文档

## 环境要求

- JDK 21+
- Maven 3.9+
- MySQL 5.7+ / MariaDB 10.x 或 PostgreSQL 12+

## 快速开始

### 1. 配置环境变量

```bash
cp .env.example .env
# 编辑 .env 配置数据库与 Kamailio RPC
```

### 2. 使用 MySQL（默认）

```bash
# 默认使用 MySQL，确保 .env 中 DB_HOST、DB_USERNAME、DB_PASSWORD、DB_DATABASE 正确
mvn spring-boot:run
```

### 3. 使用 PostgreSQL

```bash
# 激活 postgres profile
export SPRING_PROFILES_ACTIVE=postgres
# 或设置 DB_TYPE=postgres 后修改 application.yml 自动激活
mvn spring-boot:run
```

### 4. 构建与运行

```bash
# 开发
mvn spring-boot:run

# 打包（跳过测试）
mvn clean package -DskipTests

# 打包并运行测试（需 JDK 21）
mvn clean test

# 运行 JAR
java -jar target/kam-admin-api-0.0.1.jar
```

### 5. 自测（各模块控制器）

已为每个 API 模块添加 `*ControllerTest`，使用 `@WebMvcTest` + Mock 对应 Service，校验接口返回 HTTP 200 且 `code=0`。在 **JDK 21** 环境下执行：

```bash
mvn test -Dtest="*ControllerTest"
```

## API 文档

启动后访问：

- Swagger UI: http://localhost:3000/api/docs
- API 根路径: http://localhost:3000/

## 已迁移模块

- [x] 公共 DTO（ApiResponse、Pagination、PaginatedResult）
- [x] Kamailio RPC 服务（含 ul.lookup、ul.rm_contact）
- [x] **Subscriber** 用户管理（CRUD、批量删除、统计）
- [x] **Domain** 域管理（CRUD、reload、dump）
- [x] **Dispatcher** 调度器（CRUD、reload、status、set-state、stats）
- [x] **Location** 注册位置（列表、内存、统计、删除、lookup、flush）
- [x] **Permissions** 权限（Address/Trusted CRUD、reload、stats、dump）
- [x] **Acc** 计费（CDR、未接来电、统计、今日统计）
- [x] **Monitoring** 监控（health、dashboard、statistics、dialogs、overview、check-modules）
- [x] **Version** 表版本（列表、单表版本、stats）
- [x] **System** 系统（core-info、config、tls、pike、statistics、modules、status）
- [x] **Drouting** 动态路由（gateways、rules、groups、carriers CRUD + reload、stats、gw-status、carrier-status）
- [x] **Dialog** 会话（列表、active、stats、end、detail、bridge）
- [x] **Htable** 哈希表（数据库 CRUD + RPC get/sets/seti/delete/dump/reload/tables/stats）
- [x] **Lcr** LCR 路由（gateways、rules、targets CRUD + reload、dump-gws、dump-rules）
- [x] **Carrierroute** 运营商路由（carriers、domains、failure-routes CRUD + reload、dump）
- [x] **Dialplan** 拨号计划（CRUD + reload、translate、dump）
- [x] **Mtree** 内存树（CRUD + reload、match、summary）
- [x] **Pdt** 前缀域转换（CRUD + reload、list）
- [x] **Userdata** 用户数据（aliases、groups、speed-dial CRUD）
- [x] **Uac** UAC 注册（registrations CRUD + reload、info、refresh、dump）
- [x] **Usrpreferences** 用户偏好（CRUD + findByUser）
- [x] **Presence** 存在服务（presentities、watchers 列表 + cleanup、refresh-watchers、stats）
- [x] **Msilo** 离线消息（列表、按用户、删除、cleanup、stats、dump）
- [x] **Siptrace** SIP 跟踪（分页查询、按 callid、stats、cleanup）
- [x] **Topos** 拓扑隐藏（dialogs 列表、cleanup、stats）
- [x] **Secfilter** 安全过滤（CRUD + reload、print、stats、stats/reset、add-bl、add-wl）
- [x] **Rtpengine** RTPEngine/NAT（show、reload、enable、ping、nat-ping、status）

## 与 NestJS 版本对比

| 功能 | NestJS | Spring Boot 3 |
|------|--------|---------------|
| API 路径 | /api/* | /api/* |
| 响应格式 | { code, message, data, timestamp } | 一致 |
| 分页 | page, limit, keyword | 一致 |
| Swagger | /api/docs | /api/docs |

## License

[Apache License 2.0](../LICENSE)
