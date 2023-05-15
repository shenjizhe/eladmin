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
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author Jason Shen
* @date 2023-05-15
**/
@Entity
@Data
@Table(name="stock")
public class Stock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "股票ID")
    private Integer id;

    @Column(name = "`code`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "股票代码")
    private String code;

    @Column(name = "`name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "股票名称")
    private String name;

    @Column(name = "`stage`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "股票阶段")
    private Integer stage;

    @Column(name = "`role`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "股票角色")
    private Integer role;

    @Column(name = "`cycle_big`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "大周期")
    private Integer cycleBig;

    @Column(name = "`cycle_small`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "小周期")
    private Integer cycleSmall;

    @Column(name = "`industry`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "股票行业")
    private Integer industry;

    @Column(name = "`list_date`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "上市日期")
    private Timestamp listDate;

    public void copy(Stock source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
