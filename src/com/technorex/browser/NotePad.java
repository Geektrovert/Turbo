package com.technorex.browser;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.scene.control.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

class NotePad {
    private static Scene lastScene = App.stage.getScene();
    private static ArrayList<HBox> row = new ArrayList<>();
    private static double scWidth = Screen.getPrimary().getBounds().getWidth();
    private static double scHeight = Screen.getPrimary().getBounds().getHeight();

    private static void addToHBox(ArrayList<String> notes) {
        int cnt = notes.size();
        int ind = 0;
        while (ind<cnt) {
            HBox hBox = new HBox();
            for(int i=0;i<4;i++,ind++) {
                if(ind<cnt) {
                    Text text = new Text(notes.get(ind));
                    text.setTextAlignment(TextAlignment.CENTER);
                    text.setFont(Font.font("Calibri", FontPosture.REGULAR, 24));
                    text.setStyle("-fx-text-fill: #fafafa; -fx-text-alignment: center");
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setPrefWidth(scWidth/4.0);
                    textFlow.setMinWidth(scWidth/4.0);
                    textFlow.setMaxHeight(scHeight/3.0-50);
                    textFlow.setStyle("-fx-border-radius: 5; -fx-border-color: #9f9f9f; -fx-border-insets: 10 10 10 10; -fx-text-fill: #fafafa");
                    hBox.getChildren().add(textFlow);
                    hBox.setAlignment(Pos.TOP_LEFT);
                }
            }
            row.add(hBox);
        }
    }

    private static void listFilesForFolder(final File folder) throws FileNotFoundException {
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> notes = new ArrayList<>();
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                String str = "";
                Scanner scanner = new Scanner(new File(file.getPath()));
                while (scanner.hasNextLine()) {
                    str=str.concat(scanner.nextLine());
                    str+="\n";
                }
                notes.add(str);
            }
        }
        addToHBox(notes);
    }

    private static void readSavedNotes() throws FileNotFoundException {
        row.clear();
        final File folder = new File(System.getProperty("user.dir")+"\\src\\data\\nts\\");
        listFilesForFolder(folder);
    }

    private static void onExit() {
        App.stage.setScene(lastScene);
    }

    private static Scene pad() throws FileNotFoundException {
        Group notePad = new Group();
        Scene scene = new Scene(notePad,scWidth,scHeight);
        VBox sceneContainer = new VBox();
        sceneContainer.setStyle("-fx-background-color: #fafafa;");
        sceneContainer.setMinSize(scWidth,scHeight);
        VBox vBox = new VBox();
        readSavedNotes();
        for(HBox hBox: row)
            vBox.getChildren().add(hBox);
        HBox toolBar= new HBox();
        toolBar.setMinHeight(48.0);
        Button newNote = new Button("New Note");
        Button close = new Button("Close");
        EventHandler<ActionEvent> closeNotepad = event -> onExit();
        close.setOnAction(closeNotepad);
        toolBar.setAlignment(Pos.TOP_RIGHT);
        toolBar.getChildren().addAll(newNote,close);
        VBox toolBarContainer = new VBox();
        toolBarContainer.getChildren().add(toolBar);
        sceneContainer.getChildren().addAll(toolBarContainer,vBox);
        notePad.getChildren().add(sceneContainer);
        return scene;
    }

    public static void takeNote() throws FileNotFoundException {
        App.stage.setScene(pad());
    }
}
