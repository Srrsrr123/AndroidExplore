package com.example.apiexcute2.serve;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.apiexcute2.receive.LocalActivityReceiver;
import com.example.apiexcute2.util.MyFileUtil;
import com.example.apiexcute2.util.MyLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static com.example.apiexcute2.receive.LocalActivityReceiver.ON_RESUME;
import static com.example.apiexcute2.receive.LocalActivityReceiver.RESUME_ACTIVITY;

public class ServeReceiver extends BroadcastReceiver {
    public static final String API_LINK = "APILINK";
    public static final String API_MODEL = "APIMODEL";
    public static final String API_OUTPUT = "APIOUTPUT";
    public static final String API_RESPONSE = "API_RESPONSE";
    public static final String PAGE_CONTENT = "PAGE_CONTENT";
    public static final String OPEN_API = "OPEN_API";
    public static final String OPEN_API_NAME = "OPEN_API_NAME";
    public static final String OPEN_ACTIVITY_NAME = "OPEN_ACTIVITY_NAME";
    public static final String OPEN_PACKAGE_NAME = "OPEN_PACKAGE_NAME";
    public static String test;
    private String apiName;
    private String launchActivityName;
    private boolean launchFlag;
    private Context context;
    private ReentrantLock lock;
    private Condition condition;
    private MyServe myServe;
    public ServeReceiver(Context context){
        this.context = context;
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){
            case ON_RESUME:
                String showActivityName = intent.getStringExtra(RESUME_ACTIVITY);
                //MyLog.CYR("showActivityName");
                if(launchFlag&&launchActivityName.equals(showActivityName)){
                    MyLog.CYR("launch API");
                    launchAPI();
                    launchFlag = false;
                }
                break;
            case API_RESPONSE:
                ArrayList<String> pageContents = intent.getStringArrayListExtra(PAGE_CONTENT);
                HashMap<String,String> pageHash = new HashMap<String,String>();
                for(String item:pageContents){
                    int lastIndex = item.lastIndexOf(":");
                    String path = item.substring(0,lastIndex);
                    String text = item.substring(lastIndex+1,item.length());
                    pageHash.put(path,text);
                }
                handleResponse(apiName,pageHash);
                break;
            case OPEN_API:
//                CurActivity curActivity = (CurActivity) intent.getSerializableExtra("curActivity");
//                Activity currentActivity = curActivity.getCurActivity();
//                ComponentName componentName = currentActivity.getComponentName();
//                String activityName = componentName.getClassName();
//                MyLog.CYR("123123123123123123123123" + activityName);
//                test = activityName;
                break;
        }
    }
    private void launchAPI(){
        Intent intent = new Intent();
        intent.setAction(LocalActivityReceiver.START_EVENT);
        context.sendBroadcast(intent);
    }
    public void executeAPI(String APIName,String packageName){
        apiName = APIName;
        launchFlag = true;
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
        try{
            lock.lock();
            condition.await();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    private void handleResponse(final String apiName,final HashMap<String,String> pageContent){
//        AsyncTask asyncTask = new AsyncTask<String,String,JSONArray>() {
//            @Override
//            protected JSONArray doInBackground(String... path) {
//                String filePath = path[0];
//                MyLog.CYR(filePath);
//                JSONObject jsonObject = MyFileUtil.readJSONObject(filePath);
//                MyLog.CYR(jsonObject.toJSONString());
//                JSONArray userRequired = jsonObject.getJSONArray(API_OUTPUT);
//                MyLog.CYR(userRequired.toJSONString());
//                return userRequired;
//            }
//
//            @Override
//            protected void onPostExecute(JSONArray jsonArray) {
//                if(jsonArray==null){
//                    MyLog.CYR("output required is null");
//                }
//                pushOutputToUser(jsonArray,pageContent);
//            }
//        };
        String apiFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+apiName+".txt";
        JSONObject jsonObject = MyFileUtil.readJSONObject(apiFilePath);
        JSONObject userRequired = jsonObject.getJSONObject("API_OUTPUT");
        pushOutputToUser(userRequired,pageContent);
    }
    public void pushOutputToUser(JSONObject userRequired,HashMap<String,String> pageContent){
        JSONObject responseJson = new JSONObject();
        if(userRequired!=null){
            MyLog.CYR("response to user");
            Set<String> paths = userRequired.keySet();
            for(String path:paths){
                String value = pageContent.get(path);
                if(value!=null){
                    responseJson.put(value,"");
                }
            }
            responseJson.put("success","OK");
        }else{
            responseJson.put("success","OK");
        }
        MyLog.CYR("set result");
        try{
            lock.lock();
            condition.signal();
            MyLog.CYR("signal");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void bindServe(MyServe myServe){
        this.myServe = myServe;
    }

    public void setLaunchActivityName(String launchActivityName) {
        this.launchActivityName = launchActivityName;
    }

    public void setLaunchFlag(boolean launchFlag) {
        this.launchFlag = launchFlag;
    }
}
