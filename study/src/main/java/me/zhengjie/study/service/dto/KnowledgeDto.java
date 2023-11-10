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
package me.zhengjie.study.service.dto;

import lombok.Data;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author Jason Shen
* @date 2023-11-10
**/
@Data
public class KnowledgeDto implements Serializable {

    /** 知识点id */
    private Long id;

    /** 名称 */
    private String name;

    /** 标题 */
    private String title;

    /** 学科 */
    private Integer subject;

    /** 年级 */
    private Integer grade;

    /** 口诀 */
    private String mnemonic;

    /** 条件(每行一个条件) */
    private String conditions;

    /** 步骤（每行一个步骤） */
    private String steps;

    /** 内容 */
    private String content;

    /** 章节顺序 */
    private Integer chapterNum;

    /** 章节名称 */
    private String chapterName;
}