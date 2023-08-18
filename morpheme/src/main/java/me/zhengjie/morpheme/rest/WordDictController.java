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
import me.zhengjie.morpheme.domain.WordDict;
import me.zhengjie.morpheme.service.WordDictService;
import me.zhengjie.morpheme.service.dto.WordDictQueryCriteria;
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
* @date 2023-08-18
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "单词含义词典管理")
@RequestMapping("/api/wordDict")
public class WordDictController {

    private final WordDictService wordDictService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('wordDict:list')")
    public void exportWordDict(HttpServletResponse response, WordDictQueryCriteria criteria) throws IOException {
        wordDictService.download(wordDictService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询单词含义词典")
    @ApiOperation("查询单词含义词典")
    @PreAuthorize("@el.check('wordDict:list')")
    public ResponseEntity<Object> queryWordDict(WordDictQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(wordDictService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(wordDictService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增单词含义词典")
    @ApiOperation("新增单词含义词典")
    @PreAuthorize("@el.check('wordDict:add')")
    public ResponseEntity<Object> createWordDict(@Validated @RequestBody WordDict resources){
        return new ResponseEntity<>(wordDictService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改单词含义词典")
    @ApiOperation("修改单词含义词典")
    @PreAuthorize("@el.check('wordDict:edit')")
    public ResponseEntity<Object> updateWordDict(@Validated @RequestBody WordDict resources){
        wordDictService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除单词含义词典")
    @ApiOperation("删除单词含义词典")
    @PreAuthorize("@el.check('wordDict:del')")
    public ResponseEntity<Object> deleteWordDict(@RequestBody Integer[] ids) {
        wordDictService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}