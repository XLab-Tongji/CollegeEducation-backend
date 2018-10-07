package org.lab409.service.impl;

import org.lab409.entity.MajorEntity;
import org.lab409.entity.UniversityEntity;
import org.lab409.mapper.UniversityMapper;
import org.lab409.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UniversityServiceImpl implements UniversityService {

    @Autowired
    UniversityMapper universityMapper;

    @Override
    public List<UniversityEntity> getUniversities() {
        return universityMapper.getUniversities();
    }

    @Override
    public List<MajorEntity> getMajorByUniversity(int universityID) {
        return universityMapper.getMajorByUniversity(universityID);
    }
}
