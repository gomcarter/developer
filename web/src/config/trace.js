/* eslint-disable */
!(function () {
  var Class = Trace()
  var that = window._yayd_analyse_ = new Class()

  // 待配置项
  that.config = {
    userInfoKey: 'userinfo',
    // log接受服务端域名
    host: 'https://log.domain.com',
    // 记录唯一用户的僵尸cookie
    uuidKey: '__jfm_uu__',
    // 记录这个用户第一次进入咱们平台的时间，可以大致算一下新访客。
    firstTimeKey: '__jfm_ft__',
    // 设置一次访问：默认10分钟之内没有任何操作，那么结束本次访问。
    sessionKey: '__jfm_ss__',
    // 页面url和page的对应
    configKey: '__jfm_cfg__',
    // url和scp的配置关系
    urlsConfig: {
      'a.b': 'http://'
    }
  }

  that.data = that.getData()

  // that.trackPage()

  // 关闭页面之前刷新session和first_time
  window.onbeforeunload = function () {
    // 重新固化一些值
    that.getData()
  }

  function _track (ops, extraData, systemUrl/*系统调用分析接口url*/) {
    var that = this,
      urlsConfig = that.config.urlsConfig

    if (!ops.page) {
      for (var p in urlsConfig) {
        if (window.location.href.startsWith(urlsConfig[p])) {
          ops.page = p
          break
        }
      }
    }

    var data = that.data,
      params = {
        uu: data.uuid,
        or: encodeURIComponent(window.location.href),
        mi: data.memberId || '',
        sk: data.session,
        fi: data.firstTime,
        si: that.clientWidth() + 'x' + that.clientHeight(),
        //未埋点的页面以*.*来代替，这样可以找出哪些页面没有埋点
        pg: ops.page || '*.*',
        scp: ops.scp || ''
      }

    that.get(systemUrl, params)
  }

  function Trace () {
    function YAYD () {
    }

    YAYD.prototype.get = function (url, params) {
      var that = this,
        q = that.toQueryString(params)

      var hm = document.createElement('img')
      // var hm = document.createElement('iframe')
      hm.src = url + '?t=' + (+new Date()) + (q ? ('&' + q) : '')

      var s = document.getElementsByTagName('body')[0]
      hm.style.display = 'none'
      s.parentNode.insertBefore(hm, s)
    }
    /**
     * 传入参数：scp 即可
     * traceId：业务跟踪id
     */
    YAYD.prototype.trackSCP = function (scp, traceId) {
      var that = this,
        data = that.data,
        params = {
          uu: data.uuid,
          scp: scp,
          tr: traceId,
          mi: data.memberId || ''
        },
        systemUrl = this.host() + '/s.gif'

      that.get(systemUrl, params)
    }

    YAYD.prototype.trackPage = function (ops) {
      ops = ops || ({})
      _track.call(this, ops, ops.data || ({}), this.host() + '/t.gif')
    }

    YAYD.prototype.presist = function (key, data, expire) {
      try {
        if (expire) {
          expire = new Date().getTime() + expire * 1000
        }
        window.localStorage.setItem(key, JSON.stringify(
          {
            expire: expire,
            data: data
          }
        ))
        return true
      } catch (e) {
        return false
      }
    }

    YAYD.prototype.read = function (key) {
      try {
        var dataString = window.localStorage.getItem(key)

        var cached = JSON.parse(dataString)
        if (cached == null || cached.expire < new Date().getTime()) {
          window.localStorage.removeItem(key)
          return null
        }
        return cached.data
      } catch (e) {
        return null
      }
    }

    YAYD.prototype.remove = function (key) {
      try {
        window.localStorage.removeItem(key)
        return true
      } catch (e) {
        return false
      }
    }

    YAYD.prototype.setCache = function (key, data, expire) {
      try {
        if (expire) {
          expire = new Date().getTime() + expire * 1000
        }
        window.sessionStorage.setItem(key, JSON.stringify(
          {
            expire: expire,
            data: data
          }
        ))
        return true
      } catch (e) {
        return false
      }
    }

    YAYD.prototype.getCache = function (key) {
      try {
        var dataString = window.sessionStorage.getItem(key)

        var cached = JSON.parse(dataString)
        if (cached == null || cached.expire < new Date().getTime()) {
          window.sessionStorage.removeItem(key)
          return null
        }
        return cached.data
      } catch (e) {
        return null
      }
    }

    YAYD.prototype.clear = function (key) {
      try {
        window.sessionStorage.removeItem(key)
        return true
      } catch (e) {
        return false
      }
    }

    YAYD.prototype.setCookie = function (key, value, options) {
      options = options || ({})
      options.domain = options.domain || document.domain

      if (typeof options.expires === 'number') {
        /*精确到秒*/
        var days = options.expires, t = options.expires = new Date()
        t.setTime(+t + days * 1000)
      }

      if (value == null) {
        options.expires = new Date()
      }

      document.cookie = [
        key, '=', value,
        options.expires ? '; expires=' + options.expires.toGMTString() : '',
        options.domain ? '; domain=' + options.domain : '',
        options.secure ? '; secure' : '',
        '; path=/'
      ].join('')
    }

    YAYD.prototype.getCookie = function (key) {
      return (key = RegExp('(^| )' + key + '=([^;]*)(;|$)').exec(document.cookie)) ? key[2] : null
    }

    YAYD.prototype.clientHeight = function () {
      var a = document
      return parseInt(window.innerHeight || a.documentElement.clientHeight || a.body && a.body.clientHeight || 0, 10)
    }

    YAYD.prototype.clientWidth = function () {
      var a = document
      return parseInt(window.innerWidth || a.documentElement.clientWidth || a.body && a.body.clientWidth || 0, 10)
    }

    YAYD.prototype.toQueryString = function (data) {
      var that = this

      var queryString = []
      switch (that.type(data)) {
        case 'array':
          that.each(data, function (index, value) {
            var d = that.toQueryString(value)
            if (d && d !== 0) {
              queryString.push(d)
            }
          })
          break
        case 'object':
          that.each(data, function (key, value) {
            switch (that.type(value)) {
              case 'array':
                that.each(value, function (i, v) {
                  (v != null || v !== '') && queryString.push(key + '=' + v)
                })
                break
              case 'object':
                that.each(value, function (i, v) {
                  (v != null || v !== '') && queryString.push(i + '=' + v)
                })
                break
              case 'function':
              case 'undefined':
              case 'error':
              case 'regexp':
                break
              default:
                (value != null || value !== '') && queryString.push(key + '=' + value)
                break
            }
          })
          break
        case 'function':
        case 'undefined':
        case 'error':
        case 'regexp':
          return ''
        default:
          return data
      }
      return queryString.join('&')
    }

    YAYD.prototype.each = function (data, callback) {
      for (var key in data) {
        var value = data[key]
        if (data.hasOwnProperty(key) && value != null) {
          callback(key, value)
        }
      }
    }

    YAYD.prototype.type = function (obj) {
      var t = typeof obj

      if (obj == null) {
        return 'undefined'
      }

      if (t !== 'object') {
        return t
      }

      if (obj.concat != null) {
        return 'array'

      }

      if (obj.compile != null) {
        return 'regexp'
      }

      if (obj.name === 'Error') {
        return error
      }

      return t
    }

    YAYD.prototype.rq = function (name) {
      var reg = new RegExp('(\\?|&)' + name + '=([^&]*)', 'gm')
      var r = window.location.href.substr(0).match(reg)
      if (r != null) {
        var v = []
        $.each(r, function (i, k) {
          k = k.replace(new RegExp('(\\?|&)' + name + '='), '')
          if (k) {
            v.push(k)
          }
        })
        if (v.length === 1) {
          return v[0]
        }
        if (v.length === 0) {
          return null
        } else {
          return v
        }
      } else {
        return null
      }
    }

    YAYD.prototype.uuid = function (len, radix) {
      var CHARS = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('')
      var chars = CHARS, uuid = [], i
      radix = radix || chars.length

      if (len) {
        for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random() * radix]
      } else {
        var r
        uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-'
        uuid[14] = '4'

        for (i = 0; i < 36; i++) {
          if (!uuid[i]) {
            r = 0 | Math.random() * 16
            uuid[i] = chars[(i === 19) ? (r & 0x3) | 0x8 : r]
          }
        }
      }
      return uuid.join('') + '-' + Date.now().toString(16).toUpperCase()
    }

    YAYD.prototype.host = function () {
      return this.config.host
    }

    YAYD.prototype.getData = function () {
      var that = this,
        userinfo = {}

      try {
        userinfo = JSON.parse(window.localStorage.getItem(that.config.userInfoKey)) || {}
      } catch (e) {
        userinfo = {}
      }

      return {
        memberId: (userinfo.memberInfo || {}).id || '',
        uuid: that.getUUID(),
        session: that.getSession(),
        firstTime: that.getFirstTime()
      }
    }

    YAYD.prototype.getFirstTime = function () {
      var that = this,
        firstTime,
        firstTimeKey = that.config.firstTimeKey,
        cookieFirstTime = that.getCookie(firstTimeKey),
        localFirstTime = that.getCache(firstTimeKey),
        pageFirstTime = new Date().getTime(),
        //10年过期
        expires = 3650 * 60 * 60 * 24

      firstTime = cookieFirstTime || localFirstTime || pageFirstTime

      // 第一次访问时间永久不过期
      that.setCookie(firstTimeKey, firstTime, { expires: expires })
      that.presist(firstTimeKey, firstTime, expires)
      that.setCache(firstTimeKey, firstTime, expires)

      return firstTime
    }
    YAYD.prototype.getSession = function () {
      var that = this,
        session,
        sessionKey = that.config.sessionKey,
        cookieSession = that.getCookie(sessionKey),
        localSession = that.getCache(sessionKey),
        pageSession = parseInt(Math.random() * 10000),
        expires = 600

      session = cookieSession || localSession || pageSession

      that.setCookie(sessionKey, session, { expires: expires })
      that.setCache(sessionKey, session, expires)
      return session
    }

    YAYD.prototype.getUUID = function () {
      var that = this,
        uuid,
        uuidKey = that.config.uuidKey,
        sessionUUID = that.getCache(uuidKey),
        localUUID = that.read(uuidKey),
        cookieUUID = that.getCookie(uuidKey),
        pageUUID = that.uuid(),
        //100年过期
        expires = 3650 * 60 * 60 * 24

      /*先取localstorage， 再取sessionStorage，再取cookie, 再取缓存在页面的*/
      uuid = cookieUUID || localUUID || sessionUUID || pageUUID
      that.setCookie(uuidKey, uuid, { expires: expires })
      that.setCache(uuidKey, uuid, expires)
      that.presist(uuidKey, uuid, expires)

      return uuid
    }

    return YAYD
  }

  return _yayd_analyse_
})()
