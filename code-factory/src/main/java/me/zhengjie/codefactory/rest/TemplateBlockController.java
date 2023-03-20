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
import me.zhengjie.codefactory.domain.TemplateBlock;
import me.zhengjie.codefactory.service.TemplateBlockService;
import me.zhengjie.codefactory.service.dto.TemplateBlockQueryCriteria;
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
* @date 2023-03-13
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "模板块管理")
@RequestMapping("/api/templateBlock")
public class TemplateBlockController {

    private final TemplateBlockService templateBlockService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('templateBlock:list')")
    public void exportTemplateBlock(HttpServletResponse response, TemplateBlockQueryCriteria criteria) throws IOException {
        templateBlockService.download(templateBlockService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询模板块")
    @ApiOperation("查询模板块")
    @PreAuthorize("@el.check('templateBlock:list')")
    public ResponseEntity<Object> queryTemplateBlock(TemplateBlockQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(templateBlockService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增模板块")
    @ApiOperation("新增模板块")
    @PreAuthorize("@el.check('templateBlock:add')")
    public ResponseEntity<Object> createTemplateBlock(@Validated @RequestBody TemplateBlock resources){
        return new ResponseEntity<>(templateBlockService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改模板块")
    @ApiOperation("修改模板块")
    @PreAuthorize("@el.check('templateBlock:edit')")
    public ResponseEntity<Object> updateTemplateBlock(@Validated @RequestBody TemplateBlock resources){
        templateBlockService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除模板块")
    @ApiOperation("删除模板块")
    @PreAuthorize("@el.check('templateBlock:del')")
    public ResponseEntity<Object> deleteTemplateBlock(@RequestBody Long[] ids) {
        templateBlockService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}