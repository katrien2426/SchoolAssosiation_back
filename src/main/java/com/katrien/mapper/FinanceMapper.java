package com.katrien.mapper;

import com.katrien.pojo.Finance;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * @author : Katrien
 * @description :财务Mapper
 */
@Mapper
public interface FinanceMapper {
    @Select("SELECT f.*, c.club_name FROM finances f " +
            "LEFT JOIN clubs c ON f.club_id = c.club_id " +
            "WHERE f.record_id = #{recordId}")
    @Results({
        @Result(property = "recordId", column = "record_id"),
        @Result(property = "clubId", column = "club_id"),
        @Result(property = "transactionDate", column = "transaction_date"),
        @Result(property = "recordedBy", column = "recorded_by"),
        @Result(property = "clubName", column = "club_name")
    })
    Finance getFinanceById(Integer recordId);

    @Select({
        "<script>",
        "SELECT f.*, c.club_name FROM finances f",
        "LEFT JOIN clubs c ON f.club_id = c.club_id",
        "WHERE 1=1",
        "<if test='clubId != null'>",
        "  AND f.club_id = #{clubId}",
        "</if>",
        "<if test='type != null and type != \"\"'>",
        "  AND LOWER(f.type) = LOWER(#{type})",
        "</if>",
        "<if test='startDate != null and startDate != \"\"'>",
        "  AND DATE(f.transaction_date) >= DATE(#{startDate})",
        "</if>",
        "<if test='endDate != null and endDate != \"\"'>",
        "  AND DATE(f.transaction_date) &lt;= DATE(#{endDate})",
        "</if>",
        "ORDER BY f.transaction_date DESC",
        "</script>"
    })
    @Results({
        @Result(property = "recordId", column = "record_id"),
        @Result(property = "clubId", column = "club_id"),
        @Result(property = "transactionDate", column = "transaction_date"),
        @Result(property = "recordedBy", column = "recorded_by"),
        @Result(property = "clubName", column = "club_name")
    })
    List<Finance> getFinancesByClubId(
        @Param("clubId") Integer clubId, 
        @Param("type") String type,
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
    );

    @Insert("INSERT INTO finances(club_id, amount, type, description, transaction_date, recorded_by) " +
            "VALUES(#{clubId}, #{amount}, #{type}, #{description}, #{transactionDate}, #{recordedBy})")
    @Options(useGeneratedKeys = true, keyProperty = "recordId")
    int insertFinance(Finance finance);

    @Update("UPDATE finances SET amount=#{amount}, type=#{type}, " +
            "description=#{description}, transaction_date=#{transactionDate} " +
            "WHERE record_id=#{recordId}")
    int updateFinance(Finance finance);

    @Delete("DELETE FROM finances WHERE record_id = #{recordId}")
    int deleteFinance(Integer recordId);

    @Select({
        "<script>",
        "SELECT COALESCE(SUM(amount), 0) FROM finances",
        "WHERE LOWER(type) = 'income'",
        "<if test='clubId != null'>",
        "  AND club_id = #{clubId}",
        "</if>",
        "<if test='startDate != null and startDate != \"\"'>",
        "  AND DATE(transaction_date) >= DATE(#{startDate})",
        "</if>",
        "<if test='endDate != null and endDate != \"\"'>",
        "  AND DATE(transaction_date) &lt;= DATE(#{endDate})",
        "</if>",
        "</script>"
    })
    Double getTotalIncome(
        @Param("clubId") Integer clubId,
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
    );

    @Select({
        "<script>",
        "SELECT COALESCE(SUM(amount), 0) FROM finances",
        "WHERE LOWER(type) = 'expense'",
        "<if test='clubId != null'>",
        "  AND club_id = #{clubId}",
        "</if>",
        "<if test='startDate != null and startDate != \"\"'>",
        "  AND DATE(transaction_date) >= DATE(#{startDate})",
        "</if>",
        "<if test='endDate != null and endDate != \"\"'>",
        "  AND DATE(transaction_date) &lt;= DATE(#{endDate})",
        "</if>",
        "</script>"
    })
    Double getTotalExpense(
        @Param("clubId") Integer clubId,
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
    );
}
