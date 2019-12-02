package ${entity.entityPackageName};

<#if entity.hasHibernateVersion>
import com.fasterxml.jackson.annotation.JsonIgnore;
</#if>
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
 * @ClassName ${entity.className}
 * @Description
 * @author ${entity.author}
 * @date ${entity.createTime}
 */
public class ${entity.className} {

    /**
     * @Description 主键
     */
    private ${entity.idSimpleType} ${entity.idName};

<#if entity.hasHibernateVersion>
    /**
     * @Description 对象版本，乐观锁需要可使用
     */
    @JsonIgnore
    private Integer version;
</#if>

<#list entity.propList as prop>
    /**
     * @Description ${prop.note}
     */
    private ${prop.simpleType} ${prop.propName};
</#list>

    public ${entity.idSimpleType} ${entity.idGetMethod}() {
       	return ${entity.idName};
    }

    public ${entity.className} ${entity.idSetMethod}(${entity.idSimpleType} ${entity.idName}) {
        this.${entity.idName} = ${entity.idName};
        return this;
    }
<#if entity.hasHibernateVersion>
    public Integer getVersion() {
        return version;
    }

    public ${entity.className} setVersion(Integer version) {
        this.version = version;
        return this;
    }

</#if>
<#list entity.propList as prop>
    public ${prop.simpleType} ${prop.getMethod}() {
        return ${prop.propName};
    }

    public ${entity.className} ${prop.setMethod}(${prop.simpleType} ${prop.propName}) {
        this.${prop.propName} = ${prop.propName};
        return this;
    }

</#list>
}
