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
import java.time.LocalDate;

/**
* @website https://eladmin.vip
* @description /
* @author Jason Shen
* @date 2023-08-23
**/
@Data
public class StudyRecordDayDto implements Serializable {

    /** 主键 */
    private Long id;

    /** 用户id */
    private Long uid;

    /** 日期 */
    private LocalDate date;

    /** 学习内容类型（0 - 词根 1- 单词） */
    private Integer objectType;

    /** 学习内容id */
    private Long objectId;

    /** 类型（0-初次看，1-再次看） */
    private Integer type;
}