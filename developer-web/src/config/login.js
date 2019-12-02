/* eslint-disable */
import { store } from '@/config/cache';
import { delCookie, setCookie, getCookie } from '@/config/cookie';

const ENTERPRISE_USER_INFO = 'platform_user_info';
const ENTERPRISE_USER_TOKEN = 'platformuser';

// 默认两个小时过期，在router/index.js中调用refresh刷新这个过期时间
export const login = (user, time = 2) => {
  store.set(ENTERPRISE_USER_INFO, user, time * 60 * 60);
  setCookie(ENTERPRISE_USER_TOKEN, user.token, time * 60 * 60);
};

export const refresh = () => {
  if (isLogin()) {
    const user = store.get(ENTERPRISE_USER_INFO)
    login(user)
  }
};

export const isLogin = () => {
  return !!getCookie(ENTERPRISE_USER_TOKEN) && !!store.get(ENTERPRISE_USER_INFO)
};

export const logout = () => {
  store.clear(ENTERPRISE_USER_INFO);
  delCookie(ENTERPRISE_USER_TOKEN);
};

export const getUsername = () => {
  return (store.get(ENTERPRISE_USER_INFO) || {}).username;
};

export const getLogo = () => {
  return (store.get(ENTERPRISE_USER_INFO) || {}).logo;
};
