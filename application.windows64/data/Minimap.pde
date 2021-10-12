class Minimap extends Particle{
  
  float scaleX; 
  float scaleY;
  ArrayList<Float> xPos;
  ArrayList<Float> yPos;
  float dotW, dotH;
  float screenX;
  float screenY;
  
 Minimap(float a, float b, float c, float d){
   
   super(a,b,c,d); 
   
   this.dotW = 3;
   this.dotH = 3;

   this.screenX = width/2;
   this.screenY = height/2;
   
   scaleX = (this.w) / (width * mapSize); 
   scaleY = (this.h) / (height * mapSize);
   
   xPos = new ArrayList<Float>();
   yPos = new ArrayList<Float>();

   
 }
 
 void render(){            //draw and adjust positioning. 
   
  //make width and height bigger by same amount * 2 as drawn in the drawPos() so that we prevent clipping.
   
   
  fill(155,155,155); 
  rect(this.x,this.y,this.w + this.dotW * 2,this.h + this.dotH * 2);
  
  this.screenX -= scrollSpeedX;
  this.screenY -= scrollSpeedY;
  
  this.drawPos();
  this.removePos();
  
   
 }
 
 void drawPos(){          //draw the positions on the map. 
   
  fill(0); 
   
  for(int i = 0; i < xPos.size(); i++){
 
   //draw it in a region 6 over to prevent clipping. 
   //drawing again and again because the array has ALL the HISTORY of x and y positions!
    rect(xPos.get(i) * scaleX + this.x + this.dotW, yPos.get(i) * scaleY + this.y + this.dotH, this.dotW,this.dotH); 
    
  }
   
   fill(0,255,0);
   
   rect(screenX * scaleX + this.x, screenY * scaleY + this.y, 3,3);
   
   
 }
 
 void addPos(float a, float b){      //get positioning
  
   
  xPos.add(a);      
  yPos.add(b);
  
   
 }
 
 
 void removePos(){                //remove unwanted positioning.
   
   for(int i = 0; i < xPos.size(); i++){
    xPos.remove(i);
    yPos.remove(i);
     
     
   }
   
 }
 
}
