在你的项目中引入依赖: https://mvnrepository.com/artifact/com.gomcarter.frameworks/dubbo-starter

### 使用指南（目前仅支持 nacos 注册中心，<a href="https://github.com/gomcarter/developer/blob/master/README.md">注册中心配置参考</a>）
如：
```
@SpringBootApplication
// 服务提供方和服务消费方都加上标签
@EnableNacosDubbo(port = 20880)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// 服务提供
import org.apache.dubbo.config.annotation.Service;

@Service
public class FooApiImpl implements FooApi {

    @Autowired
    private FooService fooService;

    @Override
    public Foo getById(Integer id) {
        return fooService.getById(id);
    }
}

// 服务消费方
// 服务提供
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

@Service
public class BarService {

    @Reference
    private FooApi fooApi;

    public void doSomething(Integer id) {
    	// ...
	Foo foo = fooApi.getById(id);
	// ...
    }
}
```
