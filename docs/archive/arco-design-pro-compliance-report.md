# Arco Design Pro 规范符合度核对报告

> 基于 [Arco Design Pro Vue](https://github.com/arco-design/arco-design-pro-vue) 官方模板与最佳实践，对当前项目（Kamailio Dashboard）进行核对

---

## 一、整体结论

| 维度 | 符合度 | 说明 |
|------|--------|------|
| 技术栈选型 | ✅ 符合 | Vue3 + Vite + Pinia + Arco Design Vue |
| 项目结构 | ⚠️ 部分符合 | 缺少 config、directive、mock、types 等目录 |
| 代码规范 | ⚠️ 部分符合 | 缺少 ESLint、Stylelint、Commitlint 等 |
| 布局与组件 | ✅ 符合 | 布局、菜单、路由结构合理 |
| Store 设计 | ✅ 符合 | Pinia setup 语法，结构清晰 |
| API 设计 | ✅ 符合 | 统一 interceptor，模块化拆分 |
| 按需引入 | ❌ 未采用 | 全量引入 Arco，未使用 unplugin-vue-components |

---

## 二、目录结构对比

### Arco Design Pro 标准结构

```
src/
├── api/           # API 接口
├── assets/        # 静态资源
├── components/    # 公共组件
├── config/        # 全局配置（settings.json 等）
├── directive/     # 自定义指令
├── hooks/         # 组合式函数
├── layout/        # 布局组件
├── locale/        # 国际化
├── mock/          # Mock 数据
├── router/        # 路由
├── store/         # 状态管理
├── types/         # 类型定义
├── utils/         # 工具函数
├── views/         # 页面视图
├── App.vue
├── env.d.ts
└── main.ts
```

### 当前项目结构

```
src/
├── api/           ✅ 有
├── assets/        ✅ 有
├── components/    ✅ 有
├── config/        ❌ 缺失
├── directive/     ❌ 缺失
├── hooks/         ✅ 有
├── layout/        ✅ 有
├── locale/        ✅ 有（简化版）
├── mock/          ❌ 缺失
├── router/        ✅ 有
├── store/         ✅ 有
├── types/         ❌ 缺失（路由类型在 router/typings.d.ts）
├── utils/         ✅ 有
├── views/         ✅ 有
```

---

## 三、逐项核对

### 3.1 技术栈与依赖

| 项目 | Arco Pro | 当前项目 | 说明 |
|------|----------|----------|------|
| Vue | ^3.2.40 | ^3.5.13 | ✅ 版本更新 |
| Vite | ^3.2.5 | ^6.0.7 | ✅ 版本更新 |
| Pinia | ^2.0.23 | ^2.3.0 | ✅ |
| @arco-design/web-vue | ^2.44.7 | ^2.56.3 | ✅ |
| vue-router | ^4.0.14 | ^4.5.0 | ✅ |
| axios | ^0.24.0 | ^1.7.9 | ✅ |
| @vueuse/core | ^9.3.0 | ❌ 未使用 | 可选 |
| vue-i18n | ^9.2.2 | ❌ 未使用 | 当前 locale 为简易实现 |
| unplugin-vue-components | ^0.24.1 | ❌ 未使用 | 未做按需引入 |
| @arco-plugins/vite-vue | ^1.4.5 | ❌ 未使用 | 主题按需/样式按需 |

### 3.2 main.ts 入口

**Arco Pro 标准：**
```typescript
app.use(ArcoVue, {});
app.use(ArcoVueIcon);
app.use(router);
app.use(store);
app.use(i18n);
app.use(globalComponents);  // 全局组件注册
app.use(directive);         // 自定义指令
import './mock';            // Mock 数据
import '@/api/interceptor';  // 请求拦截器（在入口统一加载）
```

**当前项目：**
```typescript
app.use(ArcoVue);
app.use(ArcoVueIcon);
app.use(router);
app.use(store);
// 缺少: i18n、globalComponents、directive、mock
// 缺少: '@/api/interceptor' 在入口显式引入（通过 API 模块间接加载）
```

**建议：** 在 main.ts 中显式引入 `@/api/interceptor`，确保拦截器在首次请求前生效。

### 3.3 配置体系

**Arco Pro：** 有 `src/config/settings.json`，包含 theme、navbar、menu、footer、themeColor 等运行时配置。

**当前项目：** 无独立 config，app store 中硬编码 theme、menuCollapsed、menuWidth 等。

**建议：** 若需支持更多布局/主题配置，可引入 settings.json 模式。

### 3.4 Store 设计

**Arco Pro：** 使用 Options API 风格，state 初始化自 defaultSettings。

**当前项目：** 使用 Composition API（setup 语法），结构更简洁，符合 Pinia 推荐写法。✅

### 3.5 布局组件

**Arco Pro：** 有 `default-layout.vue`、`page-layout.vue`。

**当前项目：** 仅 `default-layout.vue`，实现侧边栏、顶栏、面包屑、主题切换、全屏、用户下拉等。✅ 满足当前需求。

### 3.6 路由与权限

**Arco Pro：** 支持 `menuFromServer`、动态菜单、多级路由。

**当前项目：** 路由静态配置，meta 含 title、icon、order、requiresAuth、hideInMenu 等，与 Arco Pro 的 RouteMeta 扩展一致。✅

### 3.7 API 与拦截器

**Arco Pro：** 统一 HttpResponse 类型，按 code 区分业务错误，401/50008 等触发登出。

**当前项目：** 拦截器实现 401 重定向登录、错误 Message 提示，逻辑清晰。API 按模块拆分（subscriber、domain、routing 等）。✅

### 3.8 代码规范工具

| 工具 | Arco Pro | 当前项目 |
|------|----------|----------|
| ESLint | ✅ .eslintrc.js | ❌ 无 |
| Stylelint | ✅ .stylelintrc.js | ❌ 无 |
| Prettier | ✅ .prettierrc.js | ✅ .prettierrc |
| Commitlint | ✅ commitlint.config.js | ❌ 无 |
| Husky + lint-staged | ✅ | ❌ 无 |

### 3.9 构建配置

**Arco Pro：** 使用 `config/` 目录，分离 dev/prod 配置，含 eslint、compression、imagemin 等插件。

**当前项目：** 单文件 vite.config.ts，配置简洁。✅ 满足当前需求，若需分环境可参考 Arco Pro 拆分。

### 3.10 国际化

**Arco Pro：** 使用 vue-i18n，完整多语言方案。

**当前项目：** 简易 locale，仅 `t()` 占位，未接入 vue-i18n。若仅支持中文可接受。

---

## 四、符合项总结

1. **技术栈**：Vue3 + Vite + Pinia + Arco Design Vue 一致
2. **布局**：a-layout + sider + header + content 结构合理
3. **Store**：Pinia setup 语法，结构清晰
4. **API**：模块化、统一拦截器、401 处理
5. **路由**：meta 扩展、权限守卫、NProgress
6. **组件使用**：Arco 组件用法规范（表格插槽、表单、Modal、Popconfirm 等）
7. **样式**：global.less、Vite 中 Less 变量定制主色

---

## 五、待改进项（按优先级）

### 高优先级

| 项 | 建议 |
|----|------|
| 拦截器显式加载 | 在 main.ts 中 `import '@/api/interceptor'` |
| ESLint | 添加 .eslintrc.js，统一代码风格 |
| 按需引入 | 可选：引入 unplugin-vue-components + @arco-plugins/vite-vue 减小打包体积 |

### 中优先级

| 项 | 建议 |
|----|------|
| config/settings.json | 若需灵活配置主题/布局，可引入 |
| types 目录 | 将全局类型抽到 src/types/ |
| Mock | 若需前端独立联调，可引入 mock 目录 |

### 低优先级

| 项 | 建议 |
|----|------|
| Stylelint | 统一 CSS/Less 规范 |
| Commitlint + Husky | 统一提交信息格式 |
| vue-i18n | 若需多语言，替换当前 locale |
| directive | 若有通用指令需求再添加 |

---

## 六、结论

当前项目在**核心架构、布局、Store、API、路由**等方面与 Arco Design Pro 最佳实践基本一致，能较好支撑业务。与官方模板的主要差异在于：

1. **工程化**：缺少 ESLint、Stylelint、Commitlint 等规范工具
2. **工程结构**：缺少 config、directive、mock、types 等目录（部分为可选）
3. **按需引入**：全量引入 Arco，未做组件按需加载

若项目为内部或中小型系统，当前实现已足够；若追求与 Arco Pro 完全对齐或需要更严格的工程规范，可按上述优先级逐步补齐。
