import type { RouteRecordRaw } from 'vue-router';
import kamailioRoutes from './modules/kamailio';

export const appRoutes: RouteRecordRaw[] = [...kamailioRoutes];
