package com.technorex.browser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class that extends Application Class as to initiate the Turbo browser.
 *
 * @author Samnan Rahee, Sihan Tawsik
 */

public class App extends Application {
    /**
     * Performs activity associated with the initialization of the Stage and Scene
     *
     * @param stage Input Stage.
     * @throws IOException Throws exception if necessary.
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("WebUI.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Turbo");
        stage.getIcons().add(new Image("Icons/icon.png"));
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setOpacity(1);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
