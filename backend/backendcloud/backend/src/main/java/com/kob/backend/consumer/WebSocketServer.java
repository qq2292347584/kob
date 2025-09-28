package com.kob.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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

    private User user;
    private Session session = null;

    //由于websocketserver不是单例模式，所以在进行注入时要用send方法进行注入
    private static UserMapper userMapper;
    public static RecordMapper recordMapper;
    private static BotMapper botMapper;
    public static RestTemplate restTemplate;
    public Game game = null;
    private final static String addPlayerUrl = "http://127.0.0.1:9000/player/add/";
    private final static String removePlayerUrl = "http://127.0.0.1:9000/player/remove/";
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {WebSocketServer.recordMapper = recordMapper;}
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {WebSocketServer.restTemplate = restTemplate;}
    @Autowired
    public void setBotMapper(BotMapper botMapper) {WebSocketServer.botMapper = botMapper;}

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
        }
    }

    public static void startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        User a = userMapper.selectById(aId);
        User b = userMapper.selectById(bId);
        Bot botA = botMapper.selectById(aBotId);
        Bot botB = botMapper.selectById(bBotId);

        Game game1 = new Game(
                13,
                14,
                20,
                a.getId(),
                botA,
                b.getId(),
                botB);
        game1.createMap();
        if(users.get(a.getId()) != null)
            users.get(a.getId()).game = game1;
        if(users.get(b.getId()) != null)
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
        if(users.get(a.getId()) != null)
            users.get(a.getId()).sendMessage(respA.toJSONString());

        JSONObject respB = new JSONObject();
        respB.put("event", "start-matching");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", a.getPhoto());
        respB.put("game", respGame);
        if(users.get(b.getId()) != null)
            users.get(b.getId()).sendMessage(respB.toJSONString());
    }

    public void startMatching(Integer botId) {
        //向后端发请求
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        data.add("rating", this.user.getRating().toString());
        data.add("bot_id", botId.toString());
        restTemplate.postForObject(WebSocketServer.addPlayerUrl, data, String.class);
    }

    public void stopMatching() {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        restTemplate.postForObject(WebSocketServer.removePlayerUrl, data, String.class);
    }

    private void move(Integer direction) {
        if(game.getPlayerA().getId().equals(user.getId())) {
            if(game.getPlayerA().getBotId().equals(-1)) //亲自出马
                game.setNextStepA(direction);
        } else if(game.getPlayerB().getId().equals(user.getId())) {
            if(game.getPlayerB().getBotId().equals(-1)) //亲自出马
                game.setNextStepB(direction);
        }
    }

    //类似于websocket的请求中转站
    @OnMessage
    public void onMessage(String message, Session session) { //当作路由
        System.out.println("received messages!");
        // 从Client接收消息
        JSONObject jsonObject = JSONObject.parseObject(message);
        String event = jsonObject.getString("event");
        if("start-matching".equals(event)){
            startMatching(jsonObject.getInteger("bot_id"));
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