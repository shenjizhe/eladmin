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
import me.zhengjie.financial.domain.StockData;
import me.zhengjie.financial.service.StockDataService;
import me.zhengjie.financial.service.dto.StockDataQueryCriteria;
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
* @date 2023-05-11
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "股票数据管理")
@RequestMapping("/api/stockData")
public class StockDataController {

    private final StockDataService stockDataService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('stockData:list')")
    public void exportStockData(HttpServletResponse response, StockDataQueryCriteria criteria) throws IOException {
        stockDataService.download(stockDataService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询股票数据")
    @ApiOperation("查询股票数据")
    @PreAuthorize("@el.check('stockData:list')")
    public ResponseEntity<Object> queryStockData(StockDataQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(stockDataService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(stockDataService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增股票数据")
    @ApiOperation("新增股票数据")
    @PreAuthorize("@el.check('stockData:add')")
    public ResponseEntity<Object> createStockData(@Validated @RequestBody StockData resources){
        return new ResponseEntity<>(stockDataService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改股票数据")
    @ApiOperation("修改股票数据")
    @PreAuthorize("@el.check('stockData:edit')")
    public ResponseEntity<Object> updateStockData(@Validated @RequestBody StockData resources){
        stockDataService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除股票数据")
    @ApiOperation("删除股票数据")
    @PreAuthorize("@el.check('stockData:del')")
    public ResponseEntity<Object> deleteStockData(@RequestBody Integer[] ids) {
        stockDataService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}