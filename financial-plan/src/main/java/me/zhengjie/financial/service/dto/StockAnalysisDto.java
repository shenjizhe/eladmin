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
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author Jason Shen
* @date 2023-05-16
**/
@Data
public class StockAnalysisDto implements Serializable {

    /** ID */
    private Integer id;

    /** 股票ID */
    private Integer stockId;

    /** 平均价格 */
    private BigDecimal priceAvg;

    /** 低位价格 */
    private BigDecimal priceLow;

    /** 低位价格90 */
    private BigDecimal priceLow90;

    /** 低位价格70 */
    private BigDecimal priceLow70;

    /** 集中度90 */
    private BigDecimal concentration90;

    /** 集中度70 */
    private BigDecimal concentration70;

    /** 主力持仓成本 */
    private BigDecimal mainHoldCost;

    /** 高位价格 */
    private BigDecimal priceHigh;

    /** 高位价格90 */
    private BigDecimal priceHigh90;

    /** 高位价格70 */
    private BigDecimal priceHigh70;
}