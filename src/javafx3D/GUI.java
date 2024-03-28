package javafx3D;

import com.sun.javafx.logging.PlatformLogger.Level;
import java.awt.Canvas;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author cepel
 */
public class GUI extends Application implements EventHandler<ActionEvent>
{
    private static final float WIDTH = 1280;
    private static final float HEIGHT = 720;
    private static int cubeID;
    private static int sphereID;
    private static int cylinderID;
    private static int numberOfObjects = 0;
    private String name;
    
    private Camera camera;
    private Menu file;
    private Menu edit;
    private Menu login;
    private MenuItem m1;
    private MenuItem m2;
    private MenuItem m3;
    private MenuItem m4;
    private MenuBar mb;
    private ExtensionGroup extension;
    private Group root;
    private BorderPane space;
    private Button cubeButton;
    private Button sphereButton;
    private Button cylinderButton;
    private ToggleButton moveButton;
    private ToggleButton rotateButton;
    private ToggleButton scaleButton;
    private ToggleGroup buttonGroup;
    private CustomBox cube;
    private CustomSphere sphere;
    private CustomCylinder cylinder;
    private static ListView listView;
    private FileChooser fileChooser;
    private Pane pane;
    private ColorPicker colorPicker;
    private String cubeName;
    private ManageData mData;
    
    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    
    
    public static void main(String[] args) {
    launch(args);
    }      
    
    private void initMoveRotationScale(ExtensionGroup group, Scene scene, Stage stage) {
        rotateButton.setOnAction(new EventHandler<ActionEvent>(){  
            //Rotate
            @Override
            public void handle(ActionEvent t) {
                    Rotate xRotate;
                    Rotate yRotate;
                    group.getTransforms().addAll(
                       xRotate = new Rotate(WIDTH/2, Rotate.X_AXIS),
                       yRotate = new Rotate(HEIGHT/2, Rotate.Y_AXIS)
                    );
                    xRotate.angleProperty().bind(angleX);
                    yRotate.angleProperty().bind(angleY);

                scene.setOnMousePressed(event1 -> {
                    anchorX = event1.getSceneX();
                    anchorY = event1.getSceneY();
                    anchorAngleX = angleX.get();
                    anchorAngleY = angleY.get();
                });
                scene.setOnMouseDragged(event2 -> {
                angleX.set(anchorAngleX - (anchorY - event2.getSceneY()));
                angleY.set(anchorAngleY + anchorX - event2.getSceneX());
            });
            }
        });
    
    stage.addEventHandler(ScrollEvent.SCROLL, event3 ->{
        double move = event3.getDeltaY();
        group.translateZProperty().set(group.getTranslateZ() - move);
    });
  }
    
    //cubeButton
    private void cubeBtn(){
        cubeButton = new Button();
        Image image = new Image(getClass().getResourceAsStream("/images/cube3D.png"));
        ImageView imageView = new ImageView(image);
        cubeButton.setGraphic(imageView);
        cubeButton.setTranslateX(20);
        cubeButton.setTranslateY(40);
        cubeButton.setOnAction(this);
    }
    
    //sphereButton
    private void sphereBtn(){
        
        sphereButton = new Button();
        Image image1 = new Image(getClass().getResourceAsStream("/images/sphere3D.png"));
        ImageView imageView1 = new ImageView(image1);
        sphereButton.setGraphic(imageView1);
        sphereButton.setTranslateX(20);
        sphereButton.setTranslateY(75);
        sphereButton.setOnAction(this);
    }
    
    private void cylinderBtn(){
        cylinderButton = new Button();
        Image image2 = new Image(getClass().getResourceAsStream("/images/cylinder3D.png"));
        ImageView imageView2 = new ImageView(image2);
        cylinderButton.setGraphic(imageView2);
        cylinderButton.setTranslateX(20);
        cylinderButton.setTranslateY(110);
        cylinderButton.setOnAction(this);
    }
    
    private void MoveButton(){
        moveButton = new ToggleButton();
        Image image3 = new Image(getClass().getResourceAsStream("/images/move3D.png"));
        ImageView imageView3 = new ImageView(image3);
        moveButton.setGraphic(imageView3);
        moveButton.setTranslateX(20);
        moveButton.setTranslateY(HEIGHT/2 - 35);
        moveButton.setOnAction(this);
    }
    
    private void RotateButton(){
        rotateButton = new ToggleButton();
        Image image4 = new Image(getClass().getResourceAsStream("/images/rotate3D.png"));
        ImageView imageView4 = new ImageView(image4);
        rotateButton.setGraphic(imageView4);
        rotateButton.setTranslateX(20);
        rotateButton.setTranslateY(HEIGHT/2);
        rotateButton.setOnAction(this);
    }
    
    private void ScaleButton() {
        scaleButton = new ToggleButton();
        Image image5 = new Image(getClass().getResourceAsStream("/images/scale3D.png"));
        ImageView imageView5 = new ImageView(image5);
        scaleButton.setGraphic(imageView5);
        scaleButton.setTranslateX(20);
        scaleButton.setTranslateY(HEIGHT/2 + 35);
        scaleButton.setOnAction(this);
    }
    
    //Menu
    private void createMenu(){
        
        file = new Menu("File");
        m1 = new MenuItem("Save...");
        m4 = new MenuItem("Open...");
        m2 = new MenuItem("Exit");
        file.getItems().addAll(m1, m4, new SeparatorMenuItem(),m2);
        
        edit = new Menu("Edit");
        m3 = new MenuItem("?");
        edit.getItems().addAll(m3);  
        
        mb = new MenuBar();
        mb.getMenus().addAll(file, edit);
    }
    //List
    private void listCounter(){
        listView.setTranslateX(1130);
        listView.setTranslateY(25);
        listView.setPrefWidth(150);
        listView.setPrefHeight(498);
        
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                /*if (listView.getSelectionModel().getSelectedItem() == myObject) {
                    myObject = selectionTexture();
                }*/

                System.out.println("clicked on " + listView.getSelectionModel().getSelectedItem());
            }
    });
    }
    
    private void textureWindow(){
        pane = new Pane();
        
        pane.setStyle("-fx-background-color: white;");
        pane.setTranslateX(1130);
        pane.setTranslateY(525);
        pane.setPrefWidth(150);
        pane.setPrefHeight(195);
        
        /*colorPicker = new ColorPicker();
        colorPicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                Color c = colorPicker.getValue(); 
                System.out.println("New Color's RGB = "+c.getRed()+" "+c.getGreen()+" "+c.getBlue());
            }
        });*/
    }
    
    private void menuOptions(Stage stage){

        /*fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
        //TODO*/
        m1.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent event) {
               
               /*try {
                   mData.saveFile();
               } catch (IOException ex) {
                   Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
               }*/
           }
        });
        m4.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent event) {
               /*
               try {
                   mData.readFile();
                   
               } catch (IOException ex) {
                   Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
               } catch (ClassNotFoundException ex) {
                   Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
               }*/
           }
        });
        m2.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent event) {
              Platform.exit();
              System.exit(0);
           }
        });
    }
    /*
    private ObjectBox selectionTexture(){
        PhongMaterial material = new PhongMaterial(Color.RED);
        myObject.setMaterial(material);
        
        return myObject;
    }*/
    
    @Override
    public void start(Stage stage) throws Exception {
        
        extension = new ExtensionGroup();
        root = new Group();
        buttonGroup = new ToggleGroup();
        listView = new ListView();
        
        listCounter();
        createMenu();
        cubeBtn();
        sphereBtn();
        cylinderBtn();
        MoveButton();
        RotateButton();
        ScaleButton();
        menuOptions(stage);
        textureWindow();
        
        root.getChildren().addAll(extension);
        
        //Scene
        camera = new PerspectiveCamera();
        
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);
        scene.getStylesheets().add(getClass().getResource("/css/stylesheet.css").toExternalForm());

        initMoveRotationScale(extension, scene, stage);

        moveButton.setToggleGroup(buttonGroup);
        rotateButton.setToggleGroup(buttonGroup);
        scaleButton.setToggleGroup(buttonGroup);
        
        root.getChildren().addAll(cubeButton, sphereButton, cylinderButton, moveButton, rotateButton, scaleButton, mb, listView, pane);
        
        mb.prefWidthProperty().bind(stage.widthProperty());
        stage.setTitle("FXML3D");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    @Override
    public void handle(ActionEvent event){
        if (event.getSource()==cubeButton) {
            cube = new CustomBox(Color.LIGHTGREY, cubeID);

            extension.translateXProperty().set(WIDTH/2);
            extension.translateYProperty().set(HEIGHT/2);
            
            extension.getChildren().add(cube);
            cubeID++;
            cube.setID(sphereID);
            String cubeName = "Cube " + Integer.toString(cube.getID());
            numberOfObjects++;
            listView.getItems().add(cubeName);
            
            System.out.println("Objects: " + numberOfObjects);
        }
        else if (event.getSource()==sphereButton) {
            sphere = new CustomSphere(Color.LIGHTGREY, sphereID);

            extension.translateXProperty().set(WIDTH/2);
            extension.translateYProperty().set(HEIGHT/2);
            
            extension.getChildren().add(sphere);
            sphereID++;
            sphere.setID(sphereID);
            String sphereName = "Sphere " + Integer.toString(sphere.getID());
            numberOfObjects++;
            listView.getItems().add(sphereName);
            
            System.out.println("Objects: " + numberOfObjects);
        }
        else if (event.getSource()==cylinderButton) {
            cylinder = new CustomCylinder(Color.LIGHTGREY, cylinderID);
            
            extension.translateXProperty().set(WIDTH/2);
            extension.translateYProperty().set(HEIGHT/2);
            
            extension.getChildren().add(cylinder);
            cylinderID++;
            cylinder.setID(cylinderID);
            String cylinderName = "Cylinder " + Integer.toString(cylinder.getID());
            numberOfObjects++;
            listView.getItems().add(cylinderName);
            
            System.out.println("Objects: " + numberOfObjects);
        }
        /*if (event.getSource()==moveButton) {
            
        }
        if (event.getSource()==rotateButton) {
            
        }
        if (event.getSource()==scaleButton) {
            
        }*/
    }
}
