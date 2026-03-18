package javafx3DV2.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx3D.config.AppConfig;
import javafx3D.handler.TransformEventHandler;
import javafx3D.service.Scene3DManager;
import javafx3D.service.SceneIOService;
import javafx3D.service.TransformService;
import javafx3D.utils.ExtensionGroup;
import javafx3D.utils.Logger;

import java.io.IOException;
import java.util.Optional;

/**
 * Refaktorovaná GUI třída s lepší architekturou a modulárností.
 * Používá dependency injection a odděluje concerns.
 */
public class GUIv2 extends Application {
    
    // Services
    private Scene3DManager scene3DManager;
    private TransformService transformService;
    private TransformEventHandler eventHandler;
    
    // UI Components
    private Stage primaryStage;
    private Scene mainScene;
    private Group root;
    private ExtensionGroup extension;
    private BorderPane layout;
    
    // Control Components
    private Button btnAdd;
    private ToggleButton btnRotate;
    private ToggleButton btnMove;
    private ToggleButton btnScale;
    private ToggleGroup toolGroup;
    private ListView<String> listView;
    private MenuBar menuBar;
    
    // Camera
    private Camera camera;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Logger.info("Aplikace spouštěna...");
        
        try {
            this.primaryStage = stage;
            initializeServices();
            initializeUI();
            setupMenuBar();
            setupControlPanel();
            setupEventHandlers();
            
            primaryStage.setTitle(AppConfig.WINDOW_TITLE);
            primaryStage.setScene(mainScene);
            primaryStage.setResizable(AppConfig.WINDOW_RESIZABLE);
            primaryStage.show();
            
            Logger.info("Aplikace úspěšně spuštěna");
        } catch (Exception e) {
            Logger.error("Chyba při startu aplikace", e);
            showErrorDialog("Startup Error", "Aplikace se nemohla spustit: " + e.getMessage());
        }
    }
    
    /**
     * Inicializuje všechny servisní třídy.
     */
    private void initializeServices() {
        Logger.debug("Inicializace služeb...");
        
        extension = new ExtensionGroup();
        root = new Group();
        root.getChildren().add(extension);
        
        scene3DManager = new Scene3DManager(extension);
        transformService = new TransformService();
        eventHandler = new TransformEventHandler(transformService, scene3DManager);
        
        Logger.debug("Služby inicializovány");
    }
    
    /**
     * Inicializuje hlavní UI komponenty.
     */
    private void initializeUI() {
        Logger.debug("Inicializace UI...");
        
        // Vytvoř kamerů
        camera = new PerspectiveCamera(true);
        camera.setTranslateZ(AppConfig.INITIAL_Z_POSITION);
        
        // Nastavit root do Scene s kamerou
        Scene rootScene = new Scene(root, AppConfig.WINDOW_WIDTH, AppConfig.WINDOW_HEIGHT);
        rootScene.setFill(Color.web(AppConfig.SCENE_BACKGROUND_COLOR));
        rootScene.setCamera(camera);
        rootScene.getStylesheets().add(getClass().getResource(AppConfig.STYLESHEET_PATH).toExternalForm());
        
        // Vytvoř main layout BorderPane - toto bude primární
        layout = new BorderPane();
        layout.setCenter(rootScene.getRoot());
        
        // Vytvoř finální scénu s BorderPane jako root
        mainScene = new Scene(layout, AppConfig.WINDOW_WIDTH, AppConfig.WINDOW_HEIGHT);
        mainScene.getStylesheets().add(getClass().getResource(AppConfig.STYLESHEET_PATH).toExternalForm());
        
        Logger.debug("UI inicializováno");
    }
    
    /**
     * Vytvoří menu bar.
     */
    private void setupMenuBar() {
        Logger.debug("Nastavování menu...");
        
        menuBar = new MenuBar();
        
        // File menu
        Menu fileMenu = createFileMenu();
        Menu editMenu = createEditMenu();
        Menu helpMenu = createHelpMenu();
        
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);
        
        // Přidej menu do top
        layout.setTop(menuBar);
    }
    
    /**
     * Vytvoří File menu.
     */
    private Menu createFileMenu() {
        Menu fileMenu = new Menu("File");
        
        MenuItem saveItem = new MenuItem("Save...");
        saveItem.setOnAction(event -> handleSaveScene());
        
        MenuItem loadItem = new MenuItem("Open...");
        loadItem.setOnAction(event -> handleLoadScene());
        
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(event -> Platform.exit());
        
        fileMenu.getItems().addAll(
                saveItem,
                loadItem,
                new SeparatorMenuItem(),
                exitItem
        );
        
        return fileMenu;
    }
    
    /**
     * Vytvoří Edit menu.
     */
    private Menu createEditMenu() {
        Menu editMenu = new Menu("Edit");
        
        MenuItem clearItem = new MenuItem("Clear Scene");
        clearItem.setOnAction(event -> handleClearScene());
        
        MenuItem resetTransformItem = new MenuItem("Reset Transform");
        resetTransformItem.setOnAction(event -> {
            transformService.resetTransforms();
        });
        
        editMenu.getItems().addAll(clearItem, resetTransformItem);
        
        return editMenu;
    }
    
    /**
     * Vytvoří Help menu.
     */
    private Menu createHelpMenu() {
        Menu helpMenu = new Menu("Help");
        
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(event -> showAboutDialog());
        
        helpMenu.getItems().add(aboutItem);
        
        return helpMenu;
    }
    
    /**
     * Vytvoří control panel s tlačítky a nástrojem.
     */
    private void setupControlPanel() {
        Logger.debug("Nastavování control panelu...");
        
        // Vytvoř toolbar pro přidání objektů
        VBox toolbar = createToolbar();
        
        // Vytvoř list view pro objekty
        listView = new ListView<>();
        listView.setPrefWidth(150);
        listView.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });
        listView.setOnMouseClicked(event -> {
            int selectedIndex = listView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < scene3DManager.getObjects().size()) {
                transformService.setSelectedShape(scene3DManager.getObjects().get(selectedIndex));
            }
        });
        
        // Vytvoř right panel s toolbarem a listview
        VBox rightPanel = new VBox(10);
        rightPanel.setStyle("-fx-padding: 10;");
        rightPanel.getChildren().addAll(toolbar, new Separator(), listView);
        
        // Přidej do layout
        layout.setLeft(toolbar);
        layout.setRight(rightPanel);
    }
    
    /**
     * Vytvoří toolbar s tlačítky.
     */
    private VBox createToolbar() {
        VBox toolbar = new VBox(10);
        toolbar.setStyle("-fx-padding: 10; -fx-border-color: #cccccc;");
        
        Label addLabel = new Label("Add Objects:");
        Button cubeBtn = createImageButton("/images/cube3D.png", "Add Cube", event -> {
            scene3DManager.addCube(Color.LIGHTGREY);
            updateObjectList();
        });
        
        Button sphereBtn = createImageButton("/images/sphere3D.png", "Add Sphere", event -> {
            scene3DManager.addSphere(Color.LIGHTGREY);
            updateObjectList();
        });
        
        Button cylinderBtn = createImageButton("/images/cylinder3D.png", "Add Cylinder", event -> {
            scene3DManager.addCylinder(Color.LIGHTGREY);
            updateObjectList();
        });
        
        Label transformLabel = new Label("Transform:");
        toolGroup = new ToggleGroup();
        
        btnMove = new ToggleButton();
        btnMove.setGraphic(getImageView("/images/move3D.png"));
        btnMove.setToggleGroup(toolGroup);
        btnMove.setOnAction(event -> setTransformMode("MOVE"));
        
        btnRotate = new ToggleButton();
        btnRotate.setGraphic(getImageView("/images/rotate3D.png"));
        btnRotate.setToggleGroup(toolGroup);
        btnRotate.setOnAction(event -> setTransformMode("ROTATE"));
        
        btnScale = new ToggleButton();
        btnScale.setGraphic(getImageView("/images/scale3D.png"));
        btnScale.setToggleGroup(toolGroup);
        btnScale.setOnAction(event -> setTransformMode("SCALE"));
        
        toolbar.getChildren().addAll(
                addLabel,
                cubeBtn,
                sphereBtn,
                cylinderBtn,
                new Separator(),
                transformLabel,
                btnMove,
                btnRotate,
                btnScale
        );
        
        return toolbar;
    }
    
    /**
     * Vytvoří button s obrázkem.
     */
    private Button createImageButton(String imagePath, String tooltip, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button btn = new Button();
        try {
            Image img = new Image(getClass().getResourceAsStream(imagePath));
            ImageView imgView = new ImageView(img);
            imgView.setFitHeight(32);
            imgView.setFitWidth(32);
            btn.setGraphic(imgView);
        } catch (Exception e) {
            Logger.warn("Obrázek nenalezen: " + imagePath);
            btn.setText(tooltip);
        }
        btn.setTooltip(new Tooltip(tooltip));
        btn.setOnAction(handler);
        return btn;
    }
    
    /**
     * Vrátí ImageView z cesty.
     */
    private ImageView getImageView(String imagePath) {
        try {
            Image img = new Image(getClass().getResourceAsStream(imagePath));
            ImageView imgView = new ImageView(img);
            imgView.setFitHeight(24);
            imgView.setFitWidth(24);
            return imgView;
        } catch (Exception e) {
            Logger.warn("Obrázek nenalezen: " + imagePath);
            return new ImageView();
        }
    }
    
    /**
     * Nastaví mód transformace.
     */
    private void setTransformMode(String mode) {
        Logger.info("Transform mode: " + mode);
        
        // Clear previous handlers
        mainScene.setOnMousePressed(null);
        mainScene.setOnMouseDragged(null);
        mainScene.setOnScroll(null);
        
        switch (mode) {
            case "MOVE" -> {
                mainScene.setOnMousePressed(eventHandler.getTransformStartHandler());
                mainScene.setOnMouseDragged(eventHandler.getMoveMouseDragHandler());
            }
            case "ROTATE" -> {
                mainScene.setOnMousePressed(eventHandler.getTransformStartHandler());
                mainScene.setOnMouseDragged(eventHandler.getRotateMouseDragHandler());
            }
            case "SCALE" -> {
                mainScene.setOnScroll(eventHandler.getScaleScrollHandler());
            }
        }
    }
    
    /**
     * Setupuje globální event handlery.
     */
    private void setupEventHandlers() {
        Logger.debug("Nastavování event handlerů...");
        
        // Zoom pomocí scroll wheelu
        mainScene.addEventHandler(ScrollEvent.SCROLL, eventHandler.getScrollZoomHandler(extension));
    }
    
    /**
     * Aktualizuje seznam objektů v ListView.
     */
    private void updateObjectList() {
        if (listView != null) {
            listView.getItems().clear();
            for (var shape : scene3DManager.getObjects()) {
                if (shape instanceof javafx3D.components.CustomBox box) {
                    listView.getItems().add("Cube " + box.getID());
                } else if (shape instanceof javafx3D.components.CustomSphere sphere) {
                    listView.getItems().add("Sphere " + sphere.getID());
                } else if (shape instanceof javafx3D.components.CustomCylinder cylinder) {
                    listView.getItems().add("Cylinder " + cylinder.getID());
                }
            }
            Logger.info("Object list updated: " + scene3DManager.getObjectCount() + " objects");
        }
    }
    
    /**
     * Zpracuje uložení scény.
     */
    private void handleSaveScene() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(AppConfig.FILE_SAVE_TITLE);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JavaFX3D Files", "*" + AppConfig.DEFAULT_SAVE_EXTENSION)
        );
        
        var file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            try {
                SceneIOService.saveScene(
                        new java.util.ArrayList<>(scene3DManager.getObjects()),
                        file.getAbsolutePath()
                );
                showInfoDialog("Success", "Scéna byla úspěšně uložena");
            } catch (IOException e) {
                Logger.error("Chyba při ukládání scény", e);
                showErrorDialog("Save Error", "Nepodařilo se uložit scénu: " + e.getMessage());
            }
        }
    }
    
    /**
     * Zpracuje načtení scény.
     */
    private void handleLoadScene() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(AppConfig.FILE_OPEN_TITLE);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JavaFX3D Files", "*" + AppConfig.DEFAULT_SAVE_EXTENSION)
        );
        
        var file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            try {
                scene3DManager.clearScene();
                var shapes = SceneIOService.loadScene(file.getAbsolutePath());
                for (var shape : shapes) {
                    scene3DManager.addObject(shape);
                }
                updateObjectList();
                showInfoDialog("Success", "Scéna byla úspěšně načtena");
            } catch (IOException e) {
                Logger.error("Chyba při načítání scény", e);
                showErrorDialog("Load Error", "Nepodařilo se načíst scénu: " + e.getMessage());
            }
        }
    }
    
    /**
     * Zpracuje vymazání scény.
     */
    private void handleClearScene() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Clear Scene");
        alert.setContentText("Chceš vymazat všechny objekty ze scény?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            scene3DManager.clearScene();
            scene3DManager.resetIdCounters();
            transformService.clearSelection();
            updateObjectList();
            Logger.info("Scene cleared");
        }
    }
    
    /**
     * Zobrazí dialog o aplikaci.
     */
    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("JavaFX 3D Editor");
        alert.setContentText("Verze 2.0\nRefaktorovaná architektura s podporou Save/Load");
        alert.showAndWait();
    }
    
    /**
     * Zobrazí chybový dialog.
     */
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Zobrazí informační dialog.
     */
    private void showInfoDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
}
