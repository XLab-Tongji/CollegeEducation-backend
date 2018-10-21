package org.lab409.controller;


import org.lab409.entity.Article;
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



    @RequestMapping(path = "article/all", method = RequestMethod.GET)
    public ResponseMessage getArticleBySectorAndKeyword(String SectorName,String SectorState,@RequestParam(value = "userID", defaultValue = "0") Integer userID,@RequestParam(value = "SectorId", defaultValue = "-1") Integer SectorId, @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "count", defaultValue = "6") Integer count, String keywords) {
        List<Article> articles = articleService.getArticleBySectorAndKeyword(SectorName,SectorState,userID,SectorId, page, count, keywords);
        return new ResponseMessage<>(articles).success();
    }
    @RequestMapping(path = "article/all/test", method = RequestMethod.GET)
    public ResponseMessage getArticle() {
        List<Article> articles = articleService.getArticle();
        return new ResponseMessage<>(articles).success();
    }
}