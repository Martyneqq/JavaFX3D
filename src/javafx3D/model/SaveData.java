package javafx3D.model;

import java.io.Serializable;

import javafx.scene.paint.Color;

/**
 *
 * @author cepel
 */
public class SaveData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private Color colorCube, colorSphere, colorCylinder;
    private int cubeid, sphereid, cylinderid;
    private double width, heightCube, heightCylinder, depth, radiusSphere, radiusCylinder;

}
