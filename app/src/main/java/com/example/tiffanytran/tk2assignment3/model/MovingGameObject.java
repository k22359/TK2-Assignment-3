package com.example.tiffanytran.tk2assignment3.model;

import android.graphics.Bitmap;

/**
 * Created by tiffanytran on 5/19/17.
 */

public abstract class MovingGameObject {

    public int xPos, yPos; //this is so each object has its own xPositon and yPosition
    public Bitmap icons[] = new Bitmap[7]; //icons for moving objects such as monsters, DigDug, rock, ghost, and its different directions

}

