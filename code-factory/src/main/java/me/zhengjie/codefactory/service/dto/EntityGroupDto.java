/*
*  Copyright 2019-2020 Zheng Jie
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
* @date 2023-02-15
**/
@Data
public class EntityGroupDto implements Serializable {

    /** 小组主键 */
    private Long id;

    /** 域ID */
    private Long domainId;

    /** 组名称 */
    private String groupName;

    /** 组描述 */
    private String groupInfo;

    /** 组标签颜色 */
    private Integer groupBackcolor;

    /** 组图标 */
    private String groupImage;
}