class Button extends Particle {

  boolean clicked;
  int r, g, b;
  String info;
  int trans;

  Button(float a, float b, float c, float d, RgbCol e, int t) {

    super(a, b, c, d);  
    this.clicked = false;
    this.r = e.r;
    this.g = e.g;
    this.b = e.b;
    this.trans = t; 
    this.info = "null";
  }

  void render(int radius) {
    fill(this.r, this.g, this.b, this.trans);
    rect(this.x, this.y, this.w, this.h,radius);
  }

  void drawText(float x, float y, int r, int g, int b) {        //draws text

    fill(r, g, b);
    text(info, x, y);
  }

  void selected() {              //checks if clicked

    if (collision(mouseX, mouseY, 0, 0, this.x, this.y, this.w, this.h)) {

      clicked = true;
    }
  }

  void highLight() {                //highlighty effect
    if (collision(mouseX, mouseY, 0, 0, this.x, this.y, this.w, this.h)) {

      this.trans = 255;

    }else{
      
     this.trans = 0;
      
    }
  }
}
