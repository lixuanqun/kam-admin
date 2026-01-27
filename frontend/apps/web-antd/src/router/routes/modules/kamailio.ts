import type { RouteRecordRaw } from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    meta: {
      icon: 'lucide:phone',
      order: 1,
      title: 'Kamailio 管理',
    },
    name: 'Kamailio',
    path: '/kamailio',
    children: [
      {
        name: 'KamailioSubscribers',
        path: '/kamailio/subscribers',
        component: () => import('#/views/kamailio/subscribers/index.vue'),
        meta: {
          icon: 'lucide:users',
          title: '用户管理',
        },
      },
      {
        name: 'KamailioDomains',
        path: '/kamailio/domains',
        component: () => import('#/views/kamailio/domains/index.vue'),
        meta: {
          icon: 'lucide:globe',
          title: '域管理',
        },
      },
      {
        name: 'KamailioDispatchers',
        path: '/kamailio/dispatchers',
        component: () => import('#/views/kamailio/dispatchers/index.vue'),
        meta: {
          icon: 'lucide:network',
          title: '调度器管理',
        },
      },
      {
        name: 'KamailioLocations',
        path: '/kamailio/locations',
        component: () => import('#/views/kamailio/locations/index.vue'),
        meta: {
          icon: 'lucide:map-pin',
          title: '注册监控',
        },
      },
      {
        name: 'KamailioPermissions',
        path: '/kamailio/permissions',
        component: () => import('#/views/kamailio/permissions/index.vue'),
        meta: {
          icon: 'lucide:shield',
          title: '权限管理',
        },
      },
      {
        name: 'KamailioCdr',
        path: '/kamailio/cdr',
        component: () => import('#/views/kamailio/cdr/index.vue'),
        meta: {
          icon: 'lucide:file-text',
          title: 'CDR 记录',
        },
      },
      {
        name: 'KamailioMonitoring',
        path: '/kamailio/monitoring',
        component: () => import('#/views/kamailio/monitoring/index.vue'),
        meta: {
          icon: 'lucide:activity',
          title: '系统监控',
        },
      },
    ],
  },
];

export default routes;
