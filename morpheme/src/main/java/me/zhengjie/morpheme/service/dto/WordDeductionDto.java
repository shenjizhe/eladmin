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
public class WordDeductionDto implements Serializable {

    /** 主键 */
    private Long id;

    /** 词根ID */
    private Long morphemeId;

    /** 单词ID */
    private Long wordId;

    /** 词素文本 */
    private String morphemeText;

    /** 源词素 */
    private String sourceText;

    /** 全文本 */
    private String fullText;

    /** 词缀类型 */
    private Integer affix;

    /** 变形类型 */
    private Integer shape;

    /** 词性 */
    private String nature;

    /** 是否是派生词 */
    private Boolean isDerive;

    /** 中文含义 */
    private String meaningChinese;

    /** 英文含义 */
    private String meaningEnglish;
}