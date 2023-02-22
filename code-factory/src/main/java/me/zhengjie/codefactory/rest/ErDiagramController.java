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
import me.zhengjie.codefactory.domain.ErDiagram;
import me.zhengjie.codefactory.service.ErDiagramService;
import me.zhengjie.codefactory.service.dto.ErDiagramQueryCriteria;
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
@Api(tags = "ER图管理")
@RequestMapping("/api/erDiagram")
public class ErDiagramController {

    private final ErDiagramService erDiagramService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('erDiagram:list')")
    public void exportErDiagram(HttpServletResponse response, ErDiagramQueryCriteria criteria) throws IOException {
        erDiagramService.download(erDiagramService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ER图")
    @ApiOperation("查询ER图")
    @PreAuthorize("@el.check('erDiagram:list')")
    public ResponseEntity<Object> queryErDiagram(ErDiagramQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(erDiagramService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ER图")
    @ApiOperation("新增ER图")
    @PreAuthorize("@el.check('erDiagram:add')")
    public ResponseEntity<Object> createErDiagram(@Validated @RequestBody ErDiagram resources){
        return new ResponseEntity<>(erDiagramService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ER图")
    @ApiOperation("修改ER图")
    @PreAuthorize("@el.check('erDiagram:edit')")
    public ResponseEntity<Object> updateErDiagram(@Validated @RequestBody ErDiagram resources){
        erDiagramService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除ER图")
    @ApiOperation("删除ER图")
    @PreAuthorize("@el.check('erDiagram:del')")
    public ResponseEntity<Object> deleteErDiagram(@RequestBody Long[] ids) {
        erDiagramService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}