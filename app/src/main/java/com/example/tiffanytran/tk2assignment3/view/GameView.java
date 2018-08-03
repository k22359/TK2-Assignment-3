package com.example.tiffanytran.tk2assignment3.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.Random;

import com.example.tiffanytran.tk2assignment3.R;
import com.example.tiffanytran.tk2assignment3.controller.GameController;
import com.example.tiffanytran.tk2assignment3.controller.GameThread;
import com.example.tiffanytran.tk2assignment3.model.DigDug;
import com.example.tiffanytran.tk2assignment3.model.GameMap;
import com.example.tiffanytran.tk2assignment3.model.Monster;
import com.example.tiffanytran.tk2assignment3.model.Rock;

import static com.example.tiffanytran.tk2assignment3.R.drawable.monster;

/**
 * Created by tiffanytran on 5/19/17.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    public DigDug digDug = new DigDug();
    public Monster[] monsters = new Monster[4];
    public Rock[] rocks = new Rock[3];
    public GameMap gameMap = new GameMap();
    final Random random = new Random();
    int direction = 0;
    int rowSize;
    int columnSize;
    Rect rect = new Rect();
    int xCoordinate;
    int yCoordinate;
    int attack = 0;

    GameController controller = new GameController(this);

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
        System.out.println("Constructor");
        monsters[0] = new Monster();
        monsters[1] = new Monster();
        monsters[2] = new Monster();
        monsters[3] = new Monster();
    }

    private void drawDigDug(Canvas canvas) { //draws digdug
        rect.set(digDug.xPos * rowSize, (digDug.yPos * columnSize), (digDug.xPos + 1) * rowSize, (digDug.yPos + 1) * columnSize);
        canvas.drawBitmap(digDug.icons[direction], null, rect, null);
    }
    private void drawBackground(Canvas canvas) { //draws background which is gameMap.map
        for (int i = 0; i < 13; i++) { //for loop goes through the 13x13 grid
            for (int j = 0; j < 13; j++) {
                rect.set(i * rowSize, (j * columnSize), (i + 1) * rowSize, (j + 1) * columnSize);
                canvas.drawBitmap(gameMap.icons[gameMap.map[i][j]], null, rect, null);
            }
        }

    }

    private void drawMonster(Canvas canvas) { //draws monster
        int monsterDirection = 0;
        int monsterRowSize = rowSize/12;  //makes a smaller rowsize and column size for inflation purposes
        int monsterColumnSize = columnSize/12;
        for (int i = 0; i < 4; i++) { //for loop to draw 4 monsters
            if (monsters[i].alive == 0) { // will only draw if monster is alive
                if (monsters[i].currentlyGhost == 0) { //what to draw when it is not a ghost
                    System.out.println("inflationIs" + monsters[i].inflation);
                    rect.set((monsters[i].xPos * rowSize) - monsters[i].inflation * monsterRowSize, (monsters[i].yPos * columnSize) - monsters[i].inflation * monsterRowSize, (monsters[i].xPos + 1) * rowSize + monsters[i].inflation * monsterRowSize, (monsters[i].yPos + 1) * columnSize + monsters[i].inflation * monsterColumnSize);
                    monsterDirection = monsters[i].direction;
                    if (monsterDirection == 2) { //when the monster is going up, the monster should be facing the right side
                        monsterDirection = 0;
                    } else if (monsterDirection == 3) { //when the monster is going down, the monster should be facing the left side
                        monsterDirection = 1;
                    }
                    canvas.drawBitmap(monsters[i].icons[monsterDirection], null, rect, null);
                    if (monsters[i].monsterAttack) { //checks if the monster should attack. If so, it draws the attack too.
                        drawMonsterAttack(i, canvas);
                    }
                }
                else{ //what to draw when it is a ghost
                    rect.set(monsters[i].xPos * rowSize, (monsters[i].yPos * columnSize), (monsters[i].xPos + 1) * rowSize, (monsters[i].yPos + 1) * columnSize);
                    canvas.drawBitmap(monsters[i].ghostIcon, null, rect, null);
                }
            }
        }
    }

    private void drawRocks(Canvas canvas) {
        for (int i = 0; i < 3; i++) { //for loop to draw 3 rocks
            rect.set(rocks[i].xPos * rowSize, (rocks[i].yPos * columnSize), (rocks[i].xPos + 1) * rowSize, (rocks[i].yPos + 1) * columnSize);
            canvas.drawBitmap(rocks[i].icons[0], null, rect, null);
        }
    }

    public void drawAttack(Canvas canvas){  //draws the attack for Digdug
        int x;
        int y;
        if(direction == 0){
            for (int i = 1; i < 4; i++) { //for loop to draw attack if DigDug is facing to the right
                x = digDug.xPos + i;
                y = digDug.yPos;
                if (x < 13) {
                    if((gameMap.map[x][y] == 1) || (gameMap.map[x][y] == 7)){  //Digdug can only attack black tunnels and grey spaces(monsters)
                        rect.set(x * rowSize, (y * columnSize), (x + 1) * rowSize, (y + 1) * columnSize);
                        canvas.drawBitmap(digDug.icons[4], null, rect, null);
                        attackAndMonsterOverlap(x, y);
                    }
                }
            }
        }
        if(direction == 1){
            for (int i = 1; i < 4; i++) { //for loop to draw attack if DigDug is facing to the left
                x = digDug.xPos - i;
                y = digDug.yPos;
                if(digDug.xPos-i >= 0) {
                    if((gameMap.map[x][y] == 1) || (gameMap.map[x][y] == 7)) {
                        rect.set((x) * rowSize, (y * columnSize), (x + 1) * rowSize, (y + 1) * columnSize);
                        canvas.drawBitmap(digDug.icons[4], null, rect, null);
                        attackAndMonsterOverlap(x, y);
                    }
                }
            }
        }
        if(direction == 2){
            for (int i = 1; i < 4; i++) { //for loop to draw attack if DigDug is facing up
                x = digDug.xPos;
                y = digDug.yPos-i;
                if(y >= 0) {
                    if((gameMap.map[x][y] == 1) || (gameMap.map[x][y] == 7)) {
                        rect.set((x) * rowSize, ((y) * columnSize), (x + 1) * rowSize, (y + 1) * columnSize);
                        canvas.drawBitmap(digDug.icons[5], null, rect, null);
                        attackAndMonsterOverlap(x, y);
                    }
                }
            }
        }
        if(direction == 3){
            for (int i = 1; i < 4; i++) { //for loop to draw attack if DigDug is facing down
                if(digDug.yPos+i < 13) {
                    x = digDug.xPos;
                    y = digDug.yPos+i;
                    if((gameMap.map[x][y] == 1) || (gameMap.map[x][y] == 7)) {
                        rect.set((x) * rowSize, ((y) * columnSize), (x + 1) * rowSize, ((y + 1) * columnSize));
                        canvas.drawBitmap(digDug.icons[5], null, rect, null);
                        attackAndMonsterOverlap(x, y);
                    }
                }
            }
        }

    }

    public void drawMonsterAttack(int i, Canvas canvas) { //draws monster's attack
        int monsterDirection = monsters[i].direction; //sees what direction monster is facing
        if (monsterDirection == 2) {
            monsterDirection = 0;
        } else if (monsterDirection == 3) {
            monsterDirection = 1;
        }

        if (monsterDirection == 0) { //monster attacks in the right direction
            int x = monsters[i].xPos + 1;
            int y = monsters[i].yPos;
            if (x < 13) {
                if ((gameMap.map[x][y] == 1)) {  //monster can only attack black tunnels (digDug is a black tunnel)
                    rect.set((x) * rowSize, (y * columnSize), (x + 1) * rowSize, (y + 1) * columnSize);
                    canvas.drawBitmap(monsters[i].icons[2], null, rect, null);
                    monsters[i].AttackX = x;  //saves the attack location so it can be used to see if it overlaps with digDig inside gameController
                    monsters[i].AttackY = y;

                }
            }
        }
        if (monsterDirection == 1) {  //left direction
            int x = monsters[i].xPos - 1;
            int y = monsters[i].yPos;
            if((x) >= 0) {
                if ((gameMap.map[x][y] == 1)) {
                    rect.set((x) * rowSize, (y * columnSize), (x + 1) * rowSize, (y + 1) * columnSize);
                    canvas.drawBitmap(monsters[i].icons[3], null, rect, null);
                    monsters[i].AttackX = x;
                    monsters[i].AttackY = y;
                }
            }

        }
    }

    private void CheckMonsterOverlap(int i) { //checks if each monster overlaps with the other monsters.
        // If it does, it changes its own x and y position and checks again. This is for initial set up.
        int monstersOverlap = 0;

        for(int j = 0; j<4 ; j++) {
            if (j != i) {
                if (monsters[i].yPos == monsters[j].yPos) {
                    if (monsters[i].xPos == monsters[j].xPos) {
                        monstersOverlap = 1;
                    }
                    if (monsters[i].xPos == monsters[j].xPos + 1) {
                        monstersOverlap = 1;
                    }
                    if (monsters[i].xPos == monsters[j].xPos - 1) {
                        monstersOverlap = 1;
                    }
                        if(monstersOverlap == 1) {
                            monsters[i].xPos = random.nextInt(12);
                            monsters[i].yPos = random.nextInt(10);
                            if (monsters[i].yPos > 4) {
                                monsters[i].yPos += 3;
                            }

                        CheckMonsterOverlap(i);

                    }
                }
            }
        }
    }


    private void CheckRocksOverlap(int i) { //checks if each monster overlaps with the other monsters.
        // If it does, it changes its own x and y position and checks again. This is for initial set up.
        int rocksOverlap = 0;

        for(int j = 0; j<3 ; j++) {
            if (j != i) {
                CheckRocksWithMonsters(i);
                if (rocks[i].yPos == rocks[j].yPos) {
                    if (rocks[i].xPos == rocks[j].xPos) {
                        rocksOverlap = 1;
                    }
                    if (rocks[i].xPos == rocks[j].xPos + 1) {
                        rocksOverlap = 1;
                    }
                    if (rocks[i].xPos == rocks[j].xPos - 1) {
                        rocksOverlap = 1;
                    }
                    if (rocksOverlap == 1) {
                            rocks[i].xPos = random.nextInt(12);
                            rocks[i].yPos = random.nextInt(10);
                            if (rocks[i].yPos > 4) {
                                rocks[i].yPos += 3;
                            }
                        CheckRocksOverlap(i);

                    }
                }
            }
        }
    }

    private void CheckRocksWithMonsters(int i) { //checks if each monster overlaps with the other monsters.
        // If it does, it changes its own x and y position and checks again. This is for initial set up.
        int RocksMonstersOverlap = 0;

        for(int j = 0; j<4 ; j++) {
                if (rocks[i].yPos == monsters[j].yPos) {
                    if (rocks[i].xPos == monsters[j].xPos) {
                        RocksMonstersOverlap = 1;
                    }
                    if (rocks[i].xPos == monsters[j].xPos + 1) {
                        RocksMonstersOverlap = 1;
                    }
                    if (rocks[i].xPos == monsters[j].xPos - 1) {
                        RocksMonstersOverlap = 1;
                    }
                    if(RocksMonstersOverlap == 1) {
                        rocks[i].xPos = random.nextInt(12);
                        rocks[i].yPos = random.nextInt(10);
                        if (rocks[i].yPos > 4) {
                            rocks[i].yPos += 3;
                        }

                        CheckRocksWithMonsters(i);

                    }
                }
            }
        }

    public void attackAndMonsterOverlap(int x, int y){ //if monster is attacked, with increase the inflation count by 2 to offset the natural delation of 1.
        for(int i = 0; i < 4; i++){
            if((x == monsters[i].xPos) && (y == monsters[i].yPos)){
                monsters[i].inflation += 2;
                System.out.println("inflationIs"+monsters[i].inflation);
            }
        }
    }

    public void draw(Canvas canvas) {
        System.out.println("draw()");
        super.draw(canvas);
        // Draw according to the game objects

        canvas.drawColor(Color.BLACK);
        int width = getWidth();
        int height = getHeight();
        rowSize = width / 13;
        columnSize = height / 15;
        Rect rectUp = new Rect();
        Rect rectDown = new Rect();
        Rect rectLeft = new Rect();
        Rect rectRight = new Rect();
        Rect attackButton = new Rect();

        drawBackground(canvas); //background is drawn. Digdug and monsters and rocks are drawn over it.
        drawDigDug(canvas);
        drawMonster(canvas);
        drawRocks(canvas);
        if (attack == 1){ //checks to see if it should draw digDug's attack
            drawAttack(canvas);
            attack = 0;
            drawMonster(canvas);
        }

        rectUp.set((width/2)-50, height-200, (width/2)+50, height-100);
        rectDown.set((width/2)-50, height-100, (width/2)+50, height);
        rectLeft.set((width/2)-150, height-100, (width/2)-50, height);
        rectRight.set((width/2)+50, height-100, (width/2)+150, height);
        attackButton.set((width/2)+200, height-150, (width/2)+300, height-50);
        //draws the arrow and attack keys.
        canvas.drawBitmap(gameMap.icons[2], null, rectUp, null);
        canvas.drawBitmap(gameMap.icons[3], null, rectDown, null);
        canvas.drawBitmap(gameMap.icons[4], null, rectLeft, null);
        canvas.drawBitmap(gameMap.icons[5], null, rectRight, null);
        canvas.drawBitmap(gameMap.icons[6], null, attackButton, null);


        if(digDug.alive == false) { //ends game when digDug dies by drawing a new ending screen
            Paint scoreColor = new Paint(Color.GREEN);
            canvas.drawColor(Color.WHITE); //this makes the background white
            scoreColor.setTextSize(40); //this sets the text size
            String Score = "Game Over!";
            canvas.drawText(Score, 325, 700, scoreColor);
        }

        if(monsters[0].allMonstersAlive == false){ //ends game when all monsters die by drawing a new ending screen
            Paint scoreColor = new Paint(Color.GREEN);
            canvas.drawColor(Color.WHITE); //this makes the background white
            scoreColor.setTextSize(40); //this sets the text size
            String Score = "You win!";
            canvas.drawText(Score, 325, 700, scoreColor);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        System.out.println("surfaceCreated()");
        //initializes all the icons needed
        gameMap.icons[0] = BitmapFactory.decodeResource(getResources(), R.drawable.dirt);
        gameMap.icons[1] = BitmapFactory.decodeResource(getResources(), R.drawable.black);
        gameMap.icons[2] = BitmapFactory.decodeResource(getResources(), R.drawable.up);
        gameMap.icons[3] = BitmapFactory.decodeResource(getResources(), R.drawable.down);
        gameMap.icons[4] = BitmapFactory.decodeResource(getResources(), R.drawable.left);
        gameMap.icons[5] = BitmapFactory.decodeResource(getResources(), R.drawable.right);
        gameMap.icons[6] = BitmapFactory.decodeResource(getResources(), R.drawable.attack);
        gameMap.icons[7] = BitmapFactory.decodeResource(getResources(), R.drawable.gray);

        digDug.icons[0] = BitmapFactory.decodeResource(getResources(), R.drawable.digdug);
        digDug.icons[1] = BitmapFactory.decodeResource(getResources(), R.drawable.digdug_left);
        digDug.icons[2] = BitmapFactory.decodeResource(getResources(), R.drawable.digdug_up);
        digDug.icons[3] = BitmapFactory.decodeResource(getResources(), R.drawable.digdug_down);
        digDug.icons[4] = BitmapFactory.decodeResource(getResources(), R.drawable.line2);
        digDug.icons[5] = BitmapFactory.decodeResource(getResources(), R.drawable.line_vertical);

        monsters[0].icons[0] = BitmapFactory.decodeResource(getResources(), R.drawable.monsters_right);
        monsters[0].icons[1] = BitmapFactory.decodeResource(getResources(), monster);
        monsters[1].icons[0] = BitmapFactory.decodeResource(getResources(), R.drawable.monsters_right);
        monsters[1].icons[1] = BitmapFactory.decodeResource(getResources(), monster);

        monsters[2].icons[0] = BitmapFactory.decodeResource(getResources(), R.drawable.fire);
        monsters[2].icons[1] = BitmapFactory.decodeResource(getResources(), R.drawable.fire_left);
        monsters[2].icons[2] = BitmapFactory.decodeResource(getResources(), R.drawable.fire_attack);
        monsters[2].icons[3] = BitmapFactory.decodeResource(getResources(), R.drawable.fire_attack_left);

        monsters[3].icons[0] = BitmapFactory.decodeResource(getResources(), R.drawable.fire);
        monsters[3].icons[1] = BitmapFactory.decodeResource(getResources(), R.drawable.fire_left);
        monsters[3].icons[2] = BitmapFactory.decodeResource(getResources(), R.drawable.fire_attack);
        monsters[3].icons[3] = BitmapFactory.decodeResource(getResources(), R.drawable.fire_attack_left);

        monsters[0].ghostIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);
        monsters[1].ghostIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);
        monsters[2].ghostIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);
        monsters[3].ghostIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);

        rocks[0] = new Rock();
        rocks[1] = new Rock();
        rocks[2] = new Rock();

        rocks[0].icons[0] = BitmapFactory.decodeResource(getResources(), R.drawable.rock);
        rocks[1].icons[0] = BitmapFactory.decodeResource(getResources(), R.drawable.rock);
        rocks[2].icons[0] = BitmapFactory.decodeResource(getResources(), R.drawable.rock);

        //drawing initial backgound
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                gameMap.map[i][j] = 0;
            }
        }
        //setting digDug initial position
        digDug.xPos = 6;
        digDug.yPos = 6;
        gameMap.map[6][6] = 1;

        //setting initial monster position
        for (int i = 0; i < 4; i++) {
            monsters[i].xPos = random.nextInt(12);
            monsters[i].yPos = random.nextInt(10);
            if (monsters[i].yPos > 4) {
                monsters[i].yPos += 3;
            }
        }
        for (int i = 0; i < 4; i++){
            CheckMonsterOverlap(i);
            }

        //setting initial tunnels for monsters. One tunnel to the left and right of every monster.
        for(int i = 0; i < 4; i++) {
            System.out.println("monsterx"+monsters[i].xPos+" "+"monstery"+monsters[i].yPos);
            gameMap.map[monsters[i].xPos][monsters[i].yPos] = 1;
            if((monsters[i].xPos+1) < 13) {
                gameMap.map[monsters[i].xPos + 1][monsters[i].yPos] = 1;
            }
            if((monsters[i].xPos) > 0) {
                gameMap.map[monsters[i].xPos - 1][monsters[i].yPos] = 1;
            }
        }

        //intializing rocks
        for (int i = 0; i < 3; i++) {
            rocks[i].xPos = random.nextInt(12);
            rocks[i].yPos = random.nextInt(10);
            if (rocks[i].yPos > 4) {
                rocks[i].yPos += 3;
            }
        }
        for (int i = 0; i < 3; i++){
            CheckRocksOverlap(i);
        }
        for (int i = 0; i < 3; i++){
            gameMap.map[rocks[i].xPos][rocks[i].yPos] = 0;
        }


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("onTouchEvent()");
        int width = getWidth();
        int height = getHeight();
        int invalid;
        if (event.getAction() == MotionEvent.ACTION_DOWN) { //will only get a X and Y coordinate once when user presses down
            xCoordinate = (int) event.getX();
            yCoordinate = (int) event.getY();
            invalid = 0; //will not allow DigDug to move in a invalid spot (rock)

            if ((xCoordinate >= ((width / 2) - 50)) && (xCoordinate <= (width / 2) + 50) && (yCoordinate >= (height - 200)) && (yCoordinate <= (height - 100))) {
                System.out.println("UP");
                if (digDug.yPos > 0) {

                    for(int i = 0; i<3; i++){ //checks to see if Digdug tried to go onto a rock
                        if((rocks[i].xPos == digDug.xPos) && (rocks[i].yPos == digDug.yPos-1)){
                            invalid = 1;
                        }
                    }

                    if (invalid == 0) {  //resets original DigDig position if so
                        digDug.yPos -= 1;
                        gameMap.map[digDug.xPos][digDug.yPos] = 1;
                    }
                }
                direction = 2;
            }
            if ((xCoordinate >= ((width / 2) - 50)) && (xCoordinate <= (width / 2) + 50) && (yCoordinate >= (height - 100)) && (yCoordinate <= height)) {
                System.out.println("DOWN");
                if (digDug.yPos < 12) {

                    for(int i = 0; i<3; i++){
                        if((rocks[i].xPos == digDug.xPos) && (rocks[i].yPos == digDug.yPos+1)){
                            invalid = 1;
                        }
                    }

                    if (invalid == 0) {
                        digDug.yPos += 1;
                        gameMap.map[digDug.xPos][digDug.yPos] = 1;
                    }
                }
                direction = 3;
            }
            if ((xCoordinate >= ((width / 2) - 150)) && (xCoordinate <= (width / 2) - 50) && (yCoordinate >= (height - 100)) && (yCoordinate <= height)) {
                System.out.println("LEFT");
                if (digDug.xPos > 0) {

                    for(int i = 0; i<3; i++){
                        if((rocks[i].xPos == digDug.xPos-1) && (rocks[i].yPos == digDug.yPos)){
                            invalid = 1;
                        }
                    }

                    if (invalid == 0) {
                        digDug.xPos -= 1;
                        gameMap.map[digDug.xPos][digDug.yPos] = 1;
                    }
                }
                direction = 1;
            }
            if ((xCoordinate >= ((width / 2) + 50)) && (xCoordinate <= (width / 2) + 150) && (yCoordinate >= (height - 100)) && (yCoordinate <= height)) {
                System.out.println("RIGHT");
                if (digDug.xPos < 12) {

                    for(int i = 0; i<3; i++){
                        if((rocks[i].xPos == digDug.xPos+1) && (rocks[i].yPos == digDug.yPos)){
                            invalid = 1;
                        }
                    }

                    if (invalid == 0) {
                        digDug.xPos += 1;
                        gameMap.map[digDug.xPos][digDug.yPos] = 1;
                    }
                }
                direction = 0;
            }
            if ((xCoordinate >= ((width / 2) + 200)) && (xCoordinate <= (width / 2) + 300) && (yCoordinate >= (height - 150)) && (yCoordinate <= (height - 50))){
                System.out.println("ATTACK");
                attack = 1;

                }

            return true;
        }
        return false;

    }
}

