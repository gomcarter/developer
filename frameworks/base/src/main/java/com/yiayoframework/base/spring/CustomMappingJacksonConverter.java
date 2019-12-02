package com.yiayoframework.base.spring;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class CustomMappingJacksonConverter extends
        MappingJackson2HttpMessageConverter {

    public CustomMappingJacksonConverter() {

        ObjectMapper mapper = this.getObjectMapper();
        //设置输出时包含属性的风格
        mapper.setSerializationInclusion(Include.ALWAYS);

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //禁止使用int代表Enum的order()來反序列化Enum,非常危險
        mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
    }
}
