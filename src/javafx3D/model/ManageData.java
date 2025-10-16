package javafx3D.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

// TODO This class is supposed to save and load data to/from file

public class ManageData {
    public void saveData(){
        SaveData sData = new SaveData();
        ArrayList<Object> objData = new ArrayList<Object>();
        
        //objData.add(sData.colorCube);
        /*objData.add(sData.getCubeId());
        objData.add(sData.getCubeWidth());
        objData.add(sData.getCubeHeight());
        objData.add(sData.getCubeDepth());
        
        //objData.add(sData.colorSphere);
        objData.add(sData.getSphereId());
        objData.add(sData.getSphereRadius());
        
        //objData.add(sData.colorCylinder);
        objData.add(sData.getCylinderId());
        objData.add(sData.getCylinderHeight());
        objData.add(sData.getCylinderRadius());*/
    }
    
    public static void saveFile(Serializable data, String fileName) throws FileNotFoundException, IOException
    {
        try(ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName))))
        {
           output.writeObject(data);
        }
    }
    
    public static Object readFile(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        try(ObjectInputStream input = new ObjectInputStream(Files.newInputStream(Paths.get(fileName))) )
        {
            return input.readObject();
        }
    }
}
