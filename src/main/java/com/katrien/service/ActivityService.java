package com.katrien.service;

import com.katrien.pojo.Activity;
import java.util.List;

public interface ActivityService {
    List<Activity> getAllActivities(String activityName, String status);
    Activity getActivityById(Integer activityId);
    List<Activity> getActivitiesByClubId(Integer clubId);
    List<Activity> getActivitiesByAdvisorId(Integer advisorId);
    boolean createActivity(Activity activity);
    boolean updateActivity(Activity activity);
    boolean deleteActivity(Integer activityId);
    List<Activity> getByStatus(String status);
}
