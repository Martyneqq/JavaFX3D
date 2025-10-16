module JavaFX3D {

    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;

    exports javafx3D.view;
    exports javafx3D.controller;
    exports javafx3D.model;
    exports javafx3D.components;
    exports javafx3D.utils;

    opens javafx3D.view; // POVINNÉ PRO MAIN TŘÍDU!

    opens javafx3D.controller to javafx.fxml;

    // opens javafx3D.model to javafx.fxml;
    // opens javafx3D.components to javafx.fxml;
    // opens javafx3D.utils to javafx.fxml;

}