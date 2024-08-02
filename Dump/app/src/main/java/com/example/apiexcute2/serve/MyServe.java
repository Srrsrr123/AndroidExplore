package com.example.apiexcute2.serve;

import android.app.Activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.example.apiexcute2.appData.DataCollection;
//import com.example.apiexcute2.model.workModel.WorkItem;
import com.example.apiexcute2.appData.State;
import com.example.apiexcute2.model.eventModel.ViewInfo;
import com.example.apiexcute2.util.MatchUtil;
import com.example.apiexcute2.util.MyLog;
import com.example.apiexcute2.util.ViewUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import fi.iki.elonen.NanoHTTPD;
/**
 * @create data: 2022/2/24
 * @author: CYR
**/
public class MyServe extends NanoHTTPD {
    private static WeakReference<Activity> activityWeakReference;
    private WeakReference<ServeReceiver> serveReceiverWeakReference;
    public State st = new State();
    public String stateId = new String();
    public State simSt = new State();
    public String simStId = new String();

    public State prest = new State();
    public String prestateId = new String();

    public MyServe(int port) {
        super(port);
    }

    public MyServe(String hostname, int port) {
        super(hostname, port);
    }
    /**
     * @create data: 2022/2/24
     * @author: CYR
     * @param:
     * @return:
    **/
    @Override
    public Response serve(IHTTPSession session) {
        Method method = session.getMethod();
        JSONObject result = new JSONObject();
        if(method==Method.POST) {
            String url = session.getUri();
            String temp = url.substring(1);
            if (temp.equals("curstate")){
                getState();
                JSONObject pageJson= (JSONObject) JSONObject.toJSON(st.getCurPage().get(0));
                result.put("stateId",stateId);
                result.put("state",pageJson);
                return newFixedLengthResponse(result.toJSONString());
            }
            else if (temp.equals("addstate")){
                if(st.getId() != null){
                    if(!(DataCollection.getHistoryState().containsKey(stateId))){
                        State s = new State();
                        s = st.deepCopy();
                        DataCollection.getHistoryState().put(stateId,s);
                        return newFixedLengthResponse("Added.");
                    }
                    return newFixedLengthResponse("Already exists.");
                }
                else{
                    return newFixedLengthResponse("The current state is null.");
                }
            }
            else if (temp.equals("getstate")){
                double similarity = getState();
                double reward = 0;
                if(similarity < -90){
                    reward =  100;
                }
                else if(similarity>0.90){
                    reward = -100;
                }
                else{
                    reward = 100*(1-similarity);
                }
                MyLog.CYR("Reward：   " + reward);
                JSONObject pageJson= (JSONObject) JSONObject.toJSON(st.getCurPage().get(0));
                //String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/pageContent.txt";
                //MyFileUtil.writeJSONObject(path, (JSONObject) JSONObject.toJSON(DataCollection.getCurPageStructureInfo().get(0)));
                result.put("stateId",stateId);
                result.put("state",pageJson);
                result.put("reward",reward);
                result.put("similarity",similarity);
                return newFixedLengthResponse(result.toJSONString());
            }

            if (temp.equals("addpage")){
                MyLog.CYR("len:     "+DataCollection.getCurPageStructureInfo().toString().length());
                DataCollection.getHistoryPageStructureInfo().put("1",DataCollection.getCurPageStructureInfo());
            }
            else if (temp.equals("getpage")){
                List<ViewInfo> s = ViewUtil.obtainStructureOfWindow(DataCollection.getCurActivity());
                MyLog.CYR("len:     "+s.toString().length());
                float sim = MatchUtil.obtainStructureSimilarity(s,DataCollection.getHistoryPageStructureInfo().get("1"));
                MyLog.CYR("sim"+sim);
            }
        }
        return newFixedLengthResponse("");
    }


    public void save(){
        String str = JSONObject.toJSONString(DataCollection.getHistoryState());
        MyLog.CYR("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        File file = new File("/sdcard/history.json");
        if(file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream,"UTF-8");
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);
            writer.write(str);
            fileOutputStream.flush();
            outputStreamWriter.flush();
            writer.flush();
            fileOutputStream.close();
            outputStreamWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @create data: 2022/2/24
     * @author: CYR
     * @param:
    **/
    private double getState(){
        List<ViewInfo> newPage = ViewUtil.obtainStructureOfWindow(DataCollection.getCurActivity());
        DataCollection.setCurPageStructureInfo(newPage);
        st.setCurPage(DataCollection.getCurPageStructureInfo());
        simStId = null;
        simSt = null;
        double s = statusComparison();
        if(s>0.9){
            stateId = simStId;
            st = simSt.deepCopy();
        }
        else{
            String orderSeq = Math.abs(UUID.randomUUID().toString().hashCode()) + "";
            while (orderSeq.length() < 16) {
                orderSeq = orderSeq + (int) (Math.random() * 10);
            }
            stateId = DataCollection.getCurActivityName()+"_"+orderSeq;
            DataCollection.getHistoryPageStructureInfo().put(stateId,DataCollection.getCurPageStructureInfo());
            st.setCurPage(DataCollection.getCurPageStructureInfo());
            st.setFatherPage(null);
            st.setGrandFatherPage(null);
            st.setId(stateId);
            st.setPreState(null);
            st.setNextState(null);
            if(st.getId() != null){
                if((!DataCollection.getHistoryState().containsKey(stateId)) && (s <= 0.9)){
                    State ss = new State();
                    ss = st.deepCopy();
                    DataCollection.getHistoryState().put(stateId,ss);
                    MyLog.CYR("Added.");
                }
                MyLog.CYR("Already exists.");
            }
            else{
                MyLog.CYR("The current state is null.");
            }
        }
        save();
        return s;
    }

    /**
     * @create data: 2022/2/24
     * @author: CYR
     * @param:
    **/
    //
    private double statusComparison(){
        float similarity = -100;
        if(DataCollection.getHistoryState().size()<=0){
            return similarity;
        }
        simSt = null;
        simStId = null;
        Object[] keys = DataCollection.getHistoryState().keySet().toArray();
        String acName = DataCollection.getCurActivityName();
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i].toString();
            if(key.startsWith(acName)){
                float sims = comparison(st,DataCollection.getHistoryState().get(key));
                MyLog.CYR(key +":    "+ st.getCurPage().toString().length()+"------"+
                        DataCollection.getHistoryState().get(key).getCurPage().toString().length());
                MyLog.CYR("Historical similarity:" + sims);
                if(similarity > sims){
                    similarity = similarity;
                }
                else{
                    similarity = sims ;
                    simSt = DataCollection.getHistoryState().get(key).deepCopy();
                    simStId = key;
                }
            }
        }
        MyLog.CYR("Final Historical Similarity:    "+similarity);
        return similarity;
    }
    /**
     * @create data: 2022/2/24
     * @author: CYR
    **/
    private float comparison(State s1,State s2){
        float sim = MatchUtil.obtainStructureSimilarity(s1.getCurPage(),s2.getCurPage());
        return sim;
    }



    public static void setActivityWeakReference(Activity activity) {
        activityWeakReference = new WeakReference<>(activity);
    }

    public void setServeReceiverWeakReference(ServeReceiver serveReceiver) {
        this.serveReceiverWeakReference = new WeakReference<>(serveReceiver);
    }

    public ViewInfo jsonToViewInfos(JSONObject json){
        ViewInfo viewInfo = null;
        float viewX = (Float)json.get("x");
        float viewY = (Float)json.get("y");
        float viewWidth = (Float)json.get("width");
        float viewHeight = (Float)json.get("height");
        String viewName = (String)json.get("viewName");
        int viewIndex = (Integer) json.get("viewIndex");
        viewInfo = new ViewInfo(viewX,viewY,viewWidth,viewHeight,
                "",viewName,viewIndex);
        if(json.get("childs")!=null){
            JSONArray jsonArray = (JSONArray) json.get("childs");
            List<ViewInfo> childInfos = new ArrayList<>();
            for(int i=0;i<jsonArray.size();i++){
                ViewInfo childViewInfo = jsonToViewInfos((JSONObject) jsonArray.get(i));
                childInfos.add(childViewInfo);
            }
            viewInfo.setChilds(childInfos);
        }
        return viewInfo;
    }
}
