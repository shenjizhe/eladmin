/*
*  Copyright 2019-2020 Zheng Jie
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
package me.zhengjie.codefactory.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.codefactory.domain.EntityModel;
import me.zhengjie.codefactory.service.EntityService;
import me.zhengjie.codefactory.service.dto.EntityQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://eladmin.vip
* @author Jason Shen
* @date 2023-02-15
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "实体管理")
@RequestMapping("/api/entity")
public class EntityController {

    private final EntityService entityService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('entity:list')")
    public void exportEntity(HttpServletResponse response, EntityQueryCriteria criteria) throws IOException {
        entityService.download(entityService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询实体")
    @ApiOperation("查询实体")
    @PreAuthorize("@el.check('entity:list')")
    public ResponseEntity<Object> queryEntity(EntityQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(entityService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增实体")
    @ApiOperation("新增实体")
    @PreAuthorize("@el.check('entity:add')")
    public ResponseEntity<Object> createEntity(@Validated @RequestBody EntityModel resources){
        return new ResponseEntity<>(entityService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改实体")
    @ApiOperation("修改实体")
    @PreAuthorize("@el.check('entity:edit')")
    public ResponseEntity<Object> updateEntity(@Validated @RequestBody EntityModel resources){
        entityService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除实体")
    @ApiOperation("删除实体")
    @PreAuthorize("@el.check('entity:del')")
    public ResponseEntity<Object> deleteEntity(@RequestBody Long[] ids) {
        entityService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}