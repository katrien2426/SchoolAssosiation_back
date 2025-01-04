package com.katrien.service.impl;

import com.katrien.mapper.ActivityApprovalMapper;
import com.katrien.mapper.ActivityMapper;
import com.katrien.pojo.Activity;
import com.katrien.pojo.ActivityApproval;
import com.katrien.service.ActivityApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityApprovalServiceImpl implements ActivityApprovalService {
    @Autowired
    private ActivityApprovalMapper approvalMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public ActivityApproval getApprovalById(Integer approvalId) {
        return approvalMapper.getApprovalById(approvalId);
    }

    @Override
    public ActivityApproval getApprovalByActivityId(Integer activityId) {
        return approvalMapper.getApprovalByActivityId(activityId);
    }

    @Override
    public List<ActivityApproval> getApprovalsByStatus(String status) {
        return approvalMapper.getApprovalsByStatus(status);
    }

    @Override
    public boolean createApproval(ActivityApproval approval) {
        if (approval.getStatus() == null) {
            approval.setStatus("pending");
        }
        if (approval.getSubmissionDate() == null) {
            approval.setSubmissionDate(LocalDateTime.now());
        }
        if (approval.getApprovalDate() == null && 
            ("approved".equals(approval.getStatus()) || "rejected".equals(approval.getStatus()))) {
            approval.setApprovalDate(LocalDateTime.now());
        }
        return approvalMapper.insertApproval(approval) > 0;
    }

    @Override
    public boolean updateApproval(ActivityApproval approval) {
        return approvalMapper.updateApproval(approval) > 0;
    }

    @Override
    public boolean deleteApproval(Integer approvalId) {
        return approvalMapper.deleteApproval(approvalId) > 0;
    }

    @Override
    public List<ActivityApproval> getPendingApprovals() {
        return approvalMapper.getPendingApprovals();
    }

    @Override
    public List<ActivityApproval> getApprovalsByActivityId(Integer activityId) {
        try {
            return approvalMapper.getApprovalsByActivityId(activityId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional
    public boolean approveActivity(Integer approvalId, Integer approverId, String comments) {
        ActivityApproval approval = approvalMapper.getApprovalById(approvalId);
        if (approval == null || !"pending".equals(approval.getStatus())) {
            return false;
        }

        // 创建新的审批记录
        ActivityApproval newApproval = new ActivityApproval();
        newApproval.setActivityId(approval.getActivityId());
        newApproval.setApprovedBy(approverId);
        newApproval.setStatus("approved");
        newApproval.setComments(comments);
        newApproval.setApprovalDate(LocalDateTime.now());
        approvalMapper.insertApproval(newApproval);

        // 更新活动状态
        Activity activity = activityMapper.getActivityById(approval.getActivityId());
        activity.setStatus("approved");
        return activityMapper.updateActivity(activity) > 0;
    }

    @Override
    @Transactional
    public boolean rejectActivity(Integer approvalId, Integer approverId, String comments) {
        ActivityApproval approval = approvalMapper.getApprovalById(approvalId);
        if (approval == null || !"pending".equals(approval.getStatus())) {
            return false;
        }

        // 创建新的审批记录
        ActivityApproval newApproval = new ActivityApproval();
        newApproval.setActivityId(approval.getActivityId());
        newApproval.setApprovedBy(approverId);
        newApproval.setStatus("rejected");
        newApproval.setComments(comments);
        newApproval.setApprovalDate(LocalDateTime.now());
        approvalMapper.insertApproval(newApproval);

        // 更新活动状态（保持为pending）
        Activity activity = activityMapper.getActivityById(approval.getActivityId());
        activity.setStatus("pending");
        return activityMapper.updateActivity(activity) > 0;
    }
}
