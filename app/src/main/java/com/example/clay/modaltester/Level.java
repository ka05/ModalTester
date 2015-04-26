package com.example.clay.modaltester;

import java.util.ArrayList;

/**
 * Created by Clay on 4/11/2015.
 */
public class Level {
    private String levelName;
    private String imgSrc;
    private String levelCompleted;
    private ArrayList<Coin> coins;

    // later will want to probably pass in the levelId from database results and get the levels coins
    public Level(String levelName, String imgSrc, String levelCompleted){
        this.levelName = levelName;
        this.imgSrc = imgSrc;
        this.levelCompleted = levelCompleted;
        // will want to get / initialize the level's coin locations here
        initCoins();
    }

    public void initCoins(){
        coins = new ArrayList<Coin>();

        // ideally we would want to get these from DB or generate them using math, rather than hardcoding.

        // psudo code:
        // loop through array of points existing for each level's boundaries
        // and do math to position the coin equidistant from each wall.

        coins.add(new Coin(20, 180, "bad"));
        coins.add(new Coin(20, 260, "sp"));
        coins.add(new Coin(180, 20, "reg"));
        coins.add(new Coin(180, 600, "reg"));
        coins.add(new Coin(300, 100, "sp"));
        coins.add(new Coin(600, 100, "bad"));
    }

    public String getImgSrc() {
        return imgSrc;
    }
    public ArrayList<Coin> getCoins() {
        return coins;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelCompleted() {
        return levelCompleted;
    }

    public void setLevelCompleted(String levelCompleted) {
        this.levelCompleted = levelCompleted;
    }
}
