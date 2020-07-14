package com.gomcarter.developer.params;

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gomcarter on 2019-06-17 16:41:01
 */
@Data
@Accessors(chain = true)
public class EndAuthParam {

    @Notes("主键")
    private Long id;

    @Notes("哪个用户的")
    private String user;

    @Notes("前端项目id")
    private Long fkEndId;
}
