package com.katrien.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.katrien.common.Result;
import com.katrien.pojo.Finance;
import com.katrien.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Katrien
 * @description : 财务控制器
 */
@RestController
@RequestMapping("/api/finances")
public class FinanceController {
    @Autowired
    private FinanceService financeService;

    // 分页查询财务记录
    @GetMapping
    public Result<Map<String, Object>> getFinances(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer clubId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        PageHelper.startPage(page, size);
        List<Finance> finances = financeService.getFinancesByClubId(clubId, type, startDate, endDate);
        PageInfo<Finance> pageInfo = new PageInfo<>(finances);

        Map<String, Object> result = new HashMap<>();
        result.put("records", pageInfo.getList());
        result.put("total", pageInfo.getTotal());

        // 添加统计信息，使用相同的日期范围
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalIncome", financeService.getTotalIncome(clubId, startDate, endDate));
        summary.put("totalExpense", financeService.getTotalExpense(clubId, startDate, endDate));
        summary.put("balance", financeService.getBalance(clubId, startDate, endDate));
        result.put("summary", summary);

        return Result.success(result);
    }

    @GetMapping("/{recordId}")
    public Result<Finance> getFinance(@PathVariable Integer recordId) {
        Finance finance = financeService.getFinanceById(recordId);
        return finance != null ? Result.success(finance) : Result.error("财务记录不存在");
    }

    @PostMapping
    public Result<Void> createFinance(@RequestBody Finance finance) {
        if (finance.getTransactionDate() == null) {
            return Result.error("交易日期不能为空");
        }
        return financeService.createFinanceRecord(finance) ? 
               Result.success() : Result.error("创建财务记录失败");
    }

    @PutMapping
    public Result<Void> updateFinance(@RequestBody Finance finance) {
        return financeService.updateFinanceRecord(finance) ? 
               Result.success() : Result.error("更新财务记录失败");
    }

    @DeleteMapping("/{recordId}")
    public Result<Void> deleteFinance(@PathVariable Integer recordId) {
        return financeService.deleteFinanceRecord(recordId) ? 
               Result.success() : Result.error("删除财务记录失败");
    }
}
