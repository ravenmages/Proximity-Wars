import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class MainRunStuff extends PApplet {


/////////////////////////////////DECLARE CUSTOM FUNCTIONS/////////////////////////////////////////////////////////////////////////////////////////

public boolean collision(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2) {        //collision detection

  boolean result = false;

  if (x1 + w1 >= x2 && x1 <= x2 + w2 && y1 + h1 >= y2 && y1 <= y2 + h2) {

    result = true;
  }

  return result;
}

public void setAttack(float radius, int i) {     //deals with attacking

  float x = fighters.get(i).x - radius;
  float y = fighters.get(i).y - radius;
  float w = radius * 2;      //*2 to make it a square with that diameter. 
  float h = radius * 2;

  //note fighting units attacks in this order; fighters, harvesters, barracks, then bases. However the AI unit attacks the same way but moves to places in this order; bases, barracks, harvesters, fighters.
  //the AI will also move to the latest build buildings/units in the specificed order as above. 
  //essentially checks to see if it has a target in range, am I already attacking, if so dont attack, if not, attack and make sure everyone knows im attacking.
  for (int j = 0; j < fighters.size(); j++) {

    if (fighters.get(i).attackMode == true && fighters.get(i).id == eID && fighters.get(j).id != fighters.get(i).id) {
      fighters.get(i).ai(fighters.get(j).x, fighters.get(j).y);
    }

    if (collision(x, y, w, h, fighters.get(j).x, fighters.get(j).y, fighters.get(j).w, fighters.get(j).h) && i!=j && fighters.get(i).id != fighters.get(j).id) {                 
      if (fighters.get(i).id == eID && fighters.get(i).id != fighters.get(j).id) {

        fighters.get(i).stopMovement(0, 0, 0, 0, 0);
      }
      fighters.get(i).attack(fighters.get(j).x +  fighters.get(j).w/2, fighters.get(j).y +  fighters.get(j).h/2);
    }
  }

  for (int j = 0; j < harvesters.size(); j++) {

    if (fighters.get(i).attackMode == true && fighters.get(i).id == eID && harvesters.get(j).id != fighters.get(i).id) {
      fighters.get(i).ai(harvesters.get(j).x, harvesters.get(j).y);
    } else if (fighters.get(i).defMode == true && fighters.get(i).id == eID && harvesters.get(j).id == fighters.get(i).id) {
      fighters.get(i).ai(harvesters.get(j).x, harvesters.get(j).y);
    }

    if (collision(x, y, w, h, harvesters.get(j).x, harvesters.get(j).y, harvesters.get(j).w, harvesters.get(j).h)) {
      if (fighters.get(i).id == eID && ((fighters.get(i).attackMode == false && fighters.get(i).id == harvesters.get(j).id) || (fighters.get(i).attackMode == true && fighters.get(i).id != harvesters.get(j).id))) {

        fighters.get(i).stopMovement(0, 0, 0, 0, 0);
      }
      if (fighters.get(i).id != harvesters.get(j).id) fighters.get(i).attack(harvesters.get(j).x +  harvesters.get(j).w/2, harvesters.get(j).y +  harvesters.get(j).h/2);
    }
  }

  for (int j = 0; j < tBarracks.size(); j++) {

    if (fighters.get(i).attackMode == true && fighters.get(i).id == eID && tBarracks.get(j).id != fighters.get(i).id && tBarracks.get(j).ghost == false) {
      fighters.get(i).ai(tBarracks.get(j).x, tBarracks.get(j).y);
    } else if (fighters.get(i).defMode == true && fighters.get(i).id == eID && tBarracks.get(j).id == fighters.get(i).id && tBarracks.get(j).ghost == false) {
      fighters.get(i).ai(tBarracks.get(j).x, tBarracks.get(j).y);
    }

    if (collision(x, y, w, h, tBarracks.get(j).x, tBarracks.get(j).y, tBarracks.get(j).w, tBarracks.get(j).h) && tBarracks.get(j).ghost == false) {
      if (fighters.get(i).id == eID && ((fighters.get(i).attackMode == false && fighters.get(i).id == tBarracks.get(j).id) || (fighters.get(i).attackMode == true && fighters.get(i).id != tBarracks.get(j).id))) {

        fighters.get(i).stopMovement(0, 0, 0, 0, 0);
      }
      if (fighters.get(i).id != tBarracks.get(j).id) fighters.get(i).attack(tBarracks.get(j).x +  tBarracks.get(j).w/2, tBarracks.get(j).y +  tBarracks.get(j).h/2);
    }
  }

  for (int j = 0; j < bases.size(); j++) {

    if (fighters.get(i).attackMode == true && fighters.get(i).id == eID && bases.get(j).id != fighters.get(i).id && bases.get(j).ghost == false) {
      fighters.get(i).ai(bases.get(j).x, bases.get(j).y);
    } else if (fighters.get(i).defMode == true && fighters.get(i).id == eID && bases.get(j).id == fighters.get(i).id && bases.get(j).ghost == false) {
      fighters.get(i).ai(bases.get(j).x, bases.get(j).y);
    }

    if (collision(x, y, w, h, bases.get(j).x, bases.get(j).y, bases.get(j).w, bases.get(j).h) && bases.get(j).ghost == false) {
      if (fighters.get(i).id == eID && ((fighters.get(i).attackMode == false && fighters.get(i).id == bases.get(j).id) || (fighters.get(i).attackMode == true && fighters.get(i).id != bases.get(j).id))) {

        fighters.get(i).stopMovement(0, 0, 0, 0, 0);
      }
      if (fighters.get(i).id != bases.get(j).id) fighters.get(i).attack(bases.get(j).x +  bases.get(j).w/2, bases.get(j).y +  bases.get(j).h/2);
    }
  }
}




public void bulletCollide(int i, float dmg) {                    //checks if bullets have hit

  bullets.get(i).hit = false; 

  float x = bullets.get(i).x;
  float y = bullets.get(i).y;


  for (int j = 0; j < fighters.size(); j++) {
    if (collision(x, y, 0, 0, fighters.get(j).x, fighters.get(j).y, fighters.get(j).w, fighters.get(j).h) && bullets.get(i).id != fighters.get(j).id) {
      fighters.get(j).getDmged(dmg);
      bullets.get(i).x = -100000;
      bullets.get(i).y = -100000;
    }
  }

  for (int j = 0; j < harvesters.size(); j++) {
    if (collision(x, y, 0, 0, harvesters.get(j).x, harvesters.get(j).y, harvesters.get(j).w, harvesters.get(j).h) && bullets.get(i).id != harvesters.get(j).id) {
      harvesters.get(j).getDmged(dmg);
      bullets.get(i).x = -100000;
      bullets.get(i).y = -100000;
    }
  }

  for (int j = 0; j < tBarracks.size(); j++) {
    if (collision(x, y, 0, 0, tBarracks.get(j).x, tBarracks.get(j).y, tBarracks.get(j).w, tBarracks.get(j).h) && bullets.get(i).id != tBarracks.get(j).id) {
      tBarracks.get(j).getDmged(dmg);
      bullets.get(i).x = -100000;
      bullets.get(i).y = -100000;
    }
  }

  for (int j = 0; j < bases.size(); j++) {
    if (collision(x, y, 0, 0, bases.get(j).x, bases.get(j).y, bases.get(j).w, bases.get(j).h) && bullets.get(i).id != bases.get(j).id) {
      bases.get(j).getDmged(dmg);
      bullets.get(i).x = -100000;
      bullets.get(i).y = -100000;
    }
  }
}



public void fixMove() {                            //we detect whos colliding and when are they doing so


  //make all collisions false first

  for (int i = 0; i < harvesters.size(); i++) {

    harvesters.get(i).moveCollide = false;
  }

  for (int i = 0; i < fighters.size(); i++) {

    fighters.get(i).moveCollide = false;
  }

  for (int i = 0; i < bases.size(); i++) {

    bases.get(i).collision = false;
  }

  for (int i = 0; i < tBarracks.size(); i++) {

    tBarracks.get(i).collision = false;
  }


  //check harvester for collisions. 

  for (int i = 0; i < harvesters.size(); i++) {
    for (int j = 0; j < harvesters.size(); j++) {


      if (collision(harvesters.get(i).x, harvesters.get(i).y, harvesters.get(i).w, harvesters.get(i).h, harvesters.get(j).x, harvesters.get(j).y, harvesters.get(j).w, harvesters.get(j).h) == true && i != j) {

        harvesters.get(i).moveCollide = true; 
        harvesters.get(j).moveCollide = true;
      }
    }


    for (int j = 0; j < mines.length; j++) {

      if (collision(mines[j].x, mines[j].y, mines[j].w, mines[j].h, harvesters.get(i).x, harvesters.get(i).y, harvesters.get(i).w, harvesters.get(i).h)) {

        harvesters.get(i).moveCollide = true;
      }
    }

    for (int j = 0; j < bases.size(); j++) {

      if (collision(bases.get(j).x, bases.get(j).y, bases.get(j).w, bases.get(j).h, harvesters.get(i).x, harvesters.get(i).y, harvesters.get(i).w, harvesters.get(i).h)) {

        harvesters.get(i).moveCollide = true;
        bases.get(j).collision = true;
      }
    }

    for (int j = 0; j < tBarracks.size(); j++) {

      if (collision(tBarracks.get(j).x, tBarracks.get(j).y, tBarracks.get(j).w, tBarracks.get(j).h, harvesters.get(i).x, harvesters.get(i).y, harvesters.get(i).w, harvesters.get(i).h)) {

        harvesters.get(i).moveCollide = true;
        tBarracks.get(j).collision = true;
      }
    }

    for (int j = 0; j < fogOwar.length; j++) {
      for (int h = 0; h < fogOwar[0].length; h++) {
        //sets the background to opaque so we can see if its the player's stuff
        if (collision(fogXCords[j][h], fogYCords[j][h], 100, 100, harvesters.get(i).x - revealRadius, harvesters.get(i).y - revealRadius, harvesters.get(i).w + revealRadius * 2, harvesters.get(i).h + revealRadius * 2) && harvesters.get(i).id == playerID) {

          fogOwar[j][h] = 0;
        }
      }
    }
  }

  //checks fighters with other stuff
  for (int i = 0; i < fighters.size(); i++) {

    for (int j = 0; j < fighters.size(); j++) {

      if (collision(fighters.get(i).x, fighters.get(i).y, fighters.get(i).w, fighters.get(i).h, fighters.get(j).x, fighters.get(j).y, fighters.get(j).w, fighters.get(j).h) == true && i != j) {

        fighters.get(i).moveCollide = true;
        fighters.get(j).moveCollide = true;
      }
    }


    for (int j = 0; j < harvesters.size(); j++) {


      if (collision(fighters.get(i).x, fighters.get(i).y, fighters.get(i).w, fighters.get(i).h, harvesters.get(j).x, harvesters.get(j).y, harvesters.get(j).w, harvesters.get(j).h) == true) {

        fighters.get(i).moveCollide = true; 
        harvesters.get(j).moveCollide = true;
      }
    }


    for (int j = 0; j < mines.length; j++) {

      if (collision(mines[j].x, mines[j].y, mines[j].w, mines[j].h, fighters.get(i).x, fighters.get(i).y, fighters.get(i).w, fighters.get(i).h)) {

        fighters.get(i).moveCollide = true;
      }
    }

    for (int j = 0; j < bases.size(); j++) {

      if (collision(bases.get(j).x, bases.get(j).y, bases.get(j).w, bases.get(j).h, fighters.get(i).x, fighters.get(i).y, fighters.get(i).w, fighters.get(i).h)) {

        fighters.get(i).moveCollide = true;
        bases.get(j).collision = true;
      }
    }

    for (int j = 0; j < tBarracks.size(); j++) {

      if (collision(tBarracks.get(j).x, tBarracks.get(j).y, tBarracks.get(j).w, tBarracks.get(j).h, fighters.get(i).x, fighters.get(i).y, fighters.get(i).w, fighters.get(i).h)) {

        fighters.get(i).moveCollide = true;
        tBarracks.get(j).collision = true;
      }
    }

    for (int j = 0; j < fogOwar.length; j++) {
      for (int h = 0; h < fogOwar[0].length; h++) {
        //sets the background to opaque so we can see if its the player's stuff
        if (collision(fogXCords[j][h], fogYCords[j][h], 100, 100, fighters.get(i).x - revealRadius, fighters.get(i).y - revealRadius, fighters.get(i).w + revealRadius * 2, fighters.get(i).h + revealRadius * 2) && fighters.get(i).id == playerID) {

          fogOwar[j][h] = 0;
        }
      }
    }
  }

  //Check the barracks with other buildings

  for (int i = 0; i < tBarracks.size(); i++) {

    for (int j = 0; j < tBarracks.size(); j++) {

      if (collision(tBarracks.get(j).x, tBarracks.get(j).y, tBarracks.get(j).w, tBarracks.get(j).h, tBarracks.get(i).x, tBarracks.get(i).y, tBarracks.get(i).w, tBarracks.get(i).h) && i!=j) {

        tBarracks.get(i).collision = true;
        tBarracks.get(j).collision = true;
      }
    }

    for (int j = 0; j < bases.size(); j++) {

      if (collision(bases.get(j).x, bases.get(j).y, bases.get(j).w, bases.get(j).h, tBarracks.get(i).x, tBarracks.get(i).y, tBarracks.get(i).w, tBarracks.get(i).h)) {

        tBarracks.get(i).collision = true;
        bases.get(j).collision = true;
      }
    }

    for (int j = 0; j < mines.length; j++) {

      if (collision(mines[j].x, mines[j].y, mines[j].w, mines[j].h, tBarracks.get(i).x, tBarracks.get(i).y, tBarracks.get(i).w, tBarracks.get(i).h)) {

        tBarracks.get(i).collision = true;
      }
    }

    for (int j = 0; j < fogOwar.length; j++) {
      for (int h = 0; h < fogOwar[0].length; h++) {
        //sets the background to opaque so we can see if its the player's stuff
        if (collision(fogXCords[j][h], fogYCords[j][h], 100, 100, tBarracks.get(i).x - revealRadius/2, tBarracks.get(i).y - revealRadius/2, tBarracks.get(i).w + revealRadius/2 * 2, tBarracks.get(i).h + revealRadius/2 * 2) && tBarracks.get(i).id == playerID && tBarracks.get(i).ghost == false) {

          fogOwar[j][h] = 0;
        }
      }
    }
  }

  //check bases with itself (all other checks already computed)

  for (int i = 0; i < bases.size(); i++) {

    for (int j = 0; j < bases.size(); j++) {

      if (collision(bases.get(j).x, bases.get(j).y, bases.get(j).w, bases.get(j).h, bases.get(i).x, bases.get(i).y, bases.get(i).w, bases.get(i).h) && i!=j) {

        bases.get(i).collision = true;
        bases.get(j).collision = true;
      }
    }

    for (int j = 0; j < fogOwar.length; j++) {
      for (int h = 0; h < fogOwar[0].length; h++) {
        //sets the background to opaque so we can see if its the player's stuff
        if (collision(fogXCords[j][h], fogYCords[j][h], 100, 100, bases.get(i).x - revealRadius, bases.get(i).y - revealRadius, bases.get(i).w + revealRadius * 2, bases.get(i).h + revealRadius * 2) && bases.get(i).id == playerID && bases.get(i).ghost == false) {

          fogOwar[j][h] = 0;
        }
      }
    }
  }
}

public void scrollCamera() {                //lets the camera move around the screen

  if (mouseX > width - offSetSensitivity && !(boundMaxX > width * mapSize)) {

    scrollSpeedX = scrollSpeed * -1;
    boundMinX += scrollSpeedX;
    boundMaxX +=scrollSpeedX * -1;
    //println("Right " + boundMinX); 
    //println("Right2 " + boundMaxX);
  } else if (mouseX < offSetSensitivity && !(boundMinX >= 0)) {

    scrollSpeedX = scrollSpeed; 
    boundMinX += scrollSpeedX;
    boundMaxX +=scrollSpeedX * -1;
    //println("Left " + boundMinX); 
    //println("Left2 " + boundMaxX);
  } else {

    scrollSpeedX = 0;
  }

  if (mouseY > height - offSetSensitivity && !(boundMaxY > height * mapSize)) {

    scrollSpeedY = -1 * scrollSpeed; 
    boundMinY += scrollSpeedY;
    boundMaxY +=scrollSpeedY * -1;
    //println("Down " + boundMinY); 
    //println("Down2 " + boundMaxY);
  } else if (mouseY < offSetSensitivity && !(boundMinY >= 0)) {

    scrollSpeedY = scrollSpeed; 
    boundMinY += scrollSpeedY;
    boundMaxY +=scrollSpeedY * -1;
    //println("Up " + boundMinY); 
    //println("Up2 " + boundMaxY);
  } else {

    scrollSpeedY = 0;
  }
}

public float[] movementCalc(float x1, float y1, float x2, float y2, float speed) {          //for bullets and moving, etc...

  float result[] = new float[2]; 
  float dx, dy, d;
  float sx, sy; 

  d = dist(x1, y1, x2, y2);
  dx = x2 - x1;
  dy = y2 - y1;

  sx = dx/d * speed;
  sy = dy/d * speed;

  result[0] = sx;
  result[1] = sy;

  return result;
}

public int randomChoice(int numOfChoice) {                  //spits out the random choice int. 

  int result = 0;
  int[] choices = new int[numOfChoice];

  for (int i = 0; i < choices.length; i++) {

    choices[i] = i;
  }

  result = choices[floor(random(numOfChoice))];

  return result;
}

public void applyButtonText(ArrayList<Button> a, int index, String text) {        //gives a button text

  a.get(index).info = text;
}

public void baseMineFix(ArrayList<Base> a, int i) {            //makes sure mines dont go over the only placed start building.

  for (int j = 0; j < a.size(); j++) {

    if (collision(mines[i].x, mines[i].y, mines[i].w, mines[i].h, a.get(j).x, a.get(j).y, a.get(j).w, a.get(j).h)) {

      mines[i] = new Mine(random(width * mapSize - 40), random(height * mapSize - 40), 40, 40, new RgbCol(255, 255, 0), 0, -1);
    }
  }
}

public void mineMineFix(int i) {              //prevents overlap of mines

  for (int j = 0; j < mines.length; j++) {

    if (collision(mines[i].x, mines[i].y, mines[i].w, mines[i].h, mines[j].x, mines[j].y, mines[j].w, mines[j].h) && i != j) {

      mines[i] = new Mine(random(width * mapSize - 40), random(height * mapSize - 40), 40, 40, new RgbCol(255, 255, 0), 0, -1);
    }
  }
}

public void mineBarracksFix(ArrayList<Barracks> b, int i) {          //prevents mine barracks overlap

  for (int j = 0; j < b.size(); j++) {

    if (collision(mines[i].x, mines[i].y, mines[i].w, mines[i].h, b.get(j).x, b.get(j).y, b.get(j).w, b.get(j).h)) {

      mines[i] = new Mine(random(width * mapSize - 40), random(height * mapSize - 40), 40, 40, new RgbCol(255, 255, 0), 0, -1);
    }
  }
}


public void mineRandom() {                  //intilizes the mines.

  for (int i = 0; i < mines.length; i++) {

    mines[i] = new Mine(random(width * mapSize - 40), random(height * mapSize - 40), 40, 40, new RgbCol(255, 255, 0), 0, -1); 

    baseMineFix(bases, i);
  }

  for (int i = 0; i < mines.length; i++) {

    mineMineFix(i);
  }

  for (int i = 0; i < mines.length; i++) {

    mineBarracksFix(tBarracks, i);
  }
}

public boolean searchMines() {            //searches through the mines

  boolean result = false; 

  for (int i = 0; i < mines.length; i++) {

    if (mines[i].collision == true) result = true;
  }

  return result;
}

public int enemyFightTask(int pCountF, int eCountF) {                        //determines to attack or not to attack                          

  int task = 0;                                                       //1 is attack, 0 is defend/not attack
  float attack = eCountF - pCountF * 0.5f; 

  if (attack > 0) task = 1;

  return task;
}

Selector selector = new Selector();               //what the player uses to select stuff

public Mine[] bubbleSort(Mine[] nums) {                  //sort the mines command

  for (int j = 0; j < nums.length; j++) {  
    for (int i = 0; i < nums.length - 1; i++) {


      if (dist(width * mapSize, height * mapSize, nums[i].x, nums[i].y) < dist(brX, brY, nums[i + 1].x, nums[i + 1].y)) {

        Mine store = nums[i];
        nums[i] = nums[i+1];
        nums[i+1] = store;
      }
    }
  }

  return nums;
}

public void menuButtonsClicked() {        //run in mousePressed to see whats clicked.

  for (int i = 0; i < menuButtons.size(); i++) {
    menuButtons.get(i).selected();
  }
}

public void resetMenuButtonClicks() {      //ensures buttons dont mess with each other even when on other screens.

  for (int i = 0; i < menuButtons.size(); i++) {

    menuButtons.get(i).clicked = false;
  }
}

public void mainMenuButtonJobs() {              //the jobs of buttons on the MAIN MENU 

  if (menuButtons.get(0).clicked == true) {                          //play button

    screen = 1;
    resetMenuButtonClicks();
  } else if (menuButtons.get(1).clicked == true) {                    //instructions

    screen = 3;
    resetMenuButtonClicks();
  } else if (menuButtons.get(2).clicked == true) {                    //options. 

    screen = 2;
    resetMenuButtonClicks();
  }
}

public void returnToMenuButtonJob(int i) {                    //job of return to menu button

  if (menuButtons.get(i).clicked == true) {

    screen = 0; 
    resetMenuButtonClicks();

    if (wonGame == true || lostGame == true) {      //if its because game is over, reset the game! 
      setup(); 
      //println(lostGame + "" + wonGame);
    }
  }
}

public void returnToMenuButton(float x, float y, float w, float h, float displaceX, float displaceY, int r, int g, int b, int i) {              //move a return to menu button depending on screen

  menuButtons.get(i).x = x;
  menuButtons.get(i).y = y;
  menuButtons.get(i).w = w;
  menuButtons.get(i).h = h;
  noStroke();
  menuButtons.get(i).highLight();
  menuButtons.get(i).render(20);
  menuButtons.get(i).drawText(menuButtons.get(i).x + displaceX, menuButtons.get(i).y + displaceY, r, g, b);
  returnToMenuButtonJob(i);
  stroke(0);
}

public float[] incrementTextYLoc(float y, int lines, int size, int spacing) {          //vertically increments the text. 

  float[] result = new float[lines];

  float currentPos = y;

  for (int i = 0; i < lines; i++) {

    if (i > 0) result[i] = currentPos + size + spacing; 
    else result[i] = currentPos;
    currentPos = result[i];
  }

  return result;
}

public void fogOfWar() {                                      //handles the fog of wars main stuff (the changing of the fog is in the collisions section!) 

  for (int i = 0; i < fogOwarX; i++) {
    for (int j = 0; j < fogOwarY; j++) {

      noStroke();
      fill(0, 0, 0, fogOwar[i][j]);

      rect(fogXCords[i][j], fogYCords[i][j], 100, 100);

      fogXCords[i][j] += scrollSpeedX;
      fogYCords[i][j] += scrollSpeedY;


      if (fogOwar[i][j] < 255) fogOwar[i][j]++;
      stroke(0);
    }
  }
}

public void endGame(boolean win, boolean lose, int i) {    //losing or winning screen.


  //println(menuButtons.get(4).clicked); 
  if (win == true) {

    fill(100, 100, 0); 
    stroke(255, 0, 0);
    strokeWeight(4);
    rect(width/2 - 150, height/2 - 125, 300, 250);
    fill(0);
    stroke(0);
    strokeWeight(1);
    textSize(30);
    text("You Win!", width/2 - 60, height/2); 
    returnToMenuButton(width/2-80, height/2 + 50, 170, 40, 10, 30, 0, 0, 0, i);
    textSize(12);
  } else if (lose == true) {

    fill(100, 100, 0); 
    stroke(255, 0, 0);
    strokeWeight(4);
    rect(width/2 - 150, height/2 - 125, 300, 250);
    fill(0);
    stroke(0);
    strokeWeight(1);
    textSize(30);
    text("You Lose", width/2 - 60, height/2); 
    returnToMenuButton(width/2-80, height/2 + 50, 170, 40, 10, 30, 0, 0, 0, i);
    textSize(12);
  }
}

/////////////////////////////////////////////////DECLARE ALL VARIABLES ////////////////////////////////////////////////////////

ArrayList<Harvester> harvesters;                         //unit arraylists
ArrayList<Fighter> fighters; 
int playerID;                                            //keep track of ids
int eID; 
boolean mouseOccupied; 
boolean shopOccupied; //is a shop already open? handles the multiple shop problem.

Minimap minimap; 

float scrollSpeed, scrollSpeedY, scrollSpeedX;                          //all for scrolling and camera
float offSetSensitivity; 
float boundMinX, boundMaxX, boundMinY, boundMaxY, mapSize; 

int[] resources;        

ArrayList<Base> bases;                                                 //more arraylists(buildings)
ArrayList<Barracks> tBarracks;

Mine[] mines;                                //mines

ArrayList<Missile> bullets;                       //bullets

ArrayList<Float> targsX;              //stores stuff to fire at.
ArrayList<Float> targsY;
ArrayList<Integer> targID;

int pFighters;                          //counts the number of stuff.
int eFighters;
int pHarvesters;
int eHarvesters; 
int mineCount; 
int pBases;
int eBases;

int taskFight;

float brX;        //bottom right of map cords; 
float brY;  

boolean wonGame;         //game conditions
boolean lostGame;

int screen;            //what screen is it on

PImage bg;             //main screen background

ArrayList<Button> menuButtons;            //menubutton stuff

int[][] fogOwar;          //for fog of war
int fogOwarX;
int fogOwarY;
int fogXCords[][];
int fogYCords[][]; 
float revealRadius;


//////////////////////// INTIALIZE STUFF ////////////////////////////////////////////////////////////////

public void setup() {                //intialize all stuff that needs ot be

  

  bg = loadImage("background.png");

  menuButtons = new ArrayList<Button>(); 


  for (int i = 0; i < 4; i++) {            //main screen buttons and default return to menu button.
    menuButtons.add(new Button(60, 150 + i * 60, 190, 40, new RgbCol(255, 255, 0), 0));
  }

  menuButtons.add(new Button(-1000, -1000, 190, 40, new RgbCol(255, 255, 0), 0));        //win/lose return to menu button

  applyButtonText(menuButtons, 0, "Play");
  applyButtonText(menuButtons, 1, "Instructions");
  applyButtonText(menuButtons, 2, "Options");
  applyButtonText(menuButtons, 3, "Main Menu");
  applyButtonText(menuButtons, 4, "Main Menu");          //this is the lose/win mainmenu button.

  screen = 0;                    //0 is the home screen, 1 is the game screen, 2 is the options screen, 3 is the instructions screen.
  mines = new Mine[10];

  mouseOccupied = false;    //used to ensure placement works withotu doubling up things.
  shopOccupied = false;

  scrollSpeed = 5;
  scrollSpeedY = 0;
  scrollSpeedX = 0;
  offSetSensitivity = 5;

  mapSize = 3.0f; 

  boundMinX = 0;
  boundMinY = 0;
  boundMaxX = width;
  boundMaxY = height;

  brX = mapSize * width; 
  brY = mapSize * height; 

  fogOwarX = ceil((mapSize * width)/ 100);
  fogOwarY = ceil((mapSize * height)/ 100);
  fogOwar = new int[fogOwarX][fogOwarY];
  fogXCords = new int[fogOwarX][fogOwarY];
  fogYCords = new int[fogOwarX][fogOwarY];
  revealRadius = 300;


  for (int i = 0; i < fogOwarX; i++) {
    for (int j = 0; j < fogOwarY; j++) {

      fogOwar[i][j] = 255;
      fogXCords[i][j] = i * 100;
      fogYCords[i][j] = j * 100;
    }
  }

  resources = new int[2];
  resources[playerID] = 0;
  resources[eID] = 0;

  wonGame = false;
  lostGame = false;

  minimap = new Minimap(637-106, 3, 100, 100);

  harvesters = new ArrayList<Harvester>();
  fighters = new ArrayList<Fighter>();

  targsX = new ArrayList<Float>();
  targsY = new ArrayList<Float>();
  targID = new ArrayList<Integer>();

  bullets = new ArrayList<Missile>();

  playerID = 0;
  eID = 1;

  taskFight = -1; 


  mineCount = mines.length;


  for (int i = 0; i < 1; i++) {
    harvesters.add(new Harvester(300, 100, 20, 20, playerID));
  }

  for (int i = 0; i < 1; i++) {
    harvesters.add(new Harvester(width * mapSize - 300, height * mapSize - 300, 20, 20, eID));
  }

  bases = new ArrayList<Base>();

  bases.add(new Base(100, 100, 100, 100, new RgbCol(255, 0, 0), playerID, 1000, -1));
  bases.add(new Base(width * mapSize - 200, height * mapSize - 200, 100, 100, new RgbCol(0, 0, 255), eID, 1000, -1));


  tBarracks = new ArrayList<Barracks>();

  tBarracks.add(new Barracks(300, 300, 60, 60, new RgbCol(255, 0, 0), playerID, -1));
  tBarracks.add(new Barracks(width * mapSize - 500, height * mapSize - 200, 60, 60, new RgbCol(0, 0, 255), eID, -1));

  //unghost the initial buildings;

  for (int i = 0; i < bases.size(); i++) {

    bases.get(i).ghost = false;
  }

  for (int i = 0; i < tBarracks.size(); i++) {

    tBarracks.get(i).ghost = false;
  }


  mineRandom();    //spawns initial mines and makes sure they dont overlap the bases
  mines = bubbleSort(mines);                //sort the mines according to distance from bottom right corner(closest to enemey base!);
}


////////////////////////////////////////////////// MAIN RUN STUFF /////////////////////////////////////////////////////////////////////


public void draw() {

  if (screen == 0) {                                                //main menu

    image(bg, 0, 0, 640, 480);                      //the bg picture. 
    //text and title
    textSize(30);
    fill(0, 0, 0);
    text("PROXIMITY WARS", width/2, height/2);
    rect(width/2 - 30, 60, 1, height - 120);    //center line in main menu
    //buttons stuff.
    for (int i = 0; i < 3; i++) {                  //only displays the 3 buttons (no need to show return to main menu.
      noStroke();
      menuButtons.get(i).highLight();
      menuButtons.get(i).render(20);
      menuButtons.get(i).drawText(menuButtons.get(i).x + 10, menuButtons.get(i).y + 30, 0, 0, 0);
    }
    mainMenuButtonJobs(); //let them do their jobs

    stroke(0);
  } else if (screen == 1) {                                                      //the actual game


    fixMove();                                                                //collisions
    scrollCamera();                                                          //move around the map with mouse.

    taskFight = enemyFightTask(pFighters, eFighters);                      //lets see if we should attack. 

    pFighters = 0;                      //reset our counters. 
    eFighters = 0;
    pHarvesters = 0;
    eHarvesters = 0;
    pBases = 0;
    eBases = 0;

    brX += scrollSpeedX;                                                //move the bottom right corner cordinates around(IMPORTANT FOR THE ENEMY AI TO KNOW WHERE THE CLOSEST MINES ARE TO THEIR STARTING LOCATION HOME BASE)
    brY += scrollSpeedY; 


    fill(255);
    rect(0, 0, width, height);

    selector.mainFunc();


    for (int i = 0; i < mines.length; i++) {                  //has to be here so everything can overlap ontop of it.

      mines[i].die();
      mines[i].mainFunc();
    }

    for (int i = 0; i < fighters.size(); i++) {            //stuff for fighters

      setAttack(200, i); 
      fighters.get(i).mainFunc();
      if (fighters.get(i).id == playerID)  minimap.addPos(fighters.get(i).trX, fighters.get(i).trY);




      if (fighters.get(i).id == playerID) pFighters++; 
      else {

        eFighters++;
        if (taskFight == 1) {
          fighters.get(i).attackMode = true; 
          fighters.get(i).defMode = false;
        } else {
          fighters.get(i).attackMode = false;
          fighters.get(i).defMode = true;
        }
      }

      if (fighters.get(i).death() == true) {            //checks if its dead, if so, kill it.
        fighters.remove(i); 
        i--;
      }
    }



    for (int i = 0; i < harvesters.size(); i++) {                  //harvesters stuff

      harvesters.get(i).mainFunc();
      if (harvesters.get(i).id == playerID)   minimap.addPos(harvesters.get(i).trX, harvesters.get(i).trY);

      if (harvesters.get(i).id == playerID) pHarvesters++; 
      else eHarvesters++;

      if (harvesters.get(i).death() == true) {
        harvesters.remove(i); 
        i--;
      }
    }




    for (int i = 0; i < bases.size(); i++) {              //bases stuff

      bases.get(i).mainFunc();

      bases.get(i).getIndex(i);

      if (bases.get(i).id == eID) {
        bases.get(i).baseAI();
        eBases++;
      } else if (bases.get(i).id == playerID) pBases++; 

      if (bases.get(i).death() == true) {
        bases.remove(i); 
        i--;
      }
    }

    for (int i = 0; i < tBarracks.size(); i++) {          //barracks stuff
      //println(tBarracks.get(i).collision + "$");
      tBarracks.get(i).mainFunc();

      if (tBarracks.get(i).death() == true) {
        tBarracks.remove(i); 
        i--;
      }
    }


    //This section runs the bullets and their death.
    for (int i = 0; i < bullets.size(); i++) {                  //bullets

      bulletCollide(i, 20);
      bullets.get(i).paint();

      if (bullets.get(i).lifeTime <= 0) {

        bullets.remove(i); 

        i--;
      }
    }

    stroke(0);

    //running shops seperatly prevents them from going under buildings. 
    //same for the health bar

    for (int i = 0; i < bases.size(); i++) {
      bases.get(i).healthBar();
    }
    for (int i = 0; i < tBarracks.size(); i++) {
      tBarracks.get(i).healthBar();
    }

    fogOfWar();

    for (int i = 0; i < bases.size(); i++) {

      bases.get(i).runShop();
    }
    for (int i = 0; i < tBarracks.size(); i++) {

      tBarracks.get(i).runShop();
    }




    minimap.render();    //minimap

    fill(255, 0, 0);
    text("Resources: " + resources[playerID], minimap.x - 100, 25);


    //handles the main menu button and its specific placement for this screen. 
    textSize(12);
    returnToMenuButton(5, 5, 90, 30, 10, 20, 255, 0, 0, 3);    


    //check if win/lose yet.
    if (pBases <= 0) lostGame = true;
    else if (eBases <= 0) {
      wonGame = true; 
      //println(eBases);
    }
    endGame(wonGame, lostGame, 4);

    //println(wonGame + " @ " + lostGame);


    //println(frameRate);
  } else if (screen == 2) {                            //options screen

    image(bg, 0, 0, 640, 480);                      //the bg picture. 
    //text and title
    textSize(30);
    fill(0, 0, 0);
    text("OPTIONS", width/2 - 70, 100);
    rect(40, 110, width - 80, 1);        //horizontal bar on screen.
    returnToMenuButton(width-210, height - 90, 180, 30, 10, 25, 0, 0, 0, 3);
    text("NO CHANGEABLE OPTIONS!",40,155);
  } else if (screen == 3) {                          //instructions screen

    image(bg, 0, 0, 640, 480);                      //the bg picture. 
    //text and title
    textSize(30);
    fill(0, 0, 0);
    text("INSTRUCTIONS", width/2 - 100, 100);
    rect(40, 110, width - 80, 1);        //horizontal bar on screen.
    returnToMenuButton(width-210, height - 90, 180, 30, 10, 25, 0, 0, 0, 3);
    textSize(14);

    //instructional text! 
    float[] temp = incrementTextYLoc(135, 15, 14, 3);

    text("The aim of the game is to destroy all the bases (squares with health bars over", 40, temp[0]);
    text("them) of the blue opponent. To do so, you can build fighting units and harve-", 40, temp[1]);
    text("sters from your barracks (the circular buildings). To attack, select (left click or left", 40, temp[2]);
    text("drag) and move (right cick) your fighters near the enemy, they will attack opposing", 40, temp[3]);
    text("units in this order; fighters, harvesters, barracks, and finally bases. To get", 40, temp[4]);
    text("resources, select your harvesters and click on the mines (gray squares), if your", 40, temp[5]);
    text("harvester is in range it will mine resource for you. NOTE that your bases can only", 40, temp[6]);
    text("build 5 buildings which can be any combination of barracks or more bases. NEW ", 40, temp[7]);
    text("bases can build 5 more buildings. Thus, ensure you always build atleast one base", 40, temp[8]);
    text("from the current base. The remaning building amount is shown in green on each", 40, temp[9]);
    text("base. Also NOTE that you can only build buildings in a specificed radius", 40, temp[10]);
    text("from the base you are building from. Buildings also reveal more fog of war", 40, temp[11]);
    text("permantely! Bases do this more than barracks do! To build buildings select your", 40, temp[12]);
    text("bases, to build units select your barracks. To move the camera, put your mouse", 40, temp[13]);
    text("at the edge of the screen in the desired direction. Good Luck!", 40, temp[14]);
  }
}


///////////////////////////////////////// MOUSE COMMANDS/OPERATIONS /////////////////////////////////////////////////////////////////////


public void mousePressed() {


  if (mouseButton == LEFT) { //lets the click select work

    menuButtonsClicked(); 
    selector.onMousePress();
    for (int i = 0; i < harvesters.size(); i++) {                                      //units do stuff

      harvesters.get(i).selectUpdate(selector.isSelectedClick(harvesters.get(i).x, harvesters.get(i).y, harvesters.get(i).w, harvesters.get(i).h));
    }

    for (int i = 0; i < fighters.size(); i++) {

      fighters.get(i).selectUpdate(selector.isSelectedClick(fighters.get(i).x, fighters.get(i).y, fighters.get(i).w, fighters.get(i).h));
    }

    
    for (int i = 0; i < bases.size(); i++) {                      //lets the buildings do their thing when clicking.

      bases.get(i).mouseDown();
    }

    for (int i = 0; i < tBarracks.size(); i++) {

      tBarracks.get(i).mouseDown();
    }
  } else if (mouseButton == RIGHT) {                              

    selector.clickAway();  
    for (int i = 0; i < harvesters.size(); i++) {                  //lets movement happen!
      harvesters.get(i).onRightClick(mouseX, mouseY);
    }

    for (int i = 0; i < fighters.size(); i++) {
      fighters.get(i).mouseRight();
      //setAttack(0,i,true);                  //attack when right click.
    }
  }
}


public void mouseDragged() {

  if (mouseButton == LEFT) {                        //Lets the drag select work.
    selector.onMouseDrag();

    for (int i = 0; i < harvesters.size(); i++) {    //selection by drag
      harvesters.get(i).selectUpdate(selector.isSelectedDrag(harvesters.get(i).x, harvesters.get(i).y, harvesters.get(i).w, harvesters.get(i).h));
    }

    for (int i = 0; i < fighters.size(); i++) {

      fighters.get(i).selectUpdate(selector.isSelectedDrag(fighters.get(i).x, fighters.get(i).y, fighters.get(i).w, fighters.get(i).h));
    }
  } else if (mouseButton == RIGHT) {
  }
}

public void mouseReleased() {

  selector.clickAway();      //resets the box when click released
}
class Barracks extends Building {

  int harvestersCost;
  int fightersCost; 
  int coolDown; 

  Barracks(float a, float b, float c, float d, RgbCol e, int ID,int builderIndex) {
    super(a, b, c, d, e, ID, builderIndex);

    this.buttonAmt = 2;
    this.health = 500;
    
    this.harvestersCost = 10;
    this.fightersCost = 20; 
    this.coolDown = 150; 



    this.intializeButtons();

    applyButtonText(this.buttons, 0, "Harvester: $" + this.harvestersCost);
    applyButtonText(this.buttons, 1, "Fighter: $" + this.fightersCost);
  }

  public void mainFunc() {
    
    if(this.ghost == false && this.healthBarIntialized == false) {              //ensures new barracks get h bars. 
      this.intializeHealthBarVars(); 
      this.healthBarIntialized = true;
    }
    
    this.render(); 
    this.place();
    this.barracksAI(); 
   

  }
  
  public void barracksAI(){                    
    
   this.coolDown--;
   if(this.id == eID && this.coolDown <= 0){
     
     if(taskFight != 1 && resources[this.id] >= this.fightersCost && eHarvesters > 2){          //if you need to attack and you can afford it, do it
       this.buildFighters();
       this.coolDown = 150;
     }else if(mineCount > 0 && resources[this.id] >= this.harvestersCost * 2 && eHarvesters < 15) {
       
       this.buildHarvesters(); 
       this.coolDown = 150; 
     }
     
     
   }
    
  }

  public void runShop() {

    if (this.openShop == true && this.id == playerID && this.ghost == false) {

      this.shopDisplay();
      shopOccupied = true; 

      this.drawButtonText();

      if (this.buttons.get(0).clicked == true && mouseOccupied == false) {            //buy harvesters

        if (resources[this.id] >= this.harvestersCost) {
          this.buildHarvesters(); 
          this.buttons.get(0).clicked = false;

        } else this.buttons.get(0).clicked = false;
      } else if (this.buttons.get(1).clicked == true && mouseOccupied == false) {        //buy fighters
        if (resources[this.id] >= this.fightersCost) {
          this.buildFighters();
          this.buttons.get(1).clicked = false;
        } else this.buttons.get(1).clicked = false;
      }
    }
  }
  
  public void buildHarvesters(){
    harvesters.add(new Harvester(this.x + this.w/2, this.y + this.h/2, 20, 20, this.id));
    resources[this.id] -= this.harvestersCost;
  }
  
  public void buildFighters(){
    fighters.add(new Fighter(this.x + this.w/2, this.y + this.h/2, 20, 20, this.id));
    resources[this.id] -= this.fightersCost;
  }



  public void render() {

    fill(this.r, this.g, this.b);
    rect(this.x, this.y, this.w, this.h, 50);
    this.parralax();
  }
}
class Base extends Building {

  int selfIndex; 
  int barracksCost;
  int basesCost; 
  int buildTimer; 
  int maxBuildings; 

  Base(float a, float b, float c, float d, RgbCol e, int ID, int health, int builderIndex) {

    super(a, b, c, d, e, ID, builderIndex); 
    this.health = health;
    this.buttonAmt = 2;

    this.intializeButtons();

    this.barracksCost = 50;
    this.basesCost = 100; 

    applyButtonText(this.buttons, 0, "Barracks: $" + this.barracksCost);
    applyButtonText(this.buttons, 1, "Mini Bases: $" + this.basesCost);

    this.maxBuildings = 5;
  }


  public void mainFunc() {

    if (this.ghost == false && this.healthBarIntialized == false) {      //ensures new bases get h bars. 
      this.intializeHealthBarVars(); 
      this.healthBarIntialized = true;
    }
    
    this.render();
    this.place();
                    //this guys AI command is called in the mainrunstuff
  }

  public void render() {

    fill(this.r, this.g, this.b);
    rect(this.x, this.y, this.w, this.h);
    fill(0, 255, 0);
    textSize(15);                                                               //just displaying how many buildings this can still build
    text(this.maxBuildings, this.x + this.w/2 - 4, this.y + this.h/2 + 4);      //the +- 4 is just to help center the text a bit better
    textSize(12); 
    this.parralax();
  }

  public void baseAI() {
    buildTimer--;

    if ((taskFight == 1 || eHarvesters > 7) && buildTimer <= 0 && this.maxBuildings > 1 && resources[id] >= this.barracksCost) {      //buy barracks
      this.buildBarracks(); 
      buildTimer = 60;
    } else if ((taskFight == 1 || eHarvesters > 7) && buildTimer <= 0 && this.maxBuildings > 0 && resources[id] >= this.basesCost) {  //buy bases
      this.buildBases(); 
      buildTimer = 60;
    }
  }

  public void runShop() {

    if (this.openShop == true && this.id == playerID && this.ghost == false) {

      this.shopDisplay();
      shopOccupied = true; 

      this.drawButtonText(); 

      if (this.placing == true) {
        this.targX = mouseX - targW/2;      //helps to know where the building will be put so we can prevent shop from closing.
        this.targY = mouseY - targH/2;
      }

      //This is where the buying stuff is. 

      if (this.buttons.get(0).clicked == true && mouseOccupied == false && this.maxBuildings > 0) {      //buy barracks(player)
        if (resources[this.id] >= this.barracksCost) {
          this.buildBarracks();           //barracks
          mouseOccupied = true;
          this.buttons.get(0).clicked = false;
        } else this.buttons.get(0).clicked = false;
      } else if (this.buttons.get(1).clicked == true && mouseOccupied == false && this.maxBuildings > 0) {    //buy bases (player). 
        if (resources[this.id] >= this.basesCost) {
          this.buildBases();                      //bases
          mouseOccupied = true;
          this.buttons.get(1).clicked = false;
        } else this.buttons.get(1).clicked = false;
      }
    }
  }

  public void buildBarracks() {
    this.targX = -10000;
    this.targY = -10000;    //initially spawn off screen to fix clipping issues. 
    this.targW = 60;
    this.targH = 60;
    tBarracks.add(new Barracks(this.targX, this.targY, this.targW, this.targH, new RgbCol(r, g, b), this.id, this.selfIndex));
    resources[this.id] -= this.barracksCost;
    this.placing = true;
    this.maxBuildings--;
  }

  public void buildBases() {
    this.targX = -10000;
    this.targY = -10000;    //initially spawn off screen to fix clipping issues. 
    this.targW = 50;
    this.targH = 50;
    bases.add(new Base(this.targX, this.targY, this.targW, this.targH, new RgbCol(r, g, b), this.id, 500, this.selfIndex));
    resources[this.id] -= this.basesCost;
    this.placing = true;
    this.maxBuildings--;
  }

  public void getIndex(int a) {          //so we can get the bases index (to send off to new buildings so they know what made them). 

    this.selfIndex = a;
  }
}
class Building extends Particle {

  int r, g, b;
  int id; 
  boolean openShop;
  float shopX, shopY, shopW, shopH;
  float healthX, healthY, healthW, healthH; 
  float healthRatio; 
  boolean healthBarIntialized; 


  ArrayList<Button> buttons;
  int buttonAmt;

  boolean ghost;
  boolean collision; 

  float targX, targY, targW, targH;            //for putting buildings down and stuff. 
  boolean placing;

  int buildInd;
  float placeRadius;


  Building(float a, float b, float c, float d, RgbCol e, int ID, int builderIndex) {

    super(a, b, c, d); 

    this.id = ID; 

    this.r = e.r;
    this.g = e.g;
    this.b = e.b;

    this.buttonAmt = 0;

    this.buttons = new ArrayList<Button>();

    this.ghost = true;                    //when being placed dont do weird stuff

    this.collision = false;

    this.openShop = false;                  //shop vars
    this.shopX = 0;
    this.shopH = 90;
    this.shopY = height-this.shopH;
    this.shopW = width;

    this.placing = false;
    this.targX = -10000;
    this.targY = -10000;
    this.placeRadius = 300;

    this.buildInd = builderIndex;
    this.healthBarIntialized = false;
  }


  public void intializeHealthBarVars() {              //health bar create!
    this.healthX = this.x;
    this.healthY = this.y - 25; 
    this.healthW = this.w;
    this.healthH = 15; 
    this.healthRatio = this.health/this.healthW;
  }

  public void healthBar() {                    //healthbar mainJob!


    fill(0, 0, 0, 255);
    rect(this.healthX, this.healthY, this.healthW, this.healthH); 
    fill(0, 255, 0);
    rect(this.healthX, this.healthY, this.health / this.healthRatio, this.healthH); 
    this.healthX += scrollSpeedX;
    this.healthY += scrollSpeedY;
  }


  public void intializeButtons() {                //make buttons

    for (int i = 0; i < this.buttonAmt; i++) {

      this.buttons.add(i, new Button(this.shopX + i * 120, this.shopY, 110, this.shopH, new RgbCol(100, 100, 0),255));
    }
  }

  public void drawButtonText() {                  //get text

    for (int i = 0; i < this.buttonAmt; i++) {

      this.buttons.get(i).drawText(this.buttons.get(i).x + 5, this.buttons.get(i).y + this.buttons.get(i).h/2, 255, 255, 255);
    }
  }

  public void shopDisplay() {                  //display our shoppppp

    fill(175);
    rect(this.shopX, this.shopY, this.shopW, this.shopH);

    for (int i = 0; i < this.buttons.size(); i++) {

      this.buttons.get(i).render(0);
    }
  }

  public void mouseDown() {        //if we click, do this!


    //shop stuff

    shopOccupied = false;        //reset this, so we dont have weird issues with open/close. 
    //println(this.placing);
    if ((collision(mouseX, mouseY, 0, 0, this.x, this.y, this.w, this.h) || (this.openShop == true && collision(mouseX, mouseY, 0, 0, this.shopX, this.shopY, this.shopW, this.shopH)) && this.ghost == false)) {    //open the shop

      if (shopOccupied == false) {
        this.openShop = true;
      }
    } else if (collision(mouseX, mouseY, 0, 0, this.targX, this.targY, this.targW, this.targH) && this.placing == true && dist(this.x, this.y, mouseX - this.targW/2, mouseY - this.targH/2) < this.placeRadius) {            //for the buildings placing stuff                                                                                                                              
      this.placing = false;                                                                                                     //prevents the shop from closing when you click to place a buildin
      this.targW = 0;                                                                                                               //relevant to the one placing. 
      this.targH = 0;
      //println("HI");
    } else {

      if (this.openShop == true) {        //if you were open, tell everyone that you have closed

        this.openShop = false;
      }
    }

    for (int i = 0; i < this.buttons.size(); i++) {                    //see if its clicked and its able to be clicked

      if (mouseOccupied == false && this.openShop == true) {
        this.buttons.get(i).selected();
      }
    }

    //placement stuff (BUT CANT PLACE IN THE SHOP WINDOW!)

    //only player; helps with variables true falses being set.
    if (this.id == playerID && this.ghost == true && this.collision == false && this.y + this.h < this.shopY && dist(bases.get(this.buildInd).x + bases.get(this.buildInd).w/2, bases.get(this.buildInd).y + bases.get(this.buildInd).h/2, this.x, this.y) < this.placeRadius) {      //relevant to the one being placed
      //println(this.targX);
      this.ghost = false; 
      mouseOccupied = false;
      this.openShop = false;
    }
  }

  public void place() {      //for new buildings, lets them be put where they need to

    if (this.ghost == true && this.id == playerID) {

      this.x = mouseX - this.w/2;
      this.y = mouseY - this.h/2;
    } 
    
    //the enemey runs this part to make their stuff

    while (this.id == eID && this.ghost == true) {        //while loop so it happens so fast the player cant see the glitchy glitchy movement stuff.
      this.x = random(bases.get(this.buildInd).x + bases.get(this.buildInd).w/2 - this.placeRadius, bases.get(this.buildInd).x + bases.get(this.buildInd).w/2 + this.placeRadius);         //randoms x around the placing bases by some radius
      this.y = random(bases.get(this.buildInd).y + bases.get(this.buildInd).h/2 - this.placeRadius, bases.get(this.buildInd).y + bases.get(this.buildInd).h/2 + this.placeRadius);          //same for y's
      fixMove(); //since we have moved this around, the collisions need to be rechecked. Usually moving things happen each screen draw and are subsequently checked. But this happens midraw. 

      if (this.collision == false && this.ghost == true && this.x + this.w < brX && this.y + this.h < brY) {

        this.ghost = false;
      }
    }
  }
}
class Button extends Particle {

  boolean clicked;
  int r, g, b;
  String info;
  int trans;

  Button(float a, float b, float c, float d, RgbCol e, int t) {

    super(a, b, c, d);  
    this.clicked = false;
    this.r = e.r;
    this.g = e.g;
    this.b = e.b;
    this.trans = t; 
    this.info = "null";
  }

  public void render(int radius) {
    fill(this.r, this.g, this.b, this.trans);
    rect(this.x, this.y, this.w, this.h,radius);
  }

  public void drawText(float x, float y, int r, int g, int b) {        //draws text

    fill(r, g, b);
    text(info, x, y);
  }

  public void selected() {              //checks if clicked

    if (collision(mouseX, mouseY, 0, 0, this.x, this.y, this.w, this.h)) {

      clicked = true;
    }
  }

  public void highLight() {                //highlighty effect
    if (collision(mouseX, mouseY, 0, 0, this.x, this.y, this.w, this.h)) {

      this.trans = 255;

    }else{
      
     this.trans = 0;
      
    }
  }
}
class Fighter extends Units {

  boolean attackMode;
  int shootTime;
  boolean doTaskA; 
  boolean defMode; 
  boolean doTaskD;


  Fighter(float X, float Y, float W, float H, int ID) {
    super(X, Y, W, H, ID);
    this.desSpeed = 2;
    this.attackMode = false;
    this.shootTime = 40;
    this.health = 200;
    this.doTaskA = true;
    this.doTaskD = true;
  }

  public void mainFunc() {

    this.paint();
  }

  public void mouseRight() {

    this.onRightClick(mouseX, mouseY);
  }


  public void paint() {                      //draw and colorize and stuff; movement too!

    this.shootTime--;

    this.colorize();

    rect(this.x, this.y, this.w, this.h, 10);

    this.stopMovement(this.x + this.w/2, this.y + this.h/2, this.targetX, this.targetY, this.fromTargDis);
    this.movement();
  }

  public void ai(float X, float Y) {        //for ai to move and stuff

    if (this.doTaskA == true && this.reachedTarg == false) {

      this.moverCalc(X, Y); 
      this.doTaskA = false;
    } else if (this.doTaskD == true && this.reachedTarg == false) {

      this.moverCalc(X, Y); 
      this.doTaskD = false;

    } 


    if (this.attackMode == false) this.doTaskA = false;
    else this.doTaskA = true;

    if (this.defMode == false) this.doTaskD = false;
    else this.doTaskD = true; 

  }

  public void attack(float tx, float ty) {          //to shooooooot!


    if (this.shootTime <= 0) {
      bullets.add(new Missile(this.x + this.w/2, this.y + this.h/2, tx, ty, this.id));
      this.shootTime = 40;
    }
  }
}
class Harvester extends Units {


  int mineSpeed;
  float timeToHarvest; 
  float desTimeToHarvest;
  boolean mining;

  Harvester(float X, float Y, float W, float H, int ID) {

    super(X, Y, W, H, ID);

    this.desSpeed = 2;

    this.health = 100;

    this.mineSpeed = 1;

    this.desTimeToHarvest = frameRate;
    this.timeToHarvest = this.desTimeToHarvest; 
    this.mining = false;
  }

  public void mainFunc() {    

    this.paint();
    this.harvest();
    //println(this.health);
  }

  public void harvestAI(float X, float Y) {      //hah! this lets the AI mine for resources

    if (this.mining == false) {

      this.moverCalc(X, Y);
    }
  }

  public void paint() {          //same as always, draw and move

    this.colorize();

    rect(this.x, this.y, this.w, this.h);

    //stop movement stuff and movement

    this.stopMovement(this.x + this.w/2, this.y + this.h/2, this.targetX, this.targetY, this.fromTargDis);

    this.movement();
  }

  public void harvest() {                              //how you mine things

    for (int i = 0; i < mines.length; i++) {

      if (mines[i].isInProx(this.x, this.y, this.w, this.h) && (mines[i].mouseOver(this.targetX, this.targetY) ||this.id == eID)) {      //if in proximity and if player clicks, mine; if ai, and in proximity, just mine.

        if (this.mining == false && this.id == eID) this.stopMovement(0, 0, 0, 0, 0);      //run only once you crazy method. 
        this.timeToHarvest--;
        if (this.timeToHarvest <= 0) {
          mines[i].resources -= this.mineSpeed; 
          mines[i].overW -= mines[i].wDec; 
          resources[id] += this.mineSpeed;
          this.timeToHarvest = this.desTimeToHarvest;
        }
      } else {
        
        if (this.id == eID && mineCount > 0) {
          this.harvestAI(mines[i].x, mines[i].y);
          
        }
      }
    }
  }
}
class Mine extends Building {

  int resources; 
  float radius;

  float overW;
  float overH;

  float wDec; 
  boolean isDead; 


  Mine(float a, float b, float c, float d, RgbCol e, int ID, int builderIndex) {

    super(a, b, c, d, e, ID, builderIndex);

    this.resources = 1000; 

    this.radius = 30;


    this.overW = this.w;
    this.overH = this.h;
    this.wDec = this.w/this.resources;

    this.isDead = false;

  }

  public void mainFunc() {                //handles drawing all the layers
  

    fill(r, g, b); 
    rect(this.x, this.y, this.w, this.h); 
    this.overLap();
    this.parralax();
  }



  public void overLap() {                      //the top layer

    fill(125, 125, 125);
    rect(this.x, this.y, this.overW, this.overH);
  }


  public void die() {                  //if you are out of resources, go away! Also recalculate/sort the array according to distance to enemey start location (bottom right). 



    if (resources <= 0 && isDead == false) {

      this.x = -100000;
      this.y = -100000;
      mineCount--;
      this.isDead = true;
      mines = bubbleSort(mines);      //resort each time one dies! 
      for (int i = 0; i < mines.length; i++) {

        println(dist(brX, brY, mines[i].x, mines[i].y));
      }
    }
  }

  public boolean isInProx(float x1, float y1, float w1, float h1) {              //is something near the mine
    boolean r = false;

    if (collision(this.x - this.radius, this.y - this.radius, this.w + this.radius * 2, this.h + this.radius * 2, x1, y1, w1, h1)) {

      r = true;
    }

    return r;
  }

  public boolean mouseOver(float clickX, float clickY) {                        //is the mine clicked. 
    boolean r = false;

    if (collision(this.x, this.y, this.w, this.h, clickX, clickY, 0, 0)) {

      r = true;
    }


    return r;
  }
}
class Minimap extends Particle{
  
  float scaleX; 
  float scaleY;
  ArrayList<Float> xPos;
  ArrayList<Float> yPos;
  float dotW, dotH;
  float screenX;
  float screenY;
  
 Minimap(float a, float b, float c, float d){
   
   super(a,b,c,d); 
   
   this.dotW = 3;
   this.dotH = 3;

   this.screenX = width/2;
   this.screenY = height/2;
   
   scaleX = (this.w) / (width * mapSize); 
   scaleY = (this.h) / (height * mapSize);
   
   xPos = new ArrayList<Float>();
   yPos = new ArrayList<Float>();

   
 }
 
 public void render(){            //draw and adjust positioning. 
   
  //make width and height bigger by same amount * 2 as drawn in the drawPos() so that we prevent clipping.
   
   
  fill(155,155,155); 
  rect(this.x,this.y,this.w + this.dotW * 2,this.h + this.dotH * 2);
  
  this.screenX -= scrollSpeedX;
  this.screenY -= scrollSpeedY;
  
  this.drawPos();
  this.removePos();
  
   
 }
 
 public void drawPos(){          //draw the positions on the map. 
   
  fill(0); 
   
  for(int i = 0; i < xPos.size(); i++){
 
   //draw it in a region 6 over to prevent clipping. 
   //drawing again and again because the array has ALL the HISTORY of x and y positions!
    rect(xPos.get(i) * scaleX + this.x + this.dotW, yPos.get(i) * scaleY + this.y + this.dotH, this.dotW,this.dotH); 
    
  }
   
   fill(0,255,0);
   
   rect(screenX * scaleX + this.x, screenY * scaleY + this.y, 3,3);
   
   
 }
 
 public void addPos(float a, float b){      //get positioning
  
   
  xPos.add(a);      
  yPos.add(b);
  
   
 }
 
 
 public void removePos(){                //remove unwanted positioning.
   
   for(int i = 0; i < xPos.size(); i++){
    xPos.remove(i);
    yPos.remove(i);
     
     
   }
   
 }
 
}
class Missile extends Particle {

  float dx, dy;
  float d;
  float desSpeed;
  float lifeTime;
  float tx, ty;
  float red, blue, green;
  boolean hit;


  int id;

  Missile(float x1, float y1, float x2, float y2, int ID) {

    super(x1, y1, 0, 0);
  
    this.tx = x2;
    this.ty = y2;

    this.hit = false;

    this.id = ID;

    this.desSpeed = 15;

    float store[] = movementCalc(x1, y1, this.tx, this.ty, this.desSpeed);        //get speeds needed for bullet when bullets spawns

    this.sx = store[0];
    this.sy = store[1];

    this.lifeTime = 150;

    this.red = 0; 


    if (this.id == playerID) {                  //colors depending on whose bullet it is. 

      this.blue = 0;
      this.green = 255;
    } else if (this.id == eID) {

      this.green = 0; 
      this.blue = 255;
    }
  }

  public void paint() {                                    //draw with lifetime and movement

    this.lifeTime--;
    strokeWeight(2);
    stroke(this.red, this.green, this.blue);
    line(this.x, this.y, this.x + this.sx, this.y + this.sy);
    strokeWeight(1);


    if (dist(this.x, this.y, this.tx, this.ty) < 10) {              //if close enough
      this.sx = 0;
      this.sy = 0;
      this.x = -1000000000;                    //send it away, the bullet will eventually die anyways.
    }


    this.move();
    
    this.parralax();


    this.tx += scrollSpeedX;
    this.ty += scrollSpeedY;



    fill(255, 0, 0);
    // rect(this.tx, this.ty, 10, 10);
  }
}
class Particle {                              //the holy grail of classes, what most classes are building off of. 

  float sx, sy, ax, ay;
  float x, y, w, h;
  float maxXBound;
  float maxYBound;
  float mapOffSet; 
  float trX, trY;
  boolean takeDmg; 

  float health;

  Particle(float a, float b, float c, float d) {
    this.x = a;
    this.y = b;
    this.w = c;
    this.h = d;

    this.sx = 0; 
    this.sy = 0;
    this.ax = 0;
    this.ay = 0;

    this.trX = a;
    this.trY = b;

    this.maxXBound = boundMaxX;
    this.maxYBound = boundMaxY;
    this.mapOffSet = 3;

    this.takeDmg = false;
  }

  public void parralax() {                      //adjust things according to camera movement.

    this.x += scrollSpeedX;
    this.y += scrollSpeedY;
  }

  public boolean death() {                    //die if need be

    boolean result = false;
    if (this.health < 0) {

      result = true;
    }

    return result;
    
  }

  public void getDmged(float dmg) {                //take dmg if you need
    
      this.health -= dmg;

  }


  public void move() {                      //move

    this.x += this.sx;
    this.y += this.sy;

    this.sx += this.ax;
    this.sy += this.ay;
    
  
  }

  public void bounderies() {

    this.bounderiesSet();      //make them
    this.bounderiesApply();    //apply them
  }

  public void bounderiesSet() {      //makes boundries of the map

    // println(this.x);
    // println(this.w);

    //These if statements keep any particle in bounds on all four sides.

    //for the maxes and mins we just check when the maxBounderies goes over width * mapsize, (it has to by a margin of 3), then we fix the max boundry. This ensures that boundries happen at the edges. 
    //Same for the heights/y direction. 



    if (boundMaxX > width * mapSize) {

      this.maxXBound = boundMaxX/3;
    } else {

      this.maxXBound = width * 3;
    }

    if (boundMaxY > height * mapSize) {

      this.maxYBound = boundMaxY/mapSize;
    } else {

      this.maxYBound = height * 3;
    }
  }

  public void bounderiesApply() {              //aplies the boundries of the map

    //now we can just fix its positions after we have what ist checking against from above. 
    if (this.x < boundMinX) {

      this.x = boundMinX;
      this.trX = 0;
    } else if (this.x + this.w + this.mapOffSet > this.maxXBound) {

      println(this.x);
      this.x = maxXBound - (this.w + this.mapOffSet);
    }

    if (this.y < boundMinY) {

      this.y = boundMinY;
      this.trY = 0;
    } else if (this.y + this.h + this.mapOffSet > this.maxYBound) {

      this.y = this.maxYBound - (this.h + this.mapOffSet);
    }

    if (this.trX + this.w > width * mapSize) {

      this.trX = width * mapSize - (this.w + this.mapOffSet);
    }

    if (this.trY + this.h > height * mapSize) {

      this.trY = height * mapSize - (this.h + this.mapOffSet);
    }
  }
}
class RgbCol{
  
  int r,g,b;
  
 RgbCol(int a, int c, int d){
   
   this.r = a;                //stores the color
   this.g = c;
   this.b = d;

 }
 
 public void display(){                //displays the colour 
   
  fill(this.r,this.g,this.b); 
   
 }

}
class Selector extends Particle{

  int col;


  Selector() {
    
    super(0,0,0,0);
    
  }


  public void mainFunc() {

    this.paint();
  }

  public void paint() {
    
    this.parralax();
    
    fill(0, 255, 0, 20);

    //draw the box if its been dragged enough

    if (this.w > 10 || this.h > 10 || this.w < -10 || this.h < -10) {
      rect(x, y, w, h);
    }
  }

  public void onMousePress() {

    //rest w and h
    this.w = 0;
    this.h = 0;

    // get x and y pos for selector

    this.x = mouseX;
    this.y = mouseY;
  }

  public void onMouseDrag() {

    //be able to drag the box

    this.w = mouseX - x;
    this.h = mouseY - y;
  }
  
  public void clickAway(){
    
    this.w = 0;                        //make it so that the box goes away
    this.h = 0;
    
  }
  
  public boolean isSelectedClick(float targetX, float targetY, float targetW, float targetH){    //checks if it has clicked something
    
    boolean result = false;
    
    if(collision(mouseX,mouseY,0,0,targetX,targetY,targetW,targetH)){
      
      result = true;
      
    }
    
    
    return result;
    
    
  }

  public boolean isSelectedDrag(float targetX, float targetY, float targetW, float targetH) {                    //checks to see if its drag selected anything

    boolean result = false;

    if (this.w > 0 && this.h > 0) {          //if drawn from top left

      if (collision(this.x, this.y, this.w, this.h, targetX, targetY, targetW, targetH) == true) {

        result = true;
      }
      
    } else if ( w < 0 && h < 0) {      //if drawn from bottom right

      if (collision(this.x + this.w, this.y + this.h, this.w * -1, this.h * -1, targetX, targetY, targetW, targetH) == true) {

        result = true;
      }
      
    } else if (w > 0 && h < 0) {      //if drawn from bottom left

      if (collision(this.x, this.y + this.h, this.w, this.h * -1, targetX, targetY, targetW, targetH) == true) {

        result = true;
      }
      
    } else if (w < 0 && h > 0) {  //if drawn from top right

      if (collision(this.x + this.w, this.y, this.w * -1, this.h, targetX, targetY, targetW, targetH) == true) {

        result = true;
      }
      
    }

    return result;
  }
}
class Units extends Particle {

  float targetX, targetY;
  boolean selected; 
  float desSpeed;
  float fromTargDis; 
  boolean moveCollide;
  float moveTimer; 
  boolean reachedTarg;

  boolean spawned; 
  boolean intialize; 

  int id;

  Units(float X, float Y, float W, float H, int ID) {

    super(X, Y, W, H); 

    this.id = ID; 

    this.desSpeed = 0;

    this.fromTargDis = 20;

    this.moveCollide = true; 
    this.moveTimer = 20;

    this.reachedTarg = false;
    this.spawned = true;
    this.intialize = true;
  }

  public void colorize() {

    if (this.id == playerID) fill(255, 0, 0);        //red for player
    else if (this.id == eID) fill (0, 0, 255);        //blue for enemey
    
    if (this.selected == true && this.id == playerID) {

      fill(0, 255, 0);
    }
  }
  


  public void movement() {                          //basic movement. Also ensures that the "true" unmodified positions are captured for the minimap. Changing positions with parralaxing will mess with the minimaps configuration. 

    this.move(); 

    //parralax effect and keep track of true x and y vals.

    this.parralax();
    this.trX +=sx;
    this.trY +=sy;


    // println(this.x);
    // println(this.trX);

    //keeps it in bounds

    this.bounderies();

    if (this.targetX != 0) {
      this.targetX += scrollSpeedX;
    }

    if (this.targetY != 0) {            
      this.targetY += scrollSpeedY;
    }

    
  }

  public void stopMovement(float x1, float y1, float x2, float y2, float distance) {                          //the algorithim that stops movement and handles merging problems. 
    //fix this next time

    if (dist(x1, y1, x2, y2) <= distance || this.intialize == true) {

      this.reachedTarg = true;  
      this.sx = random(10) - 5;
      this.sy = random(10) - 5;
      this.intialize = false;
    }

    if (this.reachedTarg == true && this.moveCollide == false) {
      this.sx = 0;
      this.sy = 0;
      this.reachedTarg = false;
      this.spawned = false;
    } else if ((this.reachedTarg == true && this.moveCollide == true) || this.spawned == true) {
      this.moveTimer--;
      if (this.moveTimer <= 0) {

        this.moveTimer = 20;
        this.sx = random(10) - 5;
        this.sy = random(10) - 5;
      }
    }
  }

  public void selectUpdate(boolean select) {                            //updates if the unit is selected or not

    if (this.id == playerID) {
      this.selected = select;
    }
  }

  public void onRightClick(float X, float Y) {                        //what to do when right click happens

    if (this.selected == true) {
      moverCalc(X,Y);
    }
  }
  
  public void moverCalc(float X, float Y) {                    //assigns speeds

      float[] store = new float[2];
      this.targetX = X;
      this.targetY = Y;

      store = movementCalc(this.x + this.w/2, this.y + this.h/2, targetX, targetY, this.desSpeed);

      this.sx = store[0];
      this.sy = store[1];
    
  }
  

}
  public void settings() {  size(640, 480); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "MainRunStuff" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
