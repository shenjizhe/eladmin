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
import me.zhengjie.financial.domain.StockEvent;
import me.zhengjie.financial.service.StockEventService;
import me.zhengjie.financial.service.dto.StockEventQueryCriteria;
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
@Api(tags = "股票事件管理")
@RequestMapping("/api/stockEvent")
public class StockEventController {

    private final StockEventService stockEventService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('stockEvent:list')")
    public void exportStockEvent(HttpServletResponse response, StockEventQueryCriteria criteria) throws IOException {
        stockEventService.download(stockEventService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询股票事件")
    @ApiOperation("查询股票事件")
    @PreAuthorize("@el.check('stockEvent:list')")
    public ResponseEntity<Object> queryStockEvent(StockEventQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(stockEventService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(stockEventService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增股票事件")
    @ApiOperation("新增股票事件")
    @PreAuthorize("@el.check('stockEvent:add')")
    public ResponseEntity<Object> createStockEvent(@Validated @RequestBody StockEvent resources){
        return new ResponseEntity<>(stockEventService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改股票事件")
    @ApiOperation("修改股票事件")
    @PreAuthorize("@el.check('stockEvent:edit')")
    public ResponseEntity<Object> updateStockEvent(@Validated @RequestBody StockEvent resources){
        stockEventService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除股票事件")
    @ApiOperation("删除股票事件")
    @PreAuthorize("@el.check('stockEvent:del')")
    public ResponseEntity<Object> deleteStockEvent(@RequestBody Integer[] ids) {
        stockEventService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}