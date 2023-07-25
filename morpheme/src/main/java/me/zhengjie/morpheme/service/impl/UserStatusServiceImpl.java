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

import me.zhengjie.morpheme.domain.UserStatus;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.morpheme.repository.UserStatusRepository;
import me.zhengjie.morpheme.service.UserStatusService;
import me.zhengjie.morpheme.service.dto.UserStatusDto;
import me.zhengjie.morpheme.service.dto.UserStatusQueryCriteria;
import me.zhengjie.morpheme.service.mapstruct.UserStatusMapper;
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
* @date 2023-07-25
**/
@Service
@RequiredArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserStatusMapper userStatusMapper;

    @Override
    public Map<String,Object> queryAll(UserStatusQueryCriteria criteria, Pageable pageable){
        Page<UserStatus> page = userStatusRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(userStatusMapper::toDto));
    }

    @Override
    public List<UserStatusDto> queryAll(UserStatusQueryCriteria criteria){
        return userStatusMapper.toDto(userStatusRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public UserStatusDto findById(Long id) {
        UserStatus userStatus = userStatusRepository.findById(id).orElseGet(UserStatus::new);
        ValidationUtil.isNull(userStatus.getId(),"UserStatus","id",id);
        return userStatusMapper.toDto(userStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserStatusDto create(UserStatus resources) {
        return userStatusMapper.toDto(userStatusRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserStatus resources) {
        UserStatus userStatus = userStatusRepository.findById(resources.getId()).orElseGet(UserStatus::new);
        ValidationUtil.isNull( userStatus.getId(),"UserStatus","id",resources.getId());
        userStatus.copy(resources);
        userStatusRepository.save(userStatus);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            userStatusRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<UserStatusDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserStatusDto userStatus : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("词根ID", userStatus.getMorphemeId());
            map.put("单词ID", userStatus.getWordId());
            map.put("用户ID", userStatus.getUserId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}