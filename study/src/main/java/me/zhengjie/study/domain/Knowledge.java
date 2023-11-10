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
package me.zhengjie.study.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.sql.Blob;

/**
* @website https://eladmin.vip
* @description /
* @author Jason Shen
* @date 2023-11-09
**/
@Entity
@Data
@Table(name="knowledge")
public class Knowledge implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "知识点id")
    private Long id;

    @Column(name = "`name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "名称")
    private String name;

    @Column(name = "`title`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "标题")
    private String title;

    @Column(name = "`subject`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "学科")
    private Integer subject;

    @Column(name = "`grade`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "年级")
    private Integer grade;

    @Column(name = "`mnemonic`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "口诀")
    private String mnemonic;

    @Column(name = "`conditions`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "条件(每行一个条件)")
    private String conditions;

    @Column(name = "`steps`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "步骤（每行一个步骤）")
    private String steps;

    @Column(name = "`content`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "内容")
    private String content;

    @Column(name = "`chapter_num`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "章节顺序")
    private Integer chapterNum;

    @Column(name = "`chapter_name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "章节名称")
    private String chapterName;

    public void copy(Knowledge source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
