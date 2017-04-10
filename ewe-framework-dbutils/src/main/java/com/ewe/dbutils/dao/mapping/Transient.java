package com.ewe.dbutils.dao.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 控制po的属性不映射到数据库字段
 * 
 * @see SqlBuilder#buildInsert(Object)
 * @see SqlBuilder#buildUpdate(Object, String)
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Transient {
	boolean value() default true;
}
