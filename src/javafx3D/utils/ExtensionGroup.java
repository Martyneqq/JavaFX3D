package javafx3D.utils;

import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public class ExtensionGroup extends Group {
        Rotate r;
        Transform t = new Rotate();
        
        void rotateByX(int angle) {
            r = new Rotate(angle, Rotate.X_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
        void rotateByY(int angle) {
            r = new Rotate(angle, Rotate.Y_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
    }
