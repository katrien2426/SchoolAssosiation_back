package com.katrien.service;

import com.katrien.pojo.Club;
import java.util.List;

public interface ClubService {
    List<Club> getAllClubs();
    Club getClubById(Integer clubId);
    int createClub(Club club);
    int updateClub(Club club);
    int deleteClub(Integer clubId);
    Integer getClubCount();
    List<Club> searchClubsByCondition(String keyword, String status);
}
