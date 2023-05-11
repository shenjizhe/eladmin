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
* @date 2023-05-11
**/
@Data
public class StockDataDto implements Serializable {

    /** ID */
    private Integer id;

    /** 股票ID */
    private Integer stockId;

    /** 总市值 */
    private BigDecimal marketValue;

    /** 流通市值 */
    private BigDecimal marketValueCircle;

    /** 均价 */
    private BigDecimal averagePrice;

    /** 动态市盈率 */
    private BigDecimal forwardPe;

    /** 静态市盈率 */
    private BigDecimal staticPe;
}