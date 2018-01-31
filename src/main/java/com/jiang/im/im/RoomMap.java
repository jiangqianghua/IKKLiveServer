package com.jiang.im.im;

import java.util.HashMap;

public class RoomMap extends HashMap<String,WatchersList> {

    private static RoomMap instance ;

    private RoomMap(){

    }

    public synchronized  static RoomMap getInstance(){
        if(instance == null){
            synchronized (RoomMap.class){
                if(instance == null){
                    instance = new RoomMap();
                }
            }
        }
        return instance ;
    }
    /**
     * add room
     * @param roomId
     */
    public void addRoom(String roomId){
        WatchersList watchersList = new WatchersList();
        this.put(roomId,watchersList);
    }

    /**
     *  delete room
     * @param roomId
     */
    public void deleteRoom(String roomId){
        if(this.containsKey(roomId)){
            this.remove(roomId);
        }
    }


    /**
     * join room
     * @param watcherInfo
     * @param roomId
     */
    public void joinRoom(String roomId,WatcherInfo watcherInfo){
        if(this.containsKey(roomId)){
            WatchersList watchersList =  this.get(roomId);
            watchersList.addWatcher(watcherInfo);
        }
    }

    /**
     * leave room
     * @param roomId
     * @param userId
     */
    public void leaveRoom(String roomId,String userId){
        if(this.containsKey(roomId)){
            WatchersList watchersList =  this.get(roomId);
            watchersList.deleteWatcher(userId);
        }
    }

    public WatcherInfo getWatcherInfo(String roomId , String userId){
        if(this.containsKey(roomId)){
            WatchersList watchersList =  this.get(roomId);
            return watchersList.findWatcher(userId);
        }
        return null ;
    }

}
