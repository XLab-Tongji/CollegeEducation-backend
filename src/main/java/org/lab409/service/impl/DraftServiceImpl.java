package org.lab409.service.impl;

import org.lab409.entity.Draft;
import org.lab409.mapper.DraftMapper;
import org.lab409.service.DraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 ** created by jiao on 2018/10/26
 */
@Service
public class DraftServiceImpl implements DraftService {

    @Autowired
    DraftMapper draftMapper;

    @Override
    public boolean saveDraft(Draft draft) {
        if(draftMapper.saveDraft(draft)!=1){
            return false;
        }
        return true;
    }
}
