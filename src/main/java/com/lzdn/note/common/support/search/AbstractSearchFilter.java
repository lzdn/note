package com.lzdn.note.common.support.search;

 
import java.io.Serializable;
import java.util.Collection;

import org.springframework.util.Assert;

import com.lzdn.note.util.StringPool;
import com.lzdn.note.util.StringUtil;

/**
 * 过滤条件抽象类
 * @package core.support.search
 */
public abstract class AbstractSearchFilter implements SearchFilter, Serializable {
    private static final long serialVersionUID = -7799365275248026150L;
    private final SearchType type;
    private final String property;
    private final SearchOperator operator;
    private final Object value;

    public AbstractSearchFilter(final SearchType type, final String property, final SearchOperator operator, final Object value) {
        Assert.notNull(type, "SearchFilter type must not be null.");
        Assert.notNull(operator, "SearchFilter operator must not be null.");
        if (StringUtil.isEmpty(property)) {
            throw new IllegalArgumentException("SearchFilter property must not be null or empty.");
        }

        String newProperty = property.replace(StringPool.DOT, StringPool.BLANK);

        boolean allowBlankValue = SearchOperator.isAllowBlankValue(operator);
        // 判定过滤属性的给定值是否为空
        boolean isValueBlank = (value == null);
        isValueBlank = isValueBlank || (value instanceof String && StringUtil.isEmpty((String) value));
        isValueBlank = isValueBlank || (value instanceof Object[] && ((Object[]) value).length == 0);
        isValueBlank = isValueBlank || (value instanceof Collection && ((Collection) value).isEmpty());
        //过滤掉空值，即不参与查询
        if (!allowBlankValue && isValueBlank) {
            throw new IllegalArgumentException("SearchFilter value must not be null or empty when operator is not unary.");
        }
        this.type = type;
        this.property = newProperty;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public SearchType getType() {
        return this.type;
    }

    @Override
    public String getProperty() {
        return this.property;
    }

    @Override
    public SearchOperator getOperator() {
        return this.operator;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractSearchFilter that = (AbstractSearchFilter) o;

        if (this.property != null ? !this.property.equals(that.property) : that.property != null) {
            return false;
        }

        if (this.operator != null ? !this.operator.equals(that.operator) : that.operator != null) {
            return false;
        }

        if (this.value != null ? !this.value.equals(that.value) : that.value != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 31 * result + this.property.hashCode();
        result = 31 * result + this.operator.hashCode();
        result = 31 * result + this.value.hashCode();

        return result;
    }
}
