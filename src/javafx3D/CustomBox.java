package javafx3D;

import java.io.Serializable;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

class CustomBox extends Box implements Serializable{
        private Color color;
        private int id = 0;
        public CustomBox(Color color, int id){
            super(100,100,100);
            this.color = color;
            this.id = id;
            setMaterial(new PhongMaterial(color));
        }
            
        public int getID(){
            return this.id;
        }
        
        public void setID(int id){
            this.id = id;
        }
        
        public Color getColor(){
            return color;
        }
        
        public void setColor(Color color){
            this.color = color;
        }
        
        
        public void set(Point3D p){
        setTranslateX(p.getX());
        setTranslateY(p.getY());
        setTranslateZ(p.getZ());
    }
}