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
package me.zhengjie.financial.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.financial.domain.Stock;
import me.zhengjie.financial.service.StockService;
import me.zhengjie.financial.service.dto.StockQueryCriteria;
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
* @date 2023-05-09
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "股票信息管理")
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('stock:list')")
    public void exportStock(HttpServletResponse response, StockQueryCriteria criteria) throws IOException {
        stockService.download(stockService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询股票信息")
    @ApiOperation("查询股票信息")
    @PreAuthorize("@el.check('stock:list')")
    public ResponseEntity<Object> queryStock(StockQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(stockService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(stockService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增股票信息")
    @ApiOperation("新增股票信息")
    @PreAuthorize("@el.check('stock:add')")
    public ResponseEntity<Object> createStock(@Validated @RequestBody Stock resources){
        return new ResponseEntity<>(stockService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改股票信息")
    @ApiOperation("修改股票信息")
    @PreAuthorize("@el.check('stock:edit')")
    public ResponseEntity<Object> updateStock(@Validated @RequestBody Stock resources){
        stockService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除股票信息")
    @ApiOperation("删除股票信息")
    @PreAuthorize("@el.check('stock:del')")
    public ResponseEntity<Object> deleteStock(@RequestBody Integer[] ids) {
        stockService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}