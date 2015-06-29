package com.dev.jaredkent.cheatercheater;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by JaredKent on 5/23/2015.
 */
public class DescSet implements Comparable<DescSet>,Serializable{
    private int score;
    private String word;

    public DescSet(int score, String word){
        this.score = score;
        this.word = word;
    }

    public int getScore(){
        return this.score;
    }

    public String getWord(){
        return this.word;
    }

    public String toString(){
        return word + " - " + score;
    }

    public int compareTo(DescSet other){
        return  other.getScore()- this.score;
    }
}
