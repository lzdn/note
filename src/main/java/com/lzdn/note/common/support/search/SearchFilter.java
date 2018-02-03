package com.lzdn.note.common.support.search;

import java.util.Collection;

/**
 * 过滤条件接口
 * @package core.support.search
 */
public interface SearchFilter {
    /**
     * 获取条件连接类型
     * @return 条件连接类型 {@link SearchType}
     */
    public SearchType getType();

    /**
     * 获取需要过滤的属性名
     * @return 属性名
     */
    public String getProperty();

    /**
     * 获取操作符
     * @return 操作符
     */
    public SearchOperator getOperator();

    /**
     * 获取给定的属性值
     * @return 属性值
     */
    public Object getValue();

    /**
     * 有无子过滤条件，如or (a = 1 and b = 2)
     * @return boolean
     */
    public boolean hasChildFilter();

    /**
     * 获取子过滤条件
     * @return
     */
    public Collection<SearchFilter> getChildFilters();
}
