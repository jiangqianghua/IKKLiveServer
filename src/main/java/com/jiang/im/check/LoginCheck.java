package com.jiang.im.check;

import java.util.Map;

public class LoginCheck {

    public static final long LOGIN_MAXTIME = 1*24*60*60 ; // 1 天 秒为单位

    /**
     * 检测是否超时
     * @param map
     * @return true超时，false未超时
     */
    public static boolean checkLoginTimeOut(long loginTime){
        long currentTime = System.currentTimeMillis()/1000;
        if(currentTime - loginTime >= LOGIN_MAXTIME)
            return true;
        return false;
    }
}
