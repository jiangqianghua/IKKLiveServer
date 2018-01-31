package com.jiang.im.im;

public class IMUtils {

    public static final int USERID = 0 ;
    public static final int NAME = 1 ;
    public static final int AVATAR = 2;
    public static final int USERTYPE = 3;
    public static String decode(String base64Str){
        return new String(org.springframework.util.Base64Utils.decodeFromString(base64Str));
    }

    public static String parse(String str,int type){
        String arr[] = str.split("&");
        if(arr.length <= type)
            return null;
        return arr[type];
    }
}
