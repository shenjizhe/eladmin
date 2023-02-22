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
* @date 2023-02-15
**/
@Entity
@Data
@Table(name="entity_group")
public class EntityGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "小组主键")
    private Long id;

    @Column(name = "`domain_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "域ID")
    private Long domainId;

    @Column(name = "`group_name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "组名称")
    private String groupName;

    @Column(name = "`group_info`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "组描述")
    private String groupInfo;

    @Column(name = "`group_backcolor`")
    @ApiModelProperty(value = "组标签颜色")
    private Integer groupBackcolor;

    @Column(name = "`group_image`")
    @ApiModelProperty(value = "组图标")
    private String groupImage;

    public void copy(EntityGroup source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
