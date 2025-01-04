package com.katrien.service.impl;

import com.katrien.mapper.MemberMapper;
import com.katrien.pojo.Member;
import com.katrien.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public Member getMemberById(Integer memberId) {
        return memberMapper.getMemberById(memberId);
    }

    @Override
    public List<Member> getMembersByClubId(Integer clubId) {
        return memberMapper.getMembersByClubId(clubId);
    }

    @Override
    public Member getMemberByStudentId(String studentId) {
        return memberMapper.getMemberByStudentId(studentId);
    }

    @Override
    public boolean createMember(Member member) {
        // 设置默认值
        if (member.getJoinDate() == null) {
            member.setJoinDate(LocalDate.now());
        }
        if (member.getStatus() == null) {
            member.setStatus("active");
        }
        if (member.getRole() == null) {
            member.setRole("普通成员");
        }
        return memberMapper.insertMember(member) > 0;
    }

    @Override
    public boolean updateMember(Member member) {
        return memberMapper.updateMember(member) > 0;
    }

    @Override
    public boolean deleteMember(Integer memberId) {
        return memberMapper.deleteMember(memberId) > 0;
    }

    @Override
    public int countActiveMembers(Integer clubId) {
        return memberMapper.countActiveMembers(clubId);
    }
}
