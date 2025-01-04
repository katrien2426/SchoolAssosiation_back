package com.katrien.service;

import com.katrien.pojo.Club;
import java.util.List;

public interface ClubService {
    // 查询所有社团
    List<Club> getAllClubs();
    
    // 根据ID查询社团
    Club getClubById(Integer clubId);
    
    // 创建社团
    int createClub(Club club);
    
    // 更新社团信息
    int updateClub(Club club);
    
    // 删除社团
    int deleteClub(Integer clubId);
    
    // 获取活跃的社团
    List<Club> getActiveClubs();
    
    // 获取社团总数
    Integer getClubCount();
}
