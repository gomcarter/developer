package com.gomcarter.developer.dto;

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
@Data
@Accessors(chain = true)
public class EndAuthDto {

    @Notes("主键")
    private Long id;

    @Notes("主键")
    private String config;
}
