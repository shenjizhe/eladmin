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
import me.zhengjie.codefactory.domain.Template;
import me.zhengjie.codefactory.service.TemplateService;
import me.zhengjie.codefactory.service.dto.TemplateQueryCriteria;
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
@Api(tags = "模板管理")
@RequestMapping("/api/template")
public class TemplateController {

    private final TemplateService templateService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('template:list')")
    public void exportTemplate(HttpServletResponse response, TemplateQueryCriteria criteria) throws IOException {
        templateService.download(templateService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询模板")
    @ApiOperation("查询模板")
    @PreAuthorize("@el.check('template:list')")
    public ResponseEntity<Object> queryTemplate(TemplateQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(templateService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增模板")
    @ApiOperation("新增模板")
    @PreAuthorize("@el.check('template:add')")
    public ResponseEntity<Object> createTemplate(@Validated @RequestBody Template resources){
        return new ResponseEntity<>(templateService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改模板")
    @ApiOperation("修改模板")
    @PreAuthorize("@el.check('template:edit')")
    public ResponseEntity<Object> updateTemplate(@Validated @RequestBody Template resources){
        templateService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除模板")
    @ApiOperation("删除模板")
    @PreAuthorize("@el.check('template:del')")
    public ResponseEntity<Object> deleteTemplate(@RequestBody Long[] ids) {
        templateService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}