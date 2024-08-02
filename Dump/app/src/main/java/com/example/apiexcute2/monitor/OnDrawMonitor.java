package com.example.apiexcute2.monitor;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.apiexcute2.appData.DataCollection;
import com.example.apiexcute2.model.eventModel.ViewInfo;
import com.example.apiexcute2.util.MatchUtil;
import com.example.apiexcute2.util.MyFileUtil;
import com.example.apiexcute2.util.MyLog;
import com.example.apiexcute2.util.ViewUtil;
import java.util.List;
/**
 * @create data: 2022/2/24
 * @author: CYR
**/
public class OnDrawMonitor {
    private static volatile OnDrawMonitor onDrawMonitor;
    private boolean isFinish = true;
    private View view;
    private List<ViewInfo> preWindowStructure;
    /**
     * @create data: 2022/2/24
     * @author: CYR
    **/
    public static OnDrawMonitor getInstance(){
        if(onDrawMonitor==null){
            synchronized (OnDrawMonitor.class){
                if(onDrawMonitor==null){
                    onDrawMonitor = new OnDrawMonitor();
                }
            }
        }
        return onDrawMonitor;
    }
    /**
     * @create data: 2022/2/24
     * @author: CYR
     * @return:
    **/
    public void sendOnDraw(View view){
        this.view = view;
        if(isFinish){
            isFinish = false;
            startTask();
        }
    }
    /**
     * @create data: 2022/2/24
     * @author: CYR
     * @param:
     * @return:
    **/
    private void startTask(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean same = false;
                while(!same){
                    //View decorView = view.getRootView();
                    List<ViewInfo> curWindowStructure = ViewUtil.obtainStructureOfWindow(view.getContext());
                    if(preWindowStructure==null){
                        //MyLog.CYR("preWindow is null");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        float sim = MatchUtil.obtainStructureSimilarity(curWindowStructure,
                                preWindowStructure);
                        if(sim>0.98f){
                            same = true;
                            isFinish = true;
                            preWindowStructure = null;
//                            DataCollection.setCurPageStructureInfo(curWindowStructure);
//                            MyLog.CYR(curWindowStructure.toString());
                        }
                        else {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    preWindowStructure = curWindowStructure;
                }
            }
        }).start();
    }
}
