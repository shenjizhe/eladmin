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
@Table(name="different_morpheme")
public class DifferentMorpheme implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "主键")
    private Long id;

    @Column(name = "`morpheme_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "词素ID")
    private Long morphemeId;

    @Column(name = "`text`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "异形词素文本(ag)")
    private String text;

    @Column(name = "`nature`")
    @ApiModelProperty(value = "词性(v-动词)")
    private String nature;

    @Column(name = "`source`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "词源")
    private String source;

    @Column(name = "`source_word`")
    @ApiModelProperty(value = "词源单词")
    private String sourceWord;

    @Column(name = "`source_description`")
    @ApiModelProperty(value = "词源说明")
    private String sourceDescription;

    @Column(name = "`meaning_chinese`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "中文含义")
    private String meaningChinese;

    @Column(name = "`meaning_english`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "英文含义")
    private String meaningEnglish;

    public void copy(DifferentMorpheme source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
