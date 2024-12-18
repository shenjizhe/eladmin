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
import me.zhengjie.morpheme.domain.WordMeaning;
import me.zhengjie.morpheme.service.WordMeaningService;
import me.zhengjie.morpheme.service.dto.WordMeaningQueryCriteria;
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
@Api(tags = "单词含义管理")
@RequestMapping("/api/wordMeaning")
public class WordMeaningController {

    private final WordMeaningService wordMeaningService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('wordMeaning:list')")
    public void exportWordMeaning(HttpServletResponse response, WordMeaningQueryCriteria criteria) throws IOException {
        wordMeaningService.download(wordMeaningService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询单词含义")
    @ApiOperation("查询单词含义")
    @PreAuthorize("@el.check('wordMeaning:list')")
    public ResponseEntity<Object> queryWordMeaning(WordMeaningQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(wordMeaningService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(wordMeaningService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增单词含义")
    @ApiOperation("新增单词含义")
    @PreAuthorize("@el.check('wordMeaning:add')")
    public ResponseEntity<Object> createWordMeaning(@Validated @RequestBody WordMeaning resources){
        return new ResponseEntity<>(wordMeaningService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改单词含义")
    @ApiOperation("修改单词含义")
    @PreAuthorize("@el.check('wordMeaning:edit')")
    public ResponseEntity<Object> updateWordMeaning(@Validated @RequestBody WordMeaning resources){
        wordMeaningService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除单词含义")
    @ApiOperation("删除单词含义")
    @PreAuthorize("@el.check('wordMeaning:del')")
    public ResponseEntity<Object> deleteWordMeaning(@RequestBody Long[] ids) {
        wordMeaningService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}