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
* @date 2023-04-10
**/
@Data
public class ServerDto implements Serializable {

    /** ID */
    private Long id;

    /** 账号 */
    private String account;

    /** IP地址 */
    private String ip;

    /** 名称 */
    private String name;

    /** 密码 */
    private String password;

    /** 私钥 */
    private String rsa;

    /** 公钥 */
    private String pub;

    /** 系统 */
    private String system;

    /** 系统版本 */
    private String version;

    /** 端口 */
    private Integer port;

    /** 进度 */
    private Integer step;
}