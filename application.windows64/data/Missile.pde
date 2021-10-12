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

  void paint() {                                    //draw with lifetime and movement

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
