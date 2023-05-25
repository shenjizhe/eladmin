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
import me.zhengjie.morpheme.domain.Dictionary;
import me.zhengjie.morpheme.service.DictionaryService;
import me.zhengjie.morpheme.service.dto.DictionaryQueryCriteria;
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
@Api(tags = "词典管理")
@RequestMapping("/api/dictionary")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('dictionary:list')")
    public void exportDictionary(HttpServletResponse response, DictionaryQueryCriteria criteria) throws IOException {
        dictionaryService.download(dictionaryService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询词典")
    @ApiOperation("查询词典")
    @PreAuthorize("@el.check('dictionary:list')")
    public ResponseEntity<Object> queryDictionary(DictionaryQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(dictionaryService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(dictionaryService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增词典")
    @ApiOperation("新增词典")
    @PreAuthorize("@el.check('dictionary:add')")
    public ResponseEntity<Object> createDictionary(@Validated @RequestBody Dictionary resources){
        return new ResponseEntity<>(dictionaryService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改词典")
    @ApiOperation("修改词典")
    @PreAuthorize("@el.check('dictionary:edit')")
    public ResponseEntity<Object> updateDictionary(@Validated @RequestBody Dictionary resources){
        dictionaryService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除词典")
    @ApiOperation("删除词典")
    @PreAuthorize("@el.check('dictionary:del')")
    public ResponseEntity<Object> deleteDictionary(@RequestBody Long[] ids) {
        dictionaryService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}