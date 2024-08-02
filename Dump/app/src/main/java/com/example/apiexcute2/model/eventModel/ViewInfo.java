package com.example.apiexcute2.model.eventModel;

import com.example.apiexcute2.appData.State;

import java.util.List;

public class ViewInfo {
    private float x,y;
    private float width,height;
    private int viewIndex;
    private String viewPath,viewName;
    private List<ViewInfo> childs;

    public ViewInfo(float x, float y, float width, float height, String xpath, String viewName,int index) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.viewPath = xpath;
        this.viewName = viewName;
        this.viewIndex = index;
    }

    public ViewInfo() {
    }

    @Override
    public String toString() {
        return "ViewInfo{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", viewIndex=" + viewIndex +
                ", viewPath='" + viewPath + '\'' +
                ", viewName='" + viewName + '\'' +
                ", childs=" + childs +
                '}';
    }

    public void setViewIndex(int viewIndex) {
        this.viewIndex = viewIndex;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setViewPath(String xpath) {
        this.viewPath = xpath;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public void setChilds(List<ViewInfo> childs) {
        this.childs = childs;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public String getViewPath() {
        return viewPath;
    }

    public String getViewName() {
        return viewName;
    }

    public List<ViewInfo> getChilds() {
        return childs;
    }

    public int getViewIndex() {
        return viewIndex;
    }


//    public ViewInfo deepCopy(){
//        ViewInfo viewInfo = new ViewInfo();
//        viewInfo.setChilds(this.getChilds());
//        viewInfo.setHeight(this.getHeight());
//        viewInfo.setViewIndex(this.getViewIndex());
//        viewInfo.setViewName(this.getViewName());
//        viewInfo.setViewPath(this.getViewPath());
//        viewInfo.setWidth(this.getWidth());
//        viewInfo.setX(this.getX());
//        viewInfo.setY(this.getY());
//        return viewInfo;
//    }
}
