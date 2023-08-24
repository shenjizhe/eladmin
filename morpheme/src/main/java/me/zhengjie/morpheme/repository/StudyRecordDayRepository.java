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

import me.zhengjie.morpheme.domain.Morpheme;
import me.zhengjie.morpheme.domain.StudyRecordDay;
import me.zhengjie.morpheme.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Jason Shen
 * @website https://eladmin.vip
 * @date 2023-08-23
 **/
public interface StudyRecordDayRepository extends JpaRepository<StudyRecordDay, Long>, JpaSpecificationExecutor<StudyRecordDay> {
    @Query("select m from Morpheme m INNER JOIN StudyRecordDay d on m.id = d.objectId where d.uid=:userId and d.date=:date and d.objectType=0")
    List<Morpheme> findMorphemes(@Param("userId") Long userId, @Param("date") Timestamp timestamp);

    @Query("select w from Word w INNER JOIN StudyRecordDay d on w.id = d.objectId where d.uid=:userId and d.date=:date and d.objectType=1 and d.type=0")
    List<Word> findAllWords(@Param("userId") Long userId, @Param("date") Timestamp timestamp);
}