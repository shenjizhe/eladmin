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
* @date 2023-02-27
**/
@Entity
@Data
@Table(name="entity_model")
public class EntityModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "主键")
    private Long id;

    @Column(name = "`domain_id`")
    @ApiModelProperty(value = "领域id")
    private Long domainId;

    @Column(name = "`name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "名称")
    private String name;

    @Column(name = "`title`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "标题")
    private String title;

    @Column(name = "`comment`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "描述")
    private String comment;

    @Column(name = "`show`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "是否显示")
    private Boolean show;

    @Column(name = "`group_id`")
    @ApiModelProperty(value = "组id")
    private Long groupId;

    @Column(name = "`type`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "实体类型")
    private Integer type;

    public void copy(EntityModel source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}