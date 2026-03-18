package javafx3D.model;

import javafx.scene.shape.Shape3D;
import javafx3D.service.SceneIOService;
import javafx3D.utils.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Fasáda pro správu dat scén.
 * Slouží jako wrapper kolem SceneIOService s podporou pro oba formáty.
 * 
 * POZNÁMKA: Nový kód by měl používat přímo SceneIOService.
 */
public class ManageData {
    
    /**
     * Uloží scénu do JSON souboru.
     * @param objects Seznam objektů k uložení
     * @param filePath Cesta k souboru
     */
    public void saveScene(List<Shape3D> objects, String filePath) throws IOException {
        SceneIOService.saveScene(objects, filePath);
    }
    
    /**
     * Načte scénu z JSON souboru.
     * @param filePath Cesta k souboru
     * @return Seznam načtených objektů
     */
    public List<Shape3D> loadScene(String filePath) throws IOException {
        return SceneIOService.loadScene(filePath);
    }
    
    /**
     * Uloží data pomocí Java serialization (zastaralé).
     * Nyní preferuj saveScene() pro JSON.
     * 
     * @deprecated Používej SceneIOService místo toho
     */
    @Deprecated(since = "2.0", forRemoval = true)
    public static void saveFile(Serializable data, String fileName) throws IOException {
        Logger.warn("saveFile() is deprecated. Use SceneIOService instead.");
        try (ObjectOutputStream output = new ObjectOutputStream(
                Files.newOutputStream(Paths.get(fileName)))) {
            output.writeObject(data);
        }
    }
    
    /**
     * Načte data pomocí Java serialization (zastaralé).
     * Nyní preferuj loadScene() pro JSON.
     * 
     * @deprecated Używej SceneIOService místo toho
     */
    @Deprecated(since = "2.0", forRemoval = true)
    public static Object readFile(String fileName) throws IOException, ClassNotFoundException {
        Logger.warn("readFile() is deprecated. Use SceneIOService instead.");
        try (ObjectInputStream input = new ObjectInputStream(
                Files.newInputStream(Paths.get(fileName)))) {
            return input.readObject();
        }
    }
    
}

