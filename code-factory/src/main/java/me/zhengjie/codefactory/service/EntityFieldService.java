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

import me.zhengjie.codefactory.domain.EntityField;
import me.zhengjie.codefactory.service.dto.EntityFieldDto;
import me.zhengjie.codefactory.service.dto.EntityFieldQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://eladmin.vip
* @description 服务接口
* @author Jason Shen
* @date 2023-02-28
**/
public interface EntityFieldService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(EntityFieldQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<EntityFieldDto>
    */
    List<EntityFieldDto> queryAll(EntityFieldQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return EntityFieldDto
     */
    EntityFieldDto findById(Long id);

    /**
    * 创建
    * @param resources /
    * @return EntityFieldDto
    */
    EntityFieldDto create(EntityField resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(EntityField resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Long[] ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<EntityFieldDto> all, HttpServletResponse response) throws IOException;
}