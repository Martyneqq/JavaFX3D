package javafx3D.components;

import java.io.Serializable;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx3D.config.AppConfig;

/**
 * Vlastní koule (Sphere) s podporou ID a barvy.
 * Implementuje lepší správu vlastností a sériování.
 */
public class CustomSphere extends Sphere implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private final int id;
    private Color color;

    public CustomSphere(Color color) {
        this(color, AppConfig.SPHERE_RADIUS);
    }
    
    public CustomSphere(Color color, double radius) {
        super(radius);
        if (color == null) {
            throw new IllegalArgumentException("Barva nemůže být null");
        }
        this.id = generateId();
        this.color = color;
        this.setMaterial(new PhongMaterial(color));
    }
    
    private static int nextId = 1;
    private static final Object ID_LOCK = new Object();
    
    private static int generateId() {
        synchronized (ID_LOCK) {
            return nextId++;
        }
    }
    
    public static void resetIdCounter() {
        synchronized (ID_LOCK) {
            nextId = 1;
        }
    }

    public int getID() {
        return this.id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if (color == null) {
            throw new IllegalArgumentException("Barva nemůže být null");
        }
        this.color = color;
        this.setMaterial(new PhongMaterial(color));
    }

    public void setPosition(Point3D position) {
        if (position == null) {
            throw new IllegalArgumentException("Pozice nemůže být null");
        }
        setTranslateX(position.getX());
        setTranslateY(position.getY());
        setTranslateZ(position.getZ());
    }
    
    public Point3D getPosition() {
        return new Point3D(getTranslateX(), getTranslateY(), getTranslateZ());
    }
    
    @Override
    public String toString() {
        return String.format("Sphere(ID=%d, Color=%s)", id, color.toString());
    }
}