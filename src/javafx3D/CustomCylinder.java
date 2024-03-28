package javafx3D;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;

class CustomCylinder extends Cylinder {
        private Color color;        
        private int id = 0;
        public CustomCylinder(Color color , int id){
            super(50, 100);
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