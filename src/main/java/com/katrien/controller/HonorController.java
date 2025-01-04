package com.katrien.controller;

import com.katrien.common.Result;
import com.katrien.pojo.Honor;
import com.katrien.service.HonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/club-honors")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true", maxAge = 3600)
public class HonorController {
    
    @Autowired
    private HonorService honorService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllHonors() {
        List<Honor> honors = honorService.getAllHonors();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取荣誉列表成功");
        response.put("data", honors);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getHonorById(@PathVariable("id") Integer honorId) {
        Honor honor = honorService.getHonorById(honorId);
        Map<String, Object> response = new HashMap<>();
        if (honor != null) {
            response.put("code", 200);
            response.put("message", "获取荣誉详情成功");
            response.put("data", honor);
            return ResponseEntity.ok(response);
        }
        response.put("code", 404);
        response.put("message", "荣誉不存在");
        return ResponseEntity.status(404).body(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createHonor(@RequestBody Honor honor) {
        Map<String, Object> response = new HashMap<>();
        if (honorService.createHonor(honor)) {
            response.put("code", 200);
            response.put("message", "创建荣誉成功");
            return ResponseEntity.ok(response);
        }
        response.put("code", 500);
        response.put("message", "创建荣誉失败");
        return ResponseEntity.status(500).body(response);
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> updateHonor(@RequestBody Honor honor) {
        Map<String, Object> response = new HashMap<>();
        if (honor.getHonorId() == null) {
            response.put("code", 400);
            response.put("message", "荣誉ID不能为空");
            return ResponseEntity.badRequest().body(response);
        }
        if (honorService.updateHonor(honor)) {
            response.put("code", 200);
            response.put("message", "更新荣誉成功");
            return ResponseEntity.ok(response);
        }
        response.put("code", 500);
        response.put("message", "更新荣誉失败");
        return ResponseEntity.status(500).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteHonor(@PathVariable("id") Integer honorId) {
        Map<String, Object> response = new HashMap<>();
        if (honorService.deleteHonor(honorId)) {
            response.put("code", 200);
            response.put("message", "删除荣誉成功");
            return ResponseEntity.ok(response);
        }
        response.put("code", 500);
        response.put("message", "删除荣誉失败");
        return ResponseEntity.status(500).body(response);
    }
}
