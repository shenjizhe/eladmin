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

import lombok.AllArgsConstructor;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.NoArgsConstructor;

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
@Table(name="morpheme")
@NoArgsConstructor
@AllArgsConstructor
public class Morpheme implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "主键")
    private Long id;

    @Column(name = "`dictionary_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "字典ID")
    private Long dictionaryId;

    @Column(name = "`number`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "编号")
    private Integer number;

    @Column(name = "`text`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "词素文本")
    private String text;

    @Column(name = "`type`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "词素类型")
    private Integer type;

    @Column(name = "`source`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "词源")
    private String source;

    @Column(name = "`meaning_chinese`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "中文含义")
    private String meaningChinese;

    @Column(name = "`meaning_english`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "英文含义")
    private String meaningEnglish;

    @Column(name = "`description`")
    @ApiModelProperty(value = "说明")
    private String description;

    public void copy(Morpheme source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
