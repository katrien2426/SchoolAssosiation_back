package com.katrien.controller;

import com.katrien.common.Result;
import com.katrien.pojo.Club;
import com.katrien.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author : Katrien
 * @description : 社团控制器
 */
@RestController
@RequestMapping("/api/clubs")
public class ClubController {

    @Autowired
    private ClubService clubService;

    @GetMapping
    public Result<List<Club>> getAllClubs() {
        List<Club> clubs = clubService.getAllClubs();
        return Result.success(clubs);
    }

    @GetMapping("/{id}")
    public Result<Club> getClubById(@PathVariable("id") Integer clubId) {
        Club club = clubService.getClubById(clubId);
        return club != null ? Result.success(club) : Result.error("社团不存在");
    }

    @PostMapping
    public Result<Club> createClub(@RequestBody Club club) {
        int result = clubService.createClub(club);
        return result > 0 ? Result.success(club) : Result.error("创建社团失败");
    }

    @PutMapping("/{id}")
    public Result<String> updateClub(@PathVariable("id") Integer clubId, @RequestBody Club club) {
        club.setClubId(clubId);
        int result = clubService.updateClub(club);
        return result > 0 ? Result.success("更新成功") : Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteClub(@PathVariable("id") Integer clubId) {
        int result = clubService.deleteClub(clubId);
        return result > 0 ? Result.success("删除成功") : Result.error("删除失败");
    }

    @GetMapping("/count")
    public Result<Integer> getClubCount() {
        Integer count = clubService.getClubCount();
        return Result.success(count);
    }

    @GetMapping("/search")
    public Result<List<Club>> searchClubs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        List<Club> clubs = clubService.searchClubsByCondition(keyword, status);
        return Result.success(clubs);
    }
}
