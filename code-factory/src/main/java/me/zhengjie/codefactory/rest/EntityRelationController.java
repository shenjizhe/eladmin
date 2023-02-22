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
import me.zhengjie.codefactory.domain.EntityRelation;
import me.zhengjie.codefactory.service.EntityRelationService;
import me.zhengjie.codefactory.service.dto.EntityRelationQueryCriteria;
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
* @date 2023-02-22
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "实体关系管理")
@RequestMapping("/api/entityRelation")
public class EntityRelationController {

    private final EntityRelationService entityRelationService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('entityRelation:list')")
    public void exportEntityRelation(HttpServletResponse response, EntityRelationQueryCriteria criteria) throws IOException {
        entityRelationService.download(entityRelationService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询实体关系")
    @ApiOperation("查询实体关系")
    @PreAuthorize("@el.check('entityRelation:list')")
    public ResponseEntity<Object> queryEntityRelation(EntityRelationQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(entityRelationService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增实体关系")
    @ApiOperation("新增实体关系")
    @PreAuthorize("@el.check('entityRelation:add')")
    public ResponseEntity<Object> createEntityRelation(@Validated @RequestBody EntityRelation resources){
        return new ResponseEntity<>(entityRelationService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改实体关系")
    @ApiOperation("修改实体关系")
    @PreAuthorize("@el.check('entityRelation:edit')")
    public ResponseEntity<Object> updateEntityRelation(@Validated @RequestBody EntityRelation resources){
        entityRelationService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除实体关系")
    @ApiOperation("删除实体关系")
    @PreAuthorize("@el.check('entityRelation:del')")
    public ResponseEntity<Object> deleteEntityRelation(@RequestBody Long[] ids) {
        entityRelationService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}