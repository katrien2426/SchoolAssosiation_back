package com.katrien.service.impl;

import com.katrien.mapper.ActivityMapper;
import com.katrien.pojo.Activity;
import com.katrien.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public List<Activity> getAllActivities(String activityName, String status) {
        try {
            return activityMapper.getAllActivities(activityName, status);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Activity getActivityById(Integer id) {
        return activityMapper.getActivityById(id);
    }

    @Override
    public List<Activity> getActivitiesByClubId(Integer clubId) {
        return activityMapper.getActivitiesByClubId(clubId);
    }

    @Override
    public List<Activity> getActivitiesByAdvisorId(Integer advisorId) {
        return activityMapper.getActivitiesByAdvisorId(advisorId);
    }

    @Override
    public boolean createActivity(Activity activity) {
        return activityMapper.insertActivity(activity) > 0;
    }

    @Override
    public boolean updateActivity(Activity activity) {
        return activityMapper.updateActivity(activity) > 0;
    }

    @Override
    public boolean deleteActivity(Integer id) {
        return activityMapper.deleteActivity(id) > 0;
    }
}
