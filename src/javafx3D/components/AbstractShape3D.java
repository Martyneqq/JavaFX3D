package javafx3D.components;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Shape3D;

/**
 * Abstraktní třída pro správu 3D objektů.
 * Zajišťuje konzistentní chování všech 3D komponent.
 */
public abstract class AbstractShape3D extends Shape3D {
    
    protected static int nextId = 1;
    protected static final Object ID_LOCK = new Object();
    
    protected final int id;
    protected Color color;
    protected PhongMaterial material;
    
    /**
     * Bezpečný generátor ID s ochranou proti race conditions.
     */
    protected static int generateId() {
        synchronized (ID_LOCK) {
            return nextId++;
        }
    }
    
    /**
     * Inicializace abstraktního 3D objektu.
     */
    protected AbstractShape3D(Color color) {
        this.id = generateId();
        this.color = color;
        this.material = new PhongMaterial(color);
        this.setMaterial(material);
    }
    
    /**
     * Resetuje ID counter pro testování.
     */
    public static void resetIdCounter() {
        synchronized (ID_LOCK) {
            nextId = 1;
        }
    }
    
    // Gettery a settery
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
        this.material = new PhongMaterial(color);
        this.setMaterial(material);
    }
    
    /**
     * Nastaví pozici objektu v 3D prostoru.
     */
    public void setPosition(Point3D position) {
        if (position == null) {
            throw new IllegalArgumentException("Pozice nemůže být null");
        }
        setTranslateX(position.getX());
        setTranslateY(position.getY());
        setTranslateZ(position.getZ());
    }
    
    /**
     * Získá aktuální pozici objektu.
     */
    public Point3D getPosition() {
        return new Point3D(getTranslateX(), getTranslateY(), getTranslateZ());
    }
    
    /**
     * Přesune objekt o určitý offset.
     */
    public void translateBy(Point3D offset) {
        if (offset == null) {
            throw new IllegalArgumentException("Offset nemůže být null");
        }
        setTranslateX(getTranslateX() + offset.getX());
        setTranslateY(getTranslateY() + offset.getY());
        setTranslateZ(getTranslateZ() + offset.getZ());
    }
    
    /**
     * Vrátí string reprezentaci objektu.
     */
    @Override
    public String toString() {
        return String.format("%s(ID=%d, Color=%s)", 
            this.getClass().getSimpleName(), 
            id, 
            color.toString());
    }
    
}
