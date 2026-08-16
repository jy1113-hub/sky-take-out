package com.sky.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/ws")
@Slf4j
public class WebSocketServer {

    // 用ConcurrentHashMap存所有连接的客户端Session
    private static final ConcurrentHashMap<Long, Session> SESSION_MAP = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        log.info("WebSocket连接建立: {}", session.getId());
        SESSION_MAP.put(System.currentTimeMillis(), session); // 暂用时间戳做key
    }

    @OnClose
    public void onClose(Session session) {
        log.info("WebSocket连接关闭: {}", session.getId());
        SESSION_MAP.values().removeIf(s -> s.equals(session));
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到客户端消息: {}", message);
    }

    /**
     * 向所有客户端广播消息（来单提醒、催单）
     */
    public void sendToAllClient(String message) {
        Collection<Session> sessions = SESSION_MAP.values();
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                log.error("WebSocket推送消息失败: {}", e.getMessage());
            }
        }
    }
}