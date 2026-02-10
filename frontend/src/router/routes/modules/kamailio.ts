import type { RouteRecordRaw } from 'vue-router';

const routes: RouteRecordRaw[] = [
  // 用户与认证
  {
    name: 'KamailioSubscribers',
    path: '/kamailio/subscribers',
    component: () => import('@/views/kamailio/subscribers/index.vue'),
    meta: { title: '用户管理' },
  },
  {
    name: 'KamailioDomains',
    path: '/kamailio/domains',
    component: () => import('@/views/kamailio/domains/index.vue'),
    meta: { title: '域管理' },
  },
  {
    name: 'KamailioUserdata',
    path: '/kamailio/userdata',
    component: () => import('@/views/kamailio/userdata/index.vue'),
    meta: { title: '用户数据' },
  },
  {
    name: 'KamailioUsrpreferences',
    path: '/kamailio/usrpreferences',
    component: () => import('@/views/kamailio/usrpreferences/index.vue'),
    meta: { title: '用户偏好' },
  },
  // 路由管理
  {
    name: 'KamailioDispatchers',
    path: '/kamailio/dispatchers',
    component: () => import('@/views/kamailio/dispatchers/index.vue'),
    meta: { title: '调度器' },
  },
  {
    name: 'KamailioDrouting',
    path: '/kamailio/drouting',
    component: () => import('@/views/kamailio/drouting/index.vue'),
    meta: { title: '动态路由' },
  },
  {
    name: 'KamailioLcr',
    path: '/kamailio/lcr',
    component: () => import('@/views/kamailio/lcr/index.vue'),
    meta: { title: 'LCR 路由' },
  },
  {
    name: 'KamailioCarrierroute',
    path: '/kamailio/carrierroute',
    component: () => import('@/views/kamailio/carrierroute/index.vue'),
    meta: { title: '运营商路由' },
  },
  {
    name: 'KamailioDialplan',
    path: '/kamailio/dialplan',
    component: () => import('@/views/kamailio/dialplan/index.vue'),
    meta: { title: '拨号计划' },
  },
  {
    name: 'KamailioMtree',
    path: '/kamailio/mtree',
    component: () => import('@/views/kamailio/mtree/index.vue'),
    meta: { title: '内存树' },
  },
  {
    name: 'KamailioPdt',
    path: '/kamailio/pdt',
    component: () => import('@/views/kamailio/pdt/index.vue'),
    meta: { title: '前缀域转换' },
  },
  // 实时监控
  {
    name: 'KamailioLocations',
    path: '/kamailio/locations',
    component: () => import('@/views/kamailio/locations/index.vue'),
    meta: { title: '注册监控' },
  },
  {
    name: 'KamailioDialogs',
    path: '/kamailio/dialogs',
    component: () => import('@/views/kamailio/dialogs/index.vue'),
    meta: { title: '对话管理' },
  },
  // 权限与安全
  {
    name: 'KamailioPermissions',
    path: '/kamailio/permissions',
    component: () => import('@/views/kamailio/permissions/index.vue'),
    meta: { title: '权限管理' },
  },
  {
    name: 'KamailioSecfilter',
    path: '/kamailio/secfilter',
    component: () => import('@/views/kamailio/secfilter/index.vue'),
    meta: { title: '安全过滤' },
  },
  // 计费与跟踪
  {
    name: 'KamailioCdr',
    path: '/kamailio/cdr',
    component: () => import('@/views/kamailio/cdr/index.vue'),
    meta: { title: 'CDR 记录' },
  },
  {
    name: 'KamailioSiptrace',
    path: '/kamailio/siptrace',
    component: () => import('@/views/kamailio/siptrace/index.vue'),
    meta: { title: 'SIP 跟踪' },
  },
  {
    name: 'KamailioTopos',
    path: '/kamailio/topos',
    component: () => import('@/views/kamailio/topos/index.vue'),
    meta: { title: '拓扑隐藏' },
  },
  // 高级功能
  {
    name: 'KamailioUac',
    path: '/kamailio/uac',
    component: () => import('@/views/kamailio/uac/index.vue'),
    meta: { title: 'UAC 注册' },
  },
  {
    name: 'KamailioHtable',
    path: '/kamailio/htable',
    component: () => import('@/views/kamailio/htable/index.vue'),
    meta: { title: '哈希表' },
  },
  {
    name: 'KamailioPresence',
    path: '/kamailio/presence',
    component: () => import('@/views/kamailio/presence/index.vue'),
    meta: { title: '存在服务' },
  },
  {
    name: 'KamailioMsilo',
    path: '/kamailio/msilo',
    component: () => import('@/views/kamailio/msilo/index.vue'),
    meta: { title: '离线消息' },
  },
  {
    name: 'KamailioRtpengine',
    path: '/kamailio/rtpengine',
    component: () => import('@/views/kamailio/rtpengine/index.vue'),
    meta: { title: 'RTPEngine' },
  },
  // 系统
  {
    name: 'KamailioSystem',
    path: '/kamailio/system',
    component: () => import('@/views/kamailio/system/index.vue'),
    meta: { title: '系统管理' },
  },
  {
    name: 'KamailioMonitoring',
    path: '/kamailio/monitoring',
    component: () => import('@/views/kamailio/monitoring/index.vue'),
    meta: { title: '监控面板' },
  },
  {
    name: 'KamailioVersion',
    path: '/kamailio/version',
    component: () => import('@/views/kamailio/version/index.vue'),
    meta: { title: '数据库版本' },
  },
];

export default routes;
