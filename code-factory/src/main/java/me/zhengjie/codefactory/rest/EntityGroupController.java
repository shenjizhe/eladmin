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
import me.zhengjie.codefactory.domain.EntityGroup;
import me.zhengjie.codefactory.service.EntityGroupService;
import me.zhengjie.codefactory.service.dto.EntityGroupQueryCriteria;
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
@Api(tags = "实体组管理")
@RequestMapping("/api/entityGroup")
public class EntityGroupController {

    private final EntityGroupService entityGroupService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('entityGroup:list')")
    public void exportEntityGroup(HttpServletResponse response, EntityGroupQueryCriteria criteria) throws IOException {
        entityGroupService.download(entityGroupService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询实体组")
    @ApiOperation("查询实体组")
    @PreAuthorize("@el.check('entityGroup:list')")
    public ResponseEntity<Object> queryEntityGroup(EntityGroupQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(entityGroupService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增实体组")
    @ApiOperation("新增实体组")
    @PreAuthorize("@el.check('entityGroup:add')")
    public ResponseEntity<Object> createEntityGroup(@Validated @RequestBody EntityGroup resources){
        return new ResponseEntity<>(entityGroupService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改实体组")
    @ApiOperation("修改实体组")
    @PreAuthorize("@el.check('entityGroup:edit')")
    public ResponseEntity<Object> updateEntityGroup(@Validated @RequestBody EntityGroup resources){
        entityGroupService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除实体组")
    @ApiOperation("删除实体组")
    @PreAuthorize("@el.check('entityGroup:del')")
    public ResponseEntity<Object> deleteEntityGroup(@RequestBody Long[] ids) {
        entityGroupService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}