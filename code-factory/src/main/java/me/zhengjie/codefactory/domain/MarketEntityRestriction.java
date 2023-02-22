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
* @date 2023-02-22
**/
@Entity
@Data
@Table(name="market_entity_restriction")
public class MarketEntityRestriction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "主键")
    private Long id;

    @Column(name = "`entity_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "实体ID")
    private Long entityId;

    @Column(name = "`entity_other_id`")
    @ApiModelProperty(value = "其他实体ID(如外键)")
    private Long entityOtherId;

    @Column(name = "`name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "约束名称")
    private String name;

    @Column(name = "`comment`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "描述")
    private String comment;

    @Column(name = "`type`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "约束类型")
    private Integer type;

    @Column(name = "`expression`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "约束表达式")
    private String expression;

    public void copy(MarketEntityRestriction source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
