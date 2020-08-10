package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.FavoriteMapper;
import com.gomcarter.developer.dto.FavoriteTree;
import com.gomcarter.developer.entity.Favorite;
import com.gomcarter.developer.params.FavoriteParam;
import com.gomcarter.frameworks.base.common.AssertUtils;
import com.gomcarter.frameworks.base.common.HexCaculateUtils;
import com.gomcarter.frameworks.base.exception.CustomException;
import com.gomcarter.frameworks.base.exception.NoPermissionException;
import com.gomcarter.frameworks.base.pager.Pageable;
import com.gomcarter.frameworks.base.streaming.Streamable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 李银 on 2020-07-30 09:37:40
 */
@Service
public class FavoriteService {

    @Resource
    private FavoriteMapper favoriteMapper;

    public Favorite insert(Favorite favorite) {
        favoriteMapper.insert(favorite);
        return favorite;
    }

    public void insertNoisy(Favorite favorite) {
        favoriteMapper.insertNoisy(favorite);
    }

    public void update(Favorite favorite) {
        favoriteMapper.update(favorite);
    }

    public void updateCas(Favorite favorite) {
        favoriteMapper.cas(favorite);
    }

    public void updateCasNoisy(Favorite favorite) {
        favoriteMapper.casNoisy(favorite);
    }

    public Favorite getById(Long id) {
        return favoriteMapper.getById(id);
    }

    public List<Favorite> getByIdList(Collection<Long> idList) {
        return favoriteMapper.getByIdList(idList);
    }

    public <P> List<Favorite> query(P params, Pageable pager) {
        return favoriteMapper.query(params, pager);
    }

    public <P> Integer count(P params) {
        return favoriteMapper.count(params);
    }

    public List<FavoriteTree> tree(String owner) {
        List<Favorite> favoriteList = this.favoriteMapper.getByOwner(owner);

        // 先转成id和tree的map
        Map<Long, FavoriteTree> treeMap = Streamable.valueOf(favoriteList)
                .map(s -> new FavoriteTree()
                        .setId(s.getId())
                        .setCode(s.getCode())
                        .setText(s.getName())
                        .setIsLeaf(s.getIsLeaf())
                        .setSort(s.getSort())
                        .setParentId(s.getFkFavoriteId()))
                .sorted(Comparator.comparing(FavoriteTree::getSort))
                .uniqueGroupbySafely(FavoriteTree::getId)
                .collect();

        //将节点之间的⽗⼦关系关联起来
        return treeMap.values()
                .stream()
                .peek(s -> {
                    FavoriteTree t = treeMap.get(s.getParentId());
                    if (t != null) {
                        t.addChild(s);
                    }
                })
                .filter(s -> s.getParentId() == null)
                .sorted(Comparator.comparing(FavoriteTree::getSort))
                // 降序排序
                .collect(Collectors.toList());
    }

    public void updateName(Long id, String name, String owner) {
        Favorite favorite = this.favoriteMapper.getById(id);
        AssertUtils.isTrue(owner.equals(favorite.getOwner()), new NoPermissionException());

        this.favoriteMapper.update(favorite.setName(name));
    }

    private void handleValidateAdd(Long parentId, String name) {
        AssertUtils.isTrue(StringUtils.isNotBlank(name), new CustomException("名称不能为空!"));
        //重名校验（相同⽗分类下）
        Favorite favorite = favoriteMapper.queryByParentIdAndName(parentId, name, null);
        AssertUtils.isNull(favorite, new CustomException(String.format("该名称[%s][%s]已经存在!", parentId, name)));
    }

    public final static String NEW_CATEGORY_DEFAULT_CODE = "00";

    private Favorite updateParent(Long parentFavoriteId) {
        if (null == parentFavoriteId) {
            return null;
        }

        Favorite favorite = getById(parentFavoriteId);
        if (favorite.getIsLeaf()) {
            favorite.setIsLeaf(false);
            update(favorite);
        }
        return favorite;
    }

    public String generateCode(Favorite parent, String owner) {
        String parentCode = (parent == null || StringUtils.isBlank(parent.getCode())) ? StringUtils.EMPTY : parent.getCode();
        String result = favoriteMapper.getMaxCode(null == parent ? null : parent.getId(), owner);
        if (result == null) {
            result = NEW_CATEGORY_DEFAULT_CODE;
        } else {
            int length = result.length();
            result = result.substring(length - NEW_CATEGORY_DEFAULT_CODE.length(), length);
        }
        return parentCode + HexCaculateUtils.codePlus(result, 36);
    }

    public Favorite create(Long parentId, String name, String owner) {
        //校验
        handleValidateAdd(parentId, name);

        // 获取子节点的数量
        Integer childrenSize = this.count(childrenParams(parentId, owner));

        //更新父分类为非叶子节点并返回对象(可能为空)
        Favorite parent = updateParent(parentId);
        //获取新分类的code(空父节点返回默认)
        String code = generateCode(parent, owner);

        // 创建新Favorite
        Favorite favorite = new Favorite()
                .setCode(code)
                .setFkFavoriteId(parentId)
                .setName(name)
                .setIsLeaf(true)
                .setOwner(owner)
                .setSort(childrenSize);

        return insert(favorite);

    }

    private FavoriteParam childrenParams(Long parentId, String owner) {
        FavoriteParam param = new FavoriteParam().setOwner(owner);
        if (parentId == null) {
            param.setFavoriteNull(true);
        } else {
            param.setFkFavoriteId(parentId);
        }
        return param;
    }

    /**
     * 当前节点的新排序
     *
     * @param id
     * @param sort
     * @param owner
     */
    public void updateSort(Long id, Integer sort, String owner) {
        Favorite favorite = this.favoriteMapper.getById(id);
        AssertUtils.isTrue(owner.equals(favorite.getOwner()), new NoPermissionException());

        Integer original = favorite.getSort();
        // 没有改变，返回
        if (original.equals(sort)) {
            return;
        }

        // 由于 original越大表示排序越靠后
        // 所以如果 original > sort（原值大于现值）=> 说明当前节点排到前面去了，则之前的位置和现在的位置之间的所有节点排序值 + 1 => 全部往后挪一个位置
        // 否则 全部 - 1
        this.favoriteMapper.updateSort(favorite.getFkFavoriteId(), owner,
                Math.min(original, sort),
                Math.max(original, sort),
                original > sort ? 1 : -1
        );

        // 把自己顺序变成最新的sort
        this.update(favorite.setSort(sort));
    }
}
