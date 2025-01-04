package com.katrien.pojo;

import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class Club {
    private Integer clubId;  // 修改为clubId以匹配数据库字段
    private String clubName;  // 改为clubName以匹配数据库的club_name字段
    private String description;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "GMT+8")
    private LocalDateTime creationDate;
    
    private Integer presidentId;
    private String unit;
    private String status;
    private String presidentName; // 社长姓名，从users表关联查询获取
}
