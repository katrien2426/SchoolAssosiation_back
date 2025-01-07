package com.katrien.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Transient;

/**
 * @author : Katrien
 * @description :财务记录
 */
@Data
public class Finance {
    private Integer recordId;
    private Integer clubId;
    private BigDecimal amount;
    private String type;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transactionDate;
    private Integer recordedBy;
    @Transient
    private String clubName;
}
