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
* @date 2023-08-29
**/
@Data
public class StudyWordStaticsDto implements Serializable {

    /** 主键 */
    private Long id;

    /** 用户ID */
    private Long uid;

    /** 学习知识ID */
    private Long objectId;

    /** 忘记次数 */
    private Integer forgetTimes;

    /** 记忆等级 */
    private Integer memeryLevel;

    /** 学习次数 */
    private Integer studyTimes;

    /** 记得次数 */
    private Integer simpleTimes;

    /** 模糊次数 */
    private Integer confuseTimes;

    /** 最后学习日期 */
    private Timestamp lastReviewTime;

    /** 最后一次学习结果 */
    private Integer lastReviewResult;

    /** 学习比例 */
    private Integer reviewRate;
}