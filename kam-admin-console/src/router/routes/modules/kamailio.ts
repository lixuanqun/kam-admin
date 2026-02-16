import type { RouteRecordRaw } from 'vue-router';

const KAMAILIO_ROUTES: RouteRecordRaw[] = [
  // ===== 监控面板 (顶级) =====
  {
    name: 'KamailioMonitoring',
    path: '/kamailio/monitoring',
    component: () => import('@/views/kamailio/monitoring/index.vue'),
    meta: { title: '监控面板', icon: 'icon-dashboard', order: 0 },
  },

  // ===== 用户管理 =====
  {
    name: 'UserManagement',
    path: '/kamailio/user',
    meta: { title: '用户管理', icon: 'icon-user-group', order: 1 },
    children: [
      {
        name: 'KamailioSubscribers',
        path: '/kamailio/subscribers',
        component: () => import('@/views/kamailio/subscribers/index.vue'),
        meta: { title: '用户管理', order: 0 },
      },
      {
        name: 'KamailioDomains',
        path: '/kamailio/domains',
        component: () => import('@/views/kamailio/domains/index.vue'),
        meta: { title: '域管理', order: 1 },
      },
      {
        name: 'KamailioUserdata',
        path: '/kamailio/userdata',
        component: () => import('@/views/kamailio/userdata/index.vue'),
        meta: { title: '用户数据', order: 2 },
      },
      {
        name: 'KamailioUsrpreferences',
        path: '/kamailio/usrpreferences',
        component: () => import('@/views/kamailio/usrpreferences/index.vue'),
        meta: { title: '用户偏好', order: 3 },
      },
    ],
  },

  // ===== 路由管理 =====
  {
    name: 'RoutingManagement',
    path: '/kamailio/routing',
    meta: { title: '路由管理', icon: 'icon-relation', order: 2 },
    children: [
      {
        name: 'KamailioDispatchers',
        path: '/kamailio/dispatchers',
        component: () => import('@/views/kamailio/dispatchers/index.vue'),
        meta: { title: '调度器', order: 0 },
      },
      {
        name: 'KamailioDrouting',
        path: '/kamailio/drouting',
        component: () => import('@/views/kamailio/drouting/index.vue'),
        meta: { title: '动态路由', order: 1 },
      },
      {
        name: 'KamailioLcr',
        path: '/kamailio/lcr',
        component: () => import('@/views/kamailio/lcr/index.vue'),
        meta: { title: 'LCR 路由', order: 2 },
      },
      {
        name: 'KamailioCarrierroute',
        path: '/kamailio/carrierroute',
        component: () => import('@/views/kamailio/carrierroute/index.vue'),
        meta: { title: '运营商路由', order: 3 },
      },
      {
        name: 'KamailioDialplan',
        path: '/kamailio/dialplan',
        component: () => import('@/views/kamailio/dialplan/index.vue'),
        meta: { title: '拨号计划', order: 4 },
      },
      {
        name: 'KamailioMtree',
        path: '/kamailio/mtree',
        component: () => import('@/views/kamailio/mtree/index.vue'),
        meta: { title: '内存树', order: 5 },
      },
      {
        name: 'KamailioPdt',
        path: '/kamailio/pdt',
        component: () => import('@/views/kamailio/pdt/index.vue'),
        meta: { title: '前缀域转换', order: 6 },
      },
    ],
  },

  // ===== 实时监控 =====
  {
    name: 'MonitorManagement',
    path: '/kamailio/monitor',
    meta: { title: '实时监控', icon: 'icon-eye', order: 3 },
    children: [
      {
        name: 'KamailioLocations',
        path: '/kamailio/locations',
        component: () => import('@/views/kamailio/locations/index.vue'),
        meta: { title: '注册监控', order: 0 },
      },
      {
        name: 'KamailioDialogs',
        path: '/kamailio/dialogs',
        component: () => import('@/views/kamailio/dialogs/index.vue'),
        meta: { title: '对话管理', order: 1 },
      },
      {
        name: 'KamailioSiptrace',
        path: '/kamailio/siptrace',
        component: () => import('@/views/kamailio/siptrace/index.vue'),
        meta: { title: 'SIP 跟踪', order: 2 },
      },
      {
        name: 'KamailioTopos',
        path: '/kamailio/topos',
        component: () => import('@/views/kamailio/topos/index.vue'),
        meta: { title: '拓扑隐藏', order: 3 },
      },
    ],
  },

  // ===== 安全管理 =====
  {
    name: 'SecurityManagement',
    path: '/kamailio/security',
    meta: { title: '安全管理', icon: 'icon-safe', order: 4 },
    children: [
      {
        name: 'KamailioPermissions',
        path: '/kamailio/permissions',
        component: () => import('@/views/kamailio/permissions/index.vue'),
        meta: { title: '权限管理', order: 0 },
      },
      {
        name: 'KamailioSecfilter',
        path: '/kamailio/secfilter',
        component: () => import('@/views/kamailio/secfilter/index.vue'),
        meta: { title: '安全过滤', order: 1 },
      },
    ],
  },

  // ===== 计费跟踪 =====
  {
    name: 'BillingManagement',
    path: '/kamailio/billing',
    meta: { title: '计费跟踪', icon: 'icon-file', order: 5 },
    children: [
      {
        name: 'KamailioCdr',
        path: '/kamailio/cdr',
        component: () => import('@/views/kamailio/cdr/index.vue'),
        meta: { title: 'CDR 记录', order: 0 },
      },
    ],
  },

  // ===== 高级功能 =====
  {
    name: 'AdvancedManagement',
    path: '/kamailio/advanced',
    meta: { title: '高级功能', icon: 'icon-apps', order: 6 },
    children: [
      {
        name: 'KamailioUac',
        path: '/kamailio/uac',
        component: () => import('@/views/kamailio/uac/index.vue'),
        meta: { title: 'UAC 注册', order: 0 },
      },
      {
        name: 'KamailioHtable',
        path: '/kamailio/htable',
        component: () => import('@/views/kamailio/htable/index.vue'),
        meta: { title: '哈希表', order: 1 },
      },
      {
        name: 'KamailioPresence',
        path: '/kamailio/presence',
        component: () => import('@/views/kamailio/presence/index.vue'),
        meta: { title: '存在服务', order: 2 },
      },
      {
        name: 'KamailioMsilo',
        path: '/kamailio/msilo',
        component: () => import('@/views/kamailio/msilo/index.vue'),
        meta: { title: '离线消息', order: 3 },
      },
    ],
  },

  // ===== 系统管理 =====
  {
    name: 'SystemManagement',
    path: '/kamailio/sys',
    meta: { title: '系统管理', icon: 'icon-settings', order: 7 },
    children: [
      {
        name: 'KamailioSystem',
        path: '/kamailio/system',
        component: () => import('@/views/kamailio/system/index.vue'),
        meta: { title: '系统管理', order: 0 },
      },
      {
        name: 'KamailioRtpengine',
        path: '/kamailio/rtpengine',
        component: () => import('@/views/kamailio/rtpengine/index.vue'),
        meta: { title: 'RTPEngine', order: 1 },
      },
      {
        name: 'KamailioVersion',
        path: '/kamailio/version',
        component: () => import('@/views/kamailio/version/index.vue'),
        meta: { title: '数据库版本', order: 2 },
      },
    ],
  },
];

export default KAMAILIO_ROUTES;
