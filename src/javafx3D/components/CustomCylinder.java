package javafx3D.components;

import java.io.Serializable;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;

public class CustomCylinder extends Cylinder implements Serializable {
    private static int nextId = 1;

    private Color color;
    private int id;

    public CustomCylinder(Color color) {
        this.id = nextId;
        nextId++;

        super(50, 100);
        this.color = color;
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


    public void set(Point3D p) {
        setTranslateX(p.getX());
        setTranslateY(p.getY());
        setTranslateZ(p.getZ());
    }
}