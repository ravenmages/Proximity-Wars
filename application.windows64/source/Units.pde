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

  void colorize() {

    if (this.id == playerID) fill(255, 0, 0);        //red for player
    else if (this.id == eID) fill (0, 0, 255);        //blue for enemey
    
    if (this.selected == true && this.id == playerID) {

      fill(0, 255, 0);
    }
  }
  


  void movement() {                          //basic movement. Also ensures that the "true" unmodified positions are captured for the minimap. Changing positions with parralaxing will mess with the minimaps configuration. 

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

  void stopMovement(float x1, float y1, float x2, float y2, float distance) {                          //the algorithim that stops movement and handles merging problems. 
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

  void selectUpdate(boolean select) {                            //updates if the unit is selected or not

    if (this.id == playerID) {
      this.selected = select;
    }
  }

  void onRightClick(float X, float Y) {                        //what to do when right click happens

    if (this.selected == true) {
      moverCalc(X,Y);
    }
  }
  
  void moverCalc(float X, float Y) {                    //assigns speeds

      float[] store = new float[2];
      this.targetX = X;
      this.targetY = Y;

      store = movementCalc(this.x + this.w/2, this.y + this.h/2, targetX, targetY, this.desSpeed);

      this.sx = store[0];
      this.sy = store[1];
    
  }
  

}
