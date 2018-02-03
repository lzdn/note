package com.lzdn.note.exception;

import org.springframework.core.NestedRuntimeException;

public class SearchException extends NestedRuntimeException {

	/** 
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
	 * @since JDK 1.8
	 */  
	private static final long serialVersionUID = 1L;

	public SearchException(String msg) {
		super(msg);
	}

	public SearchException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
