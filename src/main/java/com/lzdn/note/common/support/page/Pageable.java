package com.lzdn.note.common.support.page;

import com.lzdn.note.common.support.Sort;

/**
 * 分页条件接口
 */
public interface Pageable {
	/**
	 * 起始页码为0
	 * 
	 * @return 当前页码 int
	 */
	public int getPageNumber();

	/**
	 * @return 每页条目大小 int
	 */
	public int getPageSize();

	/**
	 * @return 页码偏移量
	 */
	public int getOffset();

	/**
	 * @return 排序条件 {@link Sort}
	 */
	public Sort getSort();

	/**
	 * 如果当前页是第一页则返回false否则返回true
	 * 
	 * @return boolean
	 */
	public boolean hasPrevious();
}
