package com.kob.matchingsystem.service.impl;

import com.kob.matchingsystem.service.MatchingService;
import com.kob.matchingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {
    public final static MatchingPool matchingPool = new MatchingPool();

    @Override
    public String addPlayer(Integer userId, Integer rating, Integer botId) {
        System.out.println("this is addPlayer: " + userId + " " + rating);
        matchingPool.addPlayer(userId, rating, botId);
        return "addPlayer success";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("this is removePlayer: " + userId);
        matchingPool.removePlayer(userId);
        return "removePlayer success";
    }
}
