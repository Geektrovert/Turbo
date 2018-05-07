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

class BookmarkWindow {
    private static Scene lastScene= App.stage.getScene();
    private static double scWidth = Screen.getPrimary().getBounds().getWidth();
    private static double scHeight = Screen.getPrimary().getBounds().getHeight();
    static void start() throws Exception {
        lastScene = App.stage.getScene();
        App.stage.setScene(showBookmark());
    }
    private static Scene showBookmark() throws Exception {
        Group window = new Group();
        Scene scene = new Scene(window,scWidth,scHeight);
        HBox hBox = new HBox();
        VBox vBox = new VBox();
        hBox.setStyle("-fx-background-color: #fafafa;");
        vBox.setStyle("-fx-background-color: #fafafa;");
        Button back=new Button("Back");
        vBox.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);
        TextArea main=new TextArea();

        main.setMinWidth(scWidth);
        main.setMaxWidth(scWidth);
        main.setPrefWidth(scWidth);
        main.setMinHeight(scHeight - 100);
        main.getStylesheets().add("/stylesheets/TakeNote.css");
        main.setEditable(false);


        EventHandler<ActionEvent> backToBrowser = event -> App.stage.setScene(lastScene);
        back.setOnAction(backToBrowser);

        main.setText(EncryptionDecryption.decrypt(new File(System.getProperty("user.dir")+"\\src\\data\\sv\\cache")));
        hBox.getChildren().addAll(main);
        vBox.getChildren().addAll(back,hBox);
        back.getStylesheets().add("/stylesheets/NotePadButton.css");
        window.getChildren().add(vBox);
        return scene;
    }
}
