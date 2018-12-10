package org.lab409.service.impl;

import org.lab409.entity.Sector;
import org.lab409.mapper.SectorMapper;
import org.lab409.service.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Created by jiao on 2018/12/9.
 */
@Service
public class SectorServiceImpl implements SectorService {

    @Autowired
    SectorMapper sectorMapper;
    @Override
    public List<Sector> getSectors(){
        return sectorMapper.getSectors();
    }
}
