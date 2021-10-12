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

  void mainFunc() {    

    this.paint();
    this.harvest();
    //println(this.health);
  }

  void harvestAI(float X, float Y) {      //hah! this lets the AI mine for resources

    if (this.mining == false) {

      this.moverCalc(X, Y);
    }
  }

  void paint() {          //same as always, draw and move

    this.colorize();

    rect(this.x, this.y, this.w, this.h);

    //stop movement stuff and movement

    this.stopMovement(this.x + this.w/2, this.y + this.h/2, this.targetX, this.targetY, this.fromTargDis);

    this.movement();
  }

  void harvest() {                              //how you mine things

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
