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
* @date 2023-08-22
**/
@Entity
@Data
@Table(name="study_event")
public class StudyEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "主键")
    private Long id;

    @Column(name = "`time`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "操作时间")
    private Timestamp time;

    @Column(name = "`event`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "事件")
    private String event;

    @Column(name = "`content`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "内容")
    private String content;

    @Column(name = "`morphemem_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "词根ID")
    private Long morphememId;

    @Column(name = "`word_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "单词ID")
    private Long wordId;

    @Column(name = "`uid`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "用户ID")
    private Long uid;

    public void copy(StudyEvent source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
