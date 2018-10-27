package org.lab409.service.impl;

import com.github.binarywang.java.emoji.EmojiConverter;
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
        //emoji表情转换器，避免 Emoji 存储出现问题
        EmojiConverter emojiConverter=EmojiConverter.getInstance();
        //将blackboard中的Blackboard_text转换
        draft.setDraft_text(emojiConverter.toHtml(draft.getDraft_text()));
        if(draftMapper.saveDraft(draft)!=1){
            return false;
        }
        return true;
    }
}
