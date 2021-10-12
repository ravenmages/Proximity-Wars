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

  void mainFunc() {                //handles drawing all the layers
  

    fill(r, g, b); 
    rect(this.x, this.y, this.w, this.h); 
    this.overLap();
    this.parralax();
  }



  void overLap() {                      //the top layer

    fill(125, 125, 125);
    rect(this.x, this.y, this.overW, this.overH);
  }


  void die() {                  //if you are out of resources, go away! Also recalculate/sort the array according to distance to enemey start location (bottom right). 



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

  boolean isInProx(float x1, float y1, float w1, float h1) {              //is something near the mine
    boolean r = false;

    if (collision(this.x - this.radius, this.y - this.radius, this.w + this.radius * 2, this.h + this.radius * 2, x1, y1, w1, h1)) {

      r = true;
    }

    return r;
  }

  boolean mouseOver(float clickX, float clickY) {                        //is the mine clicked. 
    boolean r = false;

    if (collision(this.x, this.y, this.w, this.h, clickX, clickY, 0, 0)) {

      r = true;
    }


    return r;
  }
}
