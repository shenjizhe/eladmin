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
@Table(name="stock_data_day")
public class StockDataDay implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "ID")
    private Long id;

    @Column(name = "`stock_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "股票ID")
    private Integer stockId;

    @Column(name = "`date`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "日期")
    private Timestamp date;

    @Column(name = "`event_price_open`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "开盘股价")
    private BigDecimal eventPriceOpen;

    @Column(name = "`event_price_close`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "收盘股价")
    private BigDecimal eventPriceClose;

    @Column(name = "`event_price_high`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "最高股价")
    private BigDecimal eventPriceHigh;

    @Column(name = "`event_price_low`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "最低股价")
    private BigDecimal eventPriceLow;

    @Column(name = "`trade_stock_count`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "交易量")
    private BigDecimal tradeStockCount;

    @Column(name = "`trade_stock_volume`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "交易额")
    private BigDecimal tradeStockVolume;

    @Column(name = "`turnover_rate`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "换手率")
    private BigDecimal turnoverRate;

    @Column(name = "`change_rate`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "涨跌率")
    private BigDecimal changeRate;

    @Column(name = "`change_value`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "涨跌值")
    private BigDecimal changeValue;

    public void copy(StockDataDay source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
