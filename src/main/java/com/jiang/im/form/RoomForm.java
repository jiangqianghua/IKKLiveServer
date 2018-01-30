package com.jiang.im.form;

import org.hibernate.validator.constraints.NotEmpty;

public class RoomForm {

    @NotEmpty(message="账号不能为空")
    private String userId ;

    private String liveCover ;

    @NotEmpty(message="房间主题不能未空")
    private String liveTitle ;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}
