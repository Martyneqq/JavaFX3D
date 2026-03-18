package javafx3D.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Rotate;
import javafx3D.components.CustomBox;
import javafx3D.components.CustomCylinder;
import javafx3D.components.CustomSphere;
import javafx3D.utils.Logger;

/**
 * Servisní třída pro správu transformací 3D objektů.
 * Zahrnuje rotaci, posun a změnu velikosti.
 */
public class TransformService {
    
    private Shape3D selectedShape;
    private double anchorX;
    private double anchorY;
    
    public TransformService() {
        this.selectedShape = null;
        this.anchorX = 0;
        this.anchorY = 0;
    }
    
    /**
     * Nastaví vybraný objekt pro transformaci.
     */
    public void setSelectedShape(Shape3D shape) {
        this.selectedShape = shape;
        if (shape != null) {
            Logger.info("Selected shape: " + shape.getClass().getSimpleName());
        }
    }
    
    /**
     * Získá aktuálně vybraný objekt.
     */
    public Shape3D getSelectedShape() {
        return selectedShape;
    }
    
    /**
     * Kontroluje, je-li nějaký objekt vybrán.
     */
    public boolean hasSelectedShape() {
        return selectedShape != null;
    }
    
    /**
     * Deklearuje počáteční polohu myši pro transformaci.
     */
    public void startTransform(double x, double y) {
        this.anchorX = x;
        this.anchorY = y;
    }
    
    /**
     * Rotuje vybraný objekt na základě pohybu myši.
     */
    public void rotateShape(double currentX, double currentY) {
        if (!hasSelectedShape()) {
            return;
        }
        
        double dx = anchorX - currentX;
        double dy = anchorY - currentY;
        
        // Aplikuj rotaci na Y ose (horizontální pohyb)
        selectedShape.setRotationAxis(Rotate.Y_AXIS);
        selectedShape.setRotate(selectedShape.getRotate() - dx * 0.5);
        
        // Aplikuj rotaci na X ose (vertikální pohyb)
        selectedShape.setRotationAxis(Rotate.X_AXIS);
        selectedShape.setRotate(selectedShape.getRotate() - dy * 0.5);
        
        // Aktualizuj pozici pro další iteraci
        this.anchorX = currentX;
        this.anchorY = currentY;
    }
    
    /**
     * Posune vybraný objekt na základě pohybu myši.
     */
    public void moveShape(double currentX, double currentY) {
        if (!hasSelectedShape()) {
            return;
        }
        
        double dx = currentX - anchorX;
        double dy = currentY - anchorY;
        
        selectedShape.setTranslateX(selectedShape.getTranslateX() + dx * 0.5);
        selectedShape.setTranslateY(selectedShape.getTranslateY() + dy * 0.5);
        
        this.anchorX = currentX;
        this.anchorY = currentY;
    }
    
    /**
     * Změní velikost vybraného objektu na základě škálování.
     * Podporuje pouze CustomBox, CustomSphere a CustomCylinder.
     */
    public void scaleShape(double scaleFactor) {
        if (!hasSelectedShape()) {
            return;
        }
        
        if (scaleFactor <= 0) {
            Logger.warn("Scale factor musí být větší než 0");
            return;
        }
        
        selectedShape.setScaleX(selectedShape.getScaleX() * scaleFactor);
        selectedShape.setScaleY(selectedShape.getScaleY() * scaleFactor);
        selectedShape.setScaleZ(selectedShape.getScaleZ() * scaleFactor);
        
        Logger.debug("Object scaled by " + scaleFactor);
    }
    
    /**
     * Resetuje transformace vybraného objektu.
     */
    public void resetTransforms() {
        if (!hasSelectedShape()) {
            return;
        }
        
        selectedShape.setRotate(0);
        selectedShape.setScaleX(1);
        selectedShape.setScaleY(1);
        selectedShape.setScaleZ(1);
        
        Logger.info("Transformations reset for: " + selectedShape.getClass().getSimpleName());
    }
    
    /**
     * Resetuje výběr.
     */
    public void clearSelection() {
        this.selectedShape = null;
    }
    
}
