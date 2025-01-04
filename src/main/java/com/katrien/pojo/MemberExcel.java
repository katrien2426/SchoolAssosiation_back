package com.katrien.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

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
