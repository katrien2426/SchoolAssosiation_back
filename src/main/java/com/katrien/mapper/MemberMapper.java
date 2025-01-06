package com.katrien.mapper;

import com.katrien.pojo.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MemberMapper {
    @Select("SELECT * FROM members WHERE member_id = #{memberId}")
    Member getMemberById(Integer memberId);

    @Select("SELECT * FROM members WHERE club_id = #{clubId}")
    List<Member> getMembersByClubId(Integer clubId);

    @Select("SELECT * FROM members WHERE student_id = #{studentId}")
    Member getMemberByStudentId(String studentId);

    @Insert("INSERT INTO members(club_id, name, student_id, role, join_date, contact_info, status) " +
            "VALUES(#{clubId}, #{name}, #{studentId}, #{role}, #{joinDate}, #{contactInfo}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "memberId")
    int insertMember(Member member);

    @Update("UPDATE members SET name=#{name}, role=#{role}, " +
            "contact_info=#{contactInfo}, status=#{status} " +
            "WHERE member_id=#{memberId}")
    int updateMember(Member member);

    @Delete("DELETE FROM members WHERE member_id = #{memberId}")
    int deleteMember(Integer memberId);

    @Select("SELECT COUNT(*) FROM members WHERE club_id = #{clubId} AND status = 'active'")
    int countActiveMembers(Integer clubId);

    @Select({"<script>",
            "SELECT * FROM members",
            "WHERE club_id = #{clubId}",
            "<if test='name != null and name != \"\"'>",
            "   AND name LIKE CONCAT('%', #{name}, '%')",
            "</if>",
            "<if test='studentId != null and studentId != \"\"'>",
            "   AND student_id = #{studentId}",
            "</if>",
            "<if test='role != null and role != \"\"'>",
            "   AND role = #{role}",
            "</if>",
            "<if test='status != null and status != \"\"'>",
            "   AND status = #{status}",
            "</if>",
            "</script>"})
    List<Member> searchMembers(@Param("clubId") Integer clubId, 
                             @Param("name") String name, 
                             @Param("studentId") String studentId, 
                             @Param("role") String role,
                             @Param("status") String status);
}
