package com.katrien.pojo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Transient;
import java.time.LocalDateTime;

/**
 * @author : Katrien
 * @description :荣誉类
 */
@Data
public class Honor {
    private Integer honorId;
    private String honorName;
    private Integer clubId;
    @Transient
    private String clubName;
    private String honorLevel;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime awardTime;
    private String issuingAuthority;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime updateTime;
}
