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

import me.zhengjie.financial.domain.Stock;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.financial.repository.StockRepository;
import me.zhengjie.financial.service.StockService;
import me.zhengjie.financial.service.dto.StockDto;
import me.zhengjie.financial.service.dto.StockQueryCriteria;
import me.zhengjie.financial.service.mapstruct.StockMapper;
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
* @date 2023-05-15
**/
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    @Override
    public Map<String,Object> queryAll(StockQueryCriteria criteria, Pageable pageable){
        Page<Stock> page = stockRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(stockMapper::toDto));
    }

    @Override
    public List<StockDto> queryAll(StockQueryCriteria criteria){
        return stockMapper.toDto(stockRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public StockDto findById(Integer id) {
        Stock stock = stockRepository.findById(id).orElseGet(Stock::new);
        ValidationUtil.isNull(stock.getId(),"Stock","id",id);
        return stockMapper.toDto(stock);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockDto create(Stock resources) {
        return stockMapper.toDto(stockRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Stock resources) {
        Stock stock = stockRepository.findById(resources.getId()).orElseGet(Stock::new);
        ValidationUtil.isNull( stock.getId(),"Stock","id",resources.getId());
        stock.copy(resources);
        stockRepository.save(stock);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            stockRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<StockDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StockDto stock : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("股票代码", stock.getCode());
            map.put("股票名称", stock.getName());
            map.put("股票阶段", stock.getStage());
            map.put("股票角色", stock.getRole());
            map.put("大周期", stock.getCycleBig());
            map.put("小周期", stock.getCycleSmall());
            map.put("股票行业", stock.getIndustry());
            map.put("上市日期", stock.getListDate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}