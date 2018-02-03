package com.lzdn.note.common.support.repository;

import java.lang.annotation.*;

/**
 * 开启查询缓存
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnabledQueryCache {
    boolean value() default true;
}
