package com.example.apiexcute2.util;


import android.util.Log;

import com.example.apiexcute2.model.eventModel.ViewInfo;

import java.util.ArrayList;
import java.util.List;
/**
 * @create data: 2022/2/28
 * @author: CYR
**/
public class MatchUtil {
    //assign weight of each part
    private static float p1 = 1/4f,p2 = p1 ,p3 = p1, p4 = p1;
    //the threshold of viewInfo
    private static float viewInfoThreshold = 0.6f;
    //the threshold of targetView(touch or setTe
    // xt view)
    private static float targetThreshold = viewInfoThreshold;
    //the threshold of structure
    public static float structureThreshold = 0.6f;

    /**
     * @create data: 2022/2/28
     * @author: CYR
     * @param viewInfo1
     * @param viewInfo2
    **/
    public static float obtainViewSimilarity(ViewInfo viewInfo1,ViewInfo viewInfo2){
        float partXMin = Math.min( viewInfo1.getX(), viewInfo2.getX() );
        float partXMax = Math.max( viewInfo1.getX(), viewInfo2.getX() );
        if(partXMax==0){
            partXMax += 1;
            partXMin += 1;
        }
        float partYMin = Math.min( viewInfo1.getY(), viewInfo2.getY() );
        float partYMax = Math.max( viewInfo1.getY(), viewInfo2.getY() );
        if(partYMax==0){
            partYMin += 1;
            partYMax += 1;
        }
        float partWMin = Math.min( viewInfo1.getWidth(), viewInfo2.getWidth());
        float partWMax = Math.max( viewInfo1.getWidth(), viewInfo2.getWidth());
        if(partWMax==0){
            partWMin += 1;
            partWMax += 1;
        }
        float partHMin = Math.min( viewInfo1.getHeight(), viewInfo2.getHeight());
        float partHMax = Math.max( viewInfo1.getHeight(), viewInfo2.getHeight());
        if(partHMax==0){
            partHMin += 1;
            partHMax += 1;
        }
        float res = p1 * partXMin/partXMax  +
                p2 *  partYMin/partYMax  +
                p3 * partWMin/partWMax  +
                p4 * partHMin/partHMax;
        return res;
    }
    /**
     * @create data: 2022/2/28
     * @author: CYR
    **/
    public static float obtainStructureSimilarity(List<ViewInfo> structure1,List<ViewInfo> structure2){
        float layer1 = getStructureLayer(structure1);
        float layer2 = getStructureLayer(structure2);
        float maxLayer = Math.max(layer1, layer2 );

        float viewNum1 = getViewNum(structure1);
        float viewNum2 = getViewNum(structure2);
        float maxNum = Math.max(viewNum1,viewNum2);
        int sameNum = reckonViewTreeSimilarity( structure1, structure2 );
        MyLog.CYR("Number of nodes in tree 1:"+viewNum1);
        MyLog.CYR("Number of nodes in tree 2:"+viewNum1);
        MyLog.CYR("Number of identical nodes:"+sameNum);

        float part1 = Math.min(layer1,layer2)/maxLayer*0.2f;
        float part2 = Math.min(viewNum1,viewNum2)/maxNum*0.2f;
        float part3 = sameNum/(viewNum1+viewNum2-sameNum)*0.6f;
        float res = part1 + part2 + part3;
        float ss = sameNum/Math.min(viewNum1,viewNum2);
        if(ss<0.46){
            ss = -1;
//            res = -1;
        }
        return res;
    }
    /**
     * @create data: 2022/2/28
     * @author: CYR
    **/
    public static int reckonViewTreeSimilarity(List<ViewInfo> viewInfos1,List<ViewInfo> viewInfos2){
        //the threshold of viewInfo similarity
        float w = viewInfoThreshold;
        int res = 0;
        if(viewInfos1==null){
            return res;
        }
        //reckon the number of viewInfos which similarity bigger than w
        for(ViewInfo viewInfo:viewInfos1){
//            MyLog.CYR("src: "+viewInfo.getViewName()+" "+viewInfo.getViewIndex());
            List<ViewInfo> matchedViewInfoList = searchMatchedViewInfo(viewInfo,viewInfos2);
            if(matchedViewInfoList==null){
                MyLog.CYR("match view is null");
                continue;
            }
            int maxNum = 0;
            for(ViewInfo matchViewInfo:matchedViewInfoList){
                float similarity = obtainViewSimilarity(viewInfo,matchViewInfo);
//                MyLog.CYR("check: "+similarity);
                int temp = 0;
                if(similarity>=w){
//                    res++;
                    temp = 1;
                }
//                if(viewInfo.getChilds()==null){
//                    MyLog.CYR("viewInfo child is null");
//                }else {
//                    MyLog.CYR("child1 size: "+viewInfo.getChilds().size());
//                    MyLog.CYR("child2 size: "+matchViewInfo.getChilds().size());
//                }
                temp += reckonViewTreeSimilarity(viewInfo.getChilds(), matchViewInfo.getChilds());
//                MyLog.CYR("temp: "+temp);
                maxNum = Math.max(maxNum,temp);
            }
            res += maxNum;
        }
        return res;
    }
    /**
     * @create data: 2022/2/28
     * @author: CYR
     * @param childViewInfo
     * @param childs
    **/
    private static List<ViewInfo> searchMatchedViewInfo(ViewInfo childViewInfo, List<ViewInfo> childs){
        if(childs==null){
            MyLog.CYR("view child is null");
            return null;
        }
        List<ViewInfo> matchedList = new ArrayList<>();
        for(ViewInfo viewInfo:childs){
//            Log.i("CYR","prepare: "+viewInfo.getViewName()+" "+viewInfo.getViewIndex());
            if( viewInfo.getViewName().equals(childViewInfo.getViewName()) &&
                viewInfo.getViewIndex()==childViewInfo.getViewIndex()){
                matchedList.add(viewInfo);
            }
//            if( viewInfo.getViewName().equals(childViewInfo.getViewName()) ){
//                matchedList.add(viewInfo);
//            }
        }
        return matchedList;
    }
    /**
     * @create data: 2022/2/28
     * @author: CYR
    **/
    private static int getViewNum(List<ViewInfo> structure){
        List<ViewInfo> queue = new ArrayList<>();
        queue.addAll(structure);
        int num = 0;
        while(!queue.isEmpty()){
            ViewInfo viewInfo = queue.remove(0);
            num++;
            List<ViewInfo> childViewInfos = viewInfo.getChilds();
            if(childViewInfos != null){
                queue.addAll(childViewInfos);
            }
        }
        return num;
    }
    /**
     * @create data: 2022/2/28
     * @author: CYR
     * @Description: obtain the height of the window structure
     * @param: List<ViewInfo> structure
    **/
    private static int getStructureLayer(List<ViewInfo> structure){
        int res = 0;
        for(ViewInfo viewInfo:structure){
            res = Math.max(res,getViewTreeLayer(viewInfo));
        }
        return res;
    }
    /**
     * @create data: 2022/2/28
     * @author: CYR
     * @Description: obtain the TreeLayer of the window structure
     * @param: ViewInfo viewInfo
     **/
    private static int getViewTreeLayer(ViewInfo viewInfo){
        if(viewInfo.getChilds()==null){
            return 1;
        }
        int res = 1;
        List<ViewInfo> childs = viewInfo.getChilds();
        for(ViewInfo child:childs){
            res = Math.max( res, getViewTreeLayer(child) + 1 );
        }
        return res;
    }
}
