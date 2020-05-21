package com.hamdata.websocket.server;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/websocket/{topic}")
@Component
@Slf4j
public class WebSocketServer {

    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String topic="";

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam("topic") String topic) {
        this.session = session;
        this.topic = topic;
        if (webSocketMap.containsKey(topic)) {
            webSocketMap.remove(topic);
            webSocketMap.put(topic,this);
        }else{
            webSocketMap.put(topic,this);
        }
        sendMessage("Connection successful!!!");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(topic)){
            webSocketMap.remove(topic);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("消息:" + topic + ",报文:" + message);
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("错误:" + this.topic + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送自定义消息
     */
    public static void sendInfo(String message,String topic) {
        log.info("发送消息到:" + topic + "，报文:"+message);
        if(StringUtils.isNotBlank(topic) && webSocketMap.containsKey(topic)){
            webSocketMap.get(topic).sendMessage(message);
        }
    }
}
