package com.example.apiexcute2.xposed.event;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Environment;
import android.util.Log;

import com.example.apiexcute2.appData.DataCollection;
import com.example.apiexcute2.receive.LocalActivityReceiver;
import com.example.apiexcute2.serve.MyServe;
import com.example.apiexcute2.serve.ServeReceiver;
import com.example.apiexcute2.util.MyLog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.example.apiexcute2.receive.LocalActivityReceiver.ON_RESUME;
import static com.example.apiexcute2.serve.ServeReceiver.API_RESPONSE;
import static com.example.apiexcute2.serve.ServeReceiver.OPEN_API;
/**
 * @create data: 2022/2/23
 * @author: CYR
**/
public class ActivityOnCreateHook extends XC_MethodHook {
    XC_LoadPackage.LoadPackageParam loadPackageParam;

    /**
     * @create data: 2022/2/23
     * @author: CYR
     * @return:
    **/
    public ActivityOnCreateHook(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        this.loadPackageParam = loadPackageParam;
    }
    /**
     * @create data: 2022/2/23
     * @author: CYR
    **/
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
    }
    /**
     * @create data: 2022/2/23
     * @author: CYR
     **/
    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        final Context context = (Context) param.thisObject;
        Activity activity = (Activity) param.thisObject;
        ComponentName componentName = activity.getComponentName();
        String activityName = componentName.getClassName();
        MyLog.CYR("create "+activityName);
        injectReceiver(context, activity);
    }
    /**
     * @create data: 2022/2/23
     * @author: CYR
    **/
    private void injectReceiver(Context context, Activity activity) {
        LocalActivityReceiver receiver = new LocalActivityReceiver(activity);
        IntentFilter filter = new IntentFilter();
        filter.addAction(LocalActivityReceiver.START_EVENT);
        filter.addAction(LocalActivityReceiver.EXECUTE_EVENT);
        filter.addAction(LocalActivityReceiver.ON_RESUME);
        filter.addAction(LocalActivityReceiver.CAPTURE_PAGE_CONTENT);
        Object o = XposedHelpers.getAdditionalInstanceField(activity,"iasReceiver");
        if(o!=null){
            MyLog.CYR("Already has an Activity listener.");
            return;
        }
        XposedHelpers.setAdditionalInstanceField(activity, "iasReceiver", receiver);
        activity.registerReceiver(receiver,filter);
    }
}
