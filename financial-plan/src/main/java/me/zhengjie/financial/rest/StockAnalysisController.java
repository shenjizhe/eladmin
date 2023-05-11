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
import me.zhengjie.financial.domain.StockAnalysis;
import me.zhengjie.financial.service.StockAnalysisService;
import me.zhengjie.financial.service.dto.StockAnalysisQueryCriteria;
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
@Api(tags = "股票分析管理")
@RequestMapping("/api/stockAnalysis")
public class StockAnalysisController {

    private final StockAnalysisService stockAnalysisService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('stockAnalysis:list')")
    public void exportStockAnalysis(HttpServletResponse response, StockAnalysisQueryCriteria criteria) throws IOException {
        stockAnalysisService.download(stockAnalysisService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询股票分析")
    @ApiOperation("查询股票分析")
    @PreAuthorize("@el.check('stockAnalysis:list')")
    public ResponseEntity<Object> queryStockAnalysis(StockAnalysisQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(stockAnalysisService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(stockAnalysisService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增股票分析")
    @ApiOperation("新增股票分析")
    @PreAuthorize("@el.check('stockAnalysis:add')")
    public ResponseEntity<Object> createStockAnalysis(@Validated @RequestBody StockAnalysis resources){
        return new ResponseEntity<>(stockAnalysisService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改股票分析")
    @ApiOperation("修改股票分析")
    @PreAuthorize("@el.check('stockAnalysis:edit')")
    public ResponseEntity<Object> updateStockAnalysis(@Validated @RequestBody StockAnalysis resources){
        stockAnalysisService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除股票分析")
    @ApiOperation("删除股票分析")
    @PreAuthorize("@el.check('stockAnalysis:del')")
    public ResponseEntity<Object> deleteStockAnalysis(@RequestBody Integer[] ids) {
        stockAnalysisService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}