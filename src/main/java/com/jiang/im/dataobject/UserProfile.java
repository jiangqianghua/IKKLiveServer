package com.jiang.im.dataobject;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserProfile {

    @Id
    private String userAccount ;

    private String userPassword ;

    private String userNick ="";

    private String userGender = "0" ;

    private String userSign ="";

    private String userRenzheng = "";

    private String userLocation ="";

    private int userLevel = 0 ;

    private int userGetnum = 0;

    private int userSendnum = 0;

    private String userHeader ="";

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
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

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public int getUserGetnum() {
        return userGetnum;
    }

    public void setUserGetnum(int userGetnum) {
        this.userGetnum = userGetnum;
    }

    public int getUserSendnum() {
        return userSendnum;
    }

    public void setUserSendnum(int userSendnum) {
        this.userSendnum = userSendnum;
    }

    public String getUserHeader() {
        return userHeader;
    }

    public void setUserHeader(String userHeader) {
        this.userHeader = userHeader;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "userAccount='" + userAccount + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userNick='" + userNick + '\'' +
                ", userGender=" + userGender +
                ", userSigin='" + userSign + '\'' +
                ", userRenzheng='" + userRenzheng + '\'' +
                ", userLocation='" + userLocation + '\'' +
                ", userLevel='" + userLevel + '\'' +
                ", userGetnum=" + userGetnum +
                ", userSendnum=" + userSendnum +
                '}';
    }
}
