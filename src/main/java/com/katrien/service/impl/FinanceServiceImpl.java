package com.katrien.service.impl;

import com.katrien.mapper.FinanceMapper;
import com.katrien.pojo.Finance;
import com.katrien.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FinanceServiceImpl implements FinanceService {
    @Autowired
    private FinanceMapper financeMapper;

    @Override
    public Finance getFinanceById(Integer recordId) {
        return financeMapper.getFinanceById(recordId);
    }

    @Override
    public List<Finance> getFinancesByClubId(Integer clubId, String type) {
        return financeMapper.getFinancesByClubId(clubId, type, null, null);
    }

    @Override
    public List<Finance> getFinancesByClubId(Integer clubId, String type, String startDate, String endDate) {
        return financeMapper.getFinancesByClubId(clubId, type, startDate, endDate);
    }

    @Override
    public boolean createFinanceRecord(Finance finance) {
        return financeMapper.insertFinance(finance) > 0;
    }

    @Override
    public boolean updateFinanceRecord(Finance finance) {
        return financeMapper.updateFinance(finance) > 0;
    }

    @Override
    public boolean deleteFinanceRecord(Integer recordId) {
        return financeMapper.deleteFinance(recordId) > 0;
    }

    @Override
    public Double getTotalIncome(Integer clubId) {
        return financeMapper.getTotalIncome(clubId, null, null);
    }

    @Override
    public Double getTotalIncome(Integer clubId, String startDate, String endDate) {
        return financeMapper.getTotalIncome(clubId, startDate, endDate);
    }

    @Override
    public Double getTotalExpense(Integer clubId) {
        return financeMapper.getTotalExpense(clubId, null, null);
    }

    @Override
    public Double getTotalExpense(Integer clubId, String startDate, String endDate) {
        return financeMapper.getTotalExpense(clubId, startDate, endDate);
    }

    @Override
    public Double getBalance(Integer clubId) {
        return getTotalIncome(clubId) - getTotalExpense(clubId);
    }

    @Override
    public Double getBalance(Integer clubId, String startDate, String endDate) {
        return getTotalIncome(clubId, startDate, endDate) - getTotalExpense(clubId, startDate, endDate);
    }
}
