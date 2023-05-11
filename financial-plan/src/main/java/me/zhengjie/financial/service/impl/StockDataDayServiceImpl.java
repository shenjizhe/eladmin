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

import me.zhengjie.financial.domain.StockDataDay;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.financial.repository.StockDataDayRepository;
import me.zhengjie.financial.service.StockDataDayService;
import me.zhengjie.financial.service.dto.StockDataDayDto;
import me.zhengjie.financial.service.dto.StockDataDayQueryCriteria;
import me.zhengjie.financial.service.mapstruct.StockDataDayMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://eladmin.vip
* @description 服务实现
* @author Jason Shen
* @date 2023-05-11
**/
@Service
@RequiredArgsConstructor
public class StockDataDayServiceImpl implements StockDataDayService {

    private final StockDataDayRepository stockDataDayRepository;
    private final StockDataDayMapper stockDataDayMapper;

    @Override
    public Map<String,Object> queryAll(StockDataDayQueryCriteria criteria, Pageable pageable){
        Page<StockDataDay> page = stockDataDayRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(stockDataDayMapper::toDto));
    }

    @Override
    public List<StockDataDayDto> queryAll(StockDataDayQueryCriteria criteria){
        return stockDataDayMapper.toDto(stockDataDayRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public StockDataDayDto findById(Long id) {
        StockDataDay stockDataDay = stockDataDayRepository.findById(id).orElseGet(StockDataDay::new);
        ValidationUtil.isNull(stockDataDay.getId(),"StockDataDay","id",id);
        return stockDataDayMapper.toDto(stockDataDay);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockDataDayDto create(StockDataDay resources) {
        return stockDataDayMapper.toDto(stockDataDayRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StockDataDay resources) {
        StockDataDay stockDataDay = stockDataDayRepository.findById(resources.getId()).orElseGet(StockDataDay::new);
        ValidationUtil.isNull( stockDataDay.getId(),"StockDataDay","id",resources.getId());
        stockDataDay.copy(resources);
        stockDataDayRepository.save(stockDataDay);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            stockDataDayRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<StockDataDayDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StockDataDayDto stockDataDay : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("股票ID", stockDataDay.getStockId());
            map.put("日期", stockDataDay.getDate());
            map.put("开盘股价", stockDataDay.getEventPriceOpen());
            map.put("收盘股价", stockDataDay.getEventPriceClose());
            map.put("最高股价", stockDataDay.getEventPriceHigh());
            map.put("最低股价", stockDataDay.getEventPriceLow());
            map.put("交易量", stockDataDay.getTradeStockCount());
            map.put("交易额", stockDataDay.getTradeStockVolume());
            map.put("换手率", stockDataDay.getTurnoverRate());
            map.put("涨跌率", stockDataDay.getChangeRate());
            map.put("涨跌值", stockDataDay.getChangeValue());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}