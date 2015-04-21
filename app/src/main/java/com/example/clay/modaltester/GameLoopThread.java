package com.example.clay.modaltester;

import android.graphics.Canvas;

/**
 * Created by Clay on 4/21/2015.
 */
public class GameLoopThread extends Thread {
    private BallGame.CustomDrawableView view;
    private BallGame ballGame;
    private boolean running = false;
    static final long FPS = 10;

    public GameLoopThread(BallGame.CustomDrawableView view, BallGame ballGame) {
        this.view = view;
        this.ballGame = ballGame;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @Override
    public void run() {

//        long ticksPS = 1000 / FPS;
//        long startTime;
//        long sleepTime;
//
//        while (running) {
//
//            Canvas c = null;
//            startTime = System.currentTimeMillis();
//
//            try {
//
//                synchronized (view.getHolder()) {
//
//                        c = view.getHolder().lockCanvas();
//                        System.out.println("canvas: " + c);
//                        System.out.println("view: " +view);
//                        view.draw(c);
//                    }
//                } finally {
//                if (c != null) {
//                    view.getHolder().unlockCanvasAndPost(c);
//                }
//            }
//            sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
//            try{
//                if(sleepTime > 0){
//                    sleep(sleepTime);
//                }else{
//                    sleep(10);
//                }
//            }catch (Exception e) { e.printStackTrace(); }
//        }
    }
}
