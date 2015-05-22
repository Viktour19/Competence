package com.n4labs.competence;

import java.util.List;

/**
 * Created by root on 5/20/15.
 */
public class Word {
    public String word;
    public String meaning;
    public String origin;
    List<Word> synmonyms;
    public Word() {
    }

    public Word(String meaning, String word) {
        this.meaning = meaning;
        this.word = word;
    }
}


