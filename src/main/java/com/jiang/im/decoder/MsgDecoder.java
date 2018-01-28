package com.jiang.im.decoder;

import com.jiang.im.message.MsgData;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MsgDecoder implements Decoder.Text<MsgData> {

    @Override
    public MsgData decode(String s) throws DecodeException {
        return new MsgData(s);
    }

    @Override
    public boolean willDecode(String s) {
        return false;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
