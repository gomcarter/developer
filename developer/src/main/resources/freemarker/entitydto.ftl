package ${entity.dtoPackageName};

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
 * @ClassName J${entity.className}
 * @Description
 * @author ${entity.author}
 * @date ${entity.createTime}
 */
public class J${entity.className} {

    /**
     * @Description 主键
     */
    private ${entity.idSimpleType} ${entity.idName};

<#list entity.propList as prop>
    /**
     * @Description ${prop.note}
     */
    private ${prop.simpleType} ${prop.propName};
</#list>

    public ${entity.idSimpleType} ${entity.idGetMethod}() {
       	return ${entity.idName};
    }

    public J${entity.className} ${entity.idSetMethod}(${entity.idSimpleType} ${entity.idName}) {
        this.${entity.idName} = ${entity.idName};
        return this;
    }

<#list entity.propList as prop>
    public ${prop.simpleType} ${prop.getMethod}() {
        return ${prop.propName};
    }

    public J${entity.className} ${prop.setMethod}(${prop.simpleType} ${prop.propName}) {
        this.${prop.propName} = ${prop.propName};
        return this;
    }

</#list>
}
