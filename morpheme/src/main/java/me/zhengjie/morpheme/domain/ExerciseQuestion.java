/*
*  Copyright 2019-2020 Jason Shen
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.morpheme.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author Jason Shen
* @date 2023-05-24
**/
@Entity
@Data
@Table(name="exercise_question")
public class ExerciseQuestion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "主键")
    private Long id;

    @Column(name = "`morpheme_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "词素ID")
    private Long morphemeId;

    @Column(name = "`number`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "编号")
    private Integer number;

    @Column(name = "`type`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "题目类型")
    private Integer type;

    @Column(name = "`question_stem`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "题干")
    private String questionStem;

    @Column(name = "`content`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "题目内容")
    private String content;

    @Column(name = "`options`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "选项")
    private String options;

    public void copy(ExerciseQuestion source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
