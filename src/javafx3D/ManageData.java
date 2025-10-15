package javafx3D;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ManageData {
    public void saveData(){
        SaveData sData = new SaveData();
        ArrayList<Object> objData = new ArrayList<Object>();
        
        //objData.add(sData.colorCube);
        objData.add(sData.cubeid);
        objData.add(sData.width);
        objData.add(sData.heightCube);
        objData.add(sData.depth);
        
        //objData.add(sData.colorSphere);
        objData.add(sData.sphereid);
        objData.add(sData.radiusSphere);
        
        //objData.add(sData.colorCylinder);
        objData.add(sData.cylinderid);
        objData.add(sData.heightCylinder);
        objData.add(sData.radiusCylinder);
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
