# AGENTS.md

## Cursor Cloud specific instructions

### Architecture

Kamailio Dashboard is a full-stack management UI for the Kamailio SIP Server. It is a multi-module Maven monorepo with a separate Vue 3 frontend:

- **Backend** (Java 21 / Spring Boot 3.3.5): `kam-admin-server` aggregates domain modules (`kam-admin-common`, `kam-admin-module-user`, `kam-admin-module-routing`, `kam-admin-module-session`, `kam-admin-module-monitor`, `kam-admin-module-presence`, `kam-admin-module-trace`)
- **Frontend** (Vue 3 / TypeScript / Vite / Arco Design): `kam-admin-console`
- **Infrastructure**: MySQL 8.0, Redis 7 (optional), Kamailio SIP server (for RPC features)

### Running services

Standard commands per README. Key caveats below.

**Start infrastructure (Docker)**:
```bash
docker start kam-admin-mysql kam-admin-redis 2>/dev/null || {
  docker run -d --name kam-admin-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=kamailio -e MYSQL_USER=kamailio -e MYSQL_PASSWORD=kamailiorw -p 3306:3306 -v /workspace/docker/mysql/init:/docker-entrypoint-initdb.d:ro mysql:8.0
  docker run -d --name kam-admin-redis -p 6379:6379 redis:7-alpine
}
```

**Start backend** (port 3000):
```bash
SPRING_PROFILES_ACTIVE=mysql SPRING_DATASOURCE_PASSWORD=kamailiorw SPRING_CLOUD_NACOS_CONFIG_ENABLED=false SPRING_CLOUD_NACOS_DISCOVERY_ENABLED=false AUTH_CONSOLE_METHOD=NONE mvn spring-boot:run -pl kam-admin-server
```

**Start frontend** (port 5666, proxies `/api` to `localhost:3000`):
```bash
cd kam-admin-console && pnpm dev
```

### Non-obvious gotchas

1. **Nacos must be disabled for local dev**: The backend depends on Spring Cloud Alibaba Nacos but no Nacos server is available locally. Set `SPRING_CLOUD_NACOS_CONFIG_ENABLED=false` and `SPRING_CLOUD_NACOS_DISCOVERY_ENABLED=false` or the app will fail to start.

2. **Auth must be disabled for development**: Set `AUTH_CONSOLE_METHOD=NONE` to bypass session auth on `/api/**` routes. Without this, all API calls return 401. The frontend login is purely client-side (stores a token in Pinia, no actual backend auth endpoint exists).

3. **MySQL schema initialization**: The Docker init scripts (`docker/mysql/init/`) download the Kamailio schema from GitHub on first container startup. If the container was created without internet access, the schema will be incomplete. The `auth_db-create.sql` (subscriber table) is NOT included in the default init script and must be imported manually. Additionally, the `subscriber.email_address` column needs to be nullable for the dashboard to work.

4. **Tests**: Unit tests use H2 in-memory database (profile `test`). Run with `mvn test -pl kam-admin-server -am`. The `OpenApiDocTest` (2 tests) uses `@SpringBootTest` and fails because `KamailioRpcServiceImpl` can't be instantiated in test context without a running Kamailio - these are pre-existing failures.

5. **Build**: `mvn clean install -DskipTests` from root. The root POM requires `-parameters` flag (already configured) for Spring parameter resolution.

6. **Frontend lint**: `pnpm lint` (from `kam-admin-console/`). Only warnings (no-explicit-any), zero errors.
