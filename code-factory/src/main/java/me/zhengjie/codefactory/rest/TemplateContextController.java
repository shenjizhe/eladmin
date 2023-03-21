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
import me.zhengjie.codefactory.domain.TemplateContext;
import me.zhengjie.codefactory.service.TemplateContextService;
import me.zhengjie.codefactory.service.dto.TemplateContextQueryCriteria;
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
* @date 2023-03-20
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "上下文管理")
@RequestMapping("/api/templateContext")
public class TemplateContextController {

    private final TemplateContextService templateContextService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('templateContext:list')")
    public void exportTemplateContext(HttpServletResponse response, TemplateContextQueryCriteria criteria) throws IOException {
        templateContextService.download(templateContextService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询上下文")
    @ApiOperation("查询上下文")
    @PreAuthorize("@el.check('templateContext:list')")
    public ResponseEntity<Object> queryTemplateContext(TemplateContextQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(templateContextService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(templateContextService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增上下文")
    @ApiOperation("新增上下文")
    @PreAuthorize("@el.check('templateContext:add')")
    public ResponseEntity<Object> createTemplateContext(@Validated @RequestBody TemplateContext resources){
        return new ResponseEntity<>(templateContextService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改上下文")
    @ApiOperation("修改上下文")
    @PreAuthorize("@el.check('templateContext:edit')")
    public ResponseEntity<Object> updateTemplateContext(@Validated @RequestBody TemplateContext resources){
        templateContextService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除上下文")
    @ApiOperation("删除上下文")
    @PreAuthorize("@el.check('templateContext:del')")
    public ResponseEntity<Object> deleteTemplateContext(@RequestBody Long[] ids) {
        templateContextService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}