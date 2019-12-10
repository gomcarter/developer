package ${entity.dtoPackageName};

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import lombok.Data;
import lombok.experimental.Accessors;
<#if entity.hasSetType>
import java.util.Set;
</#if>
<#if entity.hasDateType>
import java.util.Date;
</#if>
<#if entity.hasDecimalType>
import java.math.BigDecimal;
</#if>

/**
 * @author ${entity.author} on ${entity.createTime}
 */
@Data
@Accessors(chain = true)
public class ${entity.className}Dto {

    /**
     * 主键
     */
    @Notes("主键")
    private ${entity.idSimpleType} ${entity.idName};

<#list entity.propList as prop>
    /**
     * ${prop.note}
     */
    @Notes("${prop.note}")
    private ${prop.simpleType} ${prop.propName};
</#list>
}
