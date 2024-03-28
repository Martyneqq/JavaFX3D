package javafx3D;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.paint.Color;

/**
 *
 * @author cepel
 */
public class SaveData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    public Color colorCube, colorSphere, colorCylinder;
    public int cubeid, sphereid, cylinderid;
    public double width, heightCube, heightCylinder, depth, radiusSphere, radiusCylinder;
    
    
}
