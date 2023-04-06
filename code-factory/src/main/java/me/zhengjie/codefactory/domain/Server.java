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

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author Jason Shen
* @date 2023-04-06
**/
@Entity
@Data
@Table(name="server")
public class Server implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "ID")
    private Long id;

    @Column(name = "`account`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "账号")
    private String account;

    @Column(name = "`ip`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "IP地址")
    private String ip;

    @Column(name = "`name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "名称")
    private String name;

    @Column(name = "`password`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "密码")
    private String password;

    @Column(name = "`rsa`")
    @ApiModelProperty(value = "私钥")
    private String rsa;

    @Column(name = "`pub`")
    @ApiModelProperty(value = "公钥")
    private String pub;

    @Column(name = "`system`")
    @ApiModelProperty(value = "系统")
    private Integer system;

    @Column(name = "`version`")
    @ApiModelProperty(value = "系统版本")
    private String version;

    @Column(name = "`port`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "端口")
    private Integer port;

    public void copy(Server source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
