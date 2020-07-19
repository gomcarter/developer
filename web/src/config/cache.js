/* eslint-disable */
import axios from 'axios'

// 数据存储
export const store = {
  set(key, data, expire) {
    try {
      if (expire) {
        expire = new Date().getTime() + expire * 1000
      }
      window.localStorage.setItem(key, JSON.stringify(
        {
          expire,
          data,
        },
      ));
      return true;
    } catch (e) {
      return false;
    }
  },
  get(key) {
    try {
      const dataString = window.localStorage.getItem(key)

      const cached = JSON.parse(dataString);
      if (cached == null || cached.expire < new Date().getTime()) {
        window.localStorage.removeItem(key);
        return null;
      }
      return cached.data;
    } catch (e) {
      return null;
    }
  },
  clear(key) {
    try {
      window.localStorage.removeItem(key)
      return true;
    } catch (e) {
      return false;
    }
  }
}

// 数据缓存
export const cache = {
  data: {},
  set(key, data, bol = false, expire) {
    if (expire) {
      expire = new Date().getTime() + expire * 1000
    }
    const _data = {
      expire,
      data,
    };

    if (bol) {
      window.sessionStorage.setItem(key, JSON.stringify(_data));
    } else {
      this.data[key] = _data
    }
  },
  get(key, bol = false) {
    if (bol) {
      const dataString = window.sessionStorage.getItem(key)

      const cached = JSON.parse(dataString);
      if (cached == null || cached.expire < new Date().getTime()) {
        window.localStorage.removeItem(key);
        return null;
      }
      return cached.data;
    } else {
      const _data = this.data[key]
      if (_data == null || _data.expire < new Date().getTime()) {
        this.data[key] = undefined
        return null
      }
      return _data.data
    }
  },
  clear(key, bol = false) {
    if (bol) {
      sessionStorage.removeItem(key)
    } else {
      delete this.data[key]
    }
  }
}

// 建立唯一的key值
function buildUrl(url, params = {}) {
  const sortedParams = Object.keys(params).sort().reduce((result, key) => {
    result[key] = params[key]
    return result
  }, {})

  url += `?${JSON.stringify(sortedParams)}`
  return url
}

// 缓存, 只给get加缓存
export default (options = {}) => config => {
  const { url, method, params } = config
  const { local = false, expire } = options
  if (method !== 'get') {
    throw new Error('cache is only for get request');
  }

  // 建立索引
  let index = buildUrl(url, params)
  let response = cache.get(index, local)
  if (response) {
    return Promise.resolve(JSON.parse(JSON.stringify(response))) // 对象是引用，为了防止污染数据源
  } else {
    response = (async() => {
      try {
        const r = await axios.defaults.adapter(config)
        cache.set(index, r, local, expire)
        return Promise.resolve(JSON.parse(JSON.stringify(r))) // 同时发送多次一样的请求，没办法防止污染数据源，只有业务中去实现
      } catch (reason) {
        cache.clear(index, local)
        return Promise.reject(reason)
      }
    })()
  }
  return response
}
