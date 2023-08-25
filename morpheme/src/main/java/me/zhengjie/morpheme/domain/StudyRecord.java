package me.zhengjie.morpheme.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class StudyRecord implements Serializable {
    @ApiModelProperty(value = "学习记录日期")
    private LocalDate date;
    @ApiModelProperty(value = "所学词根")
    private List<Morpheme> morphemes;
    @ApiModelProperty(value = "新学单词")
    private List<WordDetail> words;
}
