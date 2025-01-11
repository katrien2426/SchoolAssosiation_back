package com.katrien.service.impl;

import com.katrien.mapper.ClubMapper;
import com.katrien.pojo.Club;
import com.katrien.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClubServiceImpl implements ClubService {

    @Autowired
    private ClubMapper clubMapper;

    @Override
    public List<Club> getAllClubs() {
        return clubMapper.getAllClubs();
    }

    @Override
    public Club getClubById(Integer clubId) {
        return clubMapper.getClubById(clubId);
    }

    @Override
    public int createClub(Club club) {
        return clubMapper.insertClub(club);
    }

    @Override
    public int updateClub(Club club) {
        return clubMapper.updateClub(club);
    }

    @Override
    public int deleteClub(Integer clubId) {
        return clubMapper.deleteClub(clubId);
    }

    @Override
    public Integer getClubCount() {
        return clubMapper.selectCount(null);
    }

    @Override
    public List<Club> searchClubsByCondition(String keyword, String status) {
        return clubMapper.searchClubsByCondition(keyword, status);
    }

    @Override
    public List<Club> getClubsWithoutPresident() {
        return clubMapper.getClubsWithoutPresident();
    }
}
