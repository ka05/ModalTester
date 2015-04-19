package com.example.clay.modaltester;

/**
 * Created by Clay on 4/11/2015.
 */
public class Level {
    private String levelName;
    private String imgSrc;

    public Level(String levelName, String imgSrc){
        this.levelName = levelName;
        this.imgSrc = imgSrc;
    }

    public String getImgSrc() {
        return imgSrc;
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
}
