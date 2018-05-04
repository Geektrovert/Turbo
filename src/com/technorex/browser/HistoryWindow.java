package com.technorex.browser;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import java.io.File;

class HistoryWindow {
    private static Scene lastScene= App.stage.getScene();
    private static double scWidth = Screen.getPrimary().getBounds().getWidth();
    private static double scHeight = Screen.getPrimary().getBounds().getHeight();
    static void start() throws Exception {
        lastScene = App.stage.getScene();
        App.stage.setScene(showHistory());
    }
    private static Scene showHistory() throws Exception {
        Group window = new Group();
        Scene scene = new Scene(window,scWidth,scHeight);
        HBox hBox = new HBox();
        VBox vBox = new VBox();
        hBox.setStyle("-fx-background-color: #fafafa;");
        vBox.setStyle("-fx-background-color: #fafafa;");
        Button back=new Button("Back");
        vBox.setAlignment(Pos.CENTER_LEFT);
        TextArea decoy1=new TextArea(),decoy2=new TextArea(),main=new TextArea();

        decoy1.setMinWidth(scWidth/3-30);
        decoy1.setMaxWidth(scWidth/3-30);
        decoy1.setPrefWidth(scWidth/3-30);
        decoy2.setMinWidth(scWidth/3-30);
        decoy2.setMaxWidth(scWidth/3-30);
        decoy2.setPrefWidth(scWidth/3-30);
        main.setMinWidth(scWidth/3+60);
        main.setMaxWidth(scWidth/3+60);
        main.setPrefWidth(scWidth/3+60);
        decoy1.setMinHeight(scHeight);
        decoy2.setMinHeight(scHeight);
        main.setMinHeight(scHeight);
        main.getStylesheets().add("/stylesheets/TakeNote.css");

        decoy1.setEditable(false);
        decoy2.setEditable(false);
        main.setEditable(false);

        decoy1.setVisible(false);
        decoy2.setVisible(false);

        EventHandler<ActionEvent> backToBrowser = event -> App.stage.setScene(lastScene);
        back.setOnAction(backToBrowser);

        main.setText(EncryptionDecryption.decrypt(new File(System.getProperty("user.dir")+"\\src\\data\\sv\\history")));
        hBox.getChildren().addAll(decoy1,main,decoy2);
        vBox.getChildren().addAll(back,hBox);
        back.getStylesheets().add("/stylesheets/NotePadButton.css");
        window.getChildren().add(vBox);
        return scene;
    }
}
