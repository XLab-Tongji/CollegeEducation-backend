package org.lab409.service.impl;

import org.lab409.entity.Article;
import org.lab409.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;



/**
 * Created by jiao on 2018/10/16.
 */
@Service
@Transactional
public class ArticleService {
    @Autowired
    ArticleMapper articleMapper;

    public List<Article> getArticleByState(Integer state, Integer page, Integer count,String keywords) {
        int start = (page - 1) * count;
        /* 待解决，目前 userID 默认为 1 */
        // Long uid = getCurrentUser().getId();
        Long uid=1L;
        return articleMapper.getArticleByState(state, start, count, uid,keywords);
    }

    public int getArticleCountByState(Integer state, Long uid,String keywords) {
        return articleMapper.getArticleCountByState(state, uid,keywords);
    }

}

