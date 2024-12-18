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
package me.zhengjie.codefactory.domain;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
* @website https://eladmin.vip
* @description /
* @author Jason Shen
* @date 2023-04-11
**/
@Entity
@Data
@Table(name="config")
public class Config implements Serializable {

    @Id
    @Column(name = "`id`")
    @ApiModelProperty(value = "主键")
    private Long id;

    @Column(name = "`key`",unique = true,nullable = false)
    @NotBlank
    @ApiModelProperty(value = "关键词( 唯一 )")
    private String key;

    @Column(name = "`type`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "类型")
    private String type;

    @Column(name = "`comment`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "描述")
    private String comment;

    @Column(name = "`value`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "数值")
    private String value;

    @Column(name = "`buid_in`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "内置参数")
    private Boolean buidIn;

    public void copy(Config source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }

    public Object getObject() {
        switch (type.toLowerCase()) {
            case "json":
                return JSONObject.parse(value);
            case "integer":
                return Integer.parseInt(value);
            case "Long":
                return Long.parseLong(value);
            case "datetime":
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDate date = LocalDate.parse(value, formatter);
                return date;
            case "boolean":
                return Boolean.parseBoolean(value);
            default:
                return value;
        }
    }
}
