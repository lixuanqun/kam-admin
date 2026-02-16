import 'vue-router';

declare module 'vue-router' {
  interface RouteMeta {
    /** 页面标题，用于菜单和面包屑 */
    title?: string;
    /** Arco Design 图标名称 */
    icon?: string;
    /** 菜单排序，数字越小越靠前 */
    order?: number;
    /** 是否需要登录认证，默认 true */
    requiresAuth?: boolean;
    /** 允许访问的角色列表，空则不限制 */
    roles?: string[];
    /** 是否在菜单中隐藏 */
    hideInMenu?: boolean;
    /** 是否在子菜单中隐藏 */
    hideChildrenInMenu?: boolean;
    /** 是否不缓存 */
    noCache?: boolean;
  }
}
