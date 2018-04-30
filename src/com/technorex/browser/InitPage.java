package com.technorex.browser;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

import java.io.File;

public class InitPage {
    private double scWidth =  Screen.getPrimary().getBounds().getWidth();
    private double scHeight = Screen.getPrimary().getBounds().getHeight();
    private File file = new File("/Icons/InitPage.png");
    private Image initImage = new Image(file.getPath());
    Scene getInitPage() {
        Group group  = new Group();
        Scene scene = new Scene(group,scWidth,scHeight);
        HBox box = new HBox();
        ImageView imageView = new ImageView(initImage);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(560.0);
        imageView.setFitWidth(960.0);
        imageView.setSmooth(true);
        box.getChildren().add(imageView);
        group.getChildren().add(box);
        return scene;
    }
}
