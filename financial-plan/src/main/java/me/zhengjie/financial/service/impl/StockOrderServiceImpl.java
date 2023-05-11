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

import me.zhengjie.financial.domain.StockOrder;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.financial.repository.StockOrderRepository;
import me.zhengjie.financial.service.StockOrderService;
import me.zhengjie.financial.service.dto.StockOrderDto;
import me.zhengjie.financial.service.dto.StockOrderQueryCriteria;
import me.zhengjie.financial.service.mapstruct.StockOrderMapper;
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
* @date 2023-05-09
**/
@Service
@RequiredArgsConstructor
public class StockOrderServiceImpl implements StockOrderService {

    private final StockOrderRepository stockOrderRepository;
    private final StockOrderMapper stockOrderMapper;

    @Override
    public Map<String,Object> queryAll(StockOrderQueryCriteria criteria, Pageable pageable){
        Page<StockOrder> page = stockOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(stockOrderMapper::toDto));
    }

    @Override
    public List<StockOrderDto> queryAll(StockOrderQueryCriteria criteria){
        return stockOrderMapper.toDto(stockOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public StockOrderDto findById(Long id) {
        StockOrder stockOrder = stockOrderRepository.findById(id).orElseGet(StockOrder::new);
        ValidationUtil.isNull(stockOrder.getId(),"StockOrder","id",id);
        return stockOrderMapper.toDto(stockOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockOrderDto create(StockOrder resources) {
        return stockOrderMapper.toDto(stockOrderRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StockOrder resources) {
        StockOrder stockOrder = stockOrderRepository.findById(resources.getId()).orElseGet(StockOrder::new);
        ValidationUtil.isNull( stockOrder.getId(),"StockOrder","id",resources.getId());
        stockOrder.copy(resources);
        stockOrderRepository.save(stockOrder);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            stockOrderRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<StockOrderDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StockOrderDto stockOrder : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("股票ID", stockOrder.getStockId());
            map.put("交易类型", stockOrder.getTradeType());
            map.put("交易价格", stockOrder.getTradePrice());
            map.put("交易股票数量", stockOrder.getTradeCount());
            map.put("交易时间", stockOrder.getTradeTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}