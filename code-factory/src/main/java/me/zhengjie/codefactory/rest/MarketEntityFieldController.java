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
import me.zhengjie.codefactory.domain.MarketEntityField;
import me.zhengjie.codefactory.service.MarketEntityFieldService;
import me.zhengjie.codefactory.service.dto.MarketEntityFieldQueryCriteria;
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
@Api(tags = "市场实体属性管理")
@RequestMapping("/api/marketEntityField")
public class MarketEntityFieldController {

    private final MarketEntityFieldService marketEntityFieldService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('marketEntityField:list')")
    public void exportMarketEntityField(HttpServletResponse response, MarketEntityFieldQueryCriteria criteria) throws IOException {
        marketEntityFieldService.download(marketEntityFieldService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询市场实体属性")
    @ApiOperation("查询市场实体属性")
    @PreAuthorize("@el.check('marketEntityField:list')")
    public ResponseEntity<Object> queryMarketEntityField(MarketEntityFieldQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(marketEntityFieldService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增市场实体属性")
    @ApiOperation("新增市场实体属性")
    @PreAuthorize("@el.check('marketEntityField:add')")
    public ResponseEntity<Object> createMarketEntityField(@Validated @RequestBody MarketEntityField resources){
        return new ResponseEntity<>(marketEntityFieldService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改市场实体属性")
    @ApiOperation("修改市场实体属性")
    @PreAuthorize("@el.check('marketEntityField:edit')")
    public ResponseEntity<Object> updateMarketEntityField(@Validated @RequestBody MarketEntityField resources){
        marketEntityFieldService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除市场实体属性")
    @ApiOperation("删除市场实体属性")
    @PreAuthorize("@el.check('marketEntityField:del')")
    public ResponseEntity<Object> deleteMarketEntityField(@RequestBody Long[] ids) {
        marketEntityFieldService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}