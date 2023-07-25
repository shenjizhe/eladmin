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
    private Map<Long, List<DifferentMorpheme>> differsMap = new LinkedHashMap<>();
    private Map<Long, List<Word>> wordsMap = new LinkedHashMap<>();

    private int morphemeIndex = -1;
    private int wordIndex = -1;

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
            List<Word> words = wordsMap.get(first.getId());
            currentUser.setWordId(words.get(0).getId());

            userStatusRepository.save(currentUser);
        } else {
            currentUser = userStatus;
        }


    }

    private void setMorpheme(Long morphemeId) {
        List<DifferentMorpheme> differs = differentMorphemeRepository.getByMorphemeId(morphemeId);
        List<Word> words = wordRepository.getByMorphemeId(morphemeId);

        differsMap.put(morphemeId, differs);
        wordsMap.put(morphemeId, words);
    }

    public void loadMorpheme() {
        differsMap.clear();
        wordsMap.clear();

        all = morphemeRepository.findAll();
        for (int i = 0; i < all.size(); i++) {
            Morpheme morpheme = all.get(i);
            setMorpheme(morpheme.getId());
        }
    }

    private int indexOfMorpheme(Long morphemeId) {
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(morphemeId)) {
                return i;
            }
        }
        return -1;
    }

    private int indexOfWord(Long morphemeId,Long wordId){
        List<Word> words = wordsMap.get(morphemeId);

        for (int i = 0; i < words.size(); i++) {
            if(words.get(i).getId().equals(wordId)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public Morpheme currentMorpheme() {
        return null;
    }

    @Override
    public Morpheme previousMorpheme() {
        return null;
    }

    @Override
    public Morpheme nextMorpheme() {
        return null;
    }

    @Override
    public Word currentWord() {
        return null;
    }

    @Override
    public Word nextWord() {
        return null;
    }

    @Override
    public Word previousWord() {
        return null;
    }
}