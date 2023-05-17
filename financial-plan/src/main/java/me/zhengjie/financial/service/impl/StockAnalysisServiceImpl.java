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

import me.zhengjie.financial.domain.StockAnalysis;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.financial.repository.StockAnalysisRepository;
import me.zhengjie.financial.service.StockAnalysisService;
import me.zhengjie.financial.service.dto.StockAnalysisDto;
import me.zhengjie.financial.service.dto.StockAnalysisQueryCriteria;
import me.zhengjie.financial.service.mapstruct.StockAnalysisMapper;
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
* @date 2023-05-16
**/
@Service
@RequiredArgsConstructor
public class StockAnalysisServiceImpl implements StockAnalysisService {

    private final StockAnalysisRepository stockAnalysisRepository;
    private final StockAnalysisMapper stockAnalysisMapper;

    @Override
    public Map<String,Object> queryAll(StockAnalysisQueryCriteria criteria, Pageable pageable){
        Page<StockAnalysis> page = stockAnalysisRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(stockAnalysisMapper::toDto));
    }

    @Override
    public List<StockAnalysisDto> queryAll(StockAnalysisQueryCriteria criteria){
        return stockAnalysisMapper.toDto(stockAnalysisRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public StockAnalysisDto findById(Integer id) {
        StockAnalysis stockAnalysis = stockAnalysisRepository.findById(id).orElseGet(StockAnalysis::new);
        ValidationUtil.isNull(stockAnalysis.getId(),"StockAnalysis","id",id);
        return stockAnalysisMapper.toDto(stockAnalysis);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockAnalysisDto create(StockAnalysis resources) {
        return stockAnalysisMapper.toDto(stockAnalysisRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StockAnalysis resources) {
        StockAnalysis stockAnalysis = stockAnalysisRepository.findById(resources.getId()).orElseGet(StockAnalysis::new);
        ValidationUtil.isNull( stockAnalysis.getId(),"StockAnalysis","id",resources.getId());
        stockAnalysis.copy(resources);
        stockAnalysisRepository.save(stockAnalysis);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            stockAnalysisRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<StockAnalysisDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StockAnalysisDto stockAnalysis : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("股票ID", stockAnalysis.getStockId());
            map.put("平均价格", stockAnalysis.getPriceAvg());
            map.put("低位价格", stockAnalysis.getPriceLow());
            map.put("低位价格90", stockAnalysis.getPriceLow90());
            map.put("低位价格70", stockAnalysis.getPriceLow70());
            map.put("集中度90", stockAnalysis.getConcentration90());
            map.put("集中度70", stockAnalysis.getConcentration70());
            map.put("主力持仓成本", stockAnalysis.getMainHoldCost());
            map.put("高位价格", stockAnalysis.getPriceHigh());
            map.put("高位价格90", stockAnalysis.getPriceHigh90());
            map.put("高位价格70", stockAnalysis.getPriceHigh70());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}