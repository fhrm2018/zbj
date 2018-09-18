package com.qiyou.dhlive.prd.component.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceAnnotation {

	public String value() default "";

	public String name() default "";

	public String pName() default "";
	
	public String pCode() default "";
	
	public String icon() default "";
	
	public String code() default "";

	public int type() default 1;

	public String group() default "";

	public String url() default "";

	public String remark() default "";

}
