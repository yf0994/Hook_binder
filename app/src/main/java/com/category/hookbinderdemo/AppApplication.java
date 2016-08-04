package com.category.hookbinderdemo; /**
 * Created by fengyin on 7/25/16.
 */

import android.app.AppOpsManager;
import android.app.Application;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author fengyin(email:594601408@qq.com)
 * @date 2016-07-25 09:18
 * @package PACKAGE_NAME
 * @description com.category.hookbinderdemo.AppApplication
 * @params
 */
public class AppApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);


    }

    @Override
    public void onCreate() {
        super.onCreate();
        hookBinder();
        AppOpsManager appOps = (AppOpsManager)getSystemService(APP_OPS_SERVICE);
        appOps.checkPackage(Binder.getCallingUid(), getPackageName());
    }



    private void hookBinder(){
        try {
            Class<?> serviceManagerClass = Class.forName("android.os.ServiceManager");
            Method getServiceMethod = serviceManagerClass.getDeclaredMethod("getService", String.class);
            IBinder originIBinder = (IBinder)getServiceMethod.invoke(null, Context.APP_OPS_SERVICE);
            IBinder proxyBinder = (IBinder) Proxy.newProxyInstance(serviceManagerClass.getClassLoader(),
                    new Class[]{IBinder.class},
                    new BinderProxyHandler(originIBinder));

            Field cacheField = serviceManagerClass.getDeclaredField("sCache");
            cacheField.setAccessible(true);
            Map<String, IBinder> cache = (Map)cacheField.get(null);
            cache.put(Context.APP_OPS_SERVICE, proxyBinder);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }
}
