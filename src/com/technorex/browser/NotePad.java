package com.technorex.browser;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class NotePad {
    private static Scene lastScene = App.stage.getScene();
    private static ArrayList<HBox> row = new ArrayList<>();
    private static Scene pad() {
        Group notePad = new Group();
        Scene scene = new Scene(notePad);
        BorderPane borderPane = new BorderPane();
        VBox vBox = new VBox();
        for(HBox hBox: row)
            vBox.getChildren().add(hBox);
        borderPane.getChildren().add(vBox);
        notePad.getChildren().add(borderPane);
        return scene;
    }

    private static void readSavedNotes() {

    }

    private static void onExit() {
        App.stage.setScene(lastScene);
    }
    public static void takeNote() {
        App.stage.setScene(pad());
    }
}
