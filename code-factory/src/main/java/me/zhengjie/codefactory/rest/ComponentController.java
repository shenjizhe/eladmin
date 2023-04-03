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
import me.zhengjie.codefactory.domain.Component;
import me.zhengjie.codefactory.service.ComponentService;
import me.zhengjie.codefactory.service.dto.ComponentQueryCriteria;
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
* @date 2023-04-03
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "组件管理")
@RequestMapping("/api/component")
public class ComponentController {

    private final ComponentService componentService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('component:list')")
    public void exportComponent(HttpServletResponse response, ComponentQueryCriteria criteria) throws IOException {
        componentService.download(componentService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询组件")
    @ApiOperation("查询组件")
    @PreAuthorize("@el.check('component:list')")
    public ResponseEntity<Object> queryComponent(ComponentQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(componentService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(componentService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增组件")
    @ApiOperation("新增组件")
    @PreAuthorize("@el.check('component:add')")
    public ResponseEntity<Object> createComponent(@Validated @RequestBody Component resources){
        return new ResponseEntity<>(componentService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改组件")
    @ApiOperation("修改组件")
    @PreAuthorize("@el.check('component:edit')")
    public ResponseEntity<Object> updateComponent(@Validated @RequestBody Component resources){
        componentService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除组件")
    @ApiOperation("删除组件")
    @PreAuthorize("@el.check('component:del')")
    public ResponseEntity<Object> deleteComponent(@RequestBody Long[] ids) {
        componentService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}