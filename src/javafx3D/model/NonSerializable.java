package javafx3D.model;

import javafx.scene.paint.Color;

/**
 * Pomocná třída pro práci s neserializovatelnými daty.
 * 
 * Třídy jako Color nemůžou být direktně serializovány
 * bez speciálního zpracování. Tato třída poskytuje
 * konverzi mezi Color a serializovatelnými formáty.
 * 
 * POZNÁMKA: Nyní se používá JSON přes SceneIOService
 *           která tuto logiku již obsahuje.
 */
public class NonSerializable {
    
    /**
     * Konvertuje Color na RGB hodnoty.
     * @param color Barvadel
     * @return Array [R, G, B] v rozsahu 0-255
     */
    public static int[] colorToRGB(Color color) {
        if (color == null) {
            return new int[]{200, 200, 200}; // Default light gray
        }
        
        return new int[]{
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255)
        };
    }
    
    /**
     * Konvertuje RGB hodnoty na Color.
     * @param r Red kanál (0-255)
     * @param g Green kanál (0-255)
     * @param b Blue kanál (0-255)
     * @return Color objekt
     */
    public static Color rgbToColor(int r, int g, int b) {
        return Color.color(r / 255.0, g / 255.0, b / 255.0);
    }
    
    /**
     * Konvertuje Color na hexadecimální string.
     * @param color Barva
     * @return Hex string (např. "#CCCCCC")
     */
    public static String colorToHex(Color color) {
        if (color == null) {
            return "#CCCCCC";
        }
        
        return String.format("#%02X%02X%02X",
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255));
    }
    
    /**
     * Konvertuje hexadecimální string na Color.
     * @param hex Hex string (např. "#FF0000")
     * @return Color objekt, nebo default pokud je hex invalid
     */
    public static Color hexToColor(String hex) {
        try {
            return Color.web(hex);
        } catch (IllegalArgumentException e) {
            return Color.LIGHTGREY; // Default
        }
    }
    
}

