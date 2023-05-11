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
* @website https://eladmin.vip
* @description /
* @author Jason Shen
* @date 2023-05-11
**/
@Data
public class StockDataDayDto implements Serializable {

    /** ID */
    private Long id;

    /** 股票ID */
    private Integer stockId;

    /** 日期 */
    private Timestamp date;

    /** 开盘股价 */
    private BigDecimal eventPriceOpen;

    /** 收盘股价 */
    private BigDecimal eventPriceClose;

    /** 最高股价 */
    private BigDecimal eventPriceHigh;

    /** 最低股价 */
    private BigDecimal eventPriceLow;

    /** 交易量 */
    private BigDecimal tradeStockCount;

    /** 交易额 */
    private BigDecimal tradeStockVolume;

    /** 换手率 */
    private BigDecimal turnoverRate;

    /** 涨跌率 */
    private BigDecimal changeRate;

    /** 涨跌值 */
    private BigDecimal changeValue;
}