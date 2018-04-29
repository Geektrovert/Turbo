package com.technorex.browser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class NotePad extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent pad = FXMLLoader.load(getClass().getResource("NotePadUI.fxml"));
        Scene scene = new Scene(pad);
        stage.setTitle("NotePad");
        stage.getIcons().add(new Image("Icons/icon.png"));
        stage.setScene(scene);
        stage.setOpacity(1);
        stage.show();
    }
}
