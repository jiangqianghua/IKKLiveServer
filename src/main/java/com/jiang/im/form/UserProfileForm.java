package com.jiang.im.form;

import org.hibernate.validator.constraints.NotEmpty;

public class UserProfileForm {

    @NotEmpty(message="账号不能为空")
    private String userAccount ;

    private String userNick ;

    private String userGender  ;

    private String userSign ;

    private String userRenzheng ;

    private String userLocation ;

    private String userHeader ;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserSign() {
        return userSign;
    }

    public void setUserSign(String userSign) {
        this.userSign = userSign;
    }

    public String getUserRenzheng() {
        return userRenzheng;
    }

    public void setUserRenzheng(String userRenzheng) {
        this.userRenzheng = userRenzheng;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getUserHeader() {
        return userHeader;
    }

    public void setUserHeader(String userHeader) {
        this.userHeader = userHeader;
    }
}
