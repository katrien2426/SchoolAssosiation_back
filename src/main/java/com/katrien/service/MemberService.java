package com.katrien.service;

import com.katrien.pojo.Member;
import java.util.List;

public interface MemberService {
    Member getMemberById(Integer memberId);
    List<Member> getMembersByClubId(Integer clubId);
    Member getMemberByStudentId(String studentId);
    boolean createMember(Member member);
    boolean updateMember(Member member);
    boolean deleteMember(Integer memberId);
    int countActiveMembers(Integer clubId);
    List<Member> searchMembers(Integer clubId, String name, String studentId, String role);
    List<Member> searchMembers(Integer clubId, String name, String studentId, String role, String status);
}
