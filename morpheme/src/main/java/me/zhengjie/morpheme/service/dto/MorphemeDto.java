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
package me.zhengjie.morpheme.service.dto;

import lombok.Data;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author Jason Shen
* @date 2023-05-24
**/
@Data
public class MorphemeDto implements Serializable {

    /** 主键 */
    private Long id;

    /** 字典ID */
    private Long dictionaryId;

    /** 编号 */
    private Integer number;

    /** 词素文本 */
    private String text;

    /** 词素类型 */
    private Integer type;

    /** 词源 */
    private String source;

    /** 中文含义 */
    private String meaningChinese;

    /** 英文含义 */
    private String meaningEnglish;

    /** 说明 */
    private String description;
}