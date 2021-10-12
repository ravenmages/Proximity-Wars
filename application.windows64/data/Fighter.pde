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

  void mainFunc() {

    this.paint();
  }

  void mouseRight() {

    this.onRightClick(mouseX, mouseY);
  }


  void paint() {                      //draw and colorize and stuff; movement too!

    this.shootTime--;

    this.colorize();

    rect(this.x, this.y, this.w, this.h, 10);

    this.stopMovement(this.x + this.w/2, this.y + this.h/2, this.targetX, this.targetY, this.fromTargDis);
    this.movement();
  }

  void ai(float X, float Y) {        //for ai to move and stuff

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

  void attack(float tx, float ty) {          //to shooooooot!


    if (this.shootTime <= 0) {
      bullets.add(new Missile(this.x + this.w/2, this.y + this.h/2, tx, ty, this.id));
      this.shootTime = 40;
    }
  }
}
