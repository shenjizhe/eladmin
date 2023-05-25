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
@Table(name="word_meaning")
public class WordMeaning implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "主键")
    private Long id;

    @Column(name = "`word_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "单词ID")
    private Long wordId;

    @Column(name = "`morpheme_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "词素ID")
    private Long morphemeId;

    @Column(name = "`nature`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "词性")
    private String nature;

    @Column(name = "`meaning_chinese`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "中文含义（倾斜）")
    private String meaningChinese;

    @Column(name = "`meaning_english`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "英文含义（to lean）")
    private String meaningEnglish;

    @Column(name = "`example_sentence_chinese`")
    @ApiModelProperty(value = "中文例句")
    private String exampleSentenceChinese;

    @Column(name = "`example_sentence_english`")
    @ApiModelProperty(value = "英文例句")
    private String exampleSentenceEnglish;

    public void copy(WordMeaning source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
