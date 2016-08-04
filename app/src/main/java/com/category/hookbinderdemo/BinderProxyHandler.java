package com.category.hookbinderdemo;/**
 * Created by fengyin on 7/25/16.
 */

import android.os.IBinder;
import android.os.IInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author fengyin(email:594601408@qq.com)
 * @date 2016-07-25 09:35
 * @package com.category.hookbinderdemo
 * @description BinderProxyHandler
 * @params
 */
public class BinderProxyHandler implements InvocationHandler {

    private static final String METHOD_QUERYLOCALINTERFACE = "queryLocalInterface";

    private IBinder mBase;
    private Class<?> mStub;
    private Class<?> mIInterface;


    public BinderProxyHandler(IBinder base){
        mBase = base;
        try {
            mStub = Class.forName("com.android.internal.app.IAppOpsService$Stub");
            mIInterface = Class.forName("com.android.internal.app.IAppOpsService");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(METHOD_QUERYLOCALINTERFACE.equals(method.getName())){
            return Proxy.newProxyInstance(proxy.getClass().getClassLoader(), new Class[]{IBinder.class, IInterface.class, this.mIInterface},
                    new BinderHandler(mBase, mStub));
        }
        return method.invoke(mBase, args);
    }
}
