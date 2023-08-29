package me.zhengjie.morpheme.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class StudyStaticsBase {

    private Long id;
    private Long uid;
    private Long objectId;
    private Integer forgetTimes;
    private Integer memeryLevel;
    private Integer studyTimes;
    private Integer simpleTimes;
    private Integer confuseTimes;
    private Timestamp lastReviewTime;
    private Integer lastReviewResult;
}
