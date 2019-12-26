/* eslint-disable */
import { store } from '@/config/cache'
import { delCookie, getCookie, setCookie } from '@/config/cookie'

const USER_INFO = 'user_info';
const USER_TOKEN = 'token';

// 默认两个小时过期，在router/index.js中调用refresh刷新这个过期时间
export const login = (user) => {
  const time = 315360000;
  store.set(USER_INFO, user, time);
  setCookie(USER_TOKEN, user.token, time);
};

export const refresh = () => {
  if (isLogin()) {
    const user = store.get(USER_INFO)
    login(user)
  }
};

export const isLogin = () => {
  return !!getCookie(USER_TOKEN) && !!store.get(USER_INFO)
};

export const logout = () => {
  store.clear(USER_INFO);
  delCookie(USER_TOKEN);
};

export const user = () => {
  return (store.get(USER_INFO) || {}).name;
};
