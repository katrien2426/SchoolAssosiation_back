package com.katrien.controller;

import com.katrien.common.Result;
import com.katrien.pojo.Activity;
import com.katrien.pojo.ActivityApproval;
import com.katrien.service.ActivityService;
import com.katrien.service.ActivityApprovalService;
import com.katrien.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Katrien
 * @description : 活动控制器
 */
@RestController
@RequestMapping("/api/activities")
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private ActivityApprovalService approvalService;

    @Autowired
    private UserService userService;

    // 获取所有活动
    @GetMapping
    public Result<List<Activity>> getAllActivities(
            @RequestParam(required = false) String activityName,
            @RequestParam(required = false) String status) {
        List<Activity> activities = activityService.getAllActivities(activityName, status);
        if (activities == null) {
            return Result.error("获取活动列表失败");
        }
        return Result.success(activities);
    }

    // 获取单个活动
    @GetMapping("/{id}")
    public Result<Activity> getActivityById(@PathVariable Integer id) {
        Activity activity = activityService.getActivityById(id);
        return activity != null ? Result.success(activity) : Result.error("活动不存在");
    }

    // 根据社团获取活动列表
    @GetMapping("/club/{clubId}")
    public Result<List<Activity>> getActivitiesByClub(@PathVariable Integer clubId) {
        List<Activity> activities = activityService.getActivitiesByClubId(clubId);
        return Result.success(activities);
    }

    // 根据状态获取活动列表
    @GetMapping("/status/{status}")
    public Result<List<Activity>> getActivitiesByStatus(@PathVariable String status) {
        List<Activity> activities = activityService.getByStatus(status);
        return activities != null ? Result.success(activities) : Result.error("获取活动列表失败");
    }

    // 创建活动
    @PostMapping
    public Result<Void> createActivity(@RequestBody Activity activity) {
        return activityService.createActivity(activity) ? Result.success() : Result.error("创建活动失败");
    }

    // 更新活动
    @PutMapping("/{id}")
    public Result<Void> updateActivity(@PathVariable Integer id, @RequestBody Activity activity) {
        activity.setActivityId(id);
        return activityService.updateActivity(activity) ? Result.success() : Result.error("更新活动失败");
    }

    // 删除活动
    @DeleteMapping("/{id}")
    public Result<Void> deleteActivity(@PathVariable Integer id) {
        return activityService.deleteActivity(id) ? Result.success() : Result.error("删除活动失败");
    }

    // 更新活动状态
    @PutMapping("/{id}/status")
    public Result<Void> updateActivityStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> request) {
        Activity activity = activityService.getActivityById(id);
        if (activity == null) {
            return Result.error("活动不存在");
        }
        // 获取状态和用户ID
        String status = (String) request.get("status");
        Integer userId = (Integer) request.get("userId");

        if (status == null || userId == null) {
            return Result.error("状态和用户ID不能为空");
        }

        // 创建新的审核记录
        ActivityApproval approval = new ActivityApproval();
        approval.setActivityId(id);
        approval.setSubmittedBy(userId);
        approval.setStatus("pending");
        approval.setSubmissionDate(LocalDateTime.now());

        if (!approvalService.createApproval(approval)) {
            return Result.error("创建审核记录失败");
        }
        // 更新活动状态
        activity.setStatus(status);
        return activityService.updateActivity(activity) ? 
            Result.success() : Result.error("更新活动状态失败");
    }

    // 审批活动
    @PutMapping("/{id}/approve")
    public Result<Void> approveActivity(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> request) {
        Activity activity = activityService.getActivityById(id);
        if (activity == null) {
            return Result.error("活动不存在");
        }

        Integer userId = (Integer) request.get("userId");
        String comments = (String) request.get("comments");
        
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }

        // 获取当前的审批记录
        ActivityApproval currentApproval = approvalService.getApprovalByActivityId(id);
        if (currentApproval == null) {
            return Result.error("找不到审批记录");
        }

        // 创建新的审核记录
        ActivityApproval approval = new ActivityApproval();
        approval.setActivityId(id);
        approval.setSubmittedBy(currentApproval.getSubmittedBy()); // 设置原始提交者
        approval.setApprovedBy(userId);
        approval.setStatus("approved");
        approval.setComments(comments != null ? comments : "已通过");
        approval.setApprovalDate(LocalDateTime.now());
        
        if (!approvalService.createApproval(approval)) {
            return Result.error("创建审核记录失败");
        }

        // 更新活动状态
        activity.setStatus("approved");
        return activityService.updateActivity(activity) ? 
            Result.success() : Result.error("审批失败");
    }

    // 拒绝活动
    @PutMapping("/{id}/reject")
    public Result<Void> rejectActivity(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> request) {
        Activity activity = activityService.getActivityById(id);
        if (activity == null) {
            return Result.error("活动不存在");
        }
        
        Integer userId = (Integer) request.get("userId");
        String comments = (String) request.get("comments");
        
        if (userId == null || comments == null || comments.trim().isEmpty()) {
            return Result.error("用户ID和拒绝理由不能为空");
        }

        // 获取当前的审批记录
        ActivityApproval currentApproval = approvalService.getApprovalByActivityId(id);
        if (currentApproval == null) {
            return Result.error("找不到审批记录");
        }

        // 创建新的审核记录
        ActivityApproval approval = new ActivityApproval();
        approval.setActivityId(id);
        approval.setSubmittedBy(currentApproval.getSubmittedBy()); // 设置原始提交者
        approval.setApprovedBy(userId);
        approval.setStatus("rejected");
        approval.setComments(comments);
        approval.setApprovalDate(LocalDateTime.now());
        
        if (!approvalService.createApproval(approval)) {
            return Result.error("创建审核记录失败");
        }

        // 更新活动状态
        activity.setStatus("draft");
        return activityService.updateActivity(activity) ? 
            Result.success() : Result.error("拒绝失败");
    }

    // 获取活动审核记录
    @GetMapping("/{id}/audit-logs")
    public Result<List<Map<String, Object>>> getActivityAuditLogs(@PathVariable Integer id) {
        List<ActivityApproval> approvals = approvalService.getApprovalsByActivityId(id);
        if (approvals == null) {
            return Result.error("获取审核记录失败");
        }

        List<Map<String, Object>> logs = new ArrayList<>();
        for (ActivityApproval approval : approvals) {
            Map<String, Object> log = new HashMap<>();
            
            // 设置日期
            log.put("date", approval.getApprovalDate() != null ? 
                approval.getApprovalDate() : approval.getSubmissionDate());
            
            // 设置用户名
            Integer userId = approval.getApprovedBy() != null ? 
                approval.getApprovedBy() : approval.getSubmittedBy();
            String userName = userService.getUserById(userId).getUsername();
            log.put("userName", userName);
            
            // 设置操作
            String action;
            switch (approval.getStatus()) {
                case "pending":
                    action = "提交审核";
                    break;
                case "approved":
                    action = "通过";
                    break;
                case "rejected":
                    action = "拒绝";
                    break;
                default:
                    action = approval.getStatus();
            }
            log.put("action", action);
            
            // 设置备注
            log.put("comments", approval.getComments());
            
            logs.add(log);
        }
        return Result.success(logs);
    }
}
