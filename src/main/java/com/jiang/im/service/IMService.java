package com.jiang.im.service;

import com.jiang.im.dataobject.RoomInfo;
import com.jiang.im.dataobject.UserProfile;
import com.jiang.im.repository.RoomInfoRepository;
import com.jiang.im.repository.UserProfileRepository;
import com.jiang.im.im.RoomMap;
import com.jiang.im.im.WatcherInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

//@Component
//@ServerEndpoint(value="/imService",encoders = {MsgEncoder.class},decoders = {MsgDecoder.class})
//@ServerEndpoint(value="/imService/{roomid}/{userid}/{name}")
public class IMService {

    public static final int USER_TYPE_HOST = 1 ;

    public static final int USER_TYPE_GUEST = 2 ;
    private static Logger logger = LoggerFactory.getLogger(IMService.class);

    private Session session ;

    private static CopyOnWriteArraySet<IMService> imServiceSet = new CopyOnWriteArraySet<>();

    //private static Map<String,IMService> userSessionMap = new HashMap<>();

    private static Map<String,Map<String,IMService>> roomIMServerMap = new HashMap<>();
    @Autowired
    UserProfileRepository userProfileRepository ;

    @Autowired
    RoomInfoRepository roomInfoRepository ;

    private String roomId ;
    private String userId;
    private int userType = USER_TYPE_HOST;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "roomid") String roomId, @PathParam(value="userid")String userId){
        this.session = session;
        imServiceSet.add(this);
        logger.info("[IMService] new connect, all counts:{}",imServiceSet.size());
        logger.info("roomid:{},userid:{}",roomId,userId);

        this.roomId = roomId ;
        this.userId = userId ;
        WatcherInfo watcherInfo = new WatcherInfo();
        UserProfile userProfile = userProfileRepository.findOne(userId);
        BeanUtils.copyProperties(userProfile,watcherInfo);
        RoomMap.getInstance().joinRoom(roomId,watcherInfo);

        RoomInfo roomInfo =roomInfoRepository.findByRoomIdAndUserId(roomId,userId);
        if(roomInfo != null){
            userType = USER_TYPE_HOST ;
        }
        if(roomIMServerMap.containsKey(roomId)){
            Map<String,IMService> userSession = roomIMServerMap.get(roomId);
            userSession.put(userId,this);
        }
        else{
            Map<String,IMService> userSession = new HashMap<>();
            userSession.put(userId,this);
            roomIMServerMap.put(roomId,userSession);
        }

        notifyRoomMsg(watcherInfo.getUserNick()+"进入教室");

    }

    @OnClose
    public void onClose(){
        imServiceSet.remove(this);
        logger.info("[IMService] disconnect, all counts:{}",imServiceSet.size());
        RoomMap.getInstance().leaveRoom(roomId,userId);
        if(roomIMServerMap.containsKey(roomId)){
            Map<String,IMService> userSession = roomIMServerMap.get(roomId);
            userSession.remove(userId);
        }
        WatcherInfo watcherInfo = RoomMap.getInstance().getWatcherInfo(roomId,userId);
        notifyRoomMsg(watcherInfo.getUserNick()+"离开教室");

    }

//    @OnMessage
//    public void onMessage(MsgData msg){
//        logger.info("[IMService] recevier msg:{}",msg.getMsg());
//    }

    public void sendMessage(String msg){
        try {
            this.session.getBasicRemote().sendText(msg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String msg,Session session){
        WatcherInfo watcherInfo = RoomMap.getInstance().getWatcherInfo(roomId,userId);
        notifyRoomMsg(watcherInfo.getUserNick()+"说:"+msg);
    }


    public void notifyRoomMsg(String msg){
        // 通知其他人，我进入房间
        Map<String,IMService> userSession = roomIMServerMap.get(roomId);
        if(userSession != null){
            for(Map.Entry<String,IMService> entry:userSession.entrySet()){
                IMService imService = entry.getValue();
                imService.sendMessage(msg);
            }
        }
    }
}
