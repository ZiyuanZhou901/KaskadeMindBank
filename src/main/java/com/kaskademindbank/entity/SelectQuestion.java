package com.kaskademindbank.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author ZiyuanZhou
 */
public class SelectQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "squestionId", type = IdType.AUTO)
    private Integer squestionId;

    private String subject;

    private String description;

    private String answerA;

    private String answerB;

    private String answerC;

    private String answerD;

    private String answer;

    private Integer userId;

    private Integer wCollect;

    private LocalDateTime upTime;

    private String picFile;

    private String voiFile;
    private String vidFile;

    public Integer getSquestionId() {
        return squestionId;
    }

    public void setSquestionId(Integer squestionId) {
        this.squestionId = squestionId;
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

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
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

    public String getVidFile() {
        return vidFile;
    }

    public void setVidFile(String vidFile) {
        this.vidFile = vidFile;
    }

    public void setVoiFile(String voiFile) {
        this.voiFile = voiFile;
    }

    @Override
    public String toString() {
        return "SelectQuestion{" +
        "squestionId = " + squestionId +
        ", subject = " + subject +
        ", description = " + description +
        ", answerA = " + answerA +
        ", answerB = " + answerB +
        ", answerC = " + answerC +
        ", answerD = " + answerD +
        ", answer = " + answer +
        ", userId = " + userId +
        ", wCollect = " + wCollect +
        ", upTime = " + upTime +
        ", picFile = " + picFile +
        ", voiFile = " + voiFile +
        "}";
    }
}
