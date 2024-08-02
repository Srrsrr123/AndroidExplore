package com.example.apiexcute2.receive;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

//import com.example.apiexcute2.Event.MethodTrackPool;
import com.example.apiexcute2.model.eventModel.ViewInfo;
//import com.example.apiexcute2.model.workModel.WorkItem;
import com.example.apiexcute2.monitor.OnDrawMonitor;
import com.example.apiexcute2.util.ActivityUtil;
import com.example.apiexcute2.util.MyLog;
import com.example.apiexcute2.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class LocalActivityReceiver extends BroadcastReceiver{
    private Activity selfActivity;
    public static final String START_EVENT = "START_EVENT";
    public static final String ON_RESUME = "ON_RESUME";
    public static final String RESUME_ACTIVITY = "RESUME_ACTIVITY";
    public static final String EXECUTE_EVENT = "EXECUTE_EVENT";
    public static final String CAPTURE_PAGE_CONTENT = "CAPTURE_PAGE_CONTENT";
    private String selfActivityName = "";
    private String showActivityName = "";
    private String selfPackageName;

    public LocalActivityReceiver(Activity activity){
        selfActivity = activity;
        selfActivityName = activity.getComponentName().getClassName();
        selfPackageName = activity.getPackageName();
    }

    public List<ViewInfo> savePage(){
        List<ViewInfo> viewInfos = new ArrayList<>();
        viewInfos = ViewUtil.obtainStructureOfWindow(selfActivity);
        return viewInfos;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        FrameLayout frameLayout;
        OnDrawMonitor onDrawMonitor = null;
        switch (action){
            case ON_RESUME:
                showActivityName = intent.getStringExtra(RESUME_ACTIVITY);
                ActivityUtil.setCurActivityName(showActivityName);
//                MyLog.CYR("show Activity finish: "+System.currentTimeMillis());
//                MyLog.CYR("slefActivity"+selfActivityName+" Current ActivityName resume "+showActivityName);
                break;
        }
    }

    private void imitateClick(View view){
        int clickPos[] = new int[2];
        view.getLocationInWindow(clickPos);
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        MyLog.CYR("rectX: "+rect.left+" rectY: "+rect.top);
        //clickPos[0]+=view.getWidth()/2;
        //clickPos[1]+=view.getHeight()/2;
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        int action = MotionEvent.ACTION_DOWN;
        int x = clickPos[0];
        int y = clickPos[1];
        MyLog.CYR("x: "+x+" y: "+y);
        int metaState = 0;
        MotionEvent motionEvent = MotionEvent.obtain(downTime, eventTime, action, x, y, metaState);
        //selfActivity.dispatchTouchEvent(motionEvent);
        view.getRootView().dispatchTouchEvent(motionEvent);
        //view.dispatchTouchEvent(motionEvent);
        action = MotionEvent.ACTION_UP;
        motionEvent = MotionEvent.obtain(downTime, eventTime, action, x, y, metaState);
        //view.dispatchTouchEvent(motionEvent);
        //selfActivity.dispatchTouchEvent(motionEvent);
        view.getRootView().dispatchTouchEvent(motionEvent);
    }
}
