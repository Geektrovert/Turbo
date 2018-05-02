package com.technorex.browser;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;

class NotePad {
    private static Scene lastScene = App.stage.getScene();
    private static ArrayList<HBox> row = new ArrayList<>();
    private static Scene pad() {
        System.out.println("Called 03");
        Group notePad = new Group();
        Scene scene = new Scene(notePad);
        BorderPane borderPane = new BorderPane();
        VBox vBox = new VBox();
        row.clear();
        readSavedNotes();
        for(HBox hBox: row)
            vBox.getChildren().add(hBox);
        borderPane.getChildren().add(vBox);
        notePad.getChildren().add(borderPane);
        return scene;
    }

    private static void listFilesForFolder(final File folder) {
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                System.out.println("File " + listOfFile.getName());
            } else if (listOfFile.isDirectory()) {
                System.out.println("Directory " + listOfFile.getName());
            }
        }
    }

    private static void readSavedNotes() {
        final File folder = new File("C:\\Users\\User\\Desktop\\Turbo\\src\\data\\nts\\");
        listFilesForFolder(folder);
    }

    private static void onExit() {
        App.stage.setScene(lastScene);
    }

    public static void takeNote() {
        System.out.println("Called 01");
        App.stage.setScene(pad());
        System.out.println("Called 02");
    }
}
