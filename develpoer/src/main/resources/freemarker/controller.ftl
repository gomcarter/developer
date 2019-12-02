package ${entity.actionPackageName};

import com.yiayoframework.base.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @ClassName: ${entity.className}Controller
 * @Description:
 * @author ${entity.author}
 * @date: ${entity.createTime}
 */
@RestController
@RequestMapping("privates/${entity.classInstanceName}")
public class ${entity.className}Controller extends BaseController{

	@Autowired
    private ${entity.className}Service ${entity.classInstanceName}Service;

}
