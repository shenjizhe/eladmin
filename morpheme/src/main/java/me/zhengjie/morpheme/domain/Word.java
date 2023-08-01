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
* @date 2023-08-01
**/
@Entity
@Data
@Table(name="word")
public class Word implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "主键")
    private Long id;

    @Column(name = "`text`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "单词文本")
    private String text;

    @Column(name = "`deduction`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "推导过程")
    private String deduction;

    @Column(name = "`nature`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "词性")
    private String nature;

    @Column(name = "`is_derive`")
    @ApiModelProperty(value = "是否是派生词素(0-不是派生词 1-是派生词)")
    private Boolean isDerive;

    @Column(name = "`description`")
    @ApiModelProperty(value = "英语的解释")
    private String description;

    @Column(name = "`phonetic`")
    @ApiModelProperty(value = "音标")
    private String phonetic;

    public void copy(Word source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
