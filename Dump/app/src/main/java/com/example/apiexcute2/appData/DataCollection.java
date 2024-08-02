package com.example.apiexcute2.appData;

import android.app.Activity;
import com.example.apiexcute2.model.eventModel.ViewInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @create data: 2022/2/24
 * @author: CYR
**/
public class DataCollection {
    private static List<String> historyActivityName = new ArrayList<>();

    private static List<ViewInfo> curPageStructureInfo = new ArrayList<>();
    private static Map<String,List<ViewInfo>> historyPageStructureInfo = new HashMap<>();
    private static State curState = new State();
    private static State preState = new State();
    private static Map<String,State> historyState = new HashMap<>();
    private static String curActivityName;
    private static Activity curActivity;

    public static List<String> getHistoryActivityName() {
        return historyActivityName;
    }

    public static void setHistoryActivityName(List<String> historyActivityName) {
        DataCollection.historyActivityName = historyActivityName;
    }

    public static String getCurActivityName() {
        return curActivityName;
    }

    public static void setCurActivityName(String curActivityName) {
        DataCollection.curActivityName = curActivityName;
    }

    public static Activity getCurActivity() {
        return curActivity;
    }

    public static void setCurActivity(Activity curActivity) {
        DataCollection.curActivity = curActivity;
    }

    public static State getPreState() {
        return preState;
    }

    public static void setPreState(State preState) {
        DataCollection.preState = preState;
    }

    public static State getCurState() {
        return curState;
    }

    public static void setCurState(State curState) {
        DataCollection.curState = curState;
    }

    public static Map<String, State> getHistoryState() {
        return historyState;
    }

    public static void setHistoryState(Map<String, State> historyState) {
        DataCollection.historyState = historyState;
    }

    public static List<ViewInfo> getCurPageStructureInfo() {
        return curPageStructureInfo;
    }

    public static void setCurPageStructureInfo(List<ViewInfo> curPageStructureInfo) {
        DataCollection.curPageStructureInfo = curPageStructureInfo;
    }

    public static Map<String, List<ViewInfo>> getHistoryPageStructureInfo() {
        return historyPageStructureInfo;
    }

    public static void setHistoryPageStructureInfo(Map<String, List<ViewInfo>> historyPageStructureInfo) {
        DataCollection.historyPageStructureInfo = historyPageStructureInfo;
    }
}
