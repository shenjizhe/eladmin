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
import me.zhengjie.financial.domain.StockOrder;
import me.zhengjie.financial.service.StockOrderService;
import me.zhengjie.financial.service.dto.StockOrderQueryCriteria;
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
@Api(tags = "股票交易管理")
@RequestMapping("/api/stockOrder")
public class StockOrderController {

    private final StockOrderService stockOrderService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('stockOrder:list')")
    public void exportStockOrder(HttpServletResponse response, StockOrderQueryCriteria criteria) throws IOException {
        stockOrderService.download(stockOrderService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询股票交易")
    @ApiOperation("查询股票交易")
    @PreAuthorize("@el.check('stockOrder:list')")
    public ResponseEntity<Object> queryStockOrder(StockOrderQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(stockOrderService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(stockOrderService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增股票交易")
    @ApiOperation("新增股票交易")
    @PreAuthorize("@el.check('stockOrder:add')")
    public ResponseEntity<Object> createStockOrder(@Validated @RequestBody StockOrder resources){
        return new ResponseEntity<>(stockOrderService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改股票交易")
    @ApiOperation("修改股票交易")
    @PreAuthorize("@el.check('stockOrder:edit')")
    public ResponseEntity<Object> updateStockOrder(@Validated @RequestBody StockOrder resources){
        stockOrderService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除股票交易")
    @ApiOperation("删除股票交易")
    @PreAuthorize("@el.check('stockOrder:del')")
    public ResponseEntity<Object> deleteStockOrder(@RequestBody Long[] ids) {
        stockOrderService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}