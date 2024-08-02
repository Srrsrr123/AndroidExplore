package com.example.apiexcute2.xposed.event;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.View;
import com.example.apiexcute2.model.eventModel.ViewInfo;
import com.example.apiexcute2.monitor.OnDrawMonitor;
import com.example.apiexcute2.util.MyLog;

import java.util.List;
import de.robv.android.xposed.XC_MethodHook;
/**
 * @create data: 2022/2/24
 * @author: CYR
**/
public class HookOnDraw extends XC_MethodHook {

    public HookOnDraw(){}
    /**
     * @create data: 2022/2/24
     * @author: CYR
     * @param:
     * @return:
    **/
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
    }

    /**
     * @create data: 2022/2/24
     * @author: CYR
     * @param:
     * @return:
    **/
    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        View view = (View) param.thisObject;
        Context context = view.getContext();
        ActivityOnResumeHook.activity = (Activity) context;
        OnDrawMonitor onDrawMonitor = OnDrawMonitor.getInstance();
        onDrawMonitor.sendOnDraw(view);
    }
}
