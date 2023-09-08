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
@Table(name="word_deduction")
public class WordDeduction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "主键")
    private Long id;

    @Column(name = "`morpheme_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "词根ID")
    private Long morphemeId;

    @Column(name = "`word_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "单词ID")
    private Long wordId;

    @Column(name = "`morpheme_text`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "词素文本")
    private String morphemeText;

    @Column(name = "`source_text`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "源词素")
    private String sourceText;

    @Column(name = "`full_text`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "全文本")
    private String fullText;

    @Column(name = "`affix`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "词缀类型")
    private Integer affix;

    @Column(name = "`shape`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "变形类型")
    private Integer shape;

    @Column(name = "`nature`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "词性")
    private String nature;

    @Column(name = "`is_derive`")
    @ApiModelProperty(value = "是否是派生词")
    private Boolean isDerive;

    @Column(name = "`meaning_chinese`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "中文含义")
    private String meaningChinese;

    @Column(name = "`meaning_english`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "英文含义")
    private String meaningEnglish;

    public void copy(WordDeduction source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }

    public String getKey(){
        String s = sourceText + "-" + affix.toString();
        return s;
    }
}
