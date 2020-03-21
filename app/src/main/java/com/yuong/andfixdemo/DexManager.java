package com.yuong.andfixdemo;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Enumeration;

import dalvik.system.DexFile;

public class DexManager {
    private Context context;

    public DexManager(Context context) {
        this.context = context;
    }

    /**
     * 加载dex文件
     *
     * @param file
     */
    public void loadDex(File file) {
        try {
            //加载dex文件
            DexFile dexFile = DexFile.loadDex(file.getAbsolutePath(), new File(context.getCacheDir(), "opt").getAbsolutePath(), Context.MODE_PRIVATE);
            //获取dex文件里面类名的集合
            Enumeration<String> entries = dexFile.entries();
            //遍历集合
            while (entries.hasMoreElements()) {
                //获取全类名 例如 com.yuong.andfixdemo.MainActivity
                String className = entries.nextElement();
                //加载外部dex文件的类  不能通过 Class.forName()这种形式，只能用下面的方法
                Class<?> outClass = dexFile.loadClass(className, context.getClassLoader());
                if (outClass != null) { //如果不为空  就开始修复
                    fixClazz(outClass);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 替换掉存在异常的类中的方法  不是替换掉那个异常的类
     * @param outClass
     */
    private void fixClazz(Class<?> outClass) {
        //获取类中所有的方法
        Method[] methods = outClass.getMethods();
        for (Method rightMethod : methods) {
            //获取方法上面的注解
            Replace replace = rightMethod.getAnnotation(Replace.class);
            if (replace == null) {  //如果注解为空，则不是我们要找的方法，继续执行下一次循环
                continue;
            }
            //拿到了修复好的 rightMethod 也要拿到本地出现异常的 wrongMethod
            String wrongClassName = replace.clazz();
            String wrongMethodName = replace.method();
            try {
                //获取出现异常的类
                Class<?> wrongClass = Class.forName(wrongClassName);
                //获取到出现异常的方法
                Method wrongMethod = wrongClass.getDeclaredMethod(wrongMethodName, rightMethod.getParameterTypes());
                //替换方法
                replaceMethod(wrongMethod, rightMethod);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 在底层替换掉错误的方法
     * @param wrongMethod
     * @param rightMethod
     */
    private native void replaceMethod(Method wrongMethod, Method rightMethod);
}
