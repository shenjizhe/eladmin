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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
* @website https://eladmin.vip
* @description /
* @author Jason Shen
* @date 2023-05-24
**/
@Data
public class WordDetail implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "单词文本")
    private String text;

    @ApiModelProperty(value = "推导过程")
    private String deduction;

    @ApiModelProperty(value = "词性")
    private String nature;

    @ApiModelProperty(value = "是否是派生词素(0-不是派生词 1-是派生词)")
    private Boolean isDerive;

    @ApiModelProperty(value = "相关推导")
    private List<WordDeduction> deductions;

    @ApiModelProperty(value = "相关含义")
    private List<WordMeaning> meanings;

    @ApiModelProperty(value = "索引")
    private int index;

    public void copy(Word source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
