class RgbCol{
  
  int r,g,b;
  
 RgbCol(int a, int c, int d){
   
   this.r = a;                //stores the color
   this.g = c;
   this.b = d;

 }
 
 void display(){                //displays the colour 
   
  fill(this.r,this.g,this.b); 
   
 }

}
