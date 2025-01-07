package com.katrien.service;

import com.katrien.pojo.Member;
import java.util.List;

public interface MemberService {
    Member getMemberById(Integer memberId);
    List<Member> getMembersByClubId(Integer clubId);
    boolean createMember(Member member);
    boolean updateMember(Member member);
    boolean deleteMember(Integer memberId);
    List<Member> searchMembers(Integer clubId, String name, String studentId, String role, String status);
}
