package com.example.apiexcute2.appData;

import com.example.apiexcute2.model.eventModel.ViewInfo;

import java.util.List;
/**
 * @create data: 2022/2/24
 * @author: CYR
**/
public class State {
    private String id;
    private List<ViewInfo> curPage;
    private List<ViewInfo> fatherPage;
    private List<ViewInfo> grandFatherPage;
    private String preState;
    private String nextState;

    public State deepCopy(){
        State s = new State();
        s.setNextState(this.getNextState());
        s.setPreState(this.getPreState());
        s.setCurPage(this.getCurPage());
        s.setId(this.getId());
        s.setFatherPage(this.getFatherPage());
        s.setGrandFatherPage(this.getGrandFatherPage());
        return s;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ViewInfo> getCurPage() {
        return curPage;
    }

    public void setCurPage(List<ViewInfo> curPage) {
        this.curPage = curPage;
    }

    public List<ViewInfo> getFatherPage() {
        return fatherPage;
    }

    public void setFatherPage(List<ViewInfo> fatherPage) {
        this.fatherPage = fatherPage;
    }

    public List<ViewInfo> getGrandFatherPage() {
        return grandFatherPage;
    }

    public void setGrandFatherPage(List<ViewInfo> grandFatherPage) {
        this.grandFatherPage = grandFatherPage;
    }

    public String getPreState() {
        return preState;
    }

    public void setPreState(String preState) {
        this.preState = preState;
    }

    public String getNextState() {
        return nextState;
    }

    public void setNextState(String nextState) {
        this.nextState = nextState;
    }

    @Override
    public String toString() {
        return "State{" +
                "id='" + id + '\'' +
                ", curPage=" + curPage +
                ", fatherPage=" + fatherPage +
                ", grandFatherPage=" + grandFatherPage +
                ", preState='" + preState + '\'' +
                ", nextState='" + nextState + '\'' +
                '}';
    }
}
