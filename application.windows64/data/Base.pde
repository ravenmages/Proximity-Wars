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


  void mainFunc() {

    if (this.ghost == false && this.healthBarIntialized == false) {      //ensures new bases get h bars. 
      this.intializeHealthBarVars(); 
      this.healthBarIntialized = true;
    }
    
    this.render();
    this.place();
                    //this guys AI command is called in the mainrunstuff
  }

  void render() {

    fill(this.r, this.g, this.b);
    rect(this.x, this.y, this.w, this.h);
    fill(0, 255, 0);
    textSize(15);                                                               //just displaying how many buildings this can still build
    text(this.maxBuildings, this.x + this.w/2 - 4, this.y + this.h/2 + 4);      //the +- 4 is just to help center the text a bit better
    textSize(12); 
    this.parralax();
  }

  void baseAI() {
    buildTimer--;

    if ((taskFight == 1 || eHarvesters > 7) && buildTimer <= 0 && this.maxBuildings > 1 && resources[id] >= this.barracksCost) {      //buy barracks
      this.buildBarracks(); 
      buildTimer = 60;
    } else if ((taskFight == 1 || eHarvesters > 7) && buildTimer <= 0 && this.maxBuildings > 0 && resources[id] >= this.basesCost) {  //buy bases
      this.buildBases(); 
      buildTimer = 60;
    }
  }

  void runShop() {

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

  void buildBarracks() {
    this.targX = -10000;
    this.targY = -10000;    //initially spawn off screen to fix clipping issues. 
    this.targW = 60;
    this.targH = 60;
    tBarracks.add(new Barracks(this.targX, this.targY, this.targW, this.targH, new RgbCol(r, g, b), this.id, this.selfIndex));
    resources[this.id] -= this.barracksCost;
    this.placing = true;
    this.maxBuildings--;
  }

  void buildBases() {
    this.targX = -10000;
    this.targY = -10000;    //initially spawn off screen to fix clipping issues. 
    this.targW = 50;
    this.targH = 50;
    bases.add(new Base(this.targX, this.targY, this.targW, this.targH, new RgbCol(r, g, b), this.id, 500, this.selfIndex));
    resources[this.id] -= this.basesCost;
    this.placing = true;
    this.maxBuildings--;
  }

  void getIndex(int a) {          //so we can get the bases index (to send off to new buildings so they know what made them). 

    this.selfIndex = a;
  }
}
