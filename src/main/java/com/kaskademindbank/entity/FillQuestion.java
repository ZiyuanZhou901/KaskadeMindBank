package com.kaskademindbank.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author ZiyuanZhou
 */
public class FillQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = " fquestionId", type = IdType.AUTO)
    private Integer  fquestionId;

    private String subject;

    private String description;

    private String answer;

    private Integer userId;


    private Integer wCollect;

    private LocalDateTime upTime;

    private String picFile;

    private String voiFile;
    private String vidFile;

    public Integer getFquestionId() {
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

    public Integer getwCollect() {
        return wCollect;
    }

    public void setwCollect(Integer wCollect) {
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

    public String getVoiFile() {
        return voiFile;
    }

    public void setVoiFile(String voiFile) {
        this.voiFile = voiFile;
    }

    public String getVidFile() {
        return vidFile;
    }

    public void setVidFile(String vidFile) {
        this.vidFile = vidFile;
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
        ", voiceFile = " + voiFile +
        "}";
    }
}
