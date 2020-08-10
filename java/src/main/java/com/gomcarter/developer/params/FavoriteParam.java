package com.gomcarter.developer.params;

import com.gomcarter.frameworks.mybatis.annotation.Condition;
import com.gomcarter.frameworks.mybatis.annotation.MatchType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 李银 on 2020-07-30 11:37:37
 */
@Data
@Accessors(chain = true)
public class FavoriteParam {

    private Long id;

    private String owner;

    private Long fkFavoriteId;

    @Condition(field = "fk_favorite_id", type = MatchType.NULL)
    private boolean favoriteNull;
}
