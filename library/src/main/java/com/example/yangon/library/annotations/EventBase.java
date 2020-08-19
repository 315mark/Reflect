package com.example.yangon.library.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)//作用在另外一个注解上
@Retention(RetentionPolicy.RUNTIME)
public @interface EventBase {

	// 监听方法
	String listenerSetter();

	//监听的接口
	Class<?> listenerType();

	//观察到用户行为后,告知回掉方法
	String callBackListener();

}
