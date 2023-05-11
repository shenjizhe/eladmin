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
package me.zhengjie.financial.domain;

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
* @date 2023-05-09
**/
@Entity
@Data
@Table(name="system_factor")
public class SystemFactor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "ID")
    private Integer id;

    @Column(name = "`key`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "系数腱")
    private String key;

    @Column(name = "`value`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "系数值")
    private String value;

    @Column(name = "`type`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "系数类型")
    private String type;

    public void copy(SystemFactor source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
