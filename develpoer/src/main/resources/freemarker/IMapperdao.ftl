package ${entity.daoPackageName};

import com.yiayoframework.base.pager.Pageable;
import ${entity.entityPackageName}.${entity.className};
import ${entity.paramPackageName}.J${entity.className}QueryParams;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 *
 * @ClassName ${entity.className}Mapper
 * @Description
 * @author ${entity.author}
 * @date ${entity.createTime}
 */
public interface ${entity.className}Mapper {

    ${entity.idSimpleType} insert(${entity.className} ${entity.classInstanceName});

    Integer update(${entity.className} ${entity.classInstanceName});

    ${entity.className} getById(${entity.idSimpleType} ${entity.idName});

    List<${entity.className}> getByIdList(@Param("${entity.idName}List") Collection<${entity.idSimpleType}> ${entity.idName}List);

    List<${entity.className}> query(
                @Param("params") J${entity.className}QueryParams params,
                @Param("pager") Pageable pager);

    Integer count(@Param("params") J${entity.className}QueryParams params);
}
