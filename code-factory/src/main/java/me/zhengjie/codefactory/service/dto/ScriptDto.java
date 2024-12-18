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
* @date 2023-04-11
**/
@Data
public class ScriptDto implements Serializable {

    /** ID */
    private Long id;

    /** 脚本 */
    private String script;

    /** 使用系统 */
    private String system;

    /** 语言 */
    private String language;

    /** 脚本类型 */
    private String type;

    /** 参数列表 */
    private String params;

    /** 脚本名称 */
    private String name;

    /** 脚本描述 */
    private String comment;

    /** 查找键 */
    private String key;

    /** 内置参数 */
    private Boolean buidIn;
}