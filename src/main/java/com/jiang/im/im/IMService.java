package com.jiang.im.im;

import com.jiang.im.dataobject.RoomInfo;
import com.jiang.im.dataobject.UserProfile;
import com.jiang.im.repository.RoomInfoRepository;
import com.jiang.im.repository.UserProfileRepository;
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

@Component
//@ServerEndpoint(value="/imService",encoders = {MsgEncoder.class},decoders = {MsgDecoder.class})
@ServerEndpoint(value="/imService/{roomid}/{info}")
public class IMService {

    public static final int USER_TYPE_HOST = 1 ;

    public static final int USER_TYPE_GUEST = 2 ;
    private static Logger logger = LoggerFactory.getLogger(IMService.class);

    private Session session ;

    //private static CopyOnWriteArraySet<IMService> imServiceSet = new CopyOnWriteArraySet<>();

    private static Map<String,IMService> userSessionMap = new HashMap<>();

    private static Map<String,Map<String, IMService>> roomIMServerMap = new HashMap<>();

    private String roomId ;
    private String userId;
    private int userType = USER_TYPE_HOST;
    private String name ;
    private String avatar;


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

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "roomid") String roomId, @PathParam(value="info")String info){
        this.session = session;
        this.roomId = roomId ;
        String infoStr = IMUtils.decode(info);

        this.userId = IMUtils.parse(infoStr,IMUtils.USERID) ;
        this.name = IMUtils.parse(infoStr,IMUtils.NAME);
        this.avatar = IMUtils.parse(infoStr,IMUtils.AVATAR);
        this.userType = Integer.parseInt(IMUtils.parse(infoStr,IMUtils.USERTYPE));
        logger.info("roomid:{},userid:{} come in",roomId,userId);
        if(roomIMServerMap.containsKey(roomId)){
            Map<String, IMService> userSession = roomIMServerMap.get(roomId);
            userSession.put(userId,this);
        }
        else{
            Map<String, IMService> userSession = new HashMap<>();
            userSession.put(userId,this);
            roomIMServerMap.put(roomId,userSession);
        }

        notifyRoomMsg(name+"进入教室");

    }

    @OnClose
    public void onClose(){
        //logger.info("[IMService] disconnect, all counts:{}",imServiceSet.size());
        notifyRoomMsg(name+"离开教室");

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
        notifyRoomMsg(name+"说:"+msg);
    }


    public void notifyRoomMsg(String msg){
        // 通知其他人，我进入房间
        Map<String, IMService> userSession = roomIMServerMap.get(roomId);
        if(userSession != null){
            for(Map.Entry<String, IMService> entry:userSession.entrySet()){
                IMService imService = entry.getValue();
                imService.sendMessage(msg);
            }
        }
    }
}
