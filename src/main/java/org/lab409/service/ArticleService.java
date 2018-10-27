package org.lab409.service;

import org.lab409.entity.Article;

import java.util.List;

public interface ArticleService {
    List<Article> getArticleBySectorAndKeyword(String SectorName,String SectorState,Integer userID,Integer SectorId, Integer page, Integer count,String keywords);
    List<Article> getArticle();
    boolean saveTopic(Article article);
}
