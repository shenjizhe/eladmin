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
package me.zhengjie.financial.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author Jason Shen
* @date 2023-05-11
**/
@Entity
@Data
@Table(name="stock_event")
public class StockEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "ID")
    private Integer id;

    @Column(name = "`stock_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "股票ID")
    private Integer stockId;

    @Column(name = "`event_type`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "事件类型")
    private Integer eventType;

    @Column(name = "`event_nature`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "事件性质")
    private Integer eventNature;

    @Column(name = "`event_level`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "重要程度")
    private Integer eventLevel;

    @Column(name = "`event_url`")
    @ApiModelProperty(value = "相关网址")
    private String eventUrl;

    @Column(name = "`event_message`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "事件消息")
    private String eventMessage;

    @Column(name = "`event_start_date`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "开始日期")
    private Timestamp eventStartDate;

    @Column(name = "`event_end_date`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "结束日期")
    private Timestamp eventEndDate;

    @Column(name = "`event_class`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "事件大类")
    private Integer eventClass;

    public void copy(StockEvent source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
