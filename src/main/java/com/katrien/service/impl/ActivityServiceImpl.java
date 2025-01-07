package com.katrien.service.impl;

import com.katrien.mapper.ActivityMapper;
import com.katrien.pojo.Activity;
import com.katrien.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Override
    public List<Activity> getByStatus(String status) {
        try {
            return activityMapper.getByStatus(status);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
