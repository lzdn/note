package com.lzdn.note.common.support;

import com.lzdn.note.common.support.page.AbstractPageRequest;

/**
 * 分页条件实现类
 * 
 * @package core.support
 */
public class PageRequest extends AbstractPageRequest {
	private static final long serialVersionUID = 3818454816004846209L;

	public static PageRequest newPageRequest(int page, int size) {
		return new PageRequest(page, size);
	}

	public static PageRequest newPageRequest(int page, int size, Sort sort) {
		return new PageRequest(page, size, sort);
	}

	public PageRequest(int page, int size, Sort.Direction direction, String... properties) {
		super(page, size, new Sort(direction, properties));
	}

	public PageRequest(int page, int size, Sort sort) {
		super(page, size, sort);
	}

	public PageRequest(int page, int size) {
		super(page, size, null);
	}

	@Override
	public String toString() {
		return "PageRequest{" + "size=" + getPageSize() + ", page=" + getPageNumber() + ", sort=" + getSort() + '}';
	}
}
