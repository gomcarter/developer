package ${entity.actionPackageName};

import ${entity.entityPackageName}.${entity.className};
import ${entity.paramPackage}.${entity.className}Param;
import ${entity.servicePackageName}.${entity.className}Service;
import com.gomcarter.frameworks.mybatis.pager.DefaultPager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ${entity.author} on ${entity.createTime}
 */
@RestController
@RequestMapping("${entity.classInstanceName}")
public class ${entity.className}Controller {

	@Autowired
    private ${entity.className}Service ${entity.classInstanceName}Service;

    @GetMapping(value = "query", name = "接口")
    List<${entity.className}> query(${entity.className}Param params, DefaultPager pager) {
        return this.${entity.classInstanceName}Service.query(params, pager);
    }

    @GetMapping(value = "query", name = "接口")
    Integer query(${entity.className}Param params) {
        return this.${entity.classInstanceName}Service.count(params);
    }

}
