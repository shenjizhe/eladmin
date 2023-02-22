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
import me.zhengjie.codefactory.domain.MarketEntityMethod;
import me.zhengjie.codefactory.service.MarketEntityMethodService;
import me.zhengjie.codefactory.service.dto.MarketEntityMethodQueryCriteria;
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
@Api(tags = "市场实体方法管理")
@RequestMapping("/api/marketEntityMethod")
public class MarketEntityMethodController {

    private final MarketEntityMethodService marketEntityMethodService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('marketEntityMethod:list')")
    public void exportMarketEntityMethod(HttpServletResponse response, MarketEntityMethodQueryCriteria criteria) throws IOException {
        marketEntityMethodService.download(marketEntityMethodService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询市场实体方法")
    @ApiOperation("查询市场实体方法")
    @PreAuthorize("@el.check('marketEntityMethod:list')")
    public ResponseEntity<Object> queryMarketEntityMethod(MarketEntityMethodQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(marketEntityMethodService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增市场实体方法")
    @ApiOperation("新增市场实体方法")
    @PreAuthorize("@el.check('marketEntityMethod:add')")
    public ResponseEntity<Object> createMarketEntityMethod(@Validated @RequestBody MarketEntityMethod resources){
        return new ResponseEntity<>(marketEntityMethodService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改市场实体方法")
    @ApiOperation("修改市场实体方法")
    @PreAuthorize("@el.check('marketEntityMethod:edit')")
    public ResponseEntity<Object> updateMarketEntityMethod(@Validated @RequestBody MarketEntityMethod resources){
        marketEntityMethodService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除市场实体方法")
    @ApiOperation("删除市场实体方法")
    @PreAuthorize("@el.check('marketEntityMethod:del')")
    public ResponseEntity<Object> deleteMarketEntityMethod(@RequestBody Long[] ids) {
        marketEntityMethodService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}