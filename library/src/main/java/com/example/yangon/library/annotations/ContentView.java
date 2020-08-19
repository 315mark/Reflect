package com.example.yangon.library.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //注解作用在类上
@Retention(RetentionPolicy.RUNTIME) //运行时通过反射 获取该注解的值
public @interface ContentView {

	//获取布局常量值
	int value();
}
