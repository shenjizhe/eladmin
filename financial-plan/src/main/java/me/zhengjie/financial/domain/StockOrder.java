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
* @date 2023-05-15
**/
@Entity
@Data
@Table(name="stock_order")
public class StockOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "订单ID")
    private Long id;

    @Column(name = "`stock_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "股票ID")
    private Integer stockId;

    @Column(name = "`trade_type`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "交易类型")
    private Integer tradeType;

    @Column(name = "`trade_price`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "交易价格")
    private BigDecimal tradePrice;

    @Column(name = "`trade_count`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "交易股票数量")
    private Integer tradeCount;

    @Column(name = "`trade_time`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "交易时间")
    private Timestamp tradeTime;

    @Column(name = "`user_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "用户id")
    private Long userId;

    public void copy(StockOrder source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
