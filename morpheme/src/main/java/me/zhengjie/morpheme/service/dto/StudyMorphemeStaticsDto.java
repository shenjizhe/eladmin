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
* @date 2023-08-28
**/
@Data
public class StudyMorphemeStaticsDto implements Serializable {

    /** 主键 */
    private Long id;

    /** 用户ID */
    private Long uid;

    /** 学习知识ID */
    private Long objectId;

    /** 状态 */
    private Integer status;

    /** 学习次数 */
    private Integer studyTims;

    /** 记得次数 */
    private Integer rememberTimes;

    /** 模糊次数 */
    private Integer obscureTimes;

    /** 忘记次数 */
    private Integer forgetTimes;

    /** 最后学习日期 */
    private Timestamp lastStudyTime;

    /** 记忆等级 */
    private Integer memeryLevel;

    /** 学习比例 */
    private Integer studyRate;

    /** 今天是否需要学 */
    private Boolean needStudyTotday;
}