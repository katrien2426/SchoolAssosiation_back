package com.katrien.service.impl;

import com.katrien.mapper.ClubMapper;
import com.katrien.mapper.ActivityMapper;
import com.katrien.mapper.UserMapper;
import com.katrien.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private ClubMapper clubMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, Integer> getStatistics() {
        Map<String, Integer> statistics = new HashMap<>();
        statistics.put("clubCount", clubMapper.selectCount(null));
        statistics.put("activityCount", activityMapper.selectCount(null));
        statistics.put("memberCount", userMapper.selectCount(null));
        return statistics;
    }
}
