package org.lab409.util;

import org.lab409.entity.Article;
import org.lab409.entity.ArticleOutput;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by jiao on 2018/11/14
 */
public class Converter {

    //convert Article to ArticleOutput
    public static List<ArticleOutput> convertArticleToArticleOutput(List<Article> articles,int sectorNameCount){
        List<ArticleOutput> articlesOutput=new ArrayList<>();
        for(int i=0;i<articles.size();i++){
            if(articles.isEmpty()){
                break;
            }
            else if(0==i){
                ArticleOutput articleOutput=new ArticleOutput();
                // copy information to articleOutput
                articleOutput.setClickingRate(articles.get(i).getClickingRate());
                articleOutput.setFavorite_count(articles.get(i).getFavorite_count());
                articleOutput.setFavourite_id(articles.get(i).getFavourite_id());
                articleOutput.setPraise_id(articles.get(i).getPraise_id());
                articleOutput.setPraiseCount(articles.get(i).getPraiseCount());
                articleOutput.setPublish_id(articles.get(i).getPublish_id());
                articleOutput.setPublish_type_id(articles.get(i).getPublish_type_id());
                articleOutput.setReplyCount(articles.get(i).getReplyCount());
                articleOutput.setSector_use_id(articles.get(i).getSector_use_id());
                articleOutput.setTopicDate(articles.get(i).getTopicDate());
                articleOutput.setTopicId(articles.get(i).getTopicId());
                articleOutput.setTopicText(articles.get(i).getTopicText());
                articleOutput.setTopicTitle(articles.get(i).getTopicTitle());
                articleOutput.setUserId(articles.get(i).getUserId());
                articleOutput.setSectorState(articles.get(i).getSectorState());
                articleOutput.getSectorName().add(articles.get(i).getSectorName());

                articlesOutput.add(articleOutput);
            }
            else if(i>0){
                if(articlesOutput.get(articlesOutput.size()-1).getTopicId().equals(articles.get(i).getTopicId())){
                    articlesOutput.get(articlesOutput.size()-1).getSectorName().add(articles.get(i).getSectorName());
                }
                else{
                    ArticleOutput articleOutput=new ArticleOutput();
                    // copy information to articleOutput
                    articleOutput.setClickingRate(articles.get(i).getClickingRate());
                    articleOutput.setFavorite_count(articles.get(i).getFavorite_count());
                    articleOutput.setFavourite_id(articles.get(i).getFavourite_id());
                    articleOutput.setPraise_id(articles.get(i).getPraise_id());
                    articleOutput.setPraiseCount(articles.get(i).getPraiseCount());
                    articleOutput.setPublish_id(articles.get(i).getPublish_id());
                    articleOutput.setPublish_type_id(articles.get(i).getPublish_type_id());
                    articleOutput.setReplyCount(articles.get(i).getReplyCount());
                    articleOutput.setSector_use_id(articles.get(i).getSector_use_id());
                    articleOutput.setTopicDate(articles.get(i).getTopicDate());
                    articleOutput.setTopicId(articles.get(i).getTopicId());
                    articleOutput.setTopicText(articles.get(i).getTopicText());
                    articleOutput.setTopicTitle(articles.get(i).getTopicTitle());
                    articleOutput.setUserId(articles.get(i).getUserId());
                    articleOutput.setSectorState(articles.get(i).getSectorState());
                    articleOutput.getSectorName().add(articles.get(i).getSectorName());

                    articlesOutput.add(articleOutput);
                }

            }
        }
        //delete article where the size of sectorName doesn't equal sectorNameCount
        if(sectorNameCount>1){
            for(int i=articlesOutput.size()-1;i>=0;i--){
                if(articlesOutput.get(i).getSectorName().size()!=sectorNameCount){
                    articlesOutput.remove(i);
                }
            }
        }
        return articlesOutput;
    }
}
