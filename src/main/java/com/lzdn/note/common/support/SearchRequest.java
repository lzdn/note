package com.lzdn.note.common.support;

 

import java.util.List;

import com.lzdn.note.common.support.page.Pageable;
import com.lzdn.note.common.support.search.AbstractSearchRequest;
import com.lzdn.note.common.support.search.SearchFilter;
import com.lzdn.note.common.support.search.Searchable;

/**
 * 查询条件实现类，包括过滤条件、分页条件、排序
 * @package core.support
 */
public class SearchRequest extends AbstractSearchRequest {
    private static final long serialVersionUID = 7126784443721187450L;

    public static SearchRequest newSearchRequest(List<SearchFilter> filters, Sort sort, Pageable page) {
        return new SearchRequest(filters, sort, page);
    }

    public static SearchRequest newSearchRequest(Sort sort, Pageable page) {
        return new SearchRequest(sort, page);
    }

    public static SearchRequest newSearchRequest() {
        return new SearchRequest();
    }

    public SearchRequest(List<SearchFilter> filters, Sort sort, Pageable page) {
        super(filters, sort, page);
    }

    public SearchRequest(Sort sort, Pageable page) {
        this(null, sort, page);
    }

    public SearchRequest() {
        this(null, null, null);
    }

    @Override
    public Searchable setPage(int pageNumber, int pageSize) {
        return setPage(new PageRequest(pageNumber, pageSize));
    }

    @Override
    public String toString() {
        return "SearchRequest{" +
                "filters=" + getSearchFilters() +
                ", page=" + getPage() +
                ", sort=" + getSort() +
                '}';
    }
}
