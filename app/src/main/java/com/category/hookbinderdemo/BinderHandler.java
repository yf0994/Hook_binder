package com.category.hookbinderdemo;/**
 * Created by fengyin on 7/25/16.
 */

import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author fengyin(email:594601408@qq.com)
 * @date 2016-07-25 09:22
 * @package com.category.hookbinderdemo
 * @description BinderHandler
 * @params
 */
public class BinderHandler implements InvocationHandler{


    // Origin binder object.
    private Object mBase;

    public BinderHandler(IBinder base, Class<?> stubCLass){
        try {
            Method asInterfaceMethod = stubCLass.getDeclaredMethod("asInterface", IBinder.class);
            mBase = asInterfaceMethod.invoke(null, base);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.i("DEBUG", method.getName());
        return method.invoke(mBase, args);
    }
}
