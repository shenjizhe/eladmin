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
* @date 2023-05-09
**/
@Entity
@Data
@Table(name="stock_analysis")
public class StockAnalysis implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "ID")
    private Integer id;

    @Column(name = "`stock_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "股票ID")
    private Integer stockId;

    @Column(name = "`price_avg`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "平均价格")
    private BigDecimal priceAvg;

    @Column(name = "`price_hign`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "高位价格")
    private BigDecimal priceHign;

    @Column(name = "`price_low`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "低位价格")
    private BigDecimal priceLow;

    @Column(name = "`estimate_type`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "评估类型")
    private Integer estimateType;

    @Column(name = "`fluctuation_cycle`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "波动周期")
    private Integer fluctuationCycle;

    public void copy(StockAnalysis source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
