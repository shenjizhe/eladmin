package me.zhengjie.morpheme.domain;

import lombok.Data;

@Data
public class MorphemePair {
    private MorphemeStudy morphemeStudy;
    private WordDetail word;

    public MorphemePair(MorphemeStudy morphemeStudy, WordDetail word) {
        this.morphemeStudy = morphemeStudy;
        this.word = word;
    }

    public MorphemePair() {
    }
}
