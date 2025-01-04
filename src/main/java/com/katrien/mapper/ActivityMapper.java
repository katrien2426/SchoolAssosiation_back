package com.katrien.mapper;

import com.katrien.pojo.Activity;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ActivityMapper {
    @Select({
        "<script>",
        "SELECT a.*, c.club_name, u.username as creator_name ",
        "FROM activities a ",
        "LEFT JOIN clubs c ON a.club_id = c.club_id ",
        "LEFT JOIN users u ON a.created_by = u.user_id ",
        "WHERE 1=1",
        "<if test='activityName != null and activityName != \"\"'>",
        "  AND a.activity_name LIKE CONCAT('%', #{activityName}, '%')",
        "</if>",
        "<if test='status != null and status != \"\"'>",
        "  AND (",
        "    CASE",
        "      WHEN #{status} = 'ongoing' THEN",
        "        a.start_date &lt;= NOW() AND a.end_date > NOW() AND a.status = 'approved'",
        "      WHEN #{status} = 'completed' THEN",
        "        a.end_date &lt;= NOW() AND a.status = 'approved'",
        "      ELSE a.status = #{status}",
        "    END",
        "  )",
        "</if>",
        "ORDER BY a.start_date DESC",
        "</script>"
    })
    List<Activity> getAllActivities(@Param("activityName") String activityName, @Param("status") String status);

    @Select("SELECT a.*, c.club_name, u.username as creator_name " +
            "FROM activities a " +
            "LEFT JOIN clubs c ON a.club_id = c.club_id " +
            "LEFT JOIN users u ON a.created_by = u.user_id " +
            "WHERE a.activity_id = #{activityId}")
    Activity getActivityById(Integer activityId);

    @Select("SELECT a.*, c.club_name, u.username as creator_name " +
            "FROM activities a " +
            "LEFT JOIN clubs c ON a.club_id = c.club_id " +
            "LEFT JOIN users u ON a.created_by = u.user_id " +
            "WHERE a.club_id = #{clubId} " +
            "ORDER BY a.start_date DESC")
    List<Activity> getActivitiesByClubId(Integer clubId);

    @Select("SELECT a.*, c.club_name, u.username as creator_name " +
            "FROM activities a " +
            "LEFT JOIN clubs c ON a.club_id = c.club_id " +
            "LEFT JOIN users u ON a.created_by = u.user_id " +
            "WHERE a.created_by = #{advisorId} " +
            "ORDER BY a.start_date DESC")
    List<Activity> getActivitiesByAdvisorId(Integer advisorId);

    @Insert("INSERT INTO activities(club_id, activity_name, description, start_date, end_date, " +
            "location, max_participants, status, created_by) " +
            "VALUES(#{clubId}, #{activityName}, #{description}, #{startDate}, #{endDate}, " +
            "#{location}, #{maxParticipants}, #{status}, #{createdBy})")
    @Options(useGeneratedKeys = true, keyProperty = "activityId")
    int insertActivity(Activity activity);

    @Update("UPDATE activities SET activity_name=#{activityName}, description=#{description}, " +
            "start_date=#{startDate}, end_date=#{endDate}, location=#{location}, " +
            "max_participants=#{maxParticipants}, status=#{status} " +
            "WHERE activity_id=#{activityId}")
    int updateActivity(Activity activity);

    @Delete("DELETE FROM activities WHERE activity_id = #{activityId}")
    int deleteActivity(Integer activityId);

    @Update("UPDATE activities SET status=#{status} WHERE activity_id=#{id}")
    int updateActivityStatus(@Param("id") Integer id, 
                           @Param("status") String status);

    @Update("UPDATE activities SET status = 'ongoing' " +
            "WHERE status = 'approved' " +
            "AND start_date <= NOW() " +
            "AND end_date > NOW()")
    int updateOngoingActivities();

    @Update("UPDATE activities SET status = 'completed' " +
            "WHERE (status = 'approved' OR status = 'ongoing') " +
            "AND end_date <= NOW()")
    int updateCompletedActivities();

    @Select("SELECT COUNT(*) FROM activities")
    Integer selectCount(Object o);
}