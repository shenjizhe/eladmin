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

import me.zhengjie.morpheme.domain.DifferentMorpheme;
import me.zhengjie.morpheme.domain.WordDeduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
* @website https://eladmin.vip
* @author Jason Shen
* @date 2023-05-24
**/
public interface WordDeductionRepository extends JpaRepository<WordDeduction, Long>, JpaSpecificationExecutor<WordDeduction> {
    @Query(value="SELECT\n" +
            "d.id,\n" +
            "d.morpheme_id,\n" +
            "d.word_id,\n" +
            "d.morpheme_text,\n" +
            "d.source_text,\n" +
            "d.full_text,\n" +
            "d.affix,\n" +
            "d.shape,\n" +
            "d.nature,\n" +
            "d.is_derive,\n" +
            "IF(m.meaning_chinese<>'',m.meaning_chinese,d.meaning_chinese) meaning_chinese,\n" +
            "IF(m.meaning_english<>'',m.meaning_english,d.meaning_english) meaning_english\n" +
            "FROM\n" +
            "word_deduction d\n" +
            "INNER JOIN morpheme m ON d.morpheme_id=m.id\n" +
            "WHERE\n" +
            "d.word_id = :wordId AND d.affix=0\n" +
            "UNION\n" +
            "SELECT\n" +
            "d.id,\n" +
            "d.morpheme_id,\n" +
            "d.word_id,\n" +
            "d.morpheme_text,\n" +
            "d.source_text,\n" +
            "d.full_text,\n" +
            "d.affix,\n" +
            "d.shape,\n" +
            "d.nature,\n" +
            "d.is_derive,\n" +
            "IF(a.meaning_chinese<>'',a.meaning_chinese,d.meaning_chinese) meaning_chinese,\n" +
            "IF(a.meaning_english<>'',a.meaning_english,d.meaning_english) meaning_english\n" +
            "FROM\n" +
            "word_deduction d\n" +
            "INNER JOIN affix_deduction_relation r ON d.id=r.deduction_id\n" +
            "INNER JOIN word_affix a ON r.affix_id = a.id\n" +
            "WHERE\n" +
            "d.word_id = :wordId AND (d.affix=1 OR d.affix=2)\n" +
            "UNION\n" +
            "SELECT * FROM word_deduction d \n" +
            "WHERE d.affix=3 AND word_id= :wordId\n" +
            "ORDER BY id",
            nativeQuery=true)
    List<WordDeduction> getByWordId(@Param("wordId")Long wrodId);
    @Query(value = "select * from word_deduction where affix >0 and affix <3", nativeQuery = true)
    List<WordDeduction> getAffixDeductions();
}