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
package me.zhengjie.morpheme.repository;

import me.zhengjie.morpheme.domain.StudyWordStatics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

/**
* @website https://eladmin.vip
* @author Jason Shen
* @date 2023-08-29
**/
public interface StudyWordStaticsRepository extends JpaRepository<StudyWordStatics, Long>, JpaSpecificationExecutor<StudyWordStatics> {
    @Query(nativeQuery = true,
            value = "SELECT\n" +
                    "\tobject_id id\n" +
                    "FROM\n" +
                    "\tstudy_word_statics\n" +
                    "WHERE\n" +
                    "\tTIMESTAMPDIFF(DAY,last_review_time,:today) >= memery_level\n" +
                    "\tAND memery_level != 999" +
                    "\tAND uid = :uid")
    List<Long> wordNeedToReview(Long uid, LocalDate today);
    @Query(nativeQuery = true,
            value = "SELECT\n" +
                    "\tobject_id\n" +
                    "FROM\n" +
                    "\tstudy_word_statics\n" +
                    "WHERE\n" +
                    "\tlast_review_time != :today\n" +
                    "AND memery_level = 999\n" +
                    "AND uid = :uid")
    List<Long> word999ToReview(Long uid, LocalDate today);
}