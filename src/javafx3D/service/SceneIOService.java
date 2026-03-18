package javafx3D.service;

import com.google.gson.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape3D;
import javafx3D.components.CustomBox;
import javafx3D.components.CustomCylinder;
import javafx3D.components.CustomSphere;
import javafx3D.utils.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Servisní třída pro ukládání a načítání 3D scén.
 * Používá JSON formát pro lepší kompatibilitu a flexibilitu.
 */
public class SceneIOService {
    
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    /**
     * Serializovatelný objekt pro uložení 3D objektu.
     */
    static class SerializableShape {
        String type;
        int id;
        String color;
        double x, y, z;
        double scaleX, scaleY, scaleZ;
        double rotateX, rotateY, rotateZ;
        double rotation;
        
        // Pro parametry objektů
        double width, height, depth;  // Box
        double radius;                // Sphere, Cylinder
        double cylinderHeight;        // Cylinder
        
        SerializableShape() {}
        
        SerializableShape(Shape3D shape) {
            this.x = shape.getTranslateX();
            this.y = shape.getTranslateY();
            this.z = shape.getTranslateZ();
            this.scaleX = shape.getScaleX();
            this.scaleY = shape.getScaleY();
            this.scaleZ = shape.getScaleZ();
            this.rotation = shape.getRotate();
            
            if (shape instanceof CustomBox box) {
                this.type = "BOX";
                this.id = box.getID();
                this.color = colorToHex(box.getColor());
                this.width = box.getWidth();
                this.height = box.getHeight();
                this.depth = box.getDepth();
            } else if (shape instanceof CustomSphere sphere) {
                this.type = "SPHERE";
                this.id = sphere.getID();
                this.color = colorToHex(sphere.getColor());
                this.radius = sphere.getRadius();
            } else if (shape instanceof CustomCylinder cylinder) {
                this.type = "CYLINDER";
                this.id = cylinder.getID();
                this.color = colorToHex(cylinder.getColor());
                this.radius = cylinder.getRadius();
                this.cylinderHeight = cylinder.getHeight();
            }
        }
    }
    
    /**
     * Uloží scénu do JSON souboru.
     */
    public static void saveScene(List<Shape3D> objects, String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path nemůže být prázdný");
        }
        
        List<SerializableShape> serializableShapes = new ArrayList<>();
        for (Shape3D shape : objects) {
            serializableShapes.add(new SerializableShape(shape));
        }
        
        JsonArray jsonArray = gson.toJsonTree(serializableShapes).getAsJsonArray();
        JsonObject root = new JsonObject();
        root.add("shapes", jsonArray);
        root.addProperty("version", "1.0");
        root.addProperty("timestamp", System.currentTimeMillis());
        
        String json = gson.toJson(root);
        Files.write(Paths.get(filePath), json.getBytes());
        
        Logger.info("Scene saved to: " + filePath);
    }
    
    /**
     * Načte scénu z JSON souboru.
     */
    public static List<Shape3D> loadScene(String filePath) throws IOException {
        if (!Files.exists(Paths.get(filePath))) {
            throw new FileNotFoundException("Soubor nebyl nalezen: " + filePath);
        }
        
        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        JsonArray shapesArray = root.getAsJsonArray("shapes");
        
        List<Shape3D> shapes = new ArrayList<>();
        
        for (JsonElement element : shapesArray) {
            JsonObject shapeJson = element.getAsJsonObject();
            Shape3D shape = deserializeShape(shapeJson);
            if (shape != null) {
                shapes.add(shape);
            }
        }
        
        Logger.info("Scene loaded from: " + filePath + " (Objects: " + shapes.size() + ")");
        return shapes;
    }
    
    /**
     * Deserializuje objekt ze JSON.
     */
    private static Shape3D deserializeShape(JsonObject json) {
        String type = json.get("type").getAsString();
        Color color = hexToColor(json.get("color").getAsString());
        
        Shape3D shape = null;
        
        switch (type) {
            case "BOX":
                double width = json.get("width").getAsDouble();
                double height = json.get("height").getAsDouble();
                double depth = json.get("depth").getAsDouble();
                shape = new CustomBox(color, width, height, depth);
                break;
            case "SPHERE":
                double radius = json.get("radius").getAsDouble();
                shape = new CustomSphere(color, radius);
                break;
            case "CYLINDER":
                double cylinderRadius = json.get("radius").getAsDouble();
                double cylinderHeight = json.get("cylinderHeight").getAsDouble();
                shape = new CustomCylinder(color, cylinderRadius, cylinderHeight);
                break;
        }
        
        if (shape != null) {
            // Obnoví transformace
            shape.setTranslateX(json.get("x").getAsDouble());
            shape.setTranslateY(json.get("y").getAsDouble());
            shape.setTranslateZ(json.get("z").getAsDouble());
            shape.setScaleX(json.get("scaleX").getAsDouble());
            shape.setScaleY(json.get("scaleY").getAsDouble());
            shape.setScaleZ(json.get("scaleZ").getAsDouble());
            shape.setRotate(json.get("rotation").getAsDouble());
        }
        
        return shape;
    }
    
    /**
     * Konvertuje barvu na hex string.
     */
    private static String colorToHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
    
    /**
     * Konvertuje hex string na barvu.
     */
    private static Color hexToColor(String hex) {
        try {
            return Color.web(hex);
        } catch (IllegalArgumentException e) {
            Logger.warn("Invalid color: " + hex + ", using default");
            return Color.LIGHTGREY;
        }
    }
    
}
