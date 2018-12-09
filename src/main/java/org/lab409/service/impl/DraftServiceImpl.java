package org.lab409.service.impl;

import com.github.binarywang.java.emoji.EmojiConverter;
import org.lab409.entity.Draft;
import org.lab409.entity.Sector;
import org.lab409.mapper.DraftMapper;
import org.lab409.mapper.SectorMapper;
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
    @Autowired
    SectorMapper sectorMapper;

    @Override
    public boolean saveDraft(Draft draft) {
        //emoji表情转换器，避免 Emoji 存储出现问题
        EmojiConverter emojiConverter=EmojiConverter.getInstance();
        //将blackboard中的Blackboard_text转换
        draft.setDraft_text(emojiConverter.toHtml(draft.getDraft_text()));
        //该草稿为文章类型
        if(draft.getPublish_type_id()==0){
            draftMapper.saveDraft(draft);
            //将 draft 的标签信息存入 forum_sector_use_draft 表
            for(int i=0;i<draft.getSectorName().size();i++) {
                Integer sector_id=sectorMapper.getSectorIdBySectorName(draft.getSectorName().get(i));
                if(sectorMapper.addAssociationToDraft(draft.getDraft_id(),sector_id)!=1){
                    return false;
                }
            }
            return true;
        }
        //该草稿为黑板报类型
        else{
            if(draftMapper.saveDraft(draft)!=1){
                return false;
            }
         return true;
        }
    }
}
