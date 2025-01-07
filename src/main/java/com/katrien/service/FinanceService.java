package com.katrien.service;

import com.katrien.pojo.Finance;
import java.util.List;

public interface FinanceService {
    Finance getFinanceById(Integer recordId);
    List<Finance> getFinancesByClubId(Integer clubId, String type);
    boolean createFinanceRecord(Finance finance);
    boolean updateFinanceRecord(Finance finance);
    boolean deleteFinanceRecord(Integer recordId);
    Double getTotalIncome(Integer clubId);
    Double getTotalExpense(Integer clubId);
    
    Double getBalance(Integer clubId);
}
