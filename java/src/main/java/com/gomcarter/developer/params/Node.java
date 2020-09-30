package com.gomcarter.developer.params;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gomcarter on 2020-03-02 10:16:26
 */
@Data
@Accessors(chain = true)
public class Node {
    private String id;

    private Interface data;
}
