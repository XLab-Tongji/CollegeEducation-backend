package org.lab409.controller;


import org.lab409.entity.*;
import org.lab409.service.ArticleService;
import org.lab409.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import javax.xml.ws.Response;
import java.util.*;

/**
 * Created by jiao on 2018/10/16.
 */
@RestController

public class ArticleController {

    @Autowired
    ArticleService articleService;
    @Autowired
    EmailService emailService;

    //获取文章
    //userID 当前用户ID
    //SectorId 用来区分搜索的方式，0 为全部，1 为按标题，2 为按标签，默认值是0
    //keywords 用来按标题搜索
    //SectorName 用来按标签搜索，用 String[] 接收(暂定)
    //SectorState 范围较大的标签，暂时没有使用
    @RequestMapping(path = "article/all", method = RequestMethod.GET)
    public ResponseMessage getArticleBySectorAndKeyword(@RequestParam(value = "SectorName",required = false) String[] SectorName,String SectorState,@RequestParam(value = "userID", defaultValue = "0") Integer userID,@RequestParam(value = "SectorId", defaultValue = "0") Integer SectorId, @RequestParam(value = "keywords",required = false)String keywords) {
        List<ArticleOutput> articlesOutput = articleService.getArticleBySectorAndKeyword(SectorName,SectorState,userID,SectorId,keywords);
        return new ResponseMessage<>(articlesOutput).success();
    }

    //测试获取文章的 api
    @RequestMapping(path = "article/all/test", method = RequestMethod.GET)
    public ResponseMessage getArticle() {
        List<Article> articles = articleService.getArticle();
        return new ResponseMessage<>(articles).success();
    }

    //向 forum_topic 表中添加数据
    @Transactional
    @RequestMapping(path="article/save",method = RequestMethod.POST)
    public ResponseMessage saveTopic(@RequestBody ArticleOutput articleoutput){
        if(articleService.saveTopic(articleoutput)){
            return new ResponseMessage<Article>(null).success();
        }
        return new ResponseMessage<Article>(null).error(202,"can't save");
    }

    //点赞 forum_topic 表中的某条内容
    @Transactional
    @RequestMapping(path="article/like",method = RequestMethod.POST)
    public ResponseMessage likeTopic(@RequestBody ArticleOutput articleOutput,@RequestParam(value = "userID",defaultValue = "0")Integer userID){
        // userID 代表点赞人的 ID
        if(articleService.likeTopic(articleOutput,userID)){
            return new ResponseMessage<Article>(null).success();
        }
        return new ResponseMessage<Article>(null).error(202,"error");
    }

    //取消点赞
    @Transactional
    @RequestMapping(path = "article/like/delete",method = RequestMethod.POST)
    public ResponseMessage deletePraise(@RequestBody ArticleOutput articleOutput,@RequestParam(value = "userID",defaultValue = "0")Integer userID){
        // userID 代表点赞人的 ID
        if(articleService.deletePraise(articleOutput,userID)){
            return new ResponseMessage<Article>(null).success();
        }
        return new ResponseMessage<Article>(null).error(202,"error");
    }

    //收藏 forum_topic 表中的某条内容
    @Transactional
    @RequestMapping(path = "article/collect",method = RequestMethod.POST)
    public ResponseMessage collectTopic(@RequestBody Favorite favorite){
        if(articleService.collectTopic(favorite)){
            return new ResponseMessage<Favorite>(null).success();
        }
        return new ResponseMessage<Favorite>(null).error(202,"error");
    }

    //delete collection
    @Transactional
    @RequestMapping(path = "article/collect/delete",method = RequestMethod.POST)
    public ResponseMessage deleteCollection(@RequestBody Favorite favorite){
        if(articleService.deleteCollection(favorite)){
            return new ResponseMessage<Favorite>(null).success();
        }
        return new ResponseMessage<Favorite>(null).error(202,"error");
    }


    //评论 forum_topic 表中的某条内容
    @Transactional
    @RequestMapping(path = "article/reply",method = RequestMethod.POST)
    public ResponseMessage replyTopic(@RequestBody Reply reply){
        if(articleService.replyTopic(reply)){
            return new ResponseMessage<Reply>(null).success();
        }
        return new ResponseMessage<Reply>(null).error(202,"error");
    }

    //获取某篇文章的所有评论
    @RequestMapping(path = "article/reply/get",method = RequestMethod.GET)
    public ResponseMessage getReply(@RequestParam(value = "TopicId") Integer TopicId,@RequestParam(value = "type",defaultValue = "0")Integer type){
        List<Reply> replies=articleService.getReply(TopicId,type);
        return new ResponseMessage<>(replies).success();
    }

    //browse an article(add ClickingRate)
    @RequestMapping(path = "article/browse",method = RequestMethod.POST)
    public ResponseMessage browseTopic(@RequestBody ArticleOutput articleOutput){
        if(articleService.browseTopic(articleOutput)){
            return new ResponseMessage<ArticleOutput>(null).success();
        }
        return new ResponseMessage<ArticleOutput>(null).error(202,"fail to increase ClickintRate");
    }

    //update url of user image
    @RequestMapping(path = "user/image/update",method = RequestMethod.POST)
    public ResponseMessage updateUrlOfUserImage(@RequestParam(value = "image")String image,@RequestParam(value = "userID")Integer userID){
        if(articleService.updateUrlOfUserImage(image,userID)){
            return new ResponseMessage<>(null).success();
        }
        return new ResponseMessage<>(null).error(202,"update failed");
    }

    //get url of user image
    @RequestMapping(path = "user/image/get",method = RequestMethod.GET)
    public ResponseMessage getUrlOfUserImage(@RequestParam(value = "userID")Integer userID){
        String url=articleService.getUrlOfUserImage(userID);
        return new ResponseMessage<>(url).success();
    }

    //send email
    @RequestMapping(path = "email",method = RequestMethod.POST)
    public ResponseMessage sendEmail(@RequestParam(value ="receiver")String receiver){
        emailService.sendSimpleEmail(receiver);
        return new ResponseMessage<>(null).success();
    }
}