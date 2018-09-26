/**
 * Version: 1.0
 * 版权: XXXXX 
 * zKF69930
 * 文件名: com/huawei/ccloud/framework/utils/ReflectUtil.java
 */
package myframelib.zkzhou.com.common.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 反射辅助操作
 * 
 * @author zKF69930
 */
public class ReflectUtil
{
    private static Object operate(Object obj, String fieldName,
        Object fieldVal, String type)
    {
        Object ret = null;
        try
        {
            // 获得对象类型
            Class<? extends Object> classType = obj.getClass();
            // 获得对象的所有属性
            Field[] fields = classType.getDeclaredFields();
            for (int i = 0; i < fields.length; i++)
            {
                Field field = fields[i];
                if (field.getName().equals(fieldName))
                {
                    
                    String firstLetter =
                        fieldName.substring(0, 1).toUpperCase(); // 获得和属性对应的getXXX()方法的名字
                    if ("set".equals(type))
                    {
                        String setMethodName =
                            "set" + firstLetter + fieldName.substring(1); // 获得和属性对应的getXXX()方法
                        Method setMethod =
                            classType.getMethod(setMethodName,
                                new Class[] {field.getType()}); // 调用原对象的getXXX()方法
                        ret = setMethod.invoke(obj, new Object[] {fieldVal});
                    }
                    if ("get".equals(type))
                    {
                        String getMethodName =
                            "get" + firstLetter + fieldName.substring(1); // 获得和属性对应的setXXX()方法的名字
                        Method getMethod =
                            classType.getMethod(getMethodName, new Class[] {});
                        ret = getMethod.invoke(obj, new Object[] {});
                    }
                    return ret;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ret;
    }
    
    /**
     * 反射获取字段值
     * @param obj  对象
     * @param fieldName 字段名
     * @return Object
     */
    public static Object getVal(Object obj, String fieldName)
    {
        return operate(obj, fieldName, null, "get");
    }
    
    /**
     *  反射设置字段值
     * @param obj 对象 
     * @param fieldName 字段名
     * @param fieldVal 值
     */
    public static void setVal(Object obj, String fieldName, Object fieldVal)
    {
        operate(obj, fieldName, fieldVal, "set");
    }
    
    private static Method getDeclaredMethod(Object object, String methodName,
        Class<?>[] parameterTypes)
    {
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass =
            superClass.getSuperclass())
        {
            try
            {
                return superClass.getDeclaredMethod(methodName, parameterTypes);
            }
            catch (NoSuchMethodException e)
            {
                e.printStackTrace();
            }
        }
        
        return null;
    }
    
    private static void makeAccessible(Field field)
    {
        if (!Modifier.isPublic(field.getModifiers()))
        {
            field.setAccessible(true);
        }
    }
    
    private static Field getDeclaredField(Object object, String filedName)
    {
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass =
            superClass.getSuperclass())
        {
            try
            {
                return superClass.getDeclaredField(filedName);
            }
            catch (NoSuchFieldException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    /**
     * 反射执行方法
     * @param object 对象
     * @param methodName 方法名
     * @param parameterTypes 参数
     * @param parameters 参数值
     * @return Object
     * @throws InvocationTargetException
     */
    public static Object invokeMethod(Object object, String methodName,
        Class<?>[] parameterTypes, Object[] parameters)
    {
        Method method = getDeclaredMethod(object, methodName, parameterTypes);
        
        if (method == null)
        {
            throw new IllegalArgumentException("Could not find method ["
                + methodName + "] on target [" + object + "]");
        }
        
        method.setAccessible(true);
        
        try
        {
            return method.invoke(object, parameters);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     *  反射设置字段值
     * @param object 对象 
     * @param fieldName 字段名
     * @param value 值
     */
    public static void setFieldValue(Object object, String fieldName,
        Object value)
    {
        Field field = getDeclaredField(object, fieldName);
        
        if (field == null)
        {
            throw new IllegalArgumentException("Could not find field ["
                + fieldName + "] on target [" + object + "]");
        }
        
        makeAccessible(field);
        
        try
        {
            field.set(object, value);
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 反射获取字段值
     * @param object  对象
     * @param fieldName 字段名
     * @return Object
     */
    public static Object getFieldValue(Object object, String fieldName)
    {
        Field field = getDeclaredField(object, fieldName);
        if (field == null)
        {
            throw new IllegalArgumentException("Could not find field ["
                + fieldName + "] on target [" + object + "]");
        }
        
        makeAccessible(field);
        
        Object result = null;
        try
        {
            result = field.get(object);
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        
        return result;
    }
    
}
