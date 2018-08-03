package com.example.tiffanytran.tk2assignment3.model;


/**
 * Created by tiffanytran on 5/19/17.
 */

public class Rock extends MovingGameObject {
    public int count;
    GameMap gameMap = new GameMap();
    public boolean shouldFall(GameMap map) {
        this.gameMap = map;
        gameMap.map[xPos][yPos]= 0;
        if(count < 2){
            count++;
            return false;
        }
        else {
            count = 0;
            if (yPos < 12) { //if it is a black tunnel(digDug is also a black tunnel) or grey(monsters) then rock can fall
                if ((gameMap.map[xPos][yPos + 1] == 1) || gameMap.map[xPos][yPos + 1] == 7) {
                    return true;
                } else {
                    return false;
                }
            }
            else return false;
        }
    }

    public void fall(GameMap map) { //this function is for when the rock falls down
        this.gameMap = map;
        gameMap.map[xPos][yPos]= 1; //the rock will be a black tunnel at first
        yPos = yPos+1;
        gameMap.map[xPos][yPos]= 0; //after the rock falls, it becomes "dirt", so that DigDug and the other monsters can't go through it

    }
}
