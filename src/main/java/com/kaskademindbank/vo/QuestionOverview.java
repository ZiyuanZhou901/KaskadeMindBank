package com.kaskademindbank.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author ZiyuanZhou
 */
public class QuestionOverview {
    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    private String subject;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private LocalDateTime upTime;

    public QuestionOverview(String type, Integer id, String subject, String description, LocalDateTime upTime) {
        this.type = type;
        this.id = id;
        this.subject = subject;
        this.description = description;
        this.upTime = upTime;
    }

}

