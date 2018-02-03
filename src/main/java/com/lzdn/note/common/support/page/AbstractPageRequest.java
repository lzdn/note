package com.lzdn.note.common.support.page;


import java.io.Serializable;

import com.lzdn.note.common.support.Sort;

/**
 * 分页条件抽象
 */
public abstract class AbstractPageRequest implements Pageable, Serializable {
    private static final long serialVersionUID = 7730999323379142877L;
    private final int page;
    private final int size;
    private final Sort sort;

    public AbstractPageRequest(int page, int size, Sort sort) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be less than zero!");
        }
        if (size < 1) {
            throw new IllegalArgumentException("Page size must not be less than one!");
        }

        this.page = page;
        this.size = size;
        this.sort = sort;
    }

    @Override
    public int getPageNumber() {
        return this.page;
    }

    @Override
    public int getPageSize() {
        return this.size;
    }

    @Override
    public int getOffset() {
        return this.page * this.size;
    }

    @Override
    public Sort getSort() {
        return this.sort;
    }

    @Override
    public boolean hasPrevious() {
        return this.page > 0;
    }

    /*
    * (non-Javadoc)
    * @see java.lang.Object#hashCode()
    */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + page;
        result = prime * result + size;

        return result;
    }

    /*
    * (non-Javadoc)
    * @see java.lang.Object#equals(java.lang.Object)
    */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        AbstractPageRequest other = (AbstractPageRequest) obj;
        return this.page == other.page && this.size == other.size;
    }
}
