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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.financial.domain.StockAnalysis;
import me.zhengjie.financial.domain.StockStatics;
import me.zhengjie.financial.service.StockAnalysisService;
import me.zhengjie.financial.service.StockStaticsService;
import me.zhengjie.financial.service.dto.StockAnalysisQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* @website https://eladmin.vip
* @author Jason Shen
* @date 2023-05-09
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "股票统计分析")
@RequestMapping("/api/stock-statics")
public class StockStaticsController {

    private final StockStaticsService stockStaticsService;
    @Log("取得股票统计信息")
    @ApiOperation("取得股票统计信息")
    @GetMapping(value = "/{id}/infos")
    @PreAuthorize("@el.check('stockAnalysis:list')")
    public ResponseEntity<StockStatics> getStockInfos(@PathVariable("id") Integer id) throws IOException {
        StockStatics statics = stockStaticsService.getStockInfos(id);
        return new ResponseEntity<>(statics,HttpStatus.OK);
    }
}