package org.lab409.service;

import org.lab409.entity.MajorEntity;
import org.lab409.entity.UniversityEntity;

import java.util.List;

public interface UniversityService {
    List<UniversityEntity> getUniversities();
    List<MajorEntity> getMajorByUniversity(int universityID);
}
