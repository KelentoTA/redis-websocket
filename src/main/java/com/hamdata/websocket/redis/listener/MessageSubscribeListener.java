package com.hamdata.websocket.redis.listener;

import com.hamdata.websocket.server.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageSubscribeListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();
        String content = new String(body);
        WebSocketServer.sendInfo(content,"topic");
    }
}
