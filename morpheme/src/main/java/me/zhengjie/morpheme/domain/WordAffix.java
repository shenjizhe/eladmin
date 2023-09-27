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
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * @author Jason Shen
 * @website https://eladmin.vip
 * @description /
 * @date 2023-09-08
 **/
@Entity
@Data
@Table(name = "word_affix")
@NoArgsConstructor
public class WordAffix implements Serializable,Meaning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "主键")
    private Long id;

    @Column(name = "`text`", nullable = false)
    @NotBlank
    @ApiModelProperty(value = "词缀文本")
    private String text;

    @Column(name = "`affix`", nullable = false)
    @NotNull
    @ApiModelProperty(value = "类型(1前缀,2后缀)")
    private Integer affix;

    @Column(name = "`meaning_chinese`", nullable = false)
    @NotBlank
    @ApiModelProperty(value = "中文含义")
    private String meaningChinese;

    @Column(name = "`meaning_english`", nullable = false)
    @NotBlank
    @ApiModelProperty(value = "英文含义（to lean）")
    private String meaningEnglish;

    public WordAffix(WordDeduction deduction) {
        text = deduction.getSourceText();
        affix = deduction.getAffix();
        meaningChinese = deduction.getMeaningChinese();
        meaningEnglish = deduction.getMeaningEnglish();
    }

    public void copy(WordAffix source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
