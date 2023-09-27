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
            "\td.id,\n" +
            "\td.morpheme_id,\n" +
            "\td.word_id,\n" +
            "\td.morpheme_text,\n" +
            "\td.source_text,\n" +
            "\td.full_text,\n" +
            "\td.affix,\n" +
            "\td.shape,\n" +
            "\td.nature,\n" +
            "\td.is_derive,\n" +
            "\n" +
            "IF (\n" +
            "\td.meaning_chinese <> '',\n" +
            "\td.meaning_chinese,\n" +
            "\tm.meaning_chinese\n" +
            ") meaning_chinese,\n" +
            "\n" +
            "IF (\n" +
            "\td.meaning_english <> '',\n" +
            "\td.meaning_english,\n" +
            "\tm.meaning_english\n" +
            ") meaning_english\n" +
            "FROM\n" +
            "\tword_deduction d\n" +
            "INNER JOIN morpheme m ON d.morpheme_id = m.id\n" +
            "WHERE\n" +
            "\td.word_id = :wordId\n" +
            "AND d.affix = 0\n" +
            "UNION\n" +
            "\tSELECT\n" +
            "\t\td.id,\n" +
            "\t\td.morpheme_id,\n" +
            "\t\td.word_id,\n" +
            "\t\td.morpheme_text,\n" +
            "\t\td.source_text,\n" +
            "\t\td.full_text,\n" +
            "\t\td.affix,\n" +
            "\t\td.shape,\n" +
            "\t\td.nature,\n" +
            "\t\td.is_derive,\n" +
            "\n" +
            "\tIF (\n" +
            "\t\td.meaning_chinese <> '',\n" +
            "\t\td.meaning_chinese,\n" +
            "\t\ta.meaning_chinese\n" +
            "\t) meaning_chinese,\n" +
            "\n" +
            "IF (\n" +
            "\td.meaning_english <> '',\n" +
            "\td.meaning_english,\n" +
            "\ta.meaning_english\n" +
            ") meaning_english\n" +
            "FROM\n" +
            "\tword_deduction d\n" +
            "INNER JOIN affix_deduction_relation r ON d.id = r.deduction_id\n" +
            "INNER JOIN word_affix a ON r.affix_id = a.id\n" +
            "WHERE\n" +
            "\td.word_id =  :wordId\n" +
            "AND (d.affix = 1 OR d.affix = 2)\n" +
            "UNION\n" +
            "\tSELECT\n" +
            "\t\t*\n" +
            "\tFROM\n" +
            "\t\tword_deduction d\n" +
            "\tWHERE\n" +
            "\t\td.affix = 3\n" +
            "\tAND word_id =  :wordId\n" +
            "\tORDER BY\n" +
            "\t\tid",
            nativeQuery=true)
    List<WordDeduction> getByWordId(@Param("wordId")Long wrodId);
    @Query(value = "select * from word_deduction where affix >0 and affix <3", nativeQuery = true)
    List<WordDeduction> getAffixDeductions();
}