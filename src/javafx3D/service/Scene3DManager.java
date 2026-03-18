package javafx3D.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.shape.Shape3D;
import javafx.scene.paint.Color;
import javafx3D.components.CustomBox;
import javafx3D.components.CustomCylinder;
import javafx3D.components.CustomSphere;
import javafx3D.utils.Logger;

/**
 * Servisní třída pro správu 3D scény.
 * Zajisťuje přidávání, odebírání a správu 3D objektů.
 */
public class Scene3DManager {
    
    private final Group sceneRoot;
    private final ObservableList<Shape3D> objects;
    
    public Scene3DManager(Group sceneRoot) {
        if (sceneRoot == null) {
            throw new IllegalArgumentException("Scene root nemůže být null");
        }
        this.sceneRoot = sceneRoot;
        this.objects = FXCollections.observableArrayList();
    }
    
    /**
     * Přídá kostku do scény.
     */
    public CustomBox addCube(Color color) {
        if (color == null) {
            color = Color.LIGHTGREY;
        }
        
        CustomBox cube = new CustomBox(color);
        addObject(cube);
        Logger.info("Cube added: " + cube);
        return cube;
    }
    
    /**
     * Přídá kouli do scény.
     */
    public CustomSphere addSphere(Color color) {
        if (color == null) {
            color = Color.LIGHTGREY;
        }
        
        CustomSphere sphere = new CustomSphere(color);
        addObject(sphere);
        Logger.info("Sphere added: " + sphere);
        return sphere;
    }
    
    /**
     * Přídá válec do scény.
     */
    public CustomCylinder addCylinder(Color color) {
        if (color == null) {
            color = Color.LIGHTGREY;
        }
        
        CustomCylinder cylinder = new CustomCylinder(color);
        addObject(cylinder);
        Logger.info("Cylinder added: " + cylinder);
        return cylinder;
    }
    
    /**
     * Přídá obecný 3D objekt do scény.
     */
    public void addObject(Shape3D shape) {
        if (shape == null) {
            throw new IllegalArgumentException("Shape nemůže být null");
        }
        
        if (!objects.contains(shape)) {
            objects.add(shape);
            sceneRoot.getChildren().add(shape);
            Logger.debug("Object added to scene: " + shape.getClass().getSimpleName());
        }
    }
    
    /**
     * Odebere objekt ze scény.
     */
    public void removeObject(Shape3D shape) {
        if (shape != null && objects.contains(shape)) {
            objects.remove(shape);
            sceneRoot.getChildren().remove(shape);
            Logger.info("Object removed from scene");
        }
    }
    
    /**
     * Zmaže všechny objekty ze scény.
     */
    public void clearScene() {
        objects.clear();
        sceneRoot.getChildren().removeAll(sceneRoot.getChildren());
        Logger.info("Scene cleared");
    }
    
    /**
     * Vrací seznam všech objektů v scéně.
     */
    public ObservableList<Shape3D> getObjects() {
        return objects;
    }
    
    /**
     * Vrací počet objektů v scéně.
     */
    public int getObjectCount() {
        return objects.size();
    }
    
    /**
     * Kontroluje, zda scéna obsahuje objekt.
     */
    public boolean contains(Shape3D shape) {
        return objects.contains(shape);
    }
    
    /**
     * Najde objekt podle ID (jelikož máme vlastní komponenty).
     */
    public Shape3D findObjectById(int id) {
        for (Shape3D shape : objects) {
            if (shape instanceof CustomBox box && box.getID() == id) {
                return shape;
            } else if (shape instanceof CustomSphere sphere && sphere.getID() == id) {
                return shape;
            } else if (shape instanceof CustomCylinder cylinder && cylinder.getID() == id) {
                return shape;
            }
        }
        return null;
    }
    
    /**
     * Resetuje ID countery pro nový projekt.
     */
    public void resetIdCounters() {
        CustomBox.resetIdCounter();
        CustomSphere.resetIdCounter();
        CustomCylinder.resetIdCounter();
        Logger.debug("ID counters reset");
    }
    
}
