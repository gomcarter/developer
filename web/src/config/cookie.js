/* eslint-disable */

const DEFAULT_CONFIG = {
  // 默认取当前浏览器一级域名
  domain: window.location.hostname.split('.').slice(-2).join('.'),
  path: '/',
  expires: null,
  secure: null,
};

/**
 *
 * @param key
 * @param value
 * @param opts : {
 *                expires : 过期时间当前时间往后多少秒（整数），不设置表示基于session级别（默认基于session）
 *                domain ： 作用域， 如果不传，区当前浏览器的一级域名；
 *                path：路径（默认根目录，但是如果cookie只希望在部分目录下才生效，可以利用此字段）, 默认根目录（domain下全网站生效）
 *                secure: true => https下才会生效，false=>http下不生效; 默认false
 *              }
 *              或者 opts直接是 expires（过期时间）
 */
export const setCookie = (key, value, opts) => {
  if (typeof opts === 'number') {
    opts = { expires: opts };
  }

  opts = opts || ({});

  if (typeof opts.expires === 'number') {
    const date = new Date();
    date.setSeconds(date.getSeconds() + opts.expires);
    opts.expires = date.toGMTString();
  }

  const optString = Object.entries(Object.assign(DEFAULT_CONFIG, opts))
    .filter(s => s[1] != null)
    .map(s => `${s[0]}=${s[1]}`)
    .join('; ');

  document.cookie = `${key}=${value}; ${optString}`;
};

export const getCookie = (key) => (key = RegExp(`(^| )${key}=([^;]*)(;|$)`).exec(document.cookie)) ? key[2] : null;

export const delCookie = (key) => {
  setCookie(key, '', -1);
};
