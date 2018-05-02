package com.technorex.browser;

import javafx.scene.Group;
import javafx.scene.Scene;

public class NotePad {
    public static void takeNote() {
        Group notePad = new Group();
        Scene scene = App.stage.getScene();
        Scene newScene = new Scene(notePad);
        App.stage.setScene(newScene);
        App.stage.setScene(scene);
    }
}
