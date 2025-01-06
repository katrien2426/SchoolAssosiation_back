package com.katrien.service;

import com.katrien.pojo.Honor;
import java.util.List;

public interface HonorService {
    List<Honor> getAllHonors();
    Honor getHonorById(Integer honorId);
    boolean createHonor(Honor honor);
    boolean updateHonor(Honor honor);
    boolean deleteHonor(Integer honorId);
    
    List<Honor> getHonorsByCondition(String honorLevel, Integer clubId, String honorName);
}
