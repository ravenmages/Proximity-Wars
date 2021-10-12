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

  void parralax() {                      //adjust things according to camera movement.

    this.x += scrollSpeedX;
    this.y += scrollSpeedY;
  }

  boolean death() {                    //die if need be

    boolean result = false;
    if (this.health < 0) {

      result = true;
    }

    return result;
    
  }

  void getDmged(float dmg) {                //take dmg if you need
    
      this.health -= dmg;

  }


  void move() {                      //move

    this.x += this.sx;
    this.y += this.sy;

    this.sx += this.ax;
    this.sy += this.ay;
    
  
  }

  void bounderies() {

    this.bounderiesSet();      //make them
    this.bounderiesApply();    //apply them
  }

  void bounderiesSet() {      //makes boundries of the map

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

  void bounderiesApply() {              //aplies the boundries of the map

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
