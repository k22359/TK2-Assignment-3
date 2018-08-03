package com.example.tiffanytran.tk2assignment3.controller;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.example.tiffanytran.tk2assignment3.view.GameView;

/**
 * Created by tiffanytran on 5/19/17.
 */

public class GameThread extends Thread {
    private GameController controller;
    private GameView gameView;

    public GameThread(GameController controller, GameView gameView) {
        this.controller = controller;
        this.gameView = gameView;
    }

    public void run() {
        SurfaceHolder sh = gameView.getHolder();
        while (true) {
            Canvas canvas = sh.lockCanvas();
            if (canvas != null) {
                controller.update(); //calls the update function inside our GameController
                gameView.draw(canvas); //calls the draw function inside our GameView
                sh.unlockCanvasAndPost(canvas);
            }
            try{
                sleep(100);
            }
            catch(InterruptedException e) {
                System.out.println("Exception occured");
            }
        }
    }
}