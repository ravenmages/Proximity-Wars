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

  void mainFunc() {
    
    if(this.ghost == false && this.healthBarIntialized == false) {              //ensures new barracks get h bars. 
      this.intializeHealthBarVars(); 
      this.healthBarIntialized = true;
    }
    
    this.render(); 
    this.place();
    this.barracksAI(); 
   

  }
  
  void barracksAI(){                    
    
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

  void runShop() {

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
  
  void buildHarvesters(){
    harvesters.add(new Harvester(this.x + this.w/2, this.y + this.h/2, 20, 20, this.id));
    resources[this.id] -= this.harvestersCost;
  }
  
  void buildFighters(){
    fighters.add(new Fighter(this.x + this.w/2, this.y + this.h/2, 20, 20, this.id));
    resources[this.id] -= this.fightersCost;
  }



  void render() {

    fill(this.r, this.g, this.b);
    rect(this.x, this.y, this.w, this.h, 50);
    this.parralax();
  }
}
