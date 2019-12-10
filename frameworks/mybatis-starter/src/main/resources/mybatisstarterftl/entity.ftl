package ${entity.entityPackageName};

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;
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
@Data
@Accessors(chain = true)
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

    //@NotReplaceableStart
    // 重新生成代码时，NotReplaceableStart -> NotReplaceableEnd 中间的内容不会被覆盖


    //@NotReplaceableEnd
}
