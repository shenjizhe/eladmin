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
package me.zhengjie.codefactory.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.codefactory.domain.EntityModel;
import me.zhengjie.codefactory.service.EntityModelService;
import me.zhengjie.codefactory.service.dto.EntityModelQueryCriteria;
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
* @date 2023-02-27
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "实体管理")
@RequestMapping("/api/entityModel")
public class EntityModelController {

    private final EntityModelService entityModelService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('entityModel:list')")
    public void exportEntityModel(HttpServletResponse response, EntityModelQueryCriteria criteria) throws IOException {
        entityModelService.download(entityModelService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询实体")
    @ApiOperation("查询实体")
    @PreAuthorize("@el.check('entityModel:list')")
    public ResponseEntity<Object> queryEntityModel(EntityModelQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(entityModelService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增实体")
    @ApiOperation("新增实体")
    @PreAuthorize("@el.check('entityModel:add')")
    public ResponseEntity<Object> createEntityModel(@Validated @RequestBody EntityModel resources){
        return new ResponseEntity<>(entityModelService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改实体")
    @ApiOperation("修改实体")
    @PreAuthorize("@el.check('entityModel:edit')")
    public ResponseEntity<Object> updateEntityModel(@Validated @RequestBody EntityModel resources){
        entityModelService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除实体")
    @ApiOperation("删除实体")
    @PreAuthorize("@el.check('entityModel:del')")
    public ResponseEntity<Object> deleteEntityModel(@RequestBody Long[] ids) {
        entityModelService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}