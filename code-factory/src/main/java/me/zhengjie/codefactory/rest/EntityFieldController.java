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
import me.zhengjie.codefactory.domain.EntityField;
import me.zhengjie.codefactory.service.EntityFieldService;
import me.zhengjie.codefactory.service.dto.EntityFieldQueryCriteria;
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
* @date 2023-02-28
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "实体属性管理")
@RequestMapping("/api/entityField")
public class EntityFieldController {

    private final EntityFieldService entityFieldService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('entityField:list')")
    public void exportEntityField(HttpServletResponse response, EntityFieldQueryCriteria criteria) throws IOException {
        entityFieldService.download(entityFieldService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询实体属性")
    @ApiOperation("查询实体属性")
    @PreAuthorize("@el.check('entityField:list')")
    public ResponseEntity<Object> queryEntityField(EntityFieldQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(entityFieldService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增实体属性")
    @ApiOperation("新增实体属性")
    @PreAuthorize("@el.check('entityField:add')")
    public ResponseEntity<Object> createEntityField(@Validated @RequestBody EntityField resources){
        return new ResponseEntity<>(entityFieldService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改实体属性")
    @ApiOperation("修改实体属性")
    @PreAuthorize("@el.check('entityField:edit')")
    public ResponseEntity<Object> updateEntityField(@Validated @RequestBody EntityField resources){
        entityFieldService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除实体属性")
    @ApiOperation("删除实体属性")
    @PreAuthorize("@el.check('entityField:del')")
    public ResponseEntity<Object> deleteEntityField(@RequestBody Long[] ids) {
        entityFieldService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}