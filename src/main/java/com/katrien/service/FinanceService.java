package com.katrien.service;

import com.katrien.pojo.Finance;
import java.util.List;

public interface FinanceService {
    Finance getFinanceById(Integer recordId);
    // 保留两个方法重载
    List<Finance> getFinancesByClubId(Integer clubId, String type);
    List<Finance> getFinancesByClubId(Integer clubId, String type, String startDate, String endDate);
    boolean createFinanceRecord(Finance finance);
    boolean updateFinanceRecord(Finance finance);
    boolean deleteFinanceRecord(Integer recordId);
    // 保留两个方法重载
    Double getTotalIncome(Integer clubId);
    Double getTotalIncome(Integer clubId, String startDate, String endDate);
    // 保留两个方法重载
    Double getTotalExpense(Integer clubId);
    Double getTotalExpense(Integer clubId, String startDate, String endDate);
    // 保留两个方法重载
    Double getBalance(Integer clubId);
    Double getBalance(Integer clubId, String startDate, String endDate);
}
