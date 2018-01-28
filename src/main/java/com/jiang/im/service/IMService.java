package com.jiang.im.service;

import com.jiang.im.decoder.MsgDecoder;
import com.jiang.im.encoder.MsgEncoder;
import com.jiang.im.message.MsgData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
//@ServerEndpoint(value="/imService",encoders = {MsgEncoder.class},decoders = {MsgDecoder.class})
@ServerEndpoint(value="/imService/{roomid}/{userid}")
public class IMService {

    private static Logger logger = LoggerFactory.getLogger(IMService.class);

    private Session session ;

    private static CopyOnWriteArraySet<IMService> imServiceSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "roomid") String roomid, @PathParam(value="userid")String userId){
        this.session = session;
        imServiceSet.add(this);
        logger.info("[IMService] new connect, all counts:{}",imServiceSet.size());
        logger.info("roomid:{},userid:{}",roomid,userId);
    }

    @OnClose
    public void onClose(){
        imServiceSet.remove(this);
        logger.info("[IMService] disconnect, all counts:{}",imServiceSet.size());
    }

//    @OnMessage
//    public void onMessage(MsgData msg){
//        logger.info("[IMService] recevier msg:{}",msg.getMsg());
//    }

    @OnMessage
    public void onMessage(String msg,Session session){
        logger.info("[IMService] recevier msg:{}",msg);
    }
}
