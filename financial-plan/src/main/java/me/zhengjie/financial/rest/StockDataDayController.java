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
import me.zhengjie.financial.domain.StockDataDay;
import me.zhengjie.financial.service.StockDataDayService;
import me.zhengjie.financial.service.dto.StockDataDayQueryCriteria;
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
@Api(tags = "股票日数据管理")
@RequestMapping("/api/stockDataDay")
public class StockDataDayController {

    private final StockDataDayService stockDataDayService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('stockDataDay:list')")
    public void exportStockDataDay(HttpServletResponse response, StockDataDayQueryCriteria criteria) throws IOException {
        stockDataDayService.download(stockDataDayService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询股票日数据")
    @ApiOperation("查询股票日数据")
    @PreAuthorize("@el.check('stockDataDay:list')")
    public ResponseEntity<Object> queryStockDataDay(StockDataDayQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(stockDataDayService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(stockDataDayService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增股票日数据")
    @ApiOperation("新增股票日数据")
    @PreAuthorize("@el.check('stockDataDay:add')")
    public ResponseEntity<Object> createStockDataDay(@Validated @RequestBody StockDataDay resources){
        return new ResponseEntity<>(stockDataDayService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改股票日数据")
    @ApiOperation("修改股票日数据")
    @PreAuthorize("@el.check('stockDataDay:edit')")
    public ResponseEntity<Object> updateStockDataDay(@Validated @RequestBody StockDataDay resources){
        stockDataDayService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除股票日数据")
    @ApiOperation("删除股票日数据")
    @PreAuthorize("@el.check('stockDataDay:del')")
    public ResponseEntity<Object> deleteStockDataDay(@RequestBody Long[] ids) {
        stockDataDayService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}