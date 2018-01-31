package com.jiang.im.im;

import java.util.LinkedList;

public class WatchersList extends LinkedList <WatcherInfo>{


    public void addWatcher(WatcherInfo watcherInfo){
        this.add(watcherInfo);
    }

    public void deleteWatcher(String userId){
        for(WatcherInfo watcherInfo:this){
            if(watcherInfo != null){
                if(userId.equals(watcherInfo.getUserAccount())){
                    this.remove(watcherInfo);
                }
            }
        }
    }

    public WatcherInfo findWatcher(String userId){
        for(WatcherInfo watcherInfo:this){
            if(watcherInfo != null){
                if(userId.equals(watcherInfo.getUserAccount())){
                    return watcherInfo ;
                }
            }
        }
        return null ;
    }
}
