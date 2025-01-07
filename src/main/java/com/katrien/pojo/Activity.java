package com.katrien.pojo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @author : Katrien
 * @description : 活动实体类
 */
@Data
public class Activity {
    private Integer activityId;
    private Integer clubId;
    private String clubName;
    private String activityName;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private Integer maxParticipants;
    private String status;
    private Integer createdBy;
    private String creatorName;
}
