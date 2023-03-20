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
package me.zhengjie.codefactory.domain;

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
* @date 2023-03-13
**/
@Entity
@Data
@Table(name="template_block")
public class TemplateBlock implements Serializable {

    @Id
    @Column(name = "`id`")
    @ApiModelProperty(value = "主键")
    private Long id;

    @Column(name = "`name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "名称")
    private String name;

    @Column(name = "`comment`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "描述")
    private String comment;

    @Column(name = "`show`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "是否显示")
    private Boolean show;

    @Column(name = "`template_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "模板ID")
    private Long templateId;

    @Column(name = "`code`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "代码")
    private String code;

    @Column(name = "`level`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "级别")
    private Integer level;

    public void copy(TemplateBlock source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
