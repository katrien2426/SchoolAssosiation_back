package com.katrien.mapper;

import com.katrien.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE user_id = #{userId}")
    User getUserById(Integer userId);

    @Select("SELECT * FROM users WHERE username = #{username}")
    User getUserByUsername(String username);

    @Select("SELECT * FROM users")
    List<User> getAllUsers();

    @Insert("INSERT INTO users(username, password_hash, real_name, email, phone, role, club_id, created_at) " +
            "VALUES(#{username}, #{passwordHash}, #{realName}, #{email}, #{phone}, #{role}, #{clubId}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int createUser(User user);

    @Update("UPDATE users SET username=#{username}, real_name=#{realName}, email=#{email}, phone=#{phone}, role=#{role}, club_id=#{clubId} WHERE user_id=#{userId}")
    int updateUser(User user);

    @Update("UPDATE users SET password_hash=#{passwordHash} WHERE user_id=#{userId}")
    int updatePassword(@Param("userId") Integer userId, @Param("passwordHash") String passwordHash);

    @Delete("DELETE FROM users WHERE user_id = #{userId}")
    int deleteUser(Integer userId);

    @Select("SELECT COUNT(*) FROM users")
    Integer selectCount(Object o);

    @Select("SELECT * FROM users WHERE club_id = #{clubId} AND role = 'club_president' LIMIT 1")
    User getClubPresident(Integer clubId);

    @Select({
        "<script>",
        "SELECT * FROM users",
        "<where>",
        "    <if test='keyword != null and keyword != \"\"'>",
        "        AND (username LIKE CONCAT('%',#{keyword},'%') OR real_name LIKE CONCAT('%',#{keyword},'%'))",
        "    </if>",
        "    <if test='role != null and role != \"\"'>",
        "        AND role = #{role}",
        "    </if>",
        "</where>",
        "</script>"
    })
    List<User> searchUsers(@Param("keyword") String keyword, @Param("role") String role);
}
