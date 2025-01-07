package com.katrien.pojo;

import lombok.Data;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Transient;

/**
 * @author : Katrien
 * @description : 社团实体类
 */
@Data
public class Club {
    private Integer clubId;
    private String clubName;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate creationDate;
    private Integer presidentId;
    private String unit;
    private String status;
    @Transient
    private String presidentName;
}
