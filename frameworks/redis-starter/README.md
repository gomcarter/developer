在你的项目中引入依赖: https://mvnrepository.com/artifact/com.gomcarter.frameworks/redis-starter

### 使用指南（目前仅支持 nacos 配置中心，<a href="https://github.com/gomcarter/developer/blob/master/README.md">配置中心配置参考</a>）

```
@SpringBootApplication
// redis的配置信息
@EnableNacosRedis(dataId = "DATA_ID", group = "GROUP")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```
nacos 中配置如下：
```
#最大空闲数
redis.maxIdle=200
redis.minIdle=10
#最大连接数
redis.maxActive=300

#最大建立连接等待时间
redis.maxWait=1000

#客户端超时时间单位是毫秒
redis.timeout=300000
redis.maxTotal=1000
#最大重试次数
redis.maxAttempts=6
#明是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
redis.testOnBorrow=true

## 密码
redis.password=REDIS_PASSWORD
## 连接地址--standalone的配置方式
standalone.host.port=REDIS_SERVER_ADDRESS:PORT

## standalone -- 单机模式， cluster -- 集群模式
redis.mode=standalone

# cluster 模式
# cluster1.host.port=192.168.1.12:7000
# cluster2.host.port=192.168.1.12:7001
# cluster3.host.port=192.168.1.13:7000
# cluster4.host.port=192.168.1.13:7001

## 是否开启缓存
redis.cache=true
```

# 核心功能

### 缓存、删除缓存、分布式锁

```

import com.gomcarter.frameworks.redis.annotation.Cache;
import com.gomcarter.frameworks.redis.annotation.DelCache;
import com.gomcarter.frameworks.redis.annotation.Lock;

@Service
public class FooService {

    @Autowired
    private FooMapper fooMapper;
    
    @Cache(key = "FooService.getById", time = -1L, argsIndex = {0}, nullable = true, await = 2000)
    // @Cache -- 默认 key 为：当前类 ${package}.FooService.getById_MD5(args), 缓存5分钟，所有参数参与 key 生成，可以缓存null 值，如果有其他线程(或集群中其他节点)在执行此方法还没有出结果，就等待10秒，过期报错
    public Foo getById(Long id) {
        return fooMapper.getById(id);
    }
    
    // 方法执行完毕之后删除缓存 FooService.getById_MD5(id)
    @DelCache(key = "FooService.getById", argsIndex = {0})
    public void update(Long id, Foo foo) {
        this.fooMapper.update(...);
    }
    
    // 如果有其他线程(或集群中其他节点)在执行此方法，则报错；如果设置 await 字段则等待设置对应的时间
    @Lock
    public Foo create(Foo foo) {
        this.fooMapper.create(...);
    }
}
```

### RedisProxy
@Service
public class FooService {

    @Autowired
    private RedisProxy redisProxy;
    
    public Foo getById(Long id) {
        redisProxy.get(key);
        redisProxy.del(key);
        redisProxy.lock(key);
        redisProxy.set(key, value);
        ....
        
    }
}


