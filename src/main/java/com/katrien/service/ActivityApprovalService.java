package com.katrien.service;

import com.katrien.pojo.ActivityApproval;
import java.util.List;

public interface ActivityApprovalService {
    boolean createApproval(ActivityApproval approval);
    ActivityApproval getApprovalByActivityId(Integer activityId);
    List<ActivityApproval> getApprovalsByActivityId(Integer activityId);
    boolean approveActivity(Integer approvalId, Integer approverId, String comments);
    boolean rejectActivity(Integer approvalId, Integer approverId, String comments);
    boolean updateApproval(ActivityApproval approval);
    boolean deleteApproval(Integer approvalId);
}
