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
package me.zhengjie.financial.service.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
 * @author Jason Shen
 * @website https://eladmin.vip
 * @description /
 * @date 2023-05-17
 **/
@Data
public class StockOrderDto implements Serializable, Comparable<StockOrderDto> {

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 股票ID
     */
    private Integer stockId;

    /**
     * 交易类型
     */
    private Integer tradeType;

    /**
     * 交易价格
     */
    private BigDecimal tradePrice;

    /**
     * 交易股票数量
     */
    private Integer tradeCount;

    /**
     * 交易时间
     */
    private Timestamp tradeTime;

    /**
     * 用户id
     */
    private Long userId;

    @Override
    public int compareTo(StockOrderDto o) {
        return tradeTime.compareTo(o.tradeTime);
    }

    public BigDecimal getTradeTotal(){
        return tradePrice.multiply(new BigDecimal(tradeCount));
    }
}