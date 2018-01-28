package com.jiang.im.encoder;

import com.jiang.im.message.MsgData;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MsgEncoder implements Encoder.Text<MsgData> {

    @Override
    public String encode(MsgData object) throws EncodeException {
        return object.getMsg();
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
