package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateServiceImpl implements UpdateService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> update(Map<String, String> data) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) token.getPrincipal();
        User user = loginUser.getUser();

        int bot_id = Integer.parseInt(data.get("bot_id"));

        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        Map<String, String> result = new HashMap<>();

        if(title == null || title.length() == 0) {
            result.put("error_message", "标题不能为空");
            return result;
        }

        if(title.length() > 100) {
            result.put("error_message", "标题长度不能大于100");
            return result;
        }

        if(description == null || description.length() == 0) {
            description = "这个用户很懒，什么也没留下~";
        }

        if(description != null && description.length() > 300) {
            result.put("error_message", "Bot描述的长度不能大于300");
            return result;
        }

        if(content == null || content.length() == 0) {
            result.put("error_message", "代码不能为空");
            return result;
        }

        if (content.length() > 10000) {
            result.put("error_message", "代码长度不能超过10000");
            return result;
        }

        Bot bot = botMapper.selectById(bot_id);
        if(bot == null) {
            result.put("error_message", "当前Bot不存在或已被删除");
            return result;
        }

        if(!bot.getUserId().equals(user.getId())) {
            result.put("error_message", "没有权限修改该Bot");
            return result;
        }

        Bot newBot = new Bot(bot.getId(), user.getId(), title, description, content, bot.getCreateTime(), new Date());
        botMapper.updateById(newBot);

        result.put("error_message", "success");
        return result;
    }
}
