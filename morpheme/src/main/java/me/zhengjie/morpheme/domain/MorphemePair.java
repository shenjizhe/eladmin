package me.zhengjie.morpheme.domain;

import lombok.Data;

@Data
public class MorphemePair {
    private MorphemeStudy morphemeStudy;
    private Word word;

    public MorphemePair(MorphemeStudy morphemeStudy, Word word) {
        this.morphemeStudy = morphemeStudy;
        this.word = word;
    }

    public MorphemePair() {
    }
}
