
/////////////////////////////////DECLARE CUSTOM FUNCTIONS/////////////////////////////////////////////////////////////////////////////////////////

boolean collision(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2) {        //collision detection

  boolean result = false;

  if (x1 + w1 >= x2 && x1 <= x2 + w2 && y1 + h1 >= y2 && y1 <= y2 + h2) {

    result = true;
  }

  return result;
}

void setAttack(float radius, int i) {     //deals with attacking

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




void bulletCollide(int i, float dmg) {                    //checks if bullets have hit

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



void fixMove() {                            //we detect whos colliding and when are they doing so


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

void scrollCamera() {                //lets the camera move around the screen

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

float[] movementCalc(float x1, float y1, float x2, float y2, float speed) {          //for bullets and moving, etc...

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

int randomChoice(int numOfChoice) {                  //spits out the random choice int. 

  int result = 0;
  int[] choices = new int[numOfChoice];

  for (int i = 0; i < choices.length; i++) {

    choices[i] = i;
  }

  result = choices[floor(random(numOfChoice))];

  return result;
}

void applyButtonText(ArrayList<Button> a, int index, String text) {        //gives a button text

  a.get(index).info = text;
}

void baseMineFix(ArrayList<Base> a, int i) {            //makes sure mines dont go over the only placed start building.

  for (int j = 0; j < a.size(); j++) {

    if (collision(mines[i].x, mines[i].y, mines[i].w, mines[i].h, a.get(j).x, a.get(j).y, a.get(j).w, a.get(j).h)) {

      mines[i] = new Mine(random(width * mapSize - 40), random(height * mapSize - 40), 40, 40, new RgbCol(255, 255, 0), 0, -1);
    }
  }
}

void mineMineFix(int i) {              //prevents overlap of mines

  for (int j = 0; j < mines.length; j++) {

    if (collision(mines[i].x, mines[i].y, mines[i].w, mines[i].h, mines[j].x, mines[j].y, mines[j].w, mines[j].h) && i != j) {

      mines[i] = new Mine(random(width * mapSize - 40), random(height * mapSize - 40), 40, 40, new RgbCol(255, 255, 0), 0, -1);
    }
  }
}

void mineBarracksFix(ArrayList<Barracks> b, int i) {          //prevents mine barracks overlap

  for (int j = 0; j < b.size(); j++) {

    if (collision(mines[i].x, mines[i].y, mines[i].w, mines[i].h, b.get(j).x, b.get(j).y, b.get(j).w, b.get(j).h)) {

      mines[i] = new Mine(random(width * mapSize - 40), random(height * mapSize - 40), 40, 40, new RgbCol(255, 255, 0), 0, -1);
    }
  }
}


void mineRandom() {                  //intilizes the mines.

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

boolean searchMines() {            //searches through the mines

  boolean result = false; 

  for (int i = 0; i < mines.length; i++) {

    if (mines[i].collision == true) result = true;
  }

  return result;
}

int enemyFightTask(int pCountF, int eCountF) {                        //determines to attack or not to attack                          

  int task = 0;                                                       //1 is attack, 0 is defend/not attack
  float attack = eCountF - pCountF * 0.5; 

  if (attack > 0) task = 1;

  return task;
}

Selector selector = new Selector();               //what the player uses to select stuff

Mine[] bubbleSort(Mine[] nums) {                  //sort the mines command

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

void menuButtonsClicked() {        //run in mousePressed to see whats clicked.

  for (int i = 0; i < menuButtons.size(); i++) {
    menuButtons.get(i).selected();
  }
}

void resetMenuButtonClicks() {      //ensures buttons dont mess with each other even when on other screens.

  for (int i = 0; i < menuButtons.size(); i++) {

    menuButtons.get(i).clicked = false;
  }
}

void mainMenuButtonJobs() {              //the jobs of buttons on the MAIN MENU 

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

void returnToMenuButtonJob(int i) {                    //job of return to menu button

  if (menuButtons.get(i).clicked == true) {

    screen = 0; 
    resetMenuButtonClicks();

    if (wonGame == true || lostGame == true) {      //if its because game is over, reset the game! 
      setup(); 
      //println(lostGame + "" + wonGame);
    }
  }
}

void returnToMenuButton(float x, float y, float w, float h, float displaceX, float displaceY, int r, int g, int b, int i) {              //move a return to menu button depending on screen

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

float[] incrementTextYLoc(float y, int lines, int size, int spacing) {          //vertically increments the text. 

  float[] result = new float[lines];

  float currentPos = y;

  for (int i = 0; i < lines; i++) {

    if (i > 0) result[i] = currentPos + size + spacing; 
    else result[i] = currentPos;
    currentPos = result[i];
  }

  return result;
}

void fogOfWar() {                                      //handles the fog of wars main stuff (the changing of the fog is in the collisions section!) 

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

void endGame(boolean win, boolean lose, int i) {    //losing or winning screen.


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

void setup() {                //intialize all stuff that needs ot be

  size(640, 480);

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

  mapSize = 3.0; 

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


void draw() {

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


void mousePressed() {


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


void mouseDragged() {

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

void mouseReleased() {

  selector.clickAway();      //resets the box when click released
}
