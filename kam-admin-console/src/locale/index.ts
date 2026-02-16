/**
 * 国际化基础框架
 * 当前版本仅支持中文，后续可扩展 vue-i18n
 */
export const LOCALE_OPTIONS = [
  { label: '简体中文', value: 'zh-CN' },
  { label: 'English', value: 'en-US' },
];

export const defaultLocale = 'zh-CN';

/** 简易翻译函数（当前直接返回 key） */
export function t(key: string): string {
  return key;
}
