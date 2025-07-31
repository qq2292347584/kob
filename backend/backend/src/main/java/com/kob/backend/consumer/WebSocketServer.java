package com.kob.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    final public static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
    final private static CopyOnWriteArraySet<User> matchpool = new CopyOnWriteArraySet<>();

    private User user;
    private Session session = null;

    //由于websocketserver不是单例模式，所以在进行注入时要用send方法进行注入
    private static UserMapper userMapper;
    public static RecordMapper recordMapper;
    private Game game = null;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {WebSocketServer.recordMapper = recordMapper;}

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        int userId = JwtAuthentication.getUserId(token);
        User user1 = userMapper.selectById(userId);

        if(user1 != null){
            this.user = user1;
            users.put(userId, this);
        } else {
            this.session.close();
        }

    }

    @OnClose
    public void onClose() {
        // 关闭链接
        if(this.user != null) {
            users.remove(this.user.getId());
            matchpool.remove(this.user);
        }
    }

    public void startMatching() {
        matchpool.add(this.user);

        while(matchpool.size() >= 2) {
            Iterator<User> iterator = matchpool.iterator();
            User a =  iterator.next();
            User b = iterator.next();
            matchpool.remove(a);
            matchpool.remove(b);

            Game game1 = new Game(13, 14, 20, a.getId(), b.getId());
            game1.createMap();

            users.get(a.getId()).game = game1;
            users.get(b.getId()).game = game1;

            game1.start(); //开启一个新的线程

            JSONObject respGame = new JSONObject();
            respGame.put("a_id", game1.getPlayerA().getId());
            respGame.put("a_sx", game1.getPlayerA().getSx());
            respGame.put("a_sy", game1.getPlayerA().getSy());
            respGame.put("b_id", game1.getPlayerB().getId());
            respGame.put("b_sx", game1.getPlayerB().getSx());
            respGame.put("b_sy", game1.getPlayerB().getSy());
            respGame.put("map", game1.getG());

            JSONObject respA = new JSONObject();
            respA.put("event", "start-matching");
            respA.put("opponent_username", b.getUsername());
            respA.put("opponent_photo", b.getPhoto());
            respA.put("game", respGame);
            users.get(a.getId()).sendMessage(respA.toJSONString());

            JSONObject respB = new JSONObject();
            respB.put("event", "start-matching");
            respB.put("opponent_username", a.getUsername());
            respB.put("opponent_photo", a.getPhoto());
            respB.put("game", respGame);
            users.get(b.getId()).sendMessage(respB.toJSONString());
        }
    }

    private void move(Integer direction) {
        if(game.getPlayerA().getId().equals(user.getId())) {
            game.setNextStepA(direction);
        } else if(game.getPlayerB().getId().equals(user.getId())) {
            game.setNextStepB(direction);
        }
    }

    public void stopMatching() {
        matchpool.remove(this.user);
    }

    @OnMessage
    public void onMessage(String message, Session session) { //当作路由
        System.out.println("received messages!");
        // 从Client接收消息
        JSONObject jsonObject = JSONObject.parseObject(message);
        String event = jsonObject.getString("event");
        if("start-matching".equals(event)){
            startMatching();
        } else if("stop-matching".equals(event)){
            stopMatching();
        } else if("move".equals(event)) {
            move(jsonObject.getInteger("direction"));
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    //后端向前端发送信息
    public void sendMessage(String message) {
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}