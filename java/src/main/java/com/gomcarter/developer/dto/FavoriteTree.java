package com.gomcarter.developer.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gomcarter.frameworks.interfaces.annotation.Ignore;
import com.gomcarter.frameworks.interfaces.annotation.Notes;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class FavoriteTree {
    @Notes(value = "id", notNull = true)
    private Long id;

    @Notes(value = "名称", notNull = true)
    private String text;

    @Notes(value = "编码", notNull = true)
    private String code;

    @Notes(value = "是否是叶子节点：末端节点", notNull = true)
    private Boolean isLeaf;

    @Notes("排序，小的排前面")
    private Integer sort;

    @JsonIgnore
    @Ignore
    private Long parentId;

    @Notes(value = "子节点列表")
    private List<FavoriteTree> nodes;

    public FavoriteTree addChild(FavoriteTree child) {
        if (child != null) {
            if (nodes == null) {
                nodes = new ArrayList<>();
            }
            nodes.add(child);
        }
        return this;
    }
}
