/**
 * Title: SingletonFactory.java
 * Description:
 * Copyright: Copyright (c) 2013-2015 luoxudong.com
 * Company: 个人
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2013-8-2 下午3:58:10
 * Version 1.0 
 */
package com.luoxudong.app.singletonfactory; 	

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

/** 
 * ClassName: SingletonFactory 
 * Description:单例管理工厂类 
 * Create by 罗旭东
 * Date 2013-8-2 下午3:58:10 
 */
public class SingletonFactory {
	private static final String TAG = SingletonFactory.class.getSimpleName();
	
	/**
	 * 缓存普通对象列表
	 */
	private static Map<String, Object> objectCache = Collections.synchronizedMap(new HashMap<String, Object>());
	
	/**
	 * 
	 * getInstance
	 * 新创建一个普通对象,并放入对象列表中
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(Class<T> clazz)
	{
		T result = null;
		synchronized(SingletonFactory.class)
		{
			if (objectCache == null)
			{
				objectCache = Collections.synchronizedMap(new HashMap<String, Object>());
			}
			
		}
		synchronized (objectCache)
		{
			result = (T) objectCache.get(clazz.getName());
			if (result == null)
			{
				result = createInstance(clazz);
				
				if (result != null)
				{
					objectCache.put(clazz.getName(), result);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 创建一个带参数对象
	 * @param clazz
	 * @param parameterTypes
	 * @param paramValues
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(Class<T> clazz, Class<?>[] parameterTypes, Object[] paramValues)
	{
		String key = clazz.getName();
		T result = null;
		synchronized(SingletonFactory.class)
		{
			if (objectCache == null)
			{
				objectCache = Collections.synchronizedMap(new HashMap<String, Object>());
			}
			
		}
		
		synchronized (objectCache)
		{
			if (paramValues != null)
			{
				for (int nIndex = 0; nIndex < paramValues.length; nIndex++)
				{
					key += "|";
					key += paramValues[nIndex].toString();
				}
			}
			
			result = (T) objectCache.get(key);
			if (result == null)
			{
				result = createInstance(clazz, parameterTypes, paramValues);
				
				if (result != null)
				{
					objectCache.put(key, result);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 利用反射创建对象,构造函数无参数
	 * 
	 * @param clazz
	 * @return
	 */
	private static <T> T createInstance(Class<T> clazz)
	{
		try
		{
			T result = clazz.newInstance();
			return result;
		}
		catch (InstantiationException e)
		{
			Log.e(TAG, "创建对象实例失败!" + e.toString());
			return null;
		}
		catch (IllegalAccessException e)
		{
			Log.e(TAG, "创建对象实例失败!" + e.toString());
			return null;
		}

	}
	
	/**
	 * 利用反射创建对象,构造函数带参数
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <T> T createInstance(Class<T> clazz,Class<?>[] parameterTypes,Object[] paramValues)
	{
		
		try
		{
			Constructor<?> constructor = clazz.getConstructor(parameterTypes);//获取构造函数
			
			T result = (T) constructor.newInstance(paramValues);//构造函数参数
			return result;
		}
		catch (InstantiationException e)
		{
			Log.e(TAG, "创建对象实例失败!" + e.toString());
			return null;
		}
		catch (Exception e)
		{
			Log.e(TAG, "创建对象实例失败!" + e.toString());
			return null;
		}

	}
	
	/**
	 * 释放单例对象缓存
	 */
	public static void releaseCache()
	{
		
		//释放普通类对象
		if (objectCache != null)
		{
			objectCache.clear();
		}
		
	}
	
	@Override
	protected void finalize() throws Throwable {
		releaseCache();
		super.finalize();
	}
}
