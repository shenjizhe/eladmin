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
* @date 2023-09-14
**/
@Entity
@Data
@Table(name="study_affix_static")
public class StudyAffixStatic implements Serializable {

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

    @Column(name = "`study_times`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "学习次数")
    private Integer studyTimes;

    @Column(name = "`simple_times`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "记得次数")
    private Integer simpleTimes;

    @Column(name = "`confuse_times`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "模糊次数")
    private Integer confuseTimes;

    @Column(name = "`forget_times`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "忘记次数")
    private Integer forgetTimes;

    @Column(name = "`last_review_time`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "最后学习日期")
    private Timestamp lastReviewTime;

    @Column(name = "`last_review_result`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "最后一次学习结果")
    private Integer lastReviewResult;

    @Column(name = "`memery_level`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "记忆等级")
    private Integer memeryLevel;

    public void copy(StudyAffixStatic source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
