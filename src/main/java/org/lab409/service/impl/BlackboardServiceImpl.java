package org.lab409.service.impl;

import org.lab409.entity.Blackboard;
import org.lab409.mapper.BlackboardMapper;
import org.lab409.service.BlackboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/*
 ** created by jiao on 2018/10/26
 */
@Service
public class BlackboardServiceImpl implements BlackboardService {

    @Autowired
    BlackboardMapper blackboardMapper;

    @Override
    public boolean saveBlackboard(Blackboard blackboard){
        if(blackboardMapper.saveBlackboard(blackboard)!=1)
            return false;
        return true;
    }
}
