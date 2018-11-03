package org.lab409.entity;
import java.util.Date;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/*
 ** created by jiao on 2018/10/26
 */

@Data
public class Draft {
    private Integer state_id=1;         //区分草稿和文章，默认为1
    private Integer publish_type_id;    //发布类型编号,0为文章，1为黑板报
    private Integer user_id;
    private Integer sector_id;          //分类id
    private String draft_name;
    private String draft_text;
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date write_date;

    public String getDraft_text() {
        return draft_text;
    }

    public void setDraft_text(String draft_text) {
        this.draft_text = draft_text;
    }
}
