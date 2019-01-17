package com.example.hl.myweibo;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class HomePageList {
    private static HomePageList sMainPageList;
    private List<HomePageItem> mMainPageList;
    public static HomePageList get(Context context){
        if(sMainPageList==null){
            sMainPageList=new HomePageList(context);
        }
        return sMainPageList;
    }

    private HomePageList(Context context){
        mMainPageList = new ArrayList<>();
    }

    public List<HomePageItem> getMainPageList(){
        return mMainPageList;
    }

    public HomePageItem getMainPageItem(UUID id){
        for(HomePageItem item:mMainPageList){
            if(item.getmId().equals(id)){
                return item;
            }
        }
        return null;
    }

    public static void clear(){
        sMainPageList=null;
    }
}
