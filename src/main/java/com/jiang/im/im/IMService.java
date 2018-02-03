package com.jiang.im.im;

import com.jiang.im.config.MyEndpointConfigure;
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

//MyEndpointConfigure 使用了这个类，websocket就归纳到类spring管理，websocket对象只有一个
@Component
//@ServerEndpoint(value="/imService",encoders = {MsgEncoder.class},decoders = {MsgDecoder.class})
@ServerEndpoint(value="/imService/{roomId}/{userId}",configurator=MyEndpointConfigure.class)
public class IMService {
    private static Logger logger = LoggerFactory.getLogger(IMService.class);

    private static Map<String,Map<String, UserSession>> roomIMServerMap = new HashMap<>();

    private static Map<String,String> sessionIdRoomMap = new HashMap<String,String>();
    @Autowired
    RoomInfoRepository roomInfoRepository;

    @Autowired
    UserProfileRepository userProfileRepository ;


    @OnOpen
    public void onOpen(Session session, @PathParam(value = "roomId") String roomId, @PathParam(value="userId")String userId){
        sessionIdRoomMap.put(session.getId(),roomId);
        logger.info("roomid:{},userid:{} come in",roomId,userId);
        Map<String, UserSession> userSessionMap = null;
        UserSession userSession = new UserSession();
        userSession.setSession(session);
        userSession.setUserId(userId);
        if(roomIMServerMap.containsKey(roomId)){
            userSessionMap = roomIMServerMap.get(roomId);
            userSessionMap.put(userId,userSession);
        }
        else{

            userSessionMap = new HashMap<>();
            userSessionMap.put(userId,userSession);
            roomIMServerMap.put(roomId,userSessionMap);
        }
        updateRoomInfo(session.getId());
    }

    @OnClose
    public void onClose(Session session){
        Map<String, UserSession> userSessionMap = null ;
        String roomId ;
        if(sessionIdRoomMap.containsKey(session.getId()))
            roomId = sessionIdRoomMap.get(session.getId());
        else
            return ;

        String userId  = null ;
        userSessionMap = roomIMServerMap.get(roomId);
        if(userSessionMap != null){
            for(Map.Entry<String, UserSession> entry:userSessionMap.entrySet()){
                UserSession userSession = entry.getValue();
                if(userSession != null && userSession.getSession().getId().equals(session.getId())){
                    userId = userSession.getUserId();
                    userSessionMap.remove(entry.getKey());
                    break;
                }
            }
        }
        if(userId == null)
            return ;
        //通知其他人，我离开了
        String json = "{\"account\":\"%s\",\"content\":\"离开了\",\"header\":\"\",\"nickName\":\"\",\"msgType\":1,\"level\":0}";
        json = String.format(json,userId);
        notifyRoomMsg(json,session.getId());
        updateRoomInfo(session.getId());
    }


    @OnMessage
    public void onMessage(String msg,Session session){
        notifyRoomMsg(msg,session.getId());
    }

    public void sendMessage(Session session,String msg){
        try {
            session.getBasicRemote().sendText(msg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void notifyRoomMsg(String msg,String sessionId){
        Map<String, UserSession> userSessionMap = null ;
        String roomId ;
        if(sessionIdRoomMap.containsKey(sessionId))
            roomId = sessionIdRoomMap.get(sessionId);
        else
            return ;

        // 通知其他人，我进入房间
        userSessionMap = roomIMServerMap.get(roomId);
        if(userSessionMap != null){
            for(Map.Entry<String, UserSession> entry:userSessionMap.entrySet()){
                UserSession userSession  = entry.getValue();
                sendMessage(userSession.getSession(),msg);
            }
        }
    }

    private void updateRoomInfo(String sessionId){
        Map<String, UserSession> userSessionMap = null ;
        String roomId ;
        if(sessionIdRoomMap.containsKey(sessionId))
            roomId = sessionIdRoomMap.get(sessionId);
        else
            return ;

        if(roomIMServerMap.containsKey(roomId)){
            userSessionMap = roomIMServerMap.get(roomId);
            if(userSessionMap != null){
                // 更新数据库，主要是更新人数
                RoomInfo roomInfo =  roomInfoRepository.findOne(Integer.parseInt(roomId));
                if(roomInfo != null) {
                    roomInfo.setWatcherNum(userSessionMap.size());
                }
                roomInfoRepository.save(roomInfo);
            }
        }
    }
}
