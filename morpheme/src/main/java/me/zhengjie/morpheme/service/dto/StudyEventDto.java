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
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author Jason Shen
* @date 2023-08-22
**/
@Data
public class StudyEventDto implements Serializable {

    /** 主键 */
    private Long id;

    /** 操作时间 */
    private Timestamp time;

    /** 事件 */
    private String event;

    /** 内容 */
    private String content;

    /** 词根ID */
    private Long morphememId;

    /** 单词ID */
    private Long wordId;

    /** 用户ID */
    private Long uid;
}