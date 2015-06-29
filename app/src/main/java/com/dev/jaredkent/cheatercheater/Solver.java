package com.dev.jaredkent.cheatercheater;

/**
 * Created by JaredKent on 5/23/2015.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class Solver {
    private List<Character> letters;
    private List<String> words;
    Map<Character, Set<String>> dictMap;
    Map<Integer, List<String>> scores;
    String c;
    static Map<Character, Integer> letScore;
    static{
        letScore = new TreeMap<Character, Integer>();
        letScore.put('a', 1);
        letScore.put('b', 3);
        letScore.put('c', 3);
        letScore.put('d', 2);
        letScore.put('e', 1);
        letScore.put('f', 4);
        letScore.put('g', 2);
        letScore.put('h', 4);
        letScore.put('i', 1);
        letScore.put('j', 8);
        letScore.put('k', 5);
        letScore.put('l', 1);
        letScore.put('m', 3);
        letScore.put('n', 1);
        letScore.put('o', 1);
        letScore.put('p', 3);
        letScore.put('q', 10);
        letScore.put('r', 1);
        letScore.put('s', 1);
        letScore.put('t', 1);
        letScore.put('u', 1);
        letScore.put('v', 4);
        letScore.put('w', 4);
        letScore.put('x', 8);
        letScore.put('y', 4);
        letScore.put('z', 10);
    }

    public Solver(String letters, Map<Character, Set<String>> dictMap, String c){
        ArrayList<Character> temp= new ArrayList<Character>();
        for(int i = 0; i < letters.length(); i++){
            temp.add(letters.charAt(i));
        }
        this.words = new ArrayList<String>();
        this.letters = temp;
        this.dictMap = dictMap;

        if(c == null)
            this.c = null;
        else
            this.c = c;
        this.scores = new TreeMap<Integer, List<String>>();
        solve(this.letters, "");
        findScores();
    }




    private void solve(List<Character> letters, String curWord){
        if(wordInMap(curWord) && !words.contains(curWord)){
            if(c == null){
                words.add(curWord);
            }else if(curWord.contains(c)){
                words.add(curWord);
            }
        }
        if(letters.size() > 0) {
            for (Character ch : letters) {
                List<Character> temp = newList(ch, letters);
                curWord += ch;
                solve(temp, curWord);
                curWord = curWord.substring(0, curWord.length() - 1);
            }
        }
    }

    /*
     * finds all of the possible words scores based on the scrabble scoring system
     */
    private void findScores(){
        for(String s : words){
            int score = 0;
            for(int i = 0; i < s.length(); i++){
                score += letScore.get(s.charAt(i));
            }
            if(scores.containsKey(score)){
                scores.get(score).add(s);
            }else{
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(s);
                scores.put(score, temp);
            }

        }
    }

    public List<DescSet> scoreSets(){
        ArrayList<DescSet> scoreSet = new ArrayList<DescSet>();
        for(String s : words){
            int score = 0;
            for(int i = 0; i < s.length(); i++){
                score += letScore.get(s.charAt(i));
            }
            scoreSet.add(new DescSet(score, s));
        }
        Collections.sort(scoreSet);
        return scoreSet;
    }

    /*
     * creates a new list removing the current letter so the list can be passed on
     * to the next call of the method
     */
    private List<Character> newList(Character c, List<Character> letter){
        ArrayList<Character> temp = new ArrayList<Character>();
        boolean found = false;
        for(Character ch: letter){
            if((!ch.equals(c)) || found == true){
                temp.add(ch);
            }else{
                found = true;
            }
        }
        return temp;
    }

    private boolean wordInMap(String word){
        if(word.length() < 1)
            return false;
        char firstLet = word.charAt(0);
        Set<String> temp = dictMap.get(firstLet);
        if(temp.contains(word))
            return true;
        return false;
    }

    public Map<Integer, List<String>> wordsScores(){
        return this.scores;
    }

    public List<String> showWords(){
        return this.words;
    }
}

