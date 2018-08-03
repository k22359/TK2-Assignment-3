package com.example.tiffanytran.tk2assignment3.controller;

import com.example.tiffanytran.tk2assignment3.model.DigDug;
import com.example.tiffanytran.tk2assignment3.model.GameMap;
import com.example.tiffanytran.tk2assignment3.model.Monster;
import com.example.tiffanytran.tk2assignment3.model.Rock;
import com.example.tiffanytran.tk2assignment3.view.GameView;

/**
 * Created by tiffanytran on 5/19/17.
 */

public class GameController{
    DigDug digDug;
    Monster[] monsters;
    Rock[] rocks;
    GameMap gameMap;

    private GameThread gameThread;

    public GameController(GameView gameView) {
        gameThread = new GameThread(this, gameView);
        gameThread.start(); //this starts the thread

        this.rocks = gameView.rocks;
        this.gameMap = gameView.gameMap;
        this.monsters = gameView.monsters;
        this.digDug = gameView.digDug;
    }

    public void update() { //update function constantly updates the grid and checks if DigDug dies, etc.

        for (int i = 0; i < monsters.length; i++) { //constantly updates the monsters' attack counter.
            monsters[i].movement(gameMap);
            if(i > 1){
                monsters[i].attack(); //When counter reaches the specified value, it attacks and sees if the attack hits DigDug

                if(monsters[i].monsterAttack){
                    if((digDug.xPos == monsters[i].AttackX) && (digDug.yPos == monsters[i].AttackY)){
                        digDug.alive = false; //DigDug dies he gets hit by the attack
                    }
                }
            }
        }

        for(int i = 0; i < 4; i++){ //this function deflates the monsters when it has already been hit.
            if(monsters[i].inflation-1 >= 0) {
                monsters[i].inflation--; //it deflates by one until it reaches its original size
                if(monsters[i].inflation == 3){
                    monsters[i].alive = 1;
                }
            }
        }

        for (int i = 0; i < rocks.length; i++) { //checks if the rock should fall or not
            if (rocks[i].shouldFall(gameMap)) {
                rocks[i].fall(gameMap); //this makes rock fall

                for (int j = 3; j >= 0; j--) {
                    if (monsters[i].currentlyGhost == 0) { //this checks if the rocks and monsters overlap; it will only do so when the monster is not a ghost
                        if ((rocks[i].yPos == monsters[j].yPos) && (rocks[i].xPos == monsters[j].xPos)) {
                            monsters[j].alive = 1;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < monsters.length; i++) { //checks if the monster and DigDug overlaps
            if(monsters[i].alive == 0) {
                if ((digDug.xPos == monsters[i].xPos) && (digDug.yPos == monsters[i].yPos)) {
                    digDug.alive = false; //DigDug dies
                }
            }
        }

        for (int i = 0; i < rocks.length; i++) { //checks if rock and DigDug overlaps
            if((digDug.xPos == rocks[i].xPos) && (digDug.yPos == rocks[i].yPos)) {
                digDug.alive = false;
            }
        }

        if((monsters[0].alive == 2) && (monsters[1].alive == 2) && (monsters[2].alive == 2) && (monsters[3].alive == 2)){
            monsters[0].allMonstersAlive = false; //if all monsters are dead, end game
        }
    }
}