package com.katrien.controller;

import com.katrien.common.R;
import com.katrien.pojo.Club;
import com.katrien.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clubs")
public class ClubController {

    @Autowired
    private ClubService clubService;

    @GetMapping
    public R<List<Club>> getAllClubs() {
        List<Club> clubs = clubService.getAllClubs();
        return R.success(clubs);
    }

    @GetMapping("/{id}")
    public R<Club> getClubById(@PathVariable("id") Integer clubId) {
        Club club = clubService.getClubById(clubId);
        return club != null ? R.success(club) : R.error("社团不存在");
    }

    @PostMapping
    public R<Club> createClub(@RequestBody Club club) {
        int result = clubService.createClub(club);
        return result > 0 ? R.success(club) : R.error("创建社团失败");
    }

    @PutMapping("/{id}")
    public R<String> updateClub(@PathVariable("id") Integer clubId, @RequestBody Club club) {
        club.setClubId(clubId);
        int result = clubService.updateClub(club);
        return result > 0 ? R.success("更新成功") : R.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public R<String> deleteClub(@PathVariable("id") Integer clubId) {
        int result = clubService.deleteClub(clubId);
        return result > 0 ? R.success("删除成功") : R.error("删除失败");
    }

    @GetMapping("/active")
    public R<List<Club>> getActiveClubs() {
        List<Club> activeClubs = clubService.getActiveClubs();
        return R.success(activeClubs);
    }

    @GetMapping("/count")
    public R<Integer> getClubCount() {
        Integer count = clubService.getClubCount();
        return R.success(count);
    }
}
