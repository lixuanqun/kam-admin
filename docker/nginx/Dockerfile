# 多阶段：构建前端 + Nginx 托管（构建上下文：项目根目录）
# 仅使用 kam-admin-console 子工程（根目录无 pnpm）
FROM node:20-alpine AS builder
WORKDIR /app
COPY kam-admin-console/ ./
RUN corepack enable pnpm && pnpm install --frozen-lockfile || pnpm install
ENV VITE_API_BASE_URL=/api
RUN pnpm run build

FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY docker/nginx/nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
