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


  void intializeHealthBarVars() {              //health bar create!
    this.healthX = this.x;
    this.healthY = this.y - 25; 
    this.healthW = this.w;
    this.healthH = 15; 
    this.healthRatio = this.health/this.healthW;
  }

  void healthBar() {                    //healthbar mainJob!


    fill(0, 0, 0, 255);
    rect(this.healthX, this.healthY, this.healthW, this.healthH); 
    fill(0, 255, 0);
    rect(this.healthX, this.healthY, this.health / this.healthRatio, this.healthH); 
    this.healthX += scrollSpeedX;
    this.healthY += scrollSpeedY;
  }


  void intializeButtons() {                //make buttons

    for (int i = 0; i < this.buttonAmt; i++) {

      this.buttons.add(i, new Button(this.shopX + i * 120, this.shopY, 110, this.shopH, new RgbCol(100, 100, 0),255));
    }
  }

  void drawButtonText() {                  //get text

    for (int i = 0; i < this.buttonAmt; i++) {

      this.buttons.get(i).drawText(this.buttons.get(i).x + 5, this.buttons.get(i).y + this.buttons.get(i).h/2, 255, 255, 255);
    }
  }

  void shopDisplay() {                  //display our shoppppp

    fill(175);
    rect(this.shopX, this.shopY, this.shopW, this.shopH);

    for (int i = 0; i < this.buttons.size(); i++) {

      this.buttons.get(i).render(0);
    }
  }

  void mouseDown() {        //if we click, do this!


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

  void place() {      //for new buildings, lets them be put where they need to

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
