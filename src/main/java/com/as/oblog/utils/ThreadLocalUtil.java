package com.as.oblog.utils;

/**
 * @author 12aaa-zone
 */

/**
 * 用户线程的线程全局变量
 * 服务器为每个用户开辟一条线程
 * 可以存储一个全局变量以供程序在该线程活跃期调用
 */
@SuppressWarnings("all")   // 告诉编译器忽略该类的所有错误信息
public class ThreadLocalUtil {
    //全局静态ThreadLocal对象,
    private static final ThreadLocal THREAD_LOCAL = new ThreadLocal();

    //根据键获取值
    public static <T> T get(){
        return (T) THREAD_LOCAL.get();
    }

    //存储键值对
    public static void set(Object value){
        THREAD_LOCAL.set(value);
    }

    //清除ThreadLocal 防止内存泄漏
    public static void remove(){
        THREAD_LOCAL.remove();
    }
}
