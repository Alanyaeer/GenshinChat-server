package com.homework.genshinchat.common;

public class BaseContext {
    private static ThreadLocal<Long>threadLocal = new ThreadLocal<>();

    public static void  setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrent(){
        return threadLocal.get();
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }

}
