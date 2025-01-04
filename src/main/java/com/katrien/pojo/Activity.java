package com.katrien.pojo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Activity {
    private Integer activityId;      // 活动唯一标识
    private Integer clubId;          // 所属社团ID
    private String clubName;         // 社团名称（非数据库字段）
    private String activityName;     // 活动名称
    private String description;      // 活动描述
    private LocalDateTime startDate; // 活动开始时间
    private LocalDateTime endDate;   // 活动结束时间
    private String location;         // 活动地点
    private Integer maxParticipants; // 最大参与人数
    private String status;           // 活动状态 (draft, pending, approved, ongoing, completed, cancelled)
    private Integer createdBy;       // 创建者ID
    private String creatorName;      // 创建者名称（非数据库字段）
}
