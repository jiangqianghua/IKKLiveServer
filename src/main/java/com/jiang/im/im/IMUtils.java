package com.jiang.im.im;

public class IMUtils {

    public static final int USERID = 0 ;
    public static final int NAME = 1 ;
    public static final int AVATAR = 2;
    public static final int USERTYPE = 3;
    public static final int ILVLIVE_CMD_ENTER = 0 ;
    public static final int ILVLIVE_CMD_LEAVE = 1 ;
    public static final int CMD_CHAT_GIFT = 2 ;
    public static final int CMD_CHAT_MSG_DANMU = 3 ;
    public static final int CMD_CHAT_MSG_LIST = 4 ;
    public static final int CMD_CHAT_HEART = 5;
    public static final int ILVLIVE_CMD_USER_LIST = 6;

    public static final String JSON_FORMAT = "{\"content\":\"%s\",\"packet\":%s,\"msgType\":%d}";
    public static String decode(String base64Str){
        return new String(org.springframework.util.Base64Utils.decodeFromString(base64Str));
    }

    public static String parse(String str,int type){
        String arr[] = str.split("&");
        if(arr.length <= type)
            return null;
        return arr[type];
    }

    public static String createEnterRoomCmd(String extendParams){
        String json = String.format(JSON_FORMAT,"",extendParams,ILVLIVE_CMD_ENTER);
        return json ;
    }

    public static String createLeaveRoomCmd(String extendParams){
        String json = String.format(JSON_FORMAT,"",extendParams,ILVLIVE_CMD_LEAVE);
        return json ;
    }

    public static String createUserInRoomCmd(String extendParams){
        String json = String.format(JSON_FORMAT,"",extendParams,ILVLIVE_CMD_USER_LIST);
        return json ;
    }

    public static String getJsonByCmd(int type,String content,String extendParams){
        String json = String.format(JSON_FORMAT,content,extendParams,type);
        return json ;
    }

}
