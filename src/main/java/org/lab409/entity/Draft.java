package org.lab409.entity;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/*
 ** created by jiao on 2018/10/26
 */

@Data
public class Draft {
    private Integer draft_id;           //自增主键
    private Integer state_id=1;         //区分草稿和文章，默认为1
    private Integer publish_type_id;    //发布类型编号,0为文章，1为黑板报
    private Integer user_id;
    private Integer sector_id=-1;          //分类id,默认为 -1 ，当存黑板报类型的草稿时该值才会变化
    private List<String> sectorName;
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

    public Integer getState_id() {
        return state_id;
    }

    public void setState_id(Integer state_id) {
        this.state_id = state_id;
    }

    public Integer getPublish_type_id() {
        return publish_type_id;
    }

    public void setPublish_type_id(Integer publish_type_id) {
        this.publish_type_id = publish_type_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getSector_id() {
        return sector_id;
    }

    public void setSector_id(Integer sector_id) {
        this.sector_id = sector_id;
    }

    public List<String> getSectorName() {
        return sectorName;
    }

    public void setSectorName(List<String> sectorName) {
        this.sectorName = sectorName;
    }

    public String getDraft_name() {
        return draft_name;
    }

    public void setDraft_name(String draft_name) {
        this.draft_name = draft_name;
    }

    public Date getWrite_date() {
        return write_date;
    }

    public void setWrite_date(Date write_date) {
        this.write_date = write_date;
    }

    public Integer getDraft_id() {
        return draft_id;
    }

    public void setDraft_id(Integer draft_id) {
        this.draft_id = draft_id;
    }
}
