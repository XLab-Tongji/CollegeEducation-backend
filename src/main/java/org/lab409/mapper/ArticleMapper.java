package org.lab409.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.lab409.entity.Article;

import java.util.List;

/**
 * Created by jiao on 2018/10/16.
 */
@Mapper
public interface ArticleMapper {
    int addNewArticle(Article article);

    int updateArticle(Article article);

    List<Article> getArticleByState(@Param("state") Integer state, @Param("start") Integer start, @Param("count") Integer count, @Param("uid") Long uid,@Param("keywords") String keywords);

    int getArticleCountByState(@Param("state") Integer state, @Param("uid") Long uid, @Param("keywords") String keywords);

    int updateArticleState(@Param("aids") Long aids[], @Param("state") Integer state);

    int deleteArticleById(@Param("aids") Long[] aids);

    Article getArticleById(Long aid);

    void pvIncrement(Long aid);

    void pvStatisticsPerDay();

    List<String> getCategories(Long uid);

    List<Integer> getDataStatistics(Long uid);
}
