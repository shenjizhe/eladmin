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
package me.zhengjie.codefactory.service.dto;

import lombok.Data;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author Jason Shen
* @date 2023-02-28
**/
@Data
public class MarketEntityFieldDto implements Serializable {

    /** 主键 */
    private Long id;

    /** 实体ID */
    private Long entityId;

    /** 名称 */
    private String name;

    /** 描述 */
    private String comment;

    /** 是否主键 */
    private Boolean pk;

    /** 数据类型 */
    private String type;
}