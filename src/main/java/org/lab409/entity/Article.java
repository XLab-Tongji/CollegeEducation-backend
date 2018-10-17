package org.lab409.entity;

import java.sql.Timestamp;
import lombok.Data;


/**
 * Created by jiao on 2018/10/16
 */
@Data
public class Article {
    private Integer TopicId;
    private Integer UserId;
    private String TopicTitle;
    private String TopicText;
    private Timestamp TopicDate;
    private Integer ReplyCount;
    private Integer ClickingRate;
    private Integer SectorId;
    private Integer PraiseCount;
    private String SectorName;
    private String SectorState;
}
