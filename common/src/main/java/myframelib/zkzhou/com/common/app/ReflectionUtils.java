
package myframelib.zkzhou.com.common.app;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtils {

    /**
     * 循环向上转型, 获取对象的 DeclaredMethod
     * 
     * @param object : 子类对象
     * @param methodName : 父类中的方法名
     * @param parameterTypes : 父类中的方法参数类型
     * @return 父类中的方法对象
     */
    public static Method getDeclaredMethod(Object object, String methodName,
                                           Class<?>... parameterTypes) {
        Method method = null;
        Class<?> clazz = object.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                return method;
            } catch (Exception e) {
                // 这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                // 如果这里的异常打印或者往外抛，则就不会执行clazz =
                // clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }
        return null;
    }

    /**
     * 直接调用对象方法, 而忽略修饰符(private, protected, default)
     * 
     * @param object : 子类对象
     * @param methodName : 父类中的方法名
     * @param parameterTypes : 父类中的方法参数类型
     * @param parameters : 父类中的方法参数
     * @return 父类中方法的执行结果
     */
    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes,
                                      Object[] parameters) {
        // 根据 对象、方法名和对应的方法参数 通过反射 调用上面的方法获取 Method 对象
        Method method = getDeclaredMethod(object, methodName, parameterTypes);
        try {
            // 抑制Java对方法进行检查,主要是针对私有方法而言
            method.setAccessible(true);
            if (null != method) {
                // 调用object 的 method 所代表的方法，其方法的参数是 parameters
                return method.invoke(object, parameters);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredField
     * 
     * @param object : 子类对象
     * @param fieldName : 父类中的属性名
     * @return 父类中的属性对象
     */
    public static Field getDeclaredField(Object object, String fieldName) {
        Class<?> clazz = object.getClass();
        return getDeclaredField(clazz, fieldName);
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredField
     * 
     * @param clazz : 子类
     * @param fieldName : 父类中的属性名
     * @return 父类中的属性对象
     */
    public static Field getDeclaredField(Class<?> clazz, String fieldName) {
        Field field = null;
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (Exception e) {
                // 这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                // 如果这里的异常打印或者往外抛，则就不会执行clazz =
                // clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }
        return null;
    }

    public static List<Field> getDeclaredFields(Class<?> clazz) {
        List<Field> list = new ArrayList<Field>();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] tempFields = null;
            try {
                tempFields = clazz.getDeclaredFields();
            } catch (Exception e) {
                // 这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                // 如果这里的异常打印或者往外抛，则就不会执行clazz =
                // clazz.getSuperclass(),最后就不会进入到父类中了
            }
            if (tempFields != null) {
                for (Field field : tempFields) {
                    list.add(field);
                }
            }
        }
        return list;
    }

    /**
     * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
     * 
     * @param object : 子类对象
     * @param fieldName : 父类中的属性名
     * @param value : 将要设置的值
     */
    public static void setFieldValue(Object object, String fieldName, Object value) {
        // 根据 对象和属性名通过反射 调用上面的方法获取 Field对象
        Field field = getDeclaredField(object, fieldName);
        try {
            // 抑制Java对其的检查
            field.setAccessible(true);
            // 将 object 中 field 所代表的值 设置为 value
            field.set(object, value);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
     * 
     * @param object : 子类对象
     * @param fieldName : 父类中的属性名
     * @return : 父类中的属性值
     */
    public static Object getFieldValue(Object object, String fieldName) {
        // 根据 对象和属性名通过反射 调用上面的方法获取 Field对象
        Field field = getDeclaredField(object, fieldName);
        try {
            // 抑制Java对其的检查
            field.setAccessible(true);
            // 获取 object 中 field 所代表的属性值
            return field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param className 类路劲的名字
     * @return 返回根据className指明的类信息
     */
    public static Class getclass(String className) {
        Class c = null;
        try {
            c = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            // Logger.getLogger(ClassMain.class.getName()).log(Level.SEVERE,
            // null, ex);
        }
        return c;
    }

    /**
     * @param name 类路劲
     * @param classParas Class类信息参数列表 如果是基本数据类型是可以使用其Tpye类型，如果用class字段是无效的
     *            如果是非数据类型可以使用的class字段来创建其Class类信息对象，这些都要遵守。
     * @param paras 实际参数列表数据
     * @return 返回Object引用的对象，实际实际创建出来的对象，如果要使用可以强制转换为自己想要的对象 带参数的反射创建对象
     */
    public static Object getInstance(String name, Class classParas[], Object paras[]) {
        Object o = null;
        try {
            Class c = getclass(name);
            Constructor con = c.getConstructor(classParas);// 获取使用当前构造方法来创建对象的Constructor对象，用它来获取构造函数的一些
            try {
                // 信息
                o = con.newInstance(paras);// 传入当前构造函数要的参数列表
            } catch (InstantiationException ex) {
                // Logger.getLogger(ClassMain.class.getName()).log(Level.SEVERE,
                // null, ex);
            } catch (IllegalAccessException ex) {
                // Logger.getLogger(ClassMain.class.getName()).log(Level.SEVERE,
                // null, ex);
            } catch (IllegalArgumentException ex) {
                // Logger.getLogger(ClassMain.class.getName()).log(Level.SEVERE,
                // null, ex);
            } catch (InvocationTargetException ex) {
                // Logger.getLogger(ClassMain.class.getName()).log(Level.SEVERE,
                // null, ex);
            }
        } catch (NoSuchMethodException ex) {
            // Logger.getLogger(ClassMain.class.getName()).log(Level.SEVERE,
            // null, ex);
        } catch (SecurityException ex) {
            // Logger.getLogger(ClassMain.class.getName()).log(Level.SEVERE,
            // null, ex);
        }

        return o;// 返回这个用Object引用的对象
    }

    /**
     * @param name 类路劲
     * @return 不带参数的反射创建对象
     */
    public static Object getInstance(String name) {
        Class c = getclass(name);
        Object o = null;
        try {
            o = c.newInstance();
        } catch (InstantiationException ex) {
            // Logger.getLogger(ClassMain.class.getName()).log(Level.SEVERE,
            // null, ex);
        } catch (IllegalAccessException ex) {
            // Logger.getLogger(ClassMain.class.getName()).log(Level.SEVERE,
            // null, ex);
        }
        return o;
    }

    public static Object tryInvoke(Object target, String methodName, Object... args) {
        Class<?>[] argTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }

        return tryInvoke(target, methodName, argTypes, args);
    }

    public static Object tryInvoke(Object target, String methodName, Class<?>[] argTypes,
                                   Object... args) {
        try {
            return target.getClass().getMethod(methodName, argTypes).invoke(target, args);
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }

        return null;
    }

    public static <E> E callWithDefault(Object target, String methodName, E defaultValue) {
        try {
            return (E) target.getClass().getMethod(methodName, (Class[]) null).invoke(target);
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }

        return defaultValue;
    }

    public static void setBoolean(Object target, String fieldName, boolean value) {
        try {
            target.getClass().getField(fieldName).setBoolean(target, value);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
    }

    public static Object getFieldValue(Class<?> fieldClass, String fieldName, Object instance) {
        try {
            final Field field = fieldClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(instance);
        } catch (Exception e) {
        }
        return null;
    }

    public static void setFieldValue(Class<?> fieldClass, String fieldName, Object instance,
                                     Object value) {
        try {
            final Field field = fieldClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception e) {
        }
    }

    public static Object invokeMethod(Class<?> methodClass, String methodName,
                                      Class<?>[] parameters, Object instance, Object... arguments) {
        try {
            final Method method = methodClass.getDeclaredMethod(methodName, parameters);
            method.setAccessible(true);
            return method.invoke(instance, arguments);
        } catch (Exception e) {
        }
        return null;
    }

}
