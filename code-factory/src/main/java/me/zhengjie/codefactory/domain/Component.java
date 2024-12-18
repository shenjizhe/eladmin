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
* @date 2023-04-03
**/
@Entity
@Data
@Table(name="component")
public class Component implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "主键")
    private Long id;

    @Column(name = "`name`",unique = true,nullable = false)
    @NotBlank
    @ApiModelProperty(value = "名称")
    private String name;

    @Column(name = "`comment`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "描述")
    private String comment;

    @Column(name = "`show`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "是否显示")
    private Boolean show;

    @Column(name = "`model_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "模型ID")
    private Long modelId;

    @Column(name = "`template_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "模块ID")
    private Long templateId;

    @Column(name = "`file_path`")
    @ApiModelProperty(value = "文件路径")
    private String filePath;

    @Column(name = "`port`")
    @ApiModelProperty(value = "结果端口")
    private String port;

    @Column(name = "`root_package`")
    @ApiModelProperty(value = "结果包名")
    private String rootPackage;

    @Column(name = "`deploy_url`")
    @ApiModelProperty(value = "域名")
    private String deployUrl;

    @Column(name = "`deploy_path`")
    @ApiModelProperty(value = "输出目录")
    private String deployPath;

    @Column(name = "`git_path`")
    @ApiModelProperty(value = "git目录")
    private String gitPath;

    @Column(name = "`git_group`")
    @ApiModelProperty(value = "git命名空间")
    private String gitGroup;

    public void copy(Component source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
