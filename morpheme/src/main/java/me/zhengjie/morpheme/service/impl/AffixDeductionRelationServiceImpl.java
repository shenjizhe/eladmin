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
package me.zhengjie.morpheme.service.impl;

import me.zhengjie.morpheme.domain.AffixDeductionRelation;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.morpheme.repository.AffixDeductionRelationRepository;
import me.zhengjie.morpheme.service.AffixDeductionRelationService;
import me.zhengjie.morpheme.service.dto.AffixDeductionRelationDto;
import me.zhengjie.morpheme.service.dto.AffixDeductionRelationQueryCriteria;
import me.zhengjie.morpheme.service.mapstruct.AffixDeductionRelationMapper;
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
* @date 2023-09-07
**/
@Service
@RequiredArgsConstructor
public class AffixDeductionRelationServiceImpl implements AffixDeductionRelationService {

    private final AffixDeductionRelationRepository affixDeductionRelationRepository;
    private final AffixDeductionRelationMapper affixDeductionRelationMapper;

    @Override
    public Map<String,Object> queryAll(AffixDeductionRelationQueryCriteria criteria, Pageable pageable){
        Page<AffixDeductionRelation> page = affixDeductionRelationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(affixDeductionRelationMapper::toDto));
    }

    @Override
    public List<AffixDeductionRelationDto> queryAll(AffixDeductionRelationQueryCriteria criteria){
        return affixDeductionRelationMapper.toDto(affixDeductionRelationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public AffixDeductionRelationDto findById(Long id) {
        AffixDeductionRelation affixDeductionRelation = affixDeductionRelationRepository.findById(id).orElseGet(AffixDeductionRelation::new);
        ValidationUtil.isNull(affixDeductionRelation.getId(),"AffixDeductionRelation","id",id);
        return affixDeductionRelationMapper.toDto(affixDeductionRelation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AffixDeductionRelationDto create(AffixDeductionRelation resources) {
        return affixDeductionRelationMapper.toDto(affixDeductionRelationRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AffixDeductionRelation resources) {
        AffixDeductionRelation affixDeductionRelation = affixDeductionRelationRepository.findById(resources.getId()).orElseGet(AffixDeductionRelation::new);
        ValidationUtil.isNull( affixDeductionRelation.getId(),"AffixDeductionRelation","id",resources.getId());
        affixDeductionRelation.copy(resources);
        affixDeductionRelationRepository.save(affixDeductionRelation);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            affixDeductionRelationRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<AffixDeductionRelationDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AffixDeductionRelationDto affixDeductionRelation : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("词缀ID", affixDeductionRelation.getAffixId());
            map.put("推导ID", affixDeductionRelation.getDeductionId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}