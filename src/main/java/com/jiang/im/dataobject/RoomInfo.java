package com.jiang.im.dataobject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class RoomInfo {

    @Id
    @GeneratedValue  // 自增的类型
    private Integer roomId ;

    private String userId ;

    private String userAvater ="" ;

    private String liveCover ="";

    private String liveTitle ="" ;

    private String userName = "";

    private int watcherNum =0;

    public Integer getRoomId() {
        return roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAvater() {
        return userAvater;
    }

    public void setUserAvater(String userAvater) {
        this.userAvater = userAvater;
    }

    public String getLiveCover() {
        return liveCover;
    }

    public void setLiveCover(String liveCover) {
        this.liveCover = liveCover;
    }

    public String getLiveTitle() {
        return liveTitle;
    }

    public void setLiveTitle(String liveTitle) {
        this.liveTitle = liveTitle;
    }

    public int getWatcherNum() {
        return watcherNum;
    }

    public void setWatcherNum(int watcherNum) {
        this.watcherNum = watcherNum;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
