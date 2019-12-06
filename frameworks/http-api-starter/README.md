在你的项目中引入依赖: https://mvnrepository.com/artifact/com.gomcarter.frameworks/redis-starter

### 使用指南（目前仅支持 nacos 配置中心，<a href="https://github.com/gomcarter/developer/blob/master/README.md">配置中心配置参考</a>）



# 核心功能

### 封装 rpc

##### 服务提供方：
```
@RestController
@RequestMapping("public/foo")
public class FooController {

    @GetMapping(value = "getById", name = "接口")
    Foo getById(Long id) {
        return this.fooService.getById(id);
    }
}
```

##### 创建一个 jar 包
```
import com.gomcarter.frameworks.httpapi.api.NacosConfigurableApi

public class DemoApi extends NacosConfigurableApi {
    
    @Override
    public NacosConfig getDiamondConfig() {
        // nacos 配置中心存储 http 的服务地址 如：
        // demo.api.getbyid=http://fooserver/public/foo/getById
        return NacosConfig.valueOf("GROUP", "DATA_ID");
    }

    public Foo getById(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        
        // 更多方法见：com.gomcarter.frameworks.httpapi.api.BaseApi
        return this.get("demo.api.getbyid", Foo.class, params);
    }
}
```

##### 调用方
```
注入一个 DemoApi 为一个 bean
@Configuration
public class Configuration {
  @Bean
  DemoApi demoApi() {
    return new DemoApi();
  }
}

使用
@Service
public class BarService {
  @Autowired
  DemoApi demoApi;
  
  public void funtion() {
    // ...
    Foo foo = demoApi.getById(1L);
    // ...
  }
}
```

