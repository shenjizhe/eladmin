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
import me.zhengjie.codefactory.domain.Script;
import me.zhengjie.codefactory.service.ScriptService;
import me.zhengjie.codefactory.service.dto.ScriptQueryCriteria;
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
* @date 2023-04-11
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "脚本管理")
@RequestMapping("/api/script")
public class ScriptController {

    private final ScriptService scriptService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('script:list')")
    public void exportScript(HttpServletResponse response, ScriptQueryCriteria criteria) throws IOException {
        scriptService.download(scriptService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询脚本")
    @ApiOperation("查询脚本")
    @PreAuthorize("@el.check('script:list')")
    public ResponseEntity<Object> queryScript(ScriptQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(scriptService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(scriptService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增脚本")
    @ApiOperation("新增脚本")
    @PreAuthorize("@el.check('script:add')")
    public ResponseEntity<Object> createScript(@Validated @RequestBody Script resources){
        return new ResponseEntity<>(scriptService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改脚本")
    @ApiOperation("修改脚本")
    @PreAuthorize("@el.check('script:edit')")
    public ResponseEntity<Object> updateScript(@Validated @RequestBody Script resources){
        scriptService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除脚本")
    @ApiOperation("删除脚本")
    @PreAuthorize("@el.check('script:del')")
    public ResponseEntity<Object> deleteScript(@RequestBody Long[] ids) {
        scriptService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}