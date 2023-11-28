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
public class JudgeQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "jquestionId", type = IdType.AUTO)
    private Integer jquestionId;

    private String subject;

    private String descripstion;

    private byte[] answer;

    private Integer userId;

    private byte[] wCollect;

    private LocalDateTime upTime;

    private String picFile;

    private String voiFile;

    public Integer getJquestionId() {
        return jquestionId;
    }

    public void setJquestionId(Integer jquestionId) {
        this.jquestionId = jquestionId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescripstion() {
        return descripstion;
    }

    public void setDescripstion(String descripstion) {
        this.descripstion = descripstion;
    }

    public byte[] getAnswer() {
        return answer;
    }

    public void setAnswer(byte[] answer) {
        this.answer = answer;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public byte[] getwCollect() {
        return wCollect;
    }

    public void setwCollect(byte[] wCollect) {
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

    @Override
    public String toString() {
        return "JudgeQuestion{" +
        "jquestionId = " + jquestionId +
        ", subject = " + subject +
        ", descripstion = " + descripstion +
        ", answer = " + answer +
        ", userId = " + userId +
        ", wCollect = " + wCollect +
        ", upTime = " + upTime +
        ", picFile = " + picFile +
        ", voiFile = " + voiFile +
        "}";
    }
}
