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
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author Jason Shen
* @date 2023-05-11
**/
@Entity
@Data
@Table(name="stock_data")
public class StockData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "ID")
    private Integer id;

    @Column(name = "`stock_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "股票ID")
    private Integer stockId;

    @Column(name = "`market_value`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "总市值")
    private BigDecimal marketValue;

    @Column(name = "`market_value_circle`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "流通市值")
    private BigDecimal marketValueCircle;

    @Column(name = "`average_price`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "均价")
    private BigDecimal averagePrice;

    @Column(name = "`forward_pe`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "动态市盈率")
    private BigDecimal forwardPe;

    @Column(name = "`static_pe`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "静态市盈率")
    private BigDecimal staticPe;

    public void copy(StockData source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
