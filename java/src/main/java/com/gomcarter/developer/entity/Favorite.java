package com.gomcarter.developer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author 李银 on 2020-07-30 11:37:37
 */
@Data
@Accessors(chain = true)
public class Favorite {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 对象版本，乐观锁需要可使用
     */
    @JsonIgnore
    private Integer version;

    /**
     * 名称
     */
    private String name;
    /**
     * 所属者
     */
    private String owner;
    /**
     * 排序，大的排前面
     */
    private Integer sort;
    /**
     * 编码：0101；01
     */
    private String code;
    /**
     * 是否叶子节点
     */
    private Boolean isLeaf;
    /**
     * 父分类ID
     */
    private Long fkFavoriteId;
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date modifyTime;

    //@NotReplaceableStart
    // 重新生成代码时，NotReplaceableStart -> NotReplaceableEnd 中间的内容不会被覆盖


    //@NotReplaceableEnd
}
