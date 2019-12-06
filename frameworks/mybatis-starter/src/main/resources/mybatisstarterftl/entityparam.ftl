package ${entity.paramPackage};

import com.gomcarter.frameworks.interfaces.annotation.Notes;
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
public class ${entity.className}Param {

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

    public ${entity.idSimpleType} ${entity.idGetMethod}() {
       	return ${entity.idName};
    }

    public ${entity.className}Param ${entity.idSetMethod}(${entity.idSimpleType} ${entity.idName}) {
        this.${entity.idName} = ${entity.idName};
        return this;
    }

<#list entity.propList as prop>
    public ${prop.simpleType} ${prop.getMethod}() {
        return ${prop.propName};
    }

    public ${entity.className}Param ${prop.setMethod}(${prop.simpleType} ${prop.propName}) {
        this.${prop.propName} = ${prop.propName};
        return this;
    }
</#list>
}
