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
package me.zhengjie.codefactory.service;

import me.zhengjie.codefactory.domain.Server;
import me.zhengjie.codefactory.service.dto.ServerDto;
import me.zhengjie.codefactory.service.dto.ServerQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://eladmin.vip
* @description 服务接口
* @author Jason Shen
* @date 2023-04-10
**/
public interface ServerService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ServerQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ServerDto>
    */
    List<ServerDto> queryAll(ServerQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ServerDto
     */
    ServerDto findById(Long id);

    /**
    * 创建
    * @param resources /
    * @return ServerDto
    */
    ServerDto create(Server resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(Server resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Long[] ids);

    /**
     * 执行脚本
     * @param id 请求的服务ID /
     * @return 脚本返回/
     */
    String execute(Long id, Long scriptId);

    /**
     * 执行脚本
     * @param id 请求的服务ID /
     * @return 脚本返回/
     */
    String execute(Long id, String key);


    /**
     * 复制文件到服务器
     * @param path 路径
     * @param content 内容
     * @return 上传结果
     */
    String copyFile(Long id,String path, String content);

    /**
     * 复制文件到服务器
     * @param path 路径
     * @param configKey 内容
     * @return 上传结果
     */
    String copyFileByKey(Long id,String path, String configKey);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<ServerDto> all, HttpServletResponse response) throws IOException;
}