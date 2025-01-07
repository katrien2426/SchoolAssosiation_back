package com.katrien.mapper;

import com.katrien.pojo.Club;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ClubMapper {
    @Select("SELECT c.*, u.real_name as presidentName, c.president_id as presidentId " +
            "FROM clubs c " +
            "LEFT JOIN users u ON c.president_id = u.user_id " +
            "WHERE c.club_id = #{clubId}")
    Club getClubById(Integer clubId);

    @Select("SELECT c.*, u.real_name as presidentName, c.president_id as presidentId " +
            "FROM clubs c " +
            "LEFT JOIN users u ON c.president_id = u.user_id")
    List<Club> getAllClubs();

    @Insert("INSERT INTO clubs(club_name, description, creation_date, president_id, unit, status) " +
            "VALUES(#{clubName}, #{description}, DATE(#{creationDate}), #{presidentId}, #{unit}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "clubId")
    int insertClub(Club club);

    @Update("UPDATE clubs SET club_name=#{clubName}, description=#{description}, " +
            "creation_date=#{creationDate}, president_id=#{presidentId}, unit=#{unit}, " +
            "status=#{status} WHERE club_id=#{clubId}")
    int updateClub(Club club);

    @Delete("DELETE FROM clubs WHERE club_id = #{clubId}")
    int deleteClub(Integer clubId);

    @Select("SELECT c.*, u.real_name as presidentName, c.president_id as presidentId " +
            "FROM clubs c " +
            "LEFT JOIN users u ON c.president_id = u.user_id " +
            "WHERE c.status = 'active'")
    List<Club> getActiveClubs();

    @Select("SELECT COUNT(*) FROM clubs")
    Integer selectCount(Object o);

    @Select("SELECT c.*, u.real_name as presidentName " +
            "FROM clubs c " +
            "LEFT JOIN users u ON c.president_id = u.user_id " +
            "WHERE c.club_name LIKE CONCAT('%', #{keyword}, '%')")
    List<Club> searchClubs(String keyword);

    @Select("SELECT c.*, u.real_name as presidentName, c.president_id as presidentId " +
            "FROM clubs c " +
            "LEFT JOIN users u ON c.president_id = u.user_id " +
            "WHERE (#{keyword} IS NULL OR c.club_name LIKE CONCAT('%', #{keyword}, '%')) " +
            "AND (#{status} IS NULL OR c.status = #{status})")
    List<Club> searchClubsByCondition(@Param("keyword") String keyword, @Param("status}") String status);
}
