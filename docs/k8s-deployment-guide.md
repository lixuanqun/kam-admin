# Kubernetes 部署指南

本文档介绍如何在 Kubernetes 集群中部署 kam-admin-server。

---

## 一、前置条件

| 依赖 | 说明 |
|------|------|
| **Kubernetes** | 1.24+，需配置 kubectl |
| **Docker / containerd** | 用于构建镜像 |
| **MySQL 或 PostgreSQL** | 集群内或集群外，需可访问 |
| **Kamailio** | 已启用 JSONRPC，需可访问 |
| **Redis**（可选） | 集群/缓存/限流 |
| **Ingress Controller**（可选） | 若使用 Ingress 暴露服务 |

---

## 二、构建镜像

在项目根目录执行：

```bash
# 构建 Docker 镜像（多阶段构建，包含 Maven 编译）
docker build -f kam-admin-server/Dockerfile -t kam-admin-server:0.0.1 .

# 若使用私有仓库，打标签并推送
docker tag kam-admin-server:0.0.1 your-registry/kam-admin-server:0.0.1
docker push your-registry/kam-admin-server:0.0.1
```

---

## 三、准备配置

### 3.1 创建 Secret

复制示例并填入实际值：

```bash
cp k8s/secret.yaml.example k8s/secret.yaml
# 编辑 k8s/secret.yaml，填入数据库密码等敏感信息
```

或使用 kubectl 创建：

```bash
kubectl create secret generic kam-admin-server-secret \
  --namespace kam-admin \
  --from-literal=SPRING_DATASOURCE_URL='jdbc:mysql://mysql:3306/kamailio' \
  --from-literal=SPRING_DATASOURCE_USERNAME='kamailio' \
  --from-literal=SPRING_DATASOURCE_PASSWORD='your_password'
```

### 3.2 修改 ConfigMap

编辑 `k8s/configmap.yaml`，按实际环境修改：

- `KAMAILIO_RPC_HOST`：Kamailio 服务地址（集群内用 Service 名，集群外用外部地址）
- `REDIS_HOST`：Redis 地址（若集群内有 Redis Service）
- `NACOS_SERVER_ADDR`：若使用 Nacos

---

## 四、部署

### 4.1 创建命名空间与资源

```bash
# 创建命名空间
kubectl apply -f k8s/namespace.yaml

# 创建 Secret（需先准备 secret.yaml）
kubectl apply -f k8s/secret.yaml

# 创建 ConfigMap
kubectl apply -f k8s/configmap.yaml

# 创建 Deployment 与 Service
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml

# 可选：创建 Ingress（需修改 host 与 ingressClassName）
kubectl apply -f k8s/ingress.yaml
```

### 4.2 验证

```bash
# 查看 Pod 状态
kubectl get pods -n kam-admin

# 查看日志
kubectl logs -f deployment/kam-admin-server -n kam-admin

# 端口转发本地访问（测试用）
kubectl port-forward svc/kam-admin-server 3000:3000 -n kam-admin
# 访问 http://localhost:3000/api/docs
```

---

## 五、健康检查

| 端点 | 用途 |
|------|------|
| `/actuator/health/liveness` | K8S 存活探针 |
| `/actuator/health/readiness` | K8S 就绪探针 |
| `/actuator/health` | 完整健康状态 |

探针配置见 `k8s/deployment.yaml`，可按需调整 `initialDelaySeconds`、`periodSeconds` 等。

---

## 六、资源配置

`deployment.yaml` 中默认：

- **requests**：memory 512Mi，cpu 250m
- **limits**：memory 1Gi，cpu 1000m

可按实际负载调整。若需水平扩缩容，可添加 HPA：

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: kam-admin-server
  namespace: kam-admin
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: kam-admin-server
  minReplicas: 1
  maxReplicas: 5
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 70
```

---

## 七、前端部署

前端（kam-admin-console）需单独构建与部署：

1. 构建：`cd kam-admin-console && pnpm run build`，产物在 `kam-admin-console/dist`
2. 使用 Nginx 镜像，将 dist 复制到 `/usr/share/nginx/html`
3. 配置 Nginx 反向代理 `/api` 到 kam-admin-server Service
4. 创建 Deployment + Service + Ingress

或使用现有 Nginx Ingress 的静态资源托管能力，将 dist 作为 ConfigMap 或单独部署。

---

## 八、目录结构

```
k8s/
├── namespace.yaml       # 命名空间
├── configmap.yaml       # 非敏感配置
├── secret.yaml.example  # Secret 模板（复制为 secret.yaml 使用）
├── deployment.yaml       # Deployment
├── service.yaml         # Service
└── ingress.yaml         # Ingress（可选）
```
