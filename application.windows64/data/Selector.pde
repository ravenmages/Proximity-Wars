class Selector extends Particle{

  int col;


  Selector() {
    
    super(0,0,0,0);
    
  }


  void mainFunc() {

    this.paint();
  }

  void paint() {
    
    this.parralax();
    
    fill(0, 255, 0, 20);

    //draw the box if its been dragged enough

    if (this.w > 10 || this.h > 10 || this.w < -10 || this.h < -10) {
      rect(x, y, w, h);
    }
  }

  void onMousePress() {

    //rest w and h
    this.w = 0;
    this.h = 0;

    // get x and y pos for selector

    this.x = mouseX;
    this.y = mouseY;
  }

  void onMouseDrag() {

    //be able to drag the box

    this.w = mouseX - x;
    this.h = mouseY - y;
  }
  
  void clickAway(){
    
    this.w = 0;                        //make it so that the box goes away
    this.h = 0;
    
  }
  
  boolean isSelectedClick(float targetX, float targetY, float targetW, float targetH){    //checks if it has clicked something
    
    boolean result = false;
    
    if(collision(mouseX,mouseY,0,0,targetX,targetY,targetW,targetH)){
      
      result = true;
      
    }
    
    
    return result;
    
    
  }

  boolean isSelectedDrag(float targetX, float targetY, float targetW, float targetH) {                    //checks to see if its drag selected anything

    boolean result = false;

    if (this.w > 0 && this.h > 0) {          //if drawn from top left

      if (collision(this.x, this.y, this.w, this.h, targetX, targetY, targetW, targetH) == true) {

        result = true;
      }
      
    } else if ( w < 0 && h < 0) {      //if drawn from bottom right

      if (collision(this.x + this.w, this.y + this.h, this.w * -1, this.h * -1, targetX, targetY, targetW, targetH) == true) {

        result = true;
      }
      
    } else if (w > 0 && h < 0) {      //if drawn from bottom left

      if (collision(this.x, this.y + this.h, this.w, this.h * -1, targetX, targetY, targetW, targetH) == true) {

        result = true;
      }
      
    } else if (w < 0 && h > 0) {  //if drawn from top right

      if (collision(this.x + this.w, this.y, this.w * -1, this.h, targetX, targetY, targetW, targetH) == true) {

        result = true;
      }
      
    }

    return result;
  }
}
