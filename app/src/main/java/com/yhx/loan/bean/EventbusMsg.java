package com.yhx.loan.bean;

/**
 * Created by 25171 on 2018/6/9.
 */

public class EventbusMsg {
    private int type;
    private Object object;
    private Class aClass;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }
}
