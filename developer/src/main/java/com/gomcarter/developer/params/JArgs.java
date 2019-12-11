package com.gomcarter.developer.params;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
@Data
@Accessors(chain = true)
public class JArgs {

    private Class key;

    private Object value;
}
