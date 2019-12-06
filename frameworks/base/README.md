# 核心功能

在你的项目中引入依赖: https://mvnrepository.com/artifact/com.gomcarter.frameworks/base

### 一、配置中心配置自动注入（目前仅支持 nacos 配置中心，<a href="https://github.com/gomcarter/developer/blob/master/README.md">注册中心配置参考</a>）
如：
```
@SpringBootApplication
// 加上标签
@NacosAutoConfig
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

@Service
public class FooService {
    
    /**
    * 配置如：
    * foo=123
    */
    @QiangDaNacosValue(value = "GROUP.DATAID.foo", autoRefreshed = true)
    private String value;

    /**
    * 配置如： 
    * foo=123
    * bar=456
    */
    @QiangDaNacosValue(value = "GROUP.DATAID", autoRefreshed = true)
    private Properties properties;

    /**
    * 配置如： 
    * foo=1234
    * bar=4565
    */
    @QiangDaNacosValue(value = "GROUP.DATAID", autoRefreshed = true)
    private Map map;

    /**
    * 配置如： 
    * {"id":123,"name":"foo"}
    */
    @QiangDaNacosValue(value = "GROUP.DATAID", autoRefreshed = true)
    private SomeClass value;

    /**
    * 配置如： 
    * [{"id":123,"name":"foo"}]
    */
    @QiangDaNacosValue(value = "GROUP.DATAID", autoRefreshed = false)
    private Set<SomeClass> valueList;
}
```
### 二、自动返回结果包装
```
@SpringBootApplication
@NacosAutoConfig
public class Application {
    // 注入RequestMappingHandlerAdapterModify及可以自动包装返回结果
    @Bean
    public RequestMappingHandlerAdapterModify requestMappingHandlerAdapterModify() {
        return new RequestMappingHandlerAdapterModify();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

@RestController
@RequestMapping("test")
public class ItemController {
	  @Autowired
    private TestService testService;

    @GetMapping(value = "query", name = "接口")
    List<Test> query(TestParam params, DefaultPager pager) {
        return this.testService.query(params, pager);
    }
}

public class Test {
  private Long id;
  private String text;
}

结果返回结果：
{
  code: 0,
  message: "message",
  success: true,
  extra: [{
    id: 1,
    text: "text1"
  }]
}
```
### 三、流（Streamable）封装
详见 https://github.com/gomcarter/developer/blob/master/frameworks/base/src/main/java/com/gomcarter/frameworks/base/streaming/Example.java

