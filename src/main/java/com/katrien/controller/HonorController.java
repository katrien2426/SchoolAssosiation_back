package com.katrien.controller;

import com.katrien.common.Result;
import com.katrien.pojo.Honor;
import com.katrien.service.HonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/club-honors")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true", maxAge = 3600)
public class HonorController {

    @Autowired
    private HonorService honorService;

    @GetMapping
    public Result<List<Honor>> getAllHonors(
            @RequestParam(required = false) String honorLevel,
            @RequestParam(required = false) Integer clubId,
            @RequestParam(required = false) String honorName) {

        List<Honor> honors;
        if (honorLevel != null || clubId != null || honorName != null) {
            honors = honorService.getHonorsByCondition(honorLevel, clubId, honorName);
        } else {
            honors = honorService.getAllHonors();
        }

        return Result.success(honors);
    }

    @GetMapping("/{id}")
    public Result<Honor> getHonorById(@PathVariable("id") Integer honorId) {
        Honor honor = honorService.getHonorById(honorId);
        if (honor != null) {
            return Result.success(honor);
        }
        return Result.error(404, "荣誉不存在");
    }

    @PostMapping
    public Result<Void> createHonor(@RequestBody Honor honor) {
        if (honorService.createHonor(honor)) {
            return Result.success();
        }
        return Result.error("创建荣誉失败");
    }

    @PutMapping
    public Result<Void> updateHonor(@RequestBody Honor honor) {
        if (honor.getHonorId() == null) {
            return Result.error(400, "荣誉ID不能为空");
        }
        if (honorService.updateHonor(honor)) {
            return Result.success();
        }
        return Result.error("更新荣誉失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteHonor(@PathVariable("id") Integer honorId) {
        if (honorService.deleteHonor(honorId)) {
            return Result.success();
        }
        return Result.error("删除荣誉失败");
    }
}