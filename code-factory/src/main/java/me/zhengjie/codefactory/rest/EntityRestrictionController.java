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
import me.zhengjie.codefactory.domain.EntityRestriction;
import me.zhengjie.codefactory.service.EntityRestrictionService;
import me.zhengjie.codefactory.service.dto.EntityRestrictionQueryCriteria;
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
* @date 2023-02-16
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "实体约束管理")
@RequestMapping("/api/entityRestriction")
public class EntityRestrictionController {

    private final EntityRestrictionService entityRestrictionService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('entityRestriction:list')")
    public void exportEntityRestriction(HttpServletResponse response, EntityRestrictionQueryCriteria criteria) throws IOException {
        entityRestrictionService.download(entityRestrictionService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询实体约束")
    @ApiOperation("查询实体约束")
    @PreAuthorize("@el.check('entityRestriction:list')")
    public ResponseEntity<Object> queryEntityRestriction(EntityRestrictionQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(entityRestrictionService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增实体约束")
    @ApiOperation("新增实体约束")
    @PreAuthorize("@el.check('entityRestriction:add')")
    public ResponseEntity<Object> createEntityRestriction(@Validated @RequestBody EntityRestriction resources){
        return new ResponseEntity<>(entityRestrictionService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改实体约束")
    @ApiOperation("修改实体约束")
    @PreAuthorize("@el.check('entityRestriction:edit')")
    public ResponseEntity<Object> updateEntityRestriction(@Validated @RequestBody EntityRestriction resources){
        entityRestrictionService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除实体约束")
    @ApiOperation("删除实体约束")
    @PreAuthorize("@el.check('entityRestriction:del')")
    public ResponseEntity<Object> deleteEntityRestriction(@RequestBody Long[] ids) {
        entityRestrictionService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}