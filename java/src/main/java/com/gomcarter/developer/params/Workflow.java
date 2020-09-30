package com.gomcarter.developer.params;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author gomcarter on 2020-03-02 10:16:26
 */
@Data
@Accessors(chain = true)
public class Workflow {

    private List<Edge> edges;

    private List<Node> nodes;
}
