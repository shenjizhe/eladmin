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
* @date 2023-04-06
**/
@Entity
@Data
@Table(name="script")
public class Script implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "ID")
    private Long id;

    @Column(name = "`script`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "脚本")
    private String script;

    @Column(name = "`system`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "使用系统")
    private String system;

    @Column(name = "`language`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "语言")
    private String language;

    @Column(name = "`type`")
    @ApiModelProperty(value = "脚本类型")
    private String type;

    @Column(name = "`params`")
    @ApiModelProperty(value = "参数列表")
    private String params;

    public void copy(Script source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
