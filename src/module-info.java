module JavaFX3D {

    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires com.google.gson;

    exports javafx3D.view;
    exports javafx3D.controller;
    exports javafx3D.model;
    exports javafx3D.components;
    exports javafx3D.utils;
    exports javafx3D.config;
    exports javafx3D.service;
    exports javafx3D.handler;

    opens javafx3D.view;
    opens javafx3DV2.view;

    opens javafx3D.controller to javafx.fxml;

}