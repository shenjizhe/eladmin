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
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author Jason Shen
* @date 2023-05-15
**/
@Data
public class StockDto implements Serializable {

    /** 股票ID */
    private Integer id;

    /** 股票代码 */
    private String code;

    /** 股票名称 */
    private String name;

    /** 股票阶段 */
    private Integer stage;

    /** 股票角色 */
    private Integer role;

    /** 大周期 */
    private Integer cycleBig;

    /** 小周期 */
    private Integer cycleSmall;

    /** 股票行业 */
    private Integer industry;

    /** 上市日期 */
    private Timestamp listDate;
}