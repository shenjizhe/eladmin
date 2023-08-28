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
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author Jason Shen
* @date 2023-08-28
**/
@Entity
@Data
@Table(name="study_word_statics")
public class StudyWordStatics implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "主键")
    private Long id;

    @Column(name = "`uid`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "用户ID")
    private Long uid;

    @Column(name = "`object_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "学习知识ID")
    private Long objectId;

    @Column(name = "`status`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "状态")
    private Integer status;

    @Column(name = "`study_tims`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "学习次数")
    private Integer studyTims;

    @Column(name = "`remember_times`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "记得次数")
    private Integer rememberTimes;

    @Column(name = "`obscure_times`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "模糊次数")
    private Integer obscureTimes;

    @Column(name = "`forget_times`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "忘记次数")
    private Integer forgetTimes;

    @Column(name = "`last_study_time`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "最后学习日期")
    private Timestamp lastStudyTime;

    @Column(name = "`memery_level`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "记忆等级")
    private Integer memeryLevel;

    @Column(name = "`study_rate`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "学习比例")
    private Integer studyRate;

    @Column(name = "`need_study_totday`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "今天是否需要学")
    private Boolean needStudyTotday;

    public void copy(StudyWordStatics source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
