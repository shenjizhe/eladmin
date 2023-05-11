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
* @date 2023-05-09
**/
@Data
public class StockAnalysisDto implements Serializable {

    /** ID */
    private Integer id;

    /** 股票ID */
    private Integer stockId;

    /** 平均价格 */
    private BigDecimal priceAvg;

    /** 高位价格 */
    private BigDecimal priceHign;

    /** 低位价格 */
    private BigDecimal priceLow;

    /** 评估类型 */
    private Integer estimateType;

    /** 波动周期 */
    private Integer fluctuationCycle;
}