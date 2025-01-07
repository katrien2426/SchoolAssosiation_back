package com.katrien.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import java.time.LocalDate;

/**
 * @author : Katrien
 * @description : 成员
 */
@Data
public class Member {
    @ExcelIgnore
    private Integer memberId;
    @ExcelIgnore
    private Integer clubId;
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("学号")
    private String studentId;
    @ExcelProperty("角色")
    private String role;
    @ExcelProperty("加入日期")
    private LocalDate joinDate;
    @ExcelProperty("联系方式")
    private String contactInfo;
    @ExcelProperty("状态")
    private String status;
}
