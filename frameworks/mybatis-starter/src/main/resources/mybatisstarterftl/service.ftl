package ${entity.servicePackageName};

import com.gomcarter.frameworks.mybatis.pager.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ${entity.daoPackageName}.${entity.className}Mapper;
import ${entity.entityPackageName}.${entity.className};

import java.util.Collection;
import java.util.List;

/**
 * @author ${entity.author} on ${entity.createTime}
 */
@Service
public class ${entity.className}Service {

    @Autowired
    private ${entity.className}Mapper ${entity.classInstanceName}Mapper;

    public void insert(${entity.className} ${entity.classInstanceName}) {
        ${entity.classInstanceName}Mapper.insert(${entity.classInstanceName});
    }

    public void insertNoisy(${entity.className} ${entity.classInstanceName}) {
        ${entity.classInstanceName}Mapper.insertNoisy(${entity.classInstanceName});
    }

    public void update(${entity.className} ${entity.classInstanceName}) {
        ${entity.classInstanceName}Mapper.update(${entity.classInstanceName});
    }

    public void updateCas(${entity.className} ${entity.classInstanceName}) {
        ${entity.classInstanceName}Mapper.cas(${entity.classInstanceName});
    }

    public void updateCasNoisy(${entity.className} ${entity.classInstanceName}) {
        ${entity.classInstanceName}Mapper.casNoisy(${entity.classInstanceName});
    }

    public ${entity.className} getById(${entity.idSimpleType} ${entity.idName}) {
        return ${entity.classInstanceName}Mapper.getById(${entity.idName});
    }

    public List<${entity.className}> getByIdList(Collection<${entity.idSimpleType}> ${entity.idName}List) {
        return ${entity.classInstanceName}Mapper.getByIdList(${entity.idName}List);
    }

    public <P> List<${entity.className}> query(P params, Pageable pager) {
        return  ${entity.classInstanceName}Mapper.query(params, pager);
    }

    public <P> Integer count(P params) {
        return  ${entity.classInstanceName}Mapper.count(params);
    }
}
