import { createRouter, createWebHistory } from 'vue-router';
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';
import { appRoutes } from './routes';

NProgress.configure({ showSpinner: false });

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/login/index.vue'),
      meta: { requiresAuth: false },
    },
    {
      path: '/',
      name: 'root',
      component: () => import('@/layout/default-layout.vue'),
      redirect: '/kamailio/monitoring',
      children: appRoutes,
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'notFound',
      component: () => import('@/views/not-found/index.vue'),
    },
  ],
  scrollBehavior() {
    return { top: 0 };
  },
});

router.beforeEach((to, _from, next) => {
  NProgress.start();
  const token = localStorage.getItem('token');
  if (to.meta.requiresAuth !== false && !token) {
    next({ name: 'login', query: { redirect: to.fullPath } });
  } else {
    next();
  }
});

router.afterEach(() => {
  NProgress.done();
});

export default router;
