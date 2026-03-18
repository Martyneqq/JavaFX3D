package javafx3D.config;

/**
 * Centrální konfigurační třída pro celou aplikaci.
 * Obsahuje všechny konstanci a nastavení GUI.
 */
public class AppConfig {
    
    // Window configuration
    public static final float WINDOW_WIDTH = 1280;
    public static final float WINDOW_HEIGHT = 720;
    public static final String WINDOW_TITLE = "JavaFX 3D Editor";
    public static final boolean WINDOW_RESIZABLE = false;
    
    // Scene configuration
    public static final String SCENE_BACKGROUND_COLOR = "SILVER";
    public static final String STYLESHEET_PATH = "/css/stylesheet.css";
    
    // Button positions
    public static final int BUTTON_X_OFFSET = 20;
    public static final int BUTTON_CUBE_Y = 40;
    public static final int BUTTON_SPHERE_Y = 75;
    public static final int BUTTON_CYLINDER_Y = 110;
    public static final double BUTTON_MID_Y = WINDOW_HEIGHT / 2 - 35;
    public static final double BUTTON_ROTATE_Y = WINDOW_HEIGHT / 2;
    public static final double BUTTON_SCALE_Y = WINDOW_HEIGHT / 2 + 35;
    
    // List view configuration
    public static final double LISTVIEW_X = 1130;
    public static final double LISTVIEW_Y = 25;
    public static final double LISTVIEW_WIDTH = 150;
    public static final double LISTVIEW_HEIGHT = 498;
    
    // Texture pane configuration
    public static final double PANE_X = 1130;
    public static final double PANE_Y = 525;
    public static final double PANE_WIDTH = 150;
    public static final double PANE_HEIGHT = 195;
    
    // 3D Object initial properties
    public static final double CUBE_SIZE = 100;
    public static final double SPHERE_RADIUS = 50;
    public static final double CYLINDER_RADIUS = 50;
    public static final double CYLINDER_HEIGHT = 100;
    
    // Transform center
    public static final double TRANSFORM_CENTER_X = WINDOW_WIDTH / 2;
    public static final double TRANSFORM_CENTER_Y = WINDOW_HEIGHT / 2;
    
    // Camera configuration
    public static final double CAMERA_ZOOM_SENSITIVITY = 1.0;
    public static final double ROTATION_SENSITIVITY = 0.5;
    public static final double INITIAL_Z_POSITION = -300;
    
    // File configuration
    public static final String DEFAULT_SAVE_EXTENSION = ".j3d";
    public static final String FILE_SAVE_TITLE = "Save 3D Scene";
    public static final String FILE_OPEN_TITLE = "Open 3D Scene";
    
    // Logging
    public static final boolean DEBUG_MODE = true;
    
}
