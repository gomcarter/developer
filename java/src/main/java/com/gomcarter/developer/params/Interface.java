package com.gomcarter.developer.params;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.RequestMethod;

@Data
@Accessors(chain = true)
public class Interface {
    private String hash; //: "db1159ab2e3a58993c5a866dcad687be"
    private Long interfaceId; //: 568
    // java: {id: 1, name: "天狗-微信中心", alias: "wechat", devDomain: "http://wechat.dev.66buy.com.cn", testDomain: "http://wechat.test.66buy.com.cn", …}
    private RequestMethod method;// : "GET"
//    private String parameters; // : (8) [{…}, {…}, {…}, {…}, {…}, {…}, {…}, {…}]
    private String url; // : "/applet/broadcast/list"
    private String javascript;
}
