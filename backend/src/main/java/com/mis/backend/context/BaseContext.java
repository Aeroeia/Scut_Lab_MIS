package com.mis.backend.context;

public class BaseContext {

    public static ThreadLocal<Long> localid = new ThreadLocal<>();
    public static ThreadLocal<Integer> localtype = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        if(localid.get() != null) {
            localid.remove();
        }
        localid.set(id);
    }

    public static Long getCurrentId() {
        return localid.get();
    }

    public static void removeCurrentId() {
        localid.remove();
    }

    public static void setCurrentType(Integer type) {
        if(localtype.get() != null) {
            localtype.remove();
        }
        localtype.set(type);
    }
    public static Integer getCurrentType(){
        return localtype.get();
    }
    public static void removeCurrentType(){
        localtype.remove();
    }
}
