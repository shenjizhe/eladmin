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
* @date 2023-08-23
**/
@Entity
@Data
@Table(name="study_record_day")
public class StudyRecordDay implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "主键")
    private Long id;

    @Column(name = "`uid`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "用户id")
    private Long uid;

    @Column(name = "`date`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "日期")
    private Timestamp date = null;

    @Column(name = "`object_type`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "学习内容类型（0 - 词根 1- 单词）")
    private Integer objectType;

    @Column(name = "`object_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "学习内容id")
    private Long objectId;

    @Column(name = "`type`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "类型（0-初次看，1-再次看）")
    private Integer type = 0;

    public void copy(StudyRecordDay source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
