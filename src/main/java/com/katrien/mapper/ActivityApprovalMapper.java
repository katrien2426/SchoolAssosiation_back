package com.katrien.mapper;

import com.katrien.pojo.ActivityApproval;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * @author : Katrien
 * @description : 活动审批Mapper
 */
@Mapper
public interface ActivityApprovalMapper {
    @Select("SELECT * FROM activity_approvals WHERE approval_id = #{approvalId}")
    ActivityApproval getApprovalById(Integer approvalId);

    @Select("SELECT * FROM activity_approvals WHERE activity_id = #{activityId} ORDER BY submission_date DESC LIMIT 1")
    ActivityApproval getApprovalByActivityId(Integer activityId);

    @Select("SELECT * FROM activity_approvals WHERE activity_id = #{activityId} ORDER BY submission_date DESC")
    List<ActivityApproval> getApprovalsByActivityId(Integer activityId);

    @Insert("INSERT INTO activity_approvals(activity_id, submitted_by, status, comments, submission_date) " +
            "VALUES(#{activityId}, #{submittedBy}, #{status}, #{comments}, #{submissionDate})")
    @Options(useGeneratedKeys = true, keyProperty = "approvalId")
    int insertApproval(ActivityApproval approval);

    @Update("UPDATE activity_approvals SET status=#{status}, comments=#{comments}, " +
            "approved_by=#{approvedBy}, approval_date=#{approvalDate} " +
            "WHERE approval_id=#{approvalId}")
    int updateApproval(ActivityApproval approval);

    @Delete("DELETE FROM activity_approvals WHERE approval_id = #{approvalId}")
    int deleteApproval(Integer approvalId);
}
