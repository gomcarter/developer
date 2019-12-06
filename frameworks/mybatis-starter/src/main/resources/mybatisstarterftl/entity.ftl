package ${entity.entityPackageName};

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @author ${entity.author} on ${entity.createTime}
 */
public class ${entity.className} {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private ${entity.idSimpleType} ${entity.idName};

<#if entity.hasHibernateVersion>
    /**
     * 对象版本，乐观锁需要可使用
     */
    @JsonIgnore
    private Integer version;
</#if>

<#list entity.propList as prop>
    /**
     * ${prop.note}
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
