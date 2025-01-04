package com.katrien.controller;

import com.katrien.common.Result;
import com.katrien.pojo.ActivityApproval;
import com.katrien.service.ActivityApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity-approvals")
public class ActivityApprovalController {
    @Autowired
    private ActivityApprovalService approvalService;

    @GetMapping("/{approvalId}")
    public Result<ActivityApproval> getApproval(@PathVariable Integer approvalId) {
        ActivityApproval approval = approvalService.getApprovalById(approvalId);
        return approval != null ? Result.success(approval) : Result.error("审批记录不存在");
    }

    @GetMapping("/activity/{activityId}")
    public Result<ActivityApproval> getApprovalByActivity(@PathVariable Integer activityId) {
        ActivityApproval approval = approvalService.getApprovalByActivityId(activityId);
        return approval != null ? Result.success(approval) : Result.error("未找到该活动的审批记录");
    }

    @GetMapping("/status/{status}")
    public Result<List<ActivityApproval>> getApprovalsByStatus(@PathVariable String status) {
        return Result.success(approvalService.getApprovalsByStatus(status));
    }

    @PostMapping
    public Result<Void> createApproval(@RequestBody ActivityApproval approval) {
        return approvalService.createApproval(approval) ? 
               Result.success() : Result.error("创建审批记录失败");
    }

    @PutMapping
    public Result<Void> updateApproval(@RequestBody ActivityApproval approval) {
        return approvalService.updateApproval(approval) ? 
               Result.success() : Result.error("更新审批记录失败");
    }

    @DeleteMapping("/{approvalId}")
    public Result<Void> deleteApproval(@PathVariable Integer approvalId) {
        return approvalService.deleteApproval(approvalId) ? 
               Result.success() : Result.error("删除审批记录失败");
    }

    @GetMapping("/pending")
    public Result<List<ActivityApproval>> getPendingApprovals() {
        return Result.success(approvalService.getPendingApprovals());
    }

    @PostMapping("/{approvalId}/approve")
    public Result<Void> approveActivity(@PathVariable Integer approvalId,
                                      @RequestParam Integer approverId,
                                      @RequestParam String comments) {
        return approvalService.approveActivity(approvalId, approverId, comments) ? 
               Result.success() : Result.error("审批通过操作失败");
    }

    @PostMapping("/{approvalId}/reject")
    public Result<Void> rejectActivity(@PathVariable Integer approvalId,
                                     @RequestParam Integer approverId,
                                     @RequestParam String comments) {
        return approvalService.rejectActivity(approvalId, approverId, comments) ? 
               Result.success() : Result.error("审批拒绝操作失败");
    }
}
