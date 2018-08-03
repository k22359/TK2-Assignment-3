package com.example.tiffanytran.tk2assignment3.model;

import android.graphics.Bitmap;

import java.util.Random;

/**
 * Created by tiffanytran on 5/19/17.
 */

public class Monster extends MovingGameObject {
    public int alive = 0;
    public Bitmap ghostIcon;
    public int direction;
    final Random random = new Random();
    public int count;
    public int attackCount = random.nextInt(2);
    public int inflation;
    public boolean monsterAttack = false;
    public int AttackX;
    public int AttackY;
    public boolean allMonstersAlive = true;
    public int ghostCount;
    public int currentlyGhost = 0;
    public void movement(GameMap gameMap) {
        //sets the background to black tunnel(1) as the background for a monster is a grey picture(7)
        if(alive == 1){ //if monster just died, the background is edited
            alive = 2; // 2 means that the monster has been dead for awhile so that it does not repeatedly edit the background where the monster previously died
            gameMap.map[xPos][yPos] = 1; //1 is black tunnel. As it was a grey picture under monster before.
        }
        if (alive == 0) { //if monster is still alive
            if (currentlyGhost == 0) {// checks if monster is not a ghost
                if (ghostCount < 40) { //counter for when monster should become a ghost
                    ghostCount++;
                    if (count < 2) {//counter to slow down monster
                        count++;
                    } else { //else the monster can move again
                        count = 0;
                        if (inflation == 0) { //so that the monster can not move when it is being attacked
                            if (direction == 0) { //right
                                if (xPos + 1 < 13) { //checks boundaries for x position
                                    if (gameMap.map[xPos + 1][yPos] == 1) {  //if black tunnel
                                        gameMap.map[xPos][yPos] = 1; //monster's previous spot turns into a tunnel
                                        xPos++;
                                        gameMap.map[xPos][yPos] = 7; //monster's current spot becomes grey
                                    } else {
                                        direction = random.nextInt(4); //randomized direction so that the monster moves randomly
                                    }
                                } else {
                                    direction = random.nextInt(4);
                                }
                            }

                            if (direction == 1) { //left
                                if (xPos - 1 >= 0) { //checks boundaries for x position to go to the left, same logic as the right direction
                                    if (gameMap.map[xPos - 1][yPos] == 1) {
                                        gameMap.map[xPos][yPos] = 1;
                                        xPos--;
                                        gameMap.map[xPos][yPos] = 7;
                                    } else {
                                        direction = random.nextInt(4);
                                    }
                                } else {
                                    direction = random.nextInt(4);
                                }
                            }

                            if (direction == 2) { //up
                                if (yPos - 1 >= 0) { //checks boundaries for y position to go up
                                    if (gameMap.map[xPos][yPos - 1] == 1) {
                                        gameMap.map[xPos][yPos] = 1;
                                        yPos--;
                                        gameMap.map[xPos][yPos] = 7;
                                    } else {
                                        direction = random.nextInt(4);
                                    }
                                } else {
                                    direction = random.nextInt(4);
                                }
                            }
                            if (direction == 3) { //down
                                if (yPos + 1 < 13) { //checks boundaries for y position to go down
                                    if (gameMap.map[xPos][yPos + 1] == 1) {
                                        gameMap.map[xPos][yPos] = 1;
                                        yPos++;
                                        gameMap.map[xPos][yPos] = 7;
                                    } else {
                                        direction = random.nextInt(4);
                                    }
                                } else {
                                    direction = random.nextInt(4);
                                }
                            }
                        }


                    }
                } else { //else it should become a ghost
                    currentlyGhost = 1; //monster is a ghost
                    gameMap.map[xPos][yPos] = 1; //when it initially becomes a ghost, the original monster's spot becomes a black tunnel
                }
            }
            else{ //ghost movement
                setNewX(); //sets a new x and y position.
                setNewY();

                if(gameMap.map[xPos][yPos] == 1){ // monster stops being a ghost when it overlaps with a black tunnel
                    currentlyGhost = 0;
                    ghostCount = 0;
                }
            }
        }
    }
    public void setNewX(){
        int x;
        x = random.nextInt(3);
        if (x == 2) {
            x = -1;
        }
        xPos += x;

        if ((xPos > 12)  ||  (xPos < 0)) {
            xPos-=x;
            setNewX();
        }
    }

    public void setNewY(){

        int y;
        y = random.nextInt(3);
        if (y == 2) {
            y = -1;
        }
        yPos += y;

        if ((yPos > 12)  ||  (yPos < 0)) {
            yPos-=y;
            setNewY();
        }
    }
    public void attack() {
        if (attackCount < 10) { //attack counter so the attack frequency is not too fast
            attackCount++;
            monsterAttack = false;
        }

        else { //the monster will attack
            attackCount = 0;
            if (direction == 0) { //attack in the right direction
                if (xPos + 1 < 13) {
                    monsterAttack = true;
                }
            }

            else if (direction == 1) { //attacking in the left direction
                if (xPos - 1 >= 0) {
                    monsterAttack = true;
                }
            }
            else {
                monsterAttack =  false; //will not attack because the attack will be out of the board.
            }
        }
    }
}