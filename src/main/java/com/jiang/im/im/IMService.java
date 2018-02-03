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
@ServerEndpoint(value="/imService/{roomId}/{userId}")
public class IMService {
    private static Logger logger = LoggerFactory.getLogger(IMService.class);

    private Session session ;

    private static Map<String,Map<String, IMService>> roomIMServerMap = new HashMap<>();

    private String roomId ;
    private String userId;

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "roomId") String roomId, @PathParam(value="userId")String userId){
        this.session = session;
        this.roomId = roomId ;
        this.userId = userId ;

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

        //notifyRoomMsg(name+"进入教室");

    }

    @OnClose
    public void onClose(){
        //logger.info("[IMService] disconnect, all counts:{}",imServiceSet.size());
        //notifyRoomMsg(name+"离开教室");

        Map<String, IMService> userSession = roomIMServerMap.get(roomId);
        if(userSession != null){
            for(Map.Entry<String, IMService> entry:userSession.entrySet()){
                IMService imService = entry.getValue();
                if(imService != null && imService.userId.equals(this.userId)){
                    userSession.remove(entry.getKey());
                    break;
                }
            }
        }
    }


    @OnMessage
    public void onMessage(String msg,Session session){
        notifyRoomMsg(msg);
    }

    public void sendMessage(String msg){
        try {
            this.session.getBasicRemote().sendText(msg);
        }catch (Exception e){
            e.printStackTrace();
        }
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
