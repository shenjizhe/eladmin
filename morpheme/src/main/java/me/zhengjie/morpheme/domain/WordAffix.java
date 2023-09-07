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
* @date 2023-09-07
**/
@Entity
@Data
@Table(name="word_affix")
public class WordAffix implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "主键")
    private Long id;

    @Column(name = "`text`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "词缀文本")
    private String text;

    @Column(name = "`affix`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "类型(1前缀,2后缀)")
    private Integer affix;

    @Column(name = "`shape`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "变形类型（0-原型，1-异形,2-省略）")
    private Integer shape;

    @Column(name = "`nature`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "词性")
    private String nature;

    @Column(name = "`is_derive`")
    @ApiModelProperty(value = "是否是派生词素(0-不是派生词 1-是派生词)")
    private Boolean isDerive;

    @Column(name = "`meaning_chinese`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "中文含义")
    private String meaningChinese;

    @Column(name = "`meaning_english`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "英文含义（to lean）")
    private String meaningEnglish;

    public void copy(WordAffix source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
