package javafx3D.handler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx3D.service.Scene3DManager;
import javafx3D.service.TransformService;
import javafx3D.utils.Logger;

/**
 * Spravuje všechny event handlery pro 3D transformace.
 * Odděluje UI logiku od business logiky.
 */
public class TransformEventHandler {
    
    private final TransformService transformService;
    private final Scene3DManager scene3DManager;
    
    public TransformEventHandler(TransformService transformService, Scene3DManager scene3DManager) {
        this.transformService = transformService;
        this.scene3DManager = scene3DManager;
    }
    
    /**
     * Vrací handler pro rotaci - reaguje na pohyb myši.
     */
    public EventHandler<MouseEvent> getRotateMouseDragHandler() {
        return event -> {
            transformService.rotateShape(event.getSceneX(), event.getSceneY());
        };
    }
    
    /**
     * Vrací handler pro posun - reaguje na pohyb myši.
     */
    public EventHandler<MouseEvent> getMoveMouseDragHandler() {
        return event -> {
            transformService.moveShape(event.getSceneX(), event.getSceneY());
        };
    }
    
    /**
     * Vrací handler pro zahájení transformace.
     */
    public EventHandler<MouseEvent> getTransformStartHandler() {
        return event -> {
            transformService.startTransform(event.getSceneX(), event.getSceneY());
        };
    }
    
    /**
     * Vrací handler pro scroll wheel - zoom.
     */
    public EventHandler<ScrollEvent> getScrollZoomHandler(javafx.scene.Group group) {
        return event -> {
            double move = event.getDeltaY();
            group.setTranslateZ(group.getTranslateZ() - move);
            Logger.debug("Zoom: " + group.getTranslateZ());
        };
    }
    
    /**
     * Vrací handler pro scale s použitím scroll wheelu.
     */
    public EventHandler<ScrollEvent> getScaleScrollHandler() {
        return event -> {
            double move = event.getDeltaY();
            double scaleFactor = 1 + (move > 0 ? 0.05 : -0.05);
            transformService.scaleShape(scaleFactor);
        };
    }
    
}
