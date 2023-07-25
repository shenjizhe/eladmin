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

import lombok.RequiredArgsConstructor;
import me.zhengjie.morpheme.domain.*;
import me.zhengjie.morpheme.repository.DifferentMorphemeRepository;
import me.zhengjie.morpheme.repository.MorphemeRepository;
import me.zhengjie.morpheme.repository.UserStatusRepository;
import me.zhengjie.morpheme.repository.WordRepository;
import me.zhengjie.morpheme.service.MorphemeService;
import me.zhengjie.morpheme.service.MorphemeStudyService;
import me.zhengjie.morpheme.service.dto.MorphemeDto;
import me.zhengjie.morpheme.service.dto.MorphemeQueryCriteria;
import me.zhengjie.morpheme.service.mapstruct.MorphemeMapper;
import me.zhengjie.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jason Shen
 * @website https://eladmin.vip
 * @description 服务实现
 * @date 2023-05-24
 **/
@Service
@RequiredArgsConstructor
public class MorphemeStudyServiceImpl implements MorphemeStudyService {
    private final MorphemeRepository morphemeRepository;
    private final DifferentMorphemeRepository differentMorphemeRepository;
    private final WordRepository wordRepository;
    private final UserStatusRepository userStatusRepository;
    private List<Morpheme> all;
    private UserStatus currentUser;
    private List<DifferentMorpheme> differs;
    private List<Word> words;
    private int index;

    @PostConstruct
    private void init() {
        loadMorpheme();

        Long currentUserId = SecurityUtils.getCurrentUserId();
        UserStatus userStatus = userStatusRepository.findOneByUserId(currentUserId);
        if (userStatus == null) {
            currentUser = new UserStatus();
            currentUser.setUserId(currentUserId);

            Morpheme first = all.get(0);
            currentUser.setMorphemeId(first.getId());
        } else {
            currentUser = userStatus;
        }
    }

    private void getCurrent(Long morphemeId) {
        differs = differentMorphemeRepository.getByMorphemeId(morphemeId);
        words = wordRepository.getByMorphemeId(morphemeId);


    }

    public void loadMorpheme() {
        all = morphemeRepository.findAll();


    }

    @Override
    public MorphemeStudy current() {

        return null;
    }

    @Override
    public MorphemeStudy previous() {
        return null;
    }

    @Override
    public MorphemeStudy next() {
        return null;
    }
}