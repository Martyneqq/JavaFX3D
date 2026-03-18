package javafx3D.model;

import javafx.scene.paint.Color;
import java.io.Serializable;

/**
 * Datový model pro ukládání vlastností 3D objektů.
 * Nyní je lepší používat JSON formát přes SceneIOService.
 * 
 * POZNÁMKA: Tato třída je zastaralá. Místo ní používej SceneIOService.
 * 
 * @deprecated Přejdi na SceneIOService.saveScene() a loadScene()
 */
@Deprecated(since = "2.0", forRemoval = true)
public class SaveData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Cube properties
    private Color colorCube;
    private int cubeId;
    private double cubeWidth;
    private double cubeHeight;
    private double cubeDepth;
    
    // Sphere properties
    private Color colorSphere;
    private int sphereId;
    private double sphereRadius;
    
    // Cylinder properties
    private Color colorCylinder;
    private int cylinderId;
    private double cylinderHeight;
    private double cylinderRadius;
    
    // Konstruktor
    public SaveData() {
    }
    
    // Cube Getters/Setters
    public Color getColorCube() { return colorCube; }
    public void setColorCube(Color colorCube) { this.colorCube = colorCube; }
    
    public int getCubeId() { return cubeId; }
    public void setCubeId(int cubeId) { this.cubeId = cubeId; }
    
    public double getCubeWidth() { return cubeWidth; }
    public void setCubeWidth(double width) { this.cubeWidth = width; }
    
    public double getCubeHeight() { return cubeHeight; }
    public void setCubeHeight(double height) { this.cubeHeight = height; }
    
    public double getCubeDepth() { return cubeDepth; }
    public void setCubeDepth(double depth) { this.cubeDepth = depth; }
    
    // Sphere Getters/Setters
    public Color getColorSphere() { return colorSphere; }
    public void setColorSphere(Color colorSphere) { this.colorSphere = colorSphere; }
    
    public int getSphereId() { return sphereId; }
    public void setSphereId(int sphereId) { this.sphereId = sphereId; }
    
    public double getSphereRadius() { return sphereRadius; }
    public void setSphereRadius(double sphereRadius) { this.sphereRadius = sphereRadius; }
    
    // Cylinder Getters/Setters
    public Color getColorCylinder() { return colorCylinder; }
    public void setColorCylinder(Color colorCylinder) { this.colorCylinder = colorCylinder; }
    
    public int getCylinderId() { return cylinderId; }
    public void setCylinderId(int cylinderId) { this.cylinderId = cylinderId; }
    
    public double getCylinderHeight() { return cylinderHeight; }
    public void setCylinderHeight(double height) { this.cylinderHeight = height; }
    
    public double getCylinderRadius() { return cylinderRadius; }
    public void setCylinderRadius(double radius) { this.cylinderRadius = radius; }
    
}
