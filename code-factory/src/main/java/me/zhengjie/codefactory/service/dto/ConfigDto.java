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

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Jason Shen
 * @website https://eladmin.vip
 * @description /
 * @date 2023-04-11
 **/
@Data
public class ConfigDto implements Serializable {

    /** 主键 */
    /**
     * 防止精度丢失
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    /**
     * 关键词( 唯一 )
     */
    private String key;

    /**
     * 类型
     */
    private String type;

    /**
     * 描述
     */
    private String comment;

    /**
     * 数值
     */
    private String value;

    /**
     * 内置参数
     */
    private Boolean buidIn;
}