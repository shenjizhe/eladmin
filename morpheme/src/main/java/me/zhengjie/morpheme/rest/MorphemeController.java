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
package me.zhengjie.morpheme.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.morpheme.domain.Morpheme;
import me.zhengjie.morpheme.service.MorphemeService;
import me.zhengjie.morpheme.service.dto.MorphemeQueryCriteria;
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
* @date 2023-05-24
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "词根管理")
@RequestMapping("/api/morpheme")
public class MorphemeController {

    private final MorphemeService morphemeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('morpheme:list')")
    public void exportMorpheme(HttpServletResponse response, MorphemeQueryCriteria criteria) throws IOException {
        morphemeService.download(morphemeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询词根")
    @ApiOperation("查询词根")
    @PreAuthorize("@el.check('morpheme:list')")
    public ResponseEntity<Object> queryMorpheme(MorphemeQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(morphemeService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(morphemeService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增词根")
    @ApiOperation("新增词根")
    @PreAuthorize("@el.check('morpheme:add')")
    public ResponseEntity<Object> createMorpheme(@Validated @RequestBody Morpheme resources){
        return new ResponseEntity<>(morphemeService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改词根")
    @ApiOperation("修改词根")
    @PreAuthorize("@el.check('morpheme:edit')")
    public ResponseEntity<Object> updateMorpheme(@Validated @RequestBody Morpheme resources){
        morphemeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除词根")
    @ApiOperation("删除词根")
    @PreAuthorize("@el.check('morpheme:del')")
    public ResponseEntity<Object> deleteMorpheme(@RequestBody Long[] ids) {
        morphemeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}