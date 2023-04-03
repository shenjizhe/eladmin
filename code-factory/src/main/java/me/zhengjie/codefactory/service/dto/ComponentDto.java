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

import lombok.Data;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author Jason Shen
* @date 2023-04-03
**/
@Data
public class ComponentDto implements Serializable {

    /** 主键 */
    private Long id;

    /** 名称 */
    private String name;

    /** 描述 */
    private String comment;

    /** 是否显示 */
    private Boolean show;

    /** 模型ID */
    private Long modelId;

    /** 模块ID */
    private Long templateId;

    /** 文件路径 */
    private String filePath;

    /** 结果端口 */
    private String port;

    /** 结果包名 */
    private String rootPackage;

    /** 域名 */
    private String deployUrl;

    /** 输出目录 */
    private String deployPath;

    /** git目录 */
    private String gitPath;

    /** git命名空间 */
    private String gitGroup;
}