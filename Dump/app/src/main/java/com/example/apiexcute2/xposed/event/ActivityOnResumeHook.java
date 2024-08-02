package com.example.apiexcute2.xposed.event;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.apiexcute2.appData.DataCollection;
import com.example.apiexcute2.appData.State;
import com.example.apiexcute2.receive.LocalActivityReceiver;
import com.example.apiexcute2.serve.MyServe;
import com.example.apiexcute2.serve.ServeReceiver;
import com.example.apiexcute2.util.MyLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;

import static com.example.apiexcute2.receive.LocalActivityReceiver.ON_RESUME;
import static com.example.apiexcute2.serve.ServeReceiver.API_RESPONSE;
import static com.example.apiexcute2.serve.ServeReceiver.OPEN_API;
/**
 * @create data: 2022/2/23
 * @author: CYR
 * @Description:  Hook Android Activity Resume function
 **/
public class ActivityOnResumeHook extends XC_MethodHook {

    public  static String activityName = "123";
    public  static Activity activity;
    /**
     * @create data: 2022/2/24
     * @author: CYR
     * @param:
     * @return:
    **/
    public ActivityOnResumeHook() {
        super();
    }
    /**
     * @create data: 2022/2/24
     * @author: CYR
     * @param:
     * @return:
    **/
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        Activity activity = (Activity) param.thisObject;
        ComponentName componentName = activity.getComponentName();
    }

    public void read(){
        File file = new File("/sdcard/history.json");
        if(!file.exists()){
            System.out.println("json file not exist");
        }
        else{
            JSONObject res = null;
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream,"UTF-8");
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder strBuilder = new StringBuilder();
                String line = null;
                while((line=reader.readLine())!=null){
                    strBuilder.append(line);
                }
                Map<String, State> historyState = JSON.parseObject(strBuilder.toString(),new TypeReference<HashMap<String, State>>() {
                });
                DataCollection.setHistoryState(historyState);
                fileInputStream.close();
                inputStreamReader.close();
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @create data: 2022/2/24
     * @author: CYR
     * @param:
     * @return:
    **/
    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        activity = (Activity) param.thisObject;
        ComponentName componentName = activity.getComponentName();
        activityName = componentName.getClassName();
        read();
        DataCollection.setCurActivityName(activityName);
        DataCollection.setCurActivity(activity);
        MyLog.CYR("OnResume "+componentName.getClassName());
//        Intent intent = new Intent();
//        intent.setAction(LocalActivityReceiver.ON_RESUME);
//        intent.putExtra(LocalActivityReceiver.RESUME_ACTIVITY,activityName);
//        activity.sendBroadcast(intent);
//        if(!hasSend){
//            hasSend = true;
//        }
    }
}
