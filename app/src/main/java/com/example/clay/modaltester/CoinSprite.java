package com.example.clay.modaltester;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;

/**
 * Created by Clay on 4/21/2015.
 */
public class CoinSprite {
    private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 3;
    private int x = 0;
    private int y = 0;
    private int xSpeed = 5;
    private BallGame ballGame;
    private Bitmap bmp;
    private int currentFrame = 0, height, width;

    public CoinSprite(BallGame ballGame, Bitmap bmp) {
        this.ballGame = ballGame;
        this.bmp = bmp;
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
    }

    private void update() {

        if (x > ballGame.windowWidth - bmp.getWidth() - xSpeed) {
            xSpeed = -5;
        }
        if (x + xSpeed< 0) {
            xSpeed = 5;
        }
        x = x + xSpeed;
        currentFrame = ++currentFrame % BMP_COLUMNS;
    }

    public void onDraw(Canvas canvas) {
        update();
        int srcX = currentFrame * width;
        int srcY = 1 * height;
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        Rect dst = new Rect(x, y, x + width, y + height);
        canvas.drawBitmap(bmp, x , 10, null);
    }
}
