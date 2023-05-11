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

import me.zhengjie.financial.domain.StockEvent;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.financial.repository.StockEventRepository;
import me.zhengjie.financial.service.StockEventService;
import me.zhengjie.financial.service.dto.StockEventDto;
import me.zhengjie.financial.service.dto.StockEventQueryCriteria;
import me.zhengjie.financial.service.mapstruct.StockEventMapper;
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
public class StockEventServiceImpl implements StockEventService {

    private final StockEventRepository stockEventRepository;
    private final StockEventMapper stockEventMapper;

    @Override
    public Map<String,Object> queryAll(StockEventQueryCriteria criteria, Pageable pageable){
        Page<StockEvent> page = stockEventRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(stockEventMapper::toDto));
    }

    @Override
    public List<StockEventDto> queryAll(StockEventQueryCriteria criteria){
        return stockEventMapper.toDto(stockEventRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public StockEventDto findById(Integer id) {
        StockEvent stockEvent = stockEventRepository.findById(id).orElseGet(StockEvent::new);
        ValidationUtil.isNull(stockEvent.getId(),"StockEvent","id",id);
        return stockEventMapper.toDto(stockEvent);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockEventDto create(StockEvent resources) {
        return stockEventMapper.toDto(stockEventRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StockEvent resources) {
        StockEvent stockEvent = stockEventRepository.findById(resources.getId()).orElseGet(StockEvent::new);
        ValidationUtil.isNull( stockEvent.getId(),"StockEvent","id",resources.getId());
        stockEvent.copy(resources);
        stockEventRepository.save(stockEvent);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            stockEventRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<StockEventDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StockEventDto stockEvent : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("股票ID", stockEvent.getStockId());
            map.put("事件类型", stockEvent.getEventType());
            map.put("事件性质", stockEvent.getEventNature());
            map.put("重要程度", stockEvent.getEventLevel());
            map.put("相关网址", stockEvent.getEventUrl());
            map.put("事件消息", stockEvent.getEventMessage());
            map.put("开始日期", stockEvent.getEventStartDate());
            map.put("结束日期", stockEvent.getEventEndDate());
            map.put("事件大类", stockEvent.getEventClass());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}