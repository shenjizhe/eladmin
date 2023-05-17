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
package me.zhengjie.financial.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.financial.domain.StockStatics;
import me.zhengjie.financial.service.*;
import me.zhengjie.financial.service.dto.*;
import me.zhengjie.utils.*;
import org.apache.poi.hpsf.Decimal;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Jason Shen
 * @website https://eladmin.vip
 * @description 服务实现
 * @date 2023-05-09
 **/
@Service
@RequiredArgsConstructor
public class StockStaticsServiceImpl implements StockStaticsService {

    private final StockService stockService;
    private final StockAnalysisService stockAnalysisService;
    private final StockOrderService stockOrderService;
    private final SystemFactorService systemFactorService;

    @Override
    public StockStatics getStockInfos(Integer id) {
        // 取得股票统计数据
        StockDto stock = stockService.findById(id);
        StockStatics stockStatics = new StockStatics(stock);
        calcAnalysis(stockStatics, id);
        calcCost(stockStatics, id);
        calcLevel(stockStatics);
        calcPriceReference(stockStatics);

        return stockStatics;
    }

    private void calcLevel(StockStatics stockStatics) {
        stockStatics.calcLevel();
    }

    private void calcAnalysis(StockStatics stockStatics, Integer id) {
        // 取得股票参考价格，使用直观方法，或者90或70分位数据
        // 计算 3分，7分位，作为目标的买入和卖出数值
        StockAnalysisQueryCriteria criteria = new StockAnalysisQueryCriteria();
        criteria.setStockId(id);
        List<StockAnalysisDto> stockAnalysis = stockAnalysisService.queryAll(criteria);

        if (stockAnalysis != null && stockAnalysis.size() > 0) {
            stockStatics.calcAnylsis(stockAnalysis.get(0), new BigDecimal(0.25));
        }
    }

    private void calcCost(StockStatics stockStatics, Integer id) {
        Long userId = SecurityUtils.getCurrentUserId();
        StockOrderQueryCriteria criteria = new StockOrderQueryCriteria();
        criteria.setUserId(userId);
        List<StockOrderDto> stockOrders = stockOrderService.queryAll(criteria);
        stockStatics.calcOrders(stockOrders);
    }

    private void calcPriceReference(StockStatics stockStatics) {
        Map<String, Object> map = systemFactorService.queryAll();
        stockStatics.calcReference(map);
    }
}