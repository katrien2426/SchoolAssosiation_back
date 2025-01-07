package com.katrien.controller;

import com.katrien.common.Result;
import com.katrien.pojo.ActivityApproval;
import com.katrien.service.ActivityApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author : Katrien
 * @description : 活动审批控制器
 */
@RestController
@RequestMapping("/api/activity-approvals")
public class ActivityApprovalController {
    @Autowired
    private ActivityApprovalService approvalService;

    // 获取活动的审批记录
    @GetMapping("/activity/{activityId}")
    public Result<ActivityApproval> getApprovalByActivity(@PathVariable Integer activityId) {
        ActivityApproval approval = approvalService.getApprovalByActivityId(activityId);
        return approval != null ? Result.success(approval) : Result.error("未找到该活动的审批记录");
    }

    // 创建审批记录
    @PostMapping
    public Result<Void> createApproval(@RequestBody ActivityApproval approval) {
        return approvalService.createApproval(approval) ? 
               Result.success() : Result.error("创建审批记录失败");
    }

    // 更新审批记录
    @PutMapping
    public Result<Void> updateApproval(@RequestBody ActivityApproval approval) {
        return approvalService.updateApproval(approval) ? 
               Result.success() : Result.error("更新审批记录失败");
    }

    // 删除审批记录
    @DeleteMapping("/{approvalId}")
    public Result<Void> deleteApproval(@PathVariable Integer approvalId) {
        return approvalService.deleteApproval(approvalId) ? 
               Result.success() : Result.error("删除审批记录失败");
    }

    // 审批通过
    @PostMapping("/{approvalId}/approve")
    public Result<Void> approveActivity(@PathVariable Integer approvalId,
                                      @RequestParam Integer approverId,
                                      @RequestParam String comments) {
        return approvalService.approveActivity(approvalId, approverId, comments) ? 
               Result.success() : Result.error("审批通过操作失败");
    }

    // 审批拒绝
    @PostMapping("/{approvalId}/reject")
    public Result<Void> rejectActivity(@PathVariable Integer approvalId,
                                     @RequestParam Integer approverId,
                                     @RequestParam String comments) {
        return approvalService.rejectActivity(approvalId, approverId, comments) ? 
               Result.success() : Result.error("审批拒绝操作失败");
    }
}
