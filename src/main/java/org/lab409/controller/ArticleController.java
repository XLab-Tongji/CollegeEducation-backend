package org.lab409.controller;


import org.lab409.entity.Article;
import org.lab409.entity.Favorite;
import org.lab409.entity.Reply;
import org.lab409.entity.ResponseMessage;
import org.lab409.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



import java.util.*;

/**
 * Created by jiao on 2018/10/16.
 */
@RestController

public class ArticleController {

    @Autowired
    ArticleService articleService;


    //获取文章
    @RequestMapping(path = "article/all", method = RequestMethod.GET)
    public ResponseMessage getArticleBySectorAndKeyword(String SectorName,String SectorState,@RequestParam(value = "userID", defaultValue = "0") Integer userID,@RequestParam(value = "SectorId", defaultValue = "-1") Integer SectorId, @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "count", defaultValue = "6") Integer count, String keywords) {
        List<Article> articles = articleService.getArticleBySectorAndKeyword(SectorName,SectorState,userID,SectorId, page, count, keywords);
        return new ResponseMessage<>(articles).success();
    }

    //测试获取文章的 api
    @RequestMapping(path = "article/all/test", method = RequestMethod.GET)
    public ResponseMessage getArticle() {
        List<Article> articles = articleService.getArticle();
        return new ResponseMessage<>(articles).success();
    }

    //向 forum_topic 表中添加数据
    @RequestMapping(path="article/save",method = RequestMethod.POST)
    public ResponseMessage saveTopic(@RequestBody Article article){
        if(articleService.saveTopic(article)){
            return new ResponseMessage<Article>(null).success();
        }
        return new ResponseMessage<Article>(null).error(202,"can't save");
    }

    //点赞 forum_topic 表中的某条内容
    @RequestMapping(path="article/like",method = RequestMethod.POST)
    public ResponseMessage likeTopic(@RequestBody Article article,@RequestParam(value = "userID",defaultValue = "0")Integer userID){
        // userID 代表点赞人的 ID
        if(articleService.likeTopic(article,userID)){
            return new ResponseMessage<Article>(null).success();
        }
        return new ResponseMessage<Article>(null).error(202,"error");
    }

    //收藏 forum_topic 表中的某条内容
    @RequestMapping(path = "article/collect",method = RequestMethod.POST)
    public ResponseMessage collectTopic(@RequestBody Favorite favorite){
        if(articleService.collectTopic(favorite)){
            return new ResponseMessage<Favorite>(null).success();
        }
        return new ResponseMessage<Favorite>(null).error(202,"error");
    }

    //评论 forum_topic 表中的某条内容
    @RequestMapping(path = "article/reply",method = RequestMethod.POST)
    public ResponseMessage replyTopic(@RequestBody Reply reply){
        if(articleService.replyTopic(reply)){
            return new ResponseMessage<Reply>(null).success();
        }
        return new ResponseMessage<Reply>(null).error(202,"error");
    }
}