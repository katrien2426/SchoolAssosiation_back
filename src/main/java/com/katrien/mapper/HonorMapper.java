package com.katrien.mapper;

import com.katrien.pojo.Honor;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface HonorMapper {
    @Select("SELECT h.*, c.club_name FROM club_honors h " +
            "LEFT JOIN clubs c ON h.club_id = c.club_id " +
            "ORDER BY h.award_time DESC")
    List<Honor> getAllHonors();

    @Select("SELECT h.*, c.club_name FROM club_honors h " +
            "LEFT JOIN clubs c ON h.club_id = c.club_id " +
            "WHERE h.honor_id = #{honorId}")
    Honor getHonorById(Integer honorId);

    @Insert("INSERT INTO club_honors(honor_name, club_id, honor_level, award_time, " +
            "issuing_authority, description) " +
            "VALUES(#{honorName}, #{clubId}, #{honorLevel}, #{awardTime}, " +
            "#{issuingAuthority}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "honorId")
    int insertHonor(Honor honor);

    @Update("UPDATE club_honors SET honor_name=#{honorName}, club_id=#{clubId}, " +
            "honor_level=#{honorLevel}, award_time=#{awardTime}, " +
            "issuing_authority=#{issuingAuthority}, description=#{description} " +
            "WHERE honor_id=#{honorId}")
    int updateHonor(Honor honor);

    @Delete("DELETE FROM club_honors WHERE honor_id = #{honorId}")
    int deleteHonor(Integer honorId);
}
