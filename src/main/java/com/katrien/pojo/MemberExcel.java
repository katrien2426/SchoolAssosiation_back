package com.katrien.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author : Katrien
 * @description :成员Excel实体类
 */
@Data
public class MemberExcel {
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("学号")
    private String studentId;
    @ExcelProperty("角色")
    private String role;
    @ExcelProperty("联系方式")
    private String contactInfo;
}
