package org.lab409.service.impl;

import com.github.binarywang.java.emoji.EmojiConverter;
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
        //emoji表情转换器，避免 Emoji 存储出现问题
        EmojiConverter emojiConverter=EmojiConverter.getInstance();
        //将blackboard中的Blackboard_text转换
        blackboard.setBlackboard_text(emojiConverter.toHtml(blackboard.getBlackboard_text()));
        if(blackboardMapper.saveBlackboard(blackboard)!=1)
            return false;
        return true;
    }
}
