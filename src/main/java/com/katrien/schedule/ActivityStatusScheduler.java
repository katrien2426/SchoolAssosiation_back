package com.katrien.schedule;

import com.katrien.mapper.ActivityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ActivityStatusScheduler {
    
    @Autowired
    private ActivityMapper activityMapper;

    // 每5分钟执行一次状态更新
    @Scheduled(fixedRate = 300000)
    @Transactional
    public void updateActivityStatus() {
        // 更新进行中的活动
        activityMapper.updateOngoingActivities();
        
        // 更新已完成的活动
        activityMapper.updateCompletedActivities();
    }
}
