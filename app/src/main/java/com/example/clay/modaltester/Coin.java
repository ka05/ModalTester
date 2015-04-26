package com.example.clay.modaltester;

import android.graphics.drawable.Drawable;

/**
 * Created by Clay on 4/20/2015.
 */
public class Coin {
    private int xPos;
    private int yPos;
    private int value;
    private String type;

    public Coin(int xPos, int yPos, String type){
        this.xPos = xPos;
        this.yPos = yPos;
        this.type = type;
        switch(type){
            case "reg":
                this.value = 10;
                break;
            case "sp":
                this.value = 50;
                break;
            case "bad":
                this.value = -100;
                break;
        }
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
