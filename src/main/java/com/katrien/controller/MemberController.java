package com.katrien.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.katrien.common.Result;
import com.katrien.pojo.Member;
import com.katrien.pojo.MemberExcel;
import com.katrien.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Katrien
 * @description : 成员控制器
 */
@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    private static final String ROLE_VICE_PRESIDENT = "副社长";
    private static final String ROLE_MEMBER = "普通成员";

    @GetMapping("/club/{clubId}")
    public Result<List<Member>> getMembersByClubId(@PathVariable("clubId") Integer clubId) {
        List<Member> members = memberService.getMembersByClubId(clubId);
        return Result.success(members);
    }

    @GetMapping("/{memberId}")
    public Result<Member> getMemberById(@PathVariable("memberId") Integer memberId) {
        Member member = memberService.getMemberById(memberId);
        return member != null ? Result.success(member) : Result.error("成员不存在");
    }

    @PostMapping
    public Result<String> createMember(@RequestBody Member member) {
        boolean result = memberService.createMember(member);
        return result ? Result.success("创建成功") : Result.error("创建失败");
    }

    @PutMapping("/{memberId}")
    public Result<String> updateMember(@PathVariable("memberId") Integer memberId, @RequestBody Member member) {
        member.setMemberId(memberId);
        boolean result = memberService.updateMember(member);
        return result ? Result.success("更新成功") : Result.error("更新失败");
    }

    @DeleteMapping("/{memberId}")
    public Result<String> deleteMember(@PathVariable("memberId") Integer memberId) {
        boolean result = memberService.deleteMember(memberId);
        return result ? Result.success("删除成功") : Result.error("删除失败");
    }

    @GetMapping("/template")
    //下载模板
    public void downloadTemplate(HttpServletResponse response) {
        try {
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            // 设置响应编码
            response.setCharacterEncoding("utf-8");
            // 设置下载文件名
            String fileName = URLEncoder.encode("社团成员导入模板", "UTF-8");
            // 设置响应头
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            // 构建模板
            List<MemberExcel> list = new ArrayList<>();

            // 添加副社长示例
            MemberExcel vicePresident = new MemberExcel();
            vicePresident.setName("张三");
            vicePresident.setStudentId("20230001");
            vicePresident.setRole(ROLE_VICE_PRESIDENT);
            vicePresident.setContactInfo("13800138000");
            list.add(vicePresident);

            // 添加普通成员示例
            MemberExcel member = new MemberExcel();
            member.setName("李四");
            member.setStudentId("20230002");
            member.setRole(ROLE_MEMBER);
            member.setContactInfo("13800138001");
            list.add(member);

            EasyExcel.write(response.getOutputStream(), MemberExcel.class)
                    .sheet("成员名单模板")
                    .doWrite(list);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().write("{\"code\":500,\"message\":\"下载模板失败：" + e.getMessage() + "\"}");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @PostMapping("/import/{clubId}")
    //导入成员
    public Result<String> importMembers(@PathVariable("clubId") Integer clubId, @RequestParam("file") MultipartFile file) {
        try {
            List<Member> successList = new ArrayList<>();
            // 读取文件
            EasyExcel.read(file.getInputStream(), MemberExcel.class, new ReadListener<MemberExcel>() {
                @Override
                // 读取到一行数据后的操作
                public void invoke(MemberExcel excel, AnalysisContext context) {
                    Member member = new Member();
                    member.setClubId(clubId);
                    member.setName(excel.getName());
                    member.setStudentId(excel.getStudentId());

                    // 验证和设置角色
                    String role = excel.getRole().trim();
                    if (!role.equals(ROLE_VICE_PRESIDENT) && !role.equals(ROLE_MEMBER)) {
                        role = ROLE_MEMBER; // 默认设置为普通成员
                    }
                    member.setRole(role);

                    member.setContactInfo(excel.getContactInfo());
                    member.setStatus("active");
                    member.setJoinDate(LocalDate.now());

                    if (memberService.createMember(member)) {
                        successList.add(member);
                    }
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    // 解析完所有数据后的操作
                }
            }).sheet().doRead();
            return Result.success("成功导入 " + successList.size() + " 条记录");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("导入失败：" + e.getMessage());
        }
    }

    //导出成员
    @GetMapping("/export/{clubId}")
    public void exportMembers(@PathVariable("clubId") Integer clubId, HttpServletResponse response) {
        try {
            // 查询社团成员
            List<Member> members = memberService.getMembersByClubId(clubId);
            // 将成员对象转换为Excel对象
            List<MemberExcel> excelList = members.stream().map(member -> {
                MemberExcel excel = new MemberExcel();
                excel.setName(member.getName());
                excel.setStudentId(member.getStudentId());
                excel.setRole(member.getRole()); // 直接使用数据库中的角色值
                excel.setContactInfo(member.getContactInfo());
                return excel;
            }).collect(Collectors.toList());

            // 设置响应
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("社团成员名单", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 写入Excel
            EasyExcel.write(response.getOutputStream(), MemberExcel.class)
                    .sheet("成员名单")
                    .doWrite(excelList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 搜索成员
    @GetMapping("/search/{clubId}")
    public Result<List<Member>> searchMembers(
            @PathVariable("clubId") Integer clubId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "studentId", required = false) String studentId,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "status", required = false) String status) {

        List<Member> members = memberService.getMembersByClubId(clubId);

        // 如果没有搜索条件，返回所有成员
        if ((name == null || name.isEmpty()) &&
                (studentId == null || studentId.isEmpty()) &&
                (role == null || role.isEmpty()) &&
                (status == null || status.isEmpty())) {
            return Result.success(members);
        }

        // 根据条件筛选
        List<Member> filteredMembers = members.stream()
                .filter(member -> {
                    boolean matchName = name == null || name.isEmpty() ||
                            member.getName().toLowerCase().contains(name.toLowerCase());
                    boolean matchStudentId = studentId == null || studentId.isEmpty() ||
                            member.getStudentId().contains(studentId);
                    boolean matchRole = role == null || role.isEmpty() ||
                            member.getRole().equals(role);
                    boolean matchStatus = status == null || status.isEmpty() ||
                            member.getStatus().equals(status);
                    return matchName && matchStudentId && matchRole && matchStatus;
                })
                .collect(Collectors.toList());

        return Result.success(filteredMembers);
    }
}
