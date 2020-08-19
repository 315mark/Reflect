package com.example.yangon.library.annotations;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ListenerInvocationHandler implements InvocationHandler {
	//通知切面那个类中的方法(拦截目标)
	private  Object target;
	private HashMap<String,Method> map =new HashMap<>();

	public ListenerInvocationHandler(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		//将本应该执行回调的方法onClick等方法替换成开发者自定义的方法(面向切面开发)
		if (target != null){
			//假如是onClick()
			String methodName = method.getName();

			//这里可以做阻塞事件 比如多次点击只执行一次

			//有需要拦截的回调方法 将不再是原来的回调方法onClick(重新赋值)
			method = map.get(methodName);
			if (method != null){
				if (method.getGenericParameterTypes().length ==0){
					return method.invoke(target);
				}
				method.invoke(target,args);
			}
		}
		return null;
	}

	//拦截回调方法,执行开发者自定义方法
	public  void add(String callbackListener,Method method){
		map.put(callbackListener,method);
	}
}
