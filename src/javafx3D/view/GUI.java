package javafx3D.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx3D.model.ManageData;
import javafx3D.components.*;
import javafx3D.utils.*;

/**
 *
 * @author cepel
 */
public class GUI extends Application implements EventHandler<ActionEvent>
{
    private static final float WIDTH = 1280;
    private static final float HEIGHT = 720;
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
    private static ListView<javafx.scene.shape.Shape3D> listView;
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

    private javafx.scene.shape.Shape3D selectedShape = null;


    public static void main(String[] args) {
    launch(args);
    }      
    
    private void initMoveRotationScale(ExtensionGroup group, Scene scene, Stage stage) {
//        rotateButton.setOnAction(new EventHandler<ActionEvent>(){
//            //Rotate
//            @Override
//            public void handle(ActionEvent t) {
//                    Rotate xRotate;
//                    Rotate yRotate;
//                    group.getTransforms().addAll(
//                       xRotate = new Rotate(WIDTH/2, Rotate.X_AXIS),
//                       yRotate = new Rotate(HEIGHT/2, Rotate.Y_AXIS)
//                    );
//                    xRotate.angleProperty().bind(angleX);
//                    yRotate.angleProperty().bind(angleY);
//
//                scene.setOnMousePressed(event1 -> {
//                    anchorX = event1.getSceneX();
//                    anchorY = event1.getSceneY();
//                    anchorAngleX = angleX.get();
//                    anchorAngleY = angleY.get();
//                });
//                scene.setOnMouseDragged(event2 -> {
//                angleX.set(anchorAngleX - (anchorY - event2.getSceneY()));
//                angleY.set(anchorAngleY + anchorX - event2.getSceneX());
//            });
//            }
//        });
    
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

        rotateButton.setOnAction(t -> {
            if (rotateButton.isSelected()) {
                Scene scene = rotateButton.getScene();

                scene.setOnMousePressed(event1 -> {
                    anchorX = event1.getSceneX();
                    anchorY = event1.getSceneY();
                });

                scene.setOnMouseDragged(event2 -> {
                    if (selectedShape != null) {
                        double dx = anchorX - event2.getSceneX();
                        double dy = anchorY - event2.getSceneY();

                        selectedShape.setRotationAxis(Rotate.Y_AXIS);
                        selectedShape.setRotate(selectedShape.getRotate() - dx * 0.5);

                        selectedShape.setRotationAxis(Rotate.X_AXIS);
                        selectedShape.setRotate(selectedShape.getRotate() - dy * 0.5);

                        anchorX = event2.getSceneX();
                        anchorY = event2.getSceneY();
                    }
                });

            } else {
                Scene scene = rotateButton.getScene();
                scene.setOnMousePressed(null);
                scene.setOnMouseDragged(null);
            }
        });
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

        listView.setCellFactory(lv -> new javafx.scene.control.ListCell<javafx.scene.shape.Shape3D>() {
            @Override
            protected void updateItem(javafx.scene.shape.Shape3D item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    // Check the object's type and use its getID() method for the name
                    if (item instanceof CustomBox tempBox) {
                        setText("Cube " + tempBox.getID());
                    } else if (item instanceof CustomSphere tempSphere) {
                        setText("Sphere " + tempSphere.getID());
                    } else if (item instanceof CustomCylinder tempCylinder) {
                        setText("Cylinder " + tempCylinder.getID());
                    } else {
                        setText(item.getClass().getSimpleName()); // Fallback name
                    }
                }
            }
        });

        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selectedShape = listView.getSelectionModel().getSelectedItem();

                if (selectedShape != null) {
                    System.out.println("Selected Object: " + selectedShape.getClass().getSimpleName() + " " + selectedShape.getId());

                    // Optionally: Highlight the selected object here
                    // e.g., selectedShape.setEffect(new Lighting());
                }
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
            cube = new CustomBox(Color.LIGHTGREY);

            extension.translateXProperty().set(WIDTH/2);
            extension.translateYProperty().set(HEIGHT/2);
            
            extension.getChildren().add(cube);

            cube.setID(cube.getID());
            String cubeName = "Cube " + Integer.toString(cube.getID());
            listView.getItems().add(cube);

            numberOfObjects++;
            System.out.println("Objects: " + numberOfObjects);
        }
        else if (event.getSource()==sphereButton) {
            sphere = new CustomSphere(Color.LIGHTGREY);

            extension.translateXProperty().set(WIDTH/2);
            extension.translateYProperty().set(HEIGHT/2);

            extension.getChildren().add(sphere);

            sphere.setID(sphere.getID());
            String cubeName = "Cube " + Integer.toString(sphere.getID());
            listView.getItems().add(sphere);

            numberOfObjects++;
            System.out.println("Objects: " + numberOfObjects);
        }
        else if (event.getSource()==cylinderButton) {
            cylinder = new CustomCylinder(Color.LIGHTGREY);

            extension.translateXProperty().set(WIDTH/2);
            extension.translateYProperty().set(HEIGHT/2);

            extension.getChildren().add(cylinder);

            cylinder.setID(cylinder.getID());
            String cubeName = "Cube " + Integer.toString(cylinder.getID());
            listView.getItems().add(cylinder);

            numberOfObjects++;
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
