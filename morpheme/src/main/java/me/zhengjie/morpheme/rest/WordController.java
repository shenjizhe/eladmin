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

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.annotation.Log;
import me.zhengjie.morpheme.domain.Word;
import me.zhengjie.morpheme.service.WordService;
import me.zhengjie.morpheme.service.dto.WordQueryCriteria;
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
* @date 2023-08-01
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "单词管理")
@RequestMapping("/api/word")
public class WordController {

    private final WordService wordService;

    @Log("翻译所有的单词，并取得其中的音标")
    @ApiOperation("翻译并取得音标")
    @GetMapping(value = "/transfer-words")
    @PreAuthorize("@el.check('word:list')")
    public void transferWords() throws IOException {
        wordService.setAllDescription();
    }

    @Log("翻译指定的单词，并取得其中的音标")
    @ApiOperation("翻译指定的单词")
    @GetMapping(value = "/transfer-words/{word}")
    @PreAuthorize("@el.check('word:list')")
    public String transferWord(@PathVariable("word") String word) throws IOException {
        return wordService.getDescription(word);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('word:list')")
    public void exportWord(HttpServletResponse response, WordQueryCriteria criteria) throws IOException {
        wordService.download(wordService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询单词")
    @ApiOperation("查询单词")
    @PreAuthorize("@el.check('word:list')")
    public ResponseEntity<Object> queryWord(WordQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(wordService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(wordService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增单词")
    @ApiOperation("新增单词")
    @PreAuthorize("@el.check('word:add')")
    public ResponseEntity<Object> createWord(@Validated @RequestBody Word resources){
        return new ResponseEntity<>(wordService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改单词")
    @ApiOperation("修改单词")
    @PreAuthorize("@el.check('word:edit')")
    public ResponseEntity<Object> updateWord(@Validated @RequestBody Word resources){
        wordService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除单词")
    @ApiOperation("删除单词")
    @PreAuthorize("@el.check('word:del')")
    public ResponseEntity<Object> deleteWord(@RequestBody Long[] ids) {
        wordService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}