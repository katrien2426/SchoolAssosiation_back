package com.katrien.service.impl;

import com.katrien.mapper.HonorMapper;
import com.katrien.pojo.Honor;
import com.katrien.service.HonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class HonorServiceImpl implements HonorService {
    
    @Autowired
    private HonorMapper honorMapper;

    @Override
    public List<Honor> getAllHonors() {
        return honorMapper.getAllHonors();
    }

    @Override
    public Honor getHonorById(Integer honorId) {
        return honorMapper.getHonorById(honorId);
    }

    @Override
    @Transactional
    public boolean createHonor(Honor honor) {
        return honorMapper.insertHonor(honor) > 0;
    }

    @Override
    @Transactional
    public boolean updateHonor(Honor honor) {
        return honorMapper.updateHonor(honor) > 0;
    }

    @Override
    @Transactional
    public boolean deleteHonor(Integer honorId) {
        return honorMapper.deleteHonor(honorId) > 0;
    }
}
