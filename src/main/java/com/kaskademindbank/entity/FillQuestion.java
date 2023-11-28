package com.kaskademindbank.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author ZiyuanZhou
 * @since 2023-11-28
 */
public class FillQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = " fquestionId", type = IdType.AUTO)
    private Integer  fquestionId;

    private String subject;

    private String description;

    private String answer;

    private Integer userId;

    private String wCollect;

    private LocalDateTime upTime;

    private String picFile;

    private String voiceFile;

    public Integer getfquestionId() {
        return  fquestionId;
    }

    public void setfquestionId(Integer  fquestionId) {
        this. fquestionId =  fquestionId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getwCollect() {
        return wCollect;
    }

    public void setwCollect(String wCollect) {
        this.wCollect = wCollect;
    }

    public LocalDateTime getUpTime() {
        return upTime;
    }

    public void setUpTime(LocalDateTime upTime) {
        this.upTime = upTime;
    }

    public String getPicFile() {
        return picFile;
    }

    public void setPicFile(String picFile) {
        this.picFile = picFile;
    }

    public String getVoiceFile() {
        return voiceFile;
    }

    public void setVoiceFile(String voiceFile) {
        this.voiceFile = voiceFile;
    }

    @Override
    public String toString() {
        return "FillQuestion{" +
        " fquestionId = " +  fquestionId +
        ", subject = " + subject +
        ", description = " + description +
        ", answer = " + answer +
        ", userId = " + userId +
        ", wCollect = " + wCollect +
        ", upTime = " + upTime +
        ", picFile = " + picFile +
        ", voiceFile = " + voiceFile +
        "}";
    }
}