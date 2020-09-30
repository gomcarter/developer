package com.gomcarter.developer.publics;

import com.gomcarter.developer.entity.TestCase;
import com.gomcarter.developer.params.Interface;
import com.gomcarter.developer.params.Node;
import com.gomcarter.developer.params.Workflow;
import com.gomcarter.developer.service.InterfacesService;
import com.gomcarter.developer.service.JavaService;
import com.gomcarter.developer.service.TestCaseService;
import com.gomcarter.frameworks.base.common.CustomStringUtils;
import com.gomcarter.frameworks.config.mapper.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gomcarter
 */
@RestController
@RequestMapping("publics/gateway")
@Slf4j
public class PublicsGatewayController {

    @Resource
    TestCaseService testCaseService;

    @Resource
    InterfacesService interfacesService;

    @Resource
    JavaService javaService;

    private static class Holder {
        static CloseableHttpClient httpClientLocal;

        static {
            RequestConfig defaultRequestConfig = RequestConfig
                    .custom()
                    .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                    .build();
            httpClientLocal = HttpClients.custom()
                    .setDefaultRequestConfig(defaultRequestConfig)
                    .build();
        }

    }

    @GetMapping(value = "{id}", name = "测试登录")
    Object test(HttpServletRequest request, @PathVariable("id") Long id) throws Exception {
        // 获取入参
        Map<String, String[]> paramsMap = request.getParameterMap();

        TestCase tc = testCaseService.getById(id);

        List<Object> result = new ArrayList<>();
        Workflow workflow = JsonMapper.buildNonNullMapper().fromJson(tc.getWorkflow(), Workflow.class);


        try {
            ScriptEngineManager m = new ScriptEngineManager();
            //获取JavaScript执行引擎
            ScriptEngine engine = m.getEngineByName("nashorn");

            for (Node node : workflow.getNodes()) {
                Interface inter = node.getData();
                Long interfaceId = inter.getInterfaceId();
                // Interfaces faces = interfacesService.getById(interfaceId);

                // Java java = javaService.getById(faces.getFkJavaId());
                // String domain = java.getTestDomain();
                String domain = "http://wechat.test.66buy.com.cn";


                // if (!domain.endsWith("/")) {
                //      domain += "/";
                // }

                // build http client
                URIBuilder uriBuilder = new URIBuilder(URI.create(domain + inter.getUrl()));
                HttpGet get = new HttpGet(uriBuilder.build());
//                Enumeration<String> names = request.getHeaderNames();
//                while(names.hasMoreElements()) {
//                    String key = names.nextElement();
//                    get.setHeader(key, request.getHeader(key));
//                }

                String res = Holder.httpClientLocal.execute(get, (response) -> {
                    StatusLine statusLine = response.getStatusLine();

                    if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
                        throw new RuntimeException("statusCode=" + statusLine.getStatusCode() + ",reason:" + statusLine.getReasonPhrase());
                    }

                    HttpEntity entity = response.getEntity();
                    if (entity == null) {
                        throw new ClientProtocolException("Response no content return.");
                    }

                    return EntityUtils.toString(entity, Charset.forName("UTF-8"));
                });
                log.info(res);

                engine.put(node.getId(), res);

                String js = "%s = JSON.parse(%s); (function() { %s })()";
                if (CustomStringUtils.isNotBlank(inter.getJavascript())) {
                    Object o = engine.eval(String.format(js, node.getId(), node.getId(), inter.getJavascript()));
                    result.add(o);
                } else {
                    result.add(JsonMapper.buildNonNullMapper().fromJson(res, Object.class));
                }
            }
        } catch (Exception e) {
            log.error("some error happened when interfaces sync: ", e);
        }

        return result;
    }

    public static void main(String[] args) {
//        String s = "{\"nodes\":[{\"id\":\"g0\",\"type\":\"rect\",\"label\":\"（g0）小程序直播列表页\",\"x\":266,\"y\":299,\"data\":{\"sleep\":null,\"interfaceId\":568,\"interfaceName\":\"小程序直播列表页\",\"history\":false,\"hash\":\"db1159ab2e3a58993c5a866dcad687be\",\"method\":\"GET\",\"url\":\"/applet/broadcast/list\",\"parameters\":[{\"key\":\"id\",\"body\":false,\"notNull\":false,\"comment\":\"置顶直播间id\",\"type\":\"Long\",\"inputType\":\"number\",\"fix\":true},{\"key\":\"replay\",\"body\":false,\"notNull\":false,\"comment\":\"是否回放\",\"type\":\"Boolean\",\"inputType\":\"switch\",\"fix\":true},{\"key\":\"state\",\"body\":false,\"notNull\":false,\"comment\":\"直播间状态\",\"type\":\"text\",\"inputType\":\"text\",\"fix\":true},{\"key\":\"storeId\",\"body\":false,\"notNull\":false,\"comment\":\"某个门店的直播\",\"type\":\"Integer\",\"inputType\":\"number\",\"fix\":true},{\"key\":\"merchantId\",\"body\":false,\"notNull\":false,\"comment\":\"集团id\",\"type\":\"Integer\",\"inputType\":\"number\",\"fix\":true},{\"key\":\"startNum\",\"body\":false,\"notNull\":false,\"comment\":\"分页参数：起始条数\",\"defaults\":0,\"type\":\"int\",\"inputType\":\"number\",\"fix\":true},{\"key\":\"pageCount\",\"body\":false,\"notNull\":false,\"comment\":\"分页参数：每页条数\",\"defaults\":20,\"type\":\"int\",\"inputType\":\"number\",\"fix\":true},{\"key\":\"tag\",\"value\":\"\",\"type\":\"text\",\"fix\":true,\"defaults\":\"600\"}],\"preParams\":[],\"headers\":[],\"javascript\":null,\"returns\":{\"notNull\":false,\"body\":false,\"type\":\"List\",\"children\":[{\"notNull\":false,\"body\":false,\"type\":\"Object\",\"children\":[{\"key\":\"id\",\"notNull\":false,\"body\":false,\"comment\":\"直播间id\",\"type\":\"Long\"},{\"key\":\"name\",\"notNull\":false,\"body\":false,\"comment\":\"房间名\",\"type\":\"String\"},{\"key\":\"anchorName\",\"notNull\":false,\"body\":false,\"comment\":\"主播名\",\"type\":\"String\"},{\"key\":\"anchorImg\",\"notNull\":false,\"body\":false,\"comment\":\"主播头像\",\"type\":\"String\"},{\"key\":\"startTime\",\"notNull\":false,\"body\":false,\"comment\":\"开始时间\",\"type\":\"Date\"},{\"key\":\"endTime\",\"notNull\":false,\"body\":false,\"comment\":\"结束时间\",\"type\":\"Date\"},{\"key\":\"liveStatus\",\"notNull\":false,\"body\":false,\"comment\":\"直播间状态：100: 待审核，101: 直播中, 102: 未开始, 103: 已结束, 104: 禁播, 105: 暂停中, 106: 异常, 107: 已过期\",\"type\":\"Integer\"},{\"key\":\"roomId\",\"notNull\":false,\"body\":false,\"comment\":\"直播提供者房间id\",\"type\":\"Integer\"},{\"key\":\"selfShareImg\",\"notNull\":false,\"body\":false,\"comment\":\"分享图（又拍云版本）\",\"type\":\"String\"},{\"key\":\"goods\",\"notNull\":false,\"body\":false,\"comment\":\"商品列表，给前端最多返回4个，为了减少存储和序列化\",\"type\":\"List\",\"children\":[{\"notNull\":false,\"body\":false,\"type\":\"Object\",\"children\":[{\"key\":\"selfCoverImg\",\"notNull\":false,\"body\":false,\"comment\":\"商品图片：又拍云版本\",\"type\":\"String\"},{\"key\":\"url\",\"notNull\":false,\"body\":false,\"comment\":\"跳转链接\",\"type\":\"String\"},{\"key\":\"price\",\"notNull\":false,\"body\":false,\"comment\":\"价格，单位：元\",\"type\":\"String\"},{\"key\":\"price2\",\"notNull\":false,\"body\":false,\"comment\":\"价格，单位：元\",\"type\":\"String\"},{\"key\":\"priceType\",\"notNull\":false,\"body\":false,\"comment\":\"1-一口价 price为一口价，price2为0； 2-区间价 price 到 price2 的区间； 3-折扣  原价price，现价price2；\",\"type\":\"Integer\"},{\"key\":\"name\",\"notNull\":false,\"body\":false,\"comment\":\"商品名称\",\"type\":\"String\"}]}]},{\"key\":\"goodsCount\",\"notNull\":false,\"body\":false,\"comment\":\"商品数量\",\"type\":\"Integer\"},{\"key\":\"replay\",\"notNull\":false,\"body\":false,\"comment\":\"是否是回放\",\"type\":\"Boolean\"}]}]},\"java\":{\"id\":1,\"name\":\"天狗-微信中心\",\"alias\":\"wechat\",\"devDomain\":\"http://wechat.dev.66buy.com.cn\",\"testDomain\":\"http://wechat.test.66buy.com.cn\",\"prevDomain\":\"http://wechat.pre.66buy.com.cn\",\"onlineDomain\":\"http://wechat.51tiangou.com\"},\"shouldRun\":true},\"size\":[140,50],\"anchorPoints\":[],\"style\":{\"waiting\":{\"radius\":4,\"fill\":\"yellow\",\"stroke\":\"#4063ff\"},\"running\":{\"radius\":4,\"fill\":\"green\",\"stroke\":\"#4063ff\"},\"success\":{\"radius\":4,\"fill\":\"springgreen\",\"stroke\":\"#4063ff\"},\"failed\":{\"radius\":4,\"fill\":\"red\",\"stroke\":\"#4063ff\"},\"ignore\":{\"radius\":4,\"fill\":\"darkgray\",\"stroke\":\"#4063ff\"},\"warning\":{\"radius\":4,\"fill\":\"orange\",\"stroke\":\"#4063ff\"},\"pending\":{\"radius\":4,\"fill\":\"orange\",\"stroke\":\"#4063ff\"},\"selected\":{\"radius\":4,\"fill\":\"#b3eee9\",\"stroke\":\"#4063ff\"},\"radius\":4,\"fill\":\"#fff\",\"stroke\":\"#4063ff\"},\"_mark\":1},{\"id\":\"g1\",\"type\":\"rect\",\"label\":\"（g1）直播列表页获取标\\r\\n\\r\\n签\",\"x\":268,\"y\":169,\"data\":{\"sleep\":null,\"interfaceId\":567,\"interfaceName\":\"直播列表页获取标\\r\\n签\",\"history\":false,\"hash\":\"79a04897b52acd6c758b6a6cc095b2c6\",\"method\":\"GET\",\"url\":\"/applet/broadcast/tag/list\",\"parameters\":[{\"key\":\"openid\",\"body\":false,\"notNull\":false,\"type\":\"String\",\"inputType\":\"text\",\"fix\":true}],\"preParams\":[],\"headers\":[],\"javascript\":null,\"returns\":{\"notNull\":false,\"body\":false,\"type\":\"Object\",\"children\":[{\"key\":\"subscribe\",\"notNull\":false,\"body\":false,\"comment\":\"是否订阅\",\"type\":\"Boolean\"},{\"key\":\"templateIdList\",\"notNull\":false,\"body\":false,\"comment\":\"模板id\",\"type\":\"List\",\"children\":[{\"notNull\":false,\"body\":false,\"type\":\"String\"}]},{\"key\":\"tagList\",\"notNull\":false,\"body\":false,\"comment\":\"标签\",\"type\":\"List\",\"children\":[{\"notNull\":false,\"body\":false,\"type\":\"Object\",\"children\":[{\"key\":\"name\",\"notNull\":false,\"body\":false,\"comment\":\"标签名称\",\"type\":\"String\"},{\"key\":\"params\",\"notNull\":false,\"body\":false,\"comment\":\"获取直播列表参数，直接拼接在url即可\",\"type\":\"String\"}]}]}]},\"java\":{\"id\":1,\"name\":\"天狗-微信中心\",\"alias\":\"wechat\",\"devDomain\":\"http://wechat.dev.66buy.com.cn\",\"testDomain\":\"http://wechat.test.66buy.com.cn\",\"prevDomain\":\"http://wechat.pre.66buy.com.cn\",\"onlineDomain\":\"http://wechat.51tiangou.com\"},\"shouldRun\":true},\"size\":[140,50],\"anchorPoints\":[],\"style\":{\"waiting\":{\"radius\":4,\"fill\":\"yellow\",\"stroke\":\"#4063ff\"},\"running\":{\"radius\":4,\"fill\":\"green\",\"stroke\":\"#4063ff\"},\"success\":{\"radius\":4,\"fill\":\"springgreen\",\"stroke\":\"#4063ff\"},\"failed\":{\"radius\":4,\"fill\":\"red\",\"stroke\":\"#4063ff\"},\"ignore\":{\"radius\":4,\"fill\":\"darkgray\",\"stroke\":\"#4063ff\"},\"warning\":{\"radius\":4,\"fill\":\"orange\",\"stroke\":\"#4063ff\"},\"pending\":{\"radius\":4,\"fill\":\"orange\",\"stroke\":\"#4063ff\"},\"selected\":{\"radius\":4,\"fill\":\"#b3eee9\",\"stroke\":\"#4063ff\"},\"radius\":4,\"fill\":\"#fff\",\"stroke\":\"#4063ff\"},\"_mark\":1},{\"id\":\"g2\",\"type\":\"rect\",\"label\":\"（g2）订阅直播列表\",\"x\":582,\"y\":171,\"data\":{\"sleep\":null,\"interfaceId\":569,\"interfaceName\":\"订阅直播列表\",\"history\":false,\"hash\":\"fef9be7f4d45d7bd3a85789dcf77cc8e\",\"method\":\"GET\",\"url\":\"/applet/broadcast/subscribe\",\"parameters\":[{\"key\":\"openid\",\"body\":false,\"notNull\":true,\"comment\":\"用户openid\",\"type\":\"String\",\"inputType\":\"text\",\"fix\":true},{\"key\":\"subscribe\",\"body\":false,\"notNull\":true,\"comment\":\"true：订阅；false：取消订阅\",\"defaults\":\"false\",\"type\":\"Boolean\",\"inputType\":\"switch\",\"fix\":true}],\"preParams\":[],\"headers\":[],\"javascript\":null,\"returns\":{\"notNull\":false,\"body\":false,\"type\":\"void\"},\"java\":{\"id\":1,\"name\":\"天狗-微信中心\",\"alias\":\"wechat\",\"devDomain\":\"http://wechat.dev.66buy.com.cn\",\"testDomain\":\"http://wechat.test.66buy.com.cn\",\"prevDomain\":\"http://wechat.pre.66buy.com.cn\",\"onlineDomain\":\"http://wechat.51tiangou.com\"},\"shouldRun\":true},\"size\":[140,50],\"anchorPoints\":[],\"style\":{\"waiting\":{\"radius\":4,\"fill\":\"yellow\",\"stroke\":\"#4063ff\"},\"running\":{\"radius\":4,\"fill\":\"green\",\"stroke\":\"#4063ff\"},\"success\":{\"radius\":4,\"fill\":\"springgreen\",\"stroke\":\"#4063ff\"},\"failed\":{\"radius\":4,\"fill\":\"red\",\"stroke\":\"#4063ff\"},\"ignore\":{\"radius\":4,\"fill\":\"darkgray\",\"stroke\":\"#4063ff\"},\"warning\":{\"radius\":4,\"fill\":\"orange\",\"stroke\":\"#4063ff\"},\"pending\":{\"radius\":4,\"fill\":\"orange\",\"stroke\":\"#4063ff\"},\"selected\":{\"radius\":4,\"fill\":\"#b3eee9\",\"stroke\":\"#4063ff\"},\"radius\":4,\"fill\":\"#fff\",\"stroke\":\"#4063ff\"},\"_mark\":1}],\"edges\":[{\"id\":\"g3\",\"label\":\"g3\",\"source\":\"g1\",\"target\":\"g0\",\"type\":\"running-line\",\"style\":{\"stroke\":\"#4063ff\",\"shadowColor\":\"black\",\"endArrow\":{\"path\":\"M 0,0 L 12,6 L 9,0 L 12,-6 Z\",\"fill\":\"#4063ff\"},\"lineAppendWidth\":20},\"startPoint\":{\"x\":267.60769230769233,\"y\":194.5},\"endPoint\":{\"x\":266.39230769230767,\"y\":273.5}}]}";
//        Workflow workflow = JsonMapper.buildNonNullMapper().fromJson(s, Workflow.class);
//        System.out.println(workflow);
        ScriptEngineManager m = new ScriptEngineManager();
        //获取JavaScript执行引擎
        ScriptEngine engine = m.getEngineByName("JavaScript");
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            try {
                engine.put("i", i);

                Object c = JsonMapper.buildNonNullMapper().fromJson(engine.eval("var a = 1; var b = 2; a + b + i") + "", Object.class);
                System.out.println(c);
            } catch (Exception e) {
                log.error("解析商品信息失败：", e);
            }
        }

        long end = System.currentTimeMillis();

        System.out.println(end - start);
    }
}
