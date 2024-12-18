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

import me.zhengjie.financial.domain.StockData;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.financial.repository.StockDataRepository;
import me.zhengjie.financial.service.StockDataService;
import me.zhengjie.financial.service.dto.StockDataDto;
import me.zhengjie.financial.service.dto.StockDataQueryCriteria;
import me.zhengjie.financial.service.mapstruct.StockDataMapper;
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
public class StockDataServiceImpl implements StockDataService {

    private final StockDataRepository stockDataRepository;
    private final StockDataMapper stockDataMapper;

    @Override
    public Map<String,Object> queryAll(StockDataQueryCriteria criteria, Pageable pageable){
        Page<StockData> page = stockDataRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(stockDataMapper::toDto));
    }

    @Override
    public List<StockDataDto> queryAll(StockDataQueryCriteria criteria){
        return stockDataMapper.toDto(stockDataRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public StockDataDto findById(Integer id) {
        StockData stockData = stockDataRepository.findById(id).orElseGet(StockData::new);
        ValidationUtil.isNull(stockData.getId(),"StockData","id",id);
        return stockDataMapper.toDto(stockData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockDataDto create(StockData resources) {
        return stockDataMapper.toDto(stockDataRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StockData resources) {
        StockData stockData = stockDataRepository.findById(resources.getId()).orElseGet(StockData::new);
        ValidationUtil.isNull( stockData.getId(),"StockData","id",resources.getId());
        stockData.copy(resources);
        stockDataRepository.save(stockData);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            stockDataRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<StockDataDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StockDataDto stockData : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("股票ID", stockData.getStockId());
            map.put("总市值", stockData.getMarketValue());
            map.put("流通市值", stockData.getMarketValueCircle());
            map.put("均价", stockData.getAveragePrice());
            map.put("动态市盈率", stockData.getForwardPe());
            map.put("静态市盈率", stockData.getStaticPe());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}