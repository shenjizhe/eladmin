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

import me.zhengjie.codefactory.domain.Config;
import me.zhengjie.codefactory.service.dto.ConfigDto;
import me.zhengjie.codefactory.service.dto.ConfigQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Jason Shen
 * @website https://eladmin.vip
 * @description 服务接口
 * @date 2023-04-11
 **/
public interface ConfigService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(ConfigQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<ConfigDto>
     */
    List<ConfigDto> queryAll(ConfigQueryCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return ConfigDto
     */
    ConfigDto findById(Long id);

    /**
     * 根据 key 返回值
     *
     * @param key 关键字
     * @return 值（根据类型进行了转换）
     */
    Object getByKey(String key);

    /**
     * 创建
     *
     * @param resources /
     * @return ConfigDto
     */
    ConfigDto create(Config resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(Config resources);

    /**
     * 多选删除
     *
     * @param ids /
     */
    void deleteAll(Long[] ids);

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<ConfigDto> all, HttpServletResponse response) throws IOException;
}