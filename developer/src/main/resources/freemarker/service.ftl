package ${entity.servicePackageName};

import com.gomcarter.frameworks.base.pager.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ${entity.daoPackageName}.${entity.className}Mapper;
import ${entity.entityPackageName}.${entity.className};
import ${entity.servicePackageName}.${entity.className}Service;
import ${entity.paramPackageName}.J${entity.className}QueryParams;

import java.util.Collection;
import java.util.List;

/**
 *
 * @ClassName ${entity.className}Service
 * @Description
 * @author ${entity.author}
 * @date ${entity.createTime}
 */
@Service
public class ${entity.className}Service {

    @Autowired
    private ${entity.className}Mapper ${entity.classInstanceName}Mapper;

    public void insert(${entity.className} ${entity.classInstanceName}) {
        ${entity.classInstanceName}Mapper.insert(${entity.classInstanceName});
    }

    public void update(${entity.className} ${entity.classInstanceName}) {
        ${entity.classInstanceName}Mapper.update(${entity.classInstanceName});
    }

    public ${entity.className} getById(${entity.idSimpleType} ${entity.idName}) {
        return ${entity.classInstanceName}Mapper.getById(${entity.idName});
    }

    public List<${entity.className}> getByIdList(Collection<${entity.idSimpleType}> ${entity.idName}List) {
        return ${entity.classInstanceName}Mapper.getByIdList(${entity.idName}List);
    }

    public List<${entity.className}> query(J${entity.className}QueryParams params, Pageable pager) {
        return  ${entity.classInstanceName}Mapper.query(params, pager);
    }

    public Integer count(J${entity.className}QueryParams params) {
        return  ${entity.classInstanceName}Mapper.count(params);
    }
}
