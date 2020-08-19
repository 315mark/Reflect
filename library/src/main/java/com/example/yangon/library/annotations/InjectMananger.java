package com.example.yangon.library.annotations;

import android.app.Activity;
import android.view.View;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectMananger {

	public static void inject(Activity activity){
		//布局注入
		injectLayout(activity);

		//控件注入
		injectView(activity);

		//事件注入
		injectEvents(activity);
	}

	private static void injectEvents(Activity activity) {
		//获取所在Acticity
		Class<? extends Activity> clazz = activity.getClass();
		//获取所有方法包括私有
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods){
			//获取方法所有的注解
			Annotation[] annotations = method.getAnnotations();
			for (Annotation annotation : annotations){
				Class<? extends Annotation> type = annotation.annotationType();
				if (type != null){
					//点击 ,长按  ,条目点击共同特性封装
					EventBase eventBase = type.getAnnotation(EventBase.class);
					if (eventBase != null){  //获取包里面所有特性
						String listenerSetter = eventBase.listenerSetter();//获取监听方法名
						Class<?> listenerType = eventBase.listenerType();//获取监听接口
						String callBackListener = eventBase.callBackListener();//获取回调方法
						//通过代理完成点击动作

						//通过annotationType获取onClick注解的value值 (R.id.xxx)
						try {
							Method value = type.getDeclaredMethod("value");
							int[] viewIds = (int[]) value.invoke(annotation);
							//面向切面 此处就是开发者自定点击事件
							ListenerInvocationHandler handler = new ListenerInvocationHandler(activity);
							handler.add(callBackListener,method);
							//获取viewids  onclik的id值
							Object listener =Proxy.newProxyInstance(listenerType.getClassLoader(),new Class[]{listenerType},handler);
							for (int viewId : viewIds ){
								//获取view
								View view = activity.findViewById(viewId);
								//控件类的父类view.class
								Method setxxx = view.getClass().getMethod(listenerSetter, listenerType);
								//执行方法
								setxxx.invoke(view,listener);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	private static void injectView(Activity activity) {
		Class<? extends Activity> clazz = activity.getClass();
		//获取所有的
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields){
			BindView bindView = field.getAnnotation(BindView.class);
			if (bindView != null){
				int viewId = bindView.value();
				try {
					Method method = clazz.getMethod("findViewById", int.class);
					//执行findViewById方法  但还没赋值
					Object view = method.invoke(activity, viewId);
					//设置注解可以访问private 不设置 不能带private
					field.setAccessible(true);
					//进行赋值操作  属性 = 执行findviewbyid(xxxx);
					field.set(activity,view);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void injectLayout(Activity activity) {
		Class<? extends Activity> clazz = activity.getClass();
		ContentView contentView= clazz.getAnnotation(ContentView.class);
		if (contentView !=null){
			int layoutId = contentView.value();
			try {
				Method method = clazz.getMethod("setContentView", int.class);
				method.invoke(activity,layoutId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
