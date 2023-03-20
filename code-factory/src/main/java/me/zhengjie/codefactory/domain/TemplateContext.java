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
* @date 2023-03-20
**/
@Entity
@Data
@Table(name="template_context")
public class TemplateContext implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "主键")
    private Long id;

    @Column(name = "`template_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "模板主键")
    private Long templateId;

    @Column(name = "`key`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "上下文关键字")
    private String key;

    @Column(name = "`content`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "说明")
    private String content;

    @Column(name = "`type`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "数据类型")
    private Integer type;

    @Column(name = "`data_type`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "数据类型")
    private String dataType;

    public void copy(TemplateContext source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
