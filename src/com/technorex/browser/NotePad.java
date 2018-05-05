package com.technorex.browser;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import java.io.File;
import java.util.ArrayList;

class NotePad {
    private static Scene lastScene = App.stage.getScene();
    private static ArrayList<HBox> row = new ArrayList<>();
    private static double scWidth = Screen.getPrimary().getBounds().getWidth();
    private static double scHeight = Screen.getPrimary().getBounds().getHeight();


    private static void addToHBox(ArrayList<String> notes, ArrayList<String> paths) {
        int cnt = notes.size();
        int ind = 0;
        while (ind < cnt) {
            HBox hBox = new HBox();
            for (int i = 0; i < 4; i++, ind++) {
                if (ind < cnt) {
                    TextArea textArea = new TextArea();
                    textArea.setId(paths.get(ind));
                    textArea.setText(notes.get(ind));
                    textArea.setWrapText(true);
                    textArea.setPrefWidth(scWidth / 4.0 - 5);
                    textArea.setMinWidth(scWidth / 4.0 - 5);
                    textArea.setMaxHeight(scHeight / 3.0 - 50);
                    textArea.setMinHeight(scHeight / 3.0 - 50);
                    textArea.setEditable(false);
                    textArea.getStylesheets().add("/stylesheets/NotePadTextButton.css");
                    hBox.getChildren().add(textArea);
                    textArea.setOnMouseClicked(event -> NotePad.newNote(textArea));
                }
            }
            row.add(hBox);
        }
    }

    private static void listFilesForFolder(final File folder) throws Exception {
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> notes = new ArrayList<>();
        ArrayList<String> paths = new ArrayList<>();
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                notes.add(EncryptionDecryption.decrypt(file));
                paths.add(file.getName());
            }
        }
        addToHBox(notes, paths);
    }

    private static void readSavedNotes() throws Exception {
        row.clear();
        final File folder = new File(System.getProperty("user.dir") + "\\src\\data\\nts\\");
        listFilesForFolder(folder);
    }

    private static void saveDataAsFile(String name, String data, String path) throws Exception {
        File file = new File(path + name);
        EncryptionDecryption.encrypt(data, file, false);
    }

    private static Scene pad() throws Exception {
        Group notePad = new Group();
        Scene scene = new Scene(notePad, scWidth, scHeight);
        VBox sceneContainer = new VBox();
        sceneContainer.setStyle("-fx-background-color: #fafafa;");
        sceneContainer.setMinSize(scWidth, scHeight);
        VBox vBox = new VBox();
        readSavedNotes();
        for (HBox hBox : row)
            vBox.getChildren().add(hBox);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        HBox toolBar = new HBox();
        Button newNote = new Button("New Note");
        Button close = new Button("Exit notepad");
        newNote.getStylesheets().add("/stylesheets/NotePadButton.css");
        close.getStylesheets().add("/stylesheets/NotePadButton.css");
        newNote.setPrefWidth(scWidth / 8.0 - 10);
        close.setPrefWidth(scWidth / 8.0 - 10);
        EventHandler<ActionEvent> closeNotepad = event -> App.stage.setScene(NotePad.lastScene);
        EventHandler<ActionEvent> openPad = event -> newNote();
        close.setOnAction(closeNotepad);
        newNote.setOnAction(openPad);
        toolBar.setAlignment(Pos.CENTER);
        toolBar.setMinHeight(scHeight * 0.075);
        toolBar.setSpacing(scWidth - newNote.getPrefWidth() * 2 - 40);
        toolBar.getChildren().addAll(newNote, close);
        VBox toolBarContainer = new VBox();
        toolBarContainer.getChildren().add(toolBar);
        sceneContainer.getChildren().addAll(toolBarContainer, vBox);
        notePad.getChildren().add(sceneContainer);
        return scene;
    }

    private static void newNote() {
        Group notePad = new Group();
        Scene scene = new Scene(notePad, scWidth, scHeight);
        VBox sceneContainer = new VBox();
        sceneContainer.setStyle("-fx-background-color: #fafafa; -fx-alignment: center");
        sceneContainer.setMinSize(scWidth, scHeight);
        TextArea textArea = new TextArea();
        TextArea titleArea = new TextArea();
        titleArea.setPromptText("Title");
        textArea.setPromptText("Type here to take note");
        textArea.setMinSize(scWidth / 4.0, scHeight * 0.7);
        textArea.setMaxSize(scWidth / 4.0, scHeight * 0.7);
        titleArea.setMinSize(scWidth / 4.0, scHeight * 0.075);
        titleArea.setMaxSize(scWidth / 4.0, scHeight * 0.075);
        HBox toolBar = new HBox();
        Button saveNote = new Button("Save note");
        Button close = new Button("Exit notepad");
        saveNote.getStylesheets().add("/stylesheets/NotePadButton.css");
        close.getStylesheets().add("/stylesheets/NotePadButton.css");
        saveNote.setPrefWidth(scWidth / 8.0 - 10);
        close.setPrefWidth(scWidth / 8.0 - 10);
        textArea.getStylesheets().add("/stylesheets/TakeNote.css");
        EventHandler<ActionEvent> closeNotepad = event -> {
            try {
                App.stage.setScene(NotePad.pad());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        close.setOnAction(closeNotepad);
        saveNote.setOnAction(event -> {
            String note = textArea.getText();
            try {
                NotePad.saveDataAsFile(titleArea.getText(), note, System.getProperty("user.dir") + "\\src\\data\\nts\\");
            } catch (Exception ignored) {
            }
            try {
                App.stage.setScene(NotePad.pad());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        HBox titleBar = new HBox();
        toolBar.setAlignment(Pos.CENTER);
        toolBar.setMinHeight(scHeight * 0.075);
        toolBar.setSpacing(0);
        toolBar.getChildren().addAll(saveNote, close);
        titleBar.getChildren().add(titleArea);
        titleBar.setAlignment(Pos.CENTER);
        textArea.setWrapText(false);
        titleArea.getStylesheets().add("/stylesheets/TakeNote.css");
        VBox toolBarContainer = new VBox();
        toolBarContainer.getChildren().add(toolBar);
        sceneContainer.setAlignment(Pos.CENTER);
        sceneContainer.getChildren().addAll(toolBarContainer, titleBar, textArea);
        notePad.getChildren().add(sceneContainer);
        notePad.setAutoSizeChildren(true);
        App.stage.setScene(scene);
    }

    private static void newNote(TextArea resource) {
        Group notePad = new Group();
        Scene scene = new Scene(notePad, scWidth, scHeight);
        VBox sceneContainer = new VBox();
        sceneContainer.setStyle("-fx-background-color: #fafafa; -fx-alignment: center");
        sceneContainer.setMinSize(scWidth, scHeight);
        TextArea textArea = new TextArea();
        TextArea titleArea = new TextArea();
        titleArea.setText(resource.getId());
        textArea.setText(resource.getText());
        textArea.setMinSize(scWidth / 4.0, scHeight * 0.7);
        textArea.setMaxSize(scWidth / 4.0, scHeight * 0.7);
        titleArea.setMinSize(scWidth / 4.0, scHeight * 0.075);
        titleArea.setMaxSize(scWidth / 4.0, scHeight * 0.075);
        HBox toolBar = new HBox();
        Button saveNote = new Button("Save note");
        Button close = new Button("Exit notepad");
        saveNote.getStylesheets().add("/stylesheets/NotePadButton.css");
        close.getStylesheets().add("/stylesheets/NotePadButton.css");
        saveNote.setPrefWidth(scWidth / 8.0 - 10);
        close.setPrefWidth(scWidth / 8.0 - 10);
        textArea.getStylesheets().add("/stylesheets/TakeNote.css");
        EventHandler<ActionEvent> closeNotepad = event -> {
            try {
                App.stage.setScene(NotePad.pad());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        EventHandler<ActionEvent> save = event -> {
            String note = textArea.getText();
            try {
                NotePad.saveDataAsFile(titleArea.getText(), note, System.getProperty("user.dir") + "\\src\\data\\nts\\");
            } catch (Exception ignored) {
            }
            try {
                App.stage.setScene(NotePad.pad());
            } catch (Exception ignore) {
            }
        };

        close.setOnAction(closeNotepad);
        saveNote.setOnAction(save);
        HBox titleBar = new HBox();
        toolBar.setAlignment(Pos.CENTER);
        toolBar.setMinHeight(scHeight * 0.075);
        toolBar.setSpacing(0);
        toolBar.getChildren().addAll(saveNote, close);
        titleBar.getChildren().add(titleArea);
        titleBar.setAlignment(Pos.CENTER);
        textArea.setWrapText(false);
        titleArea.getStylesheets().add("/stylesheets/TakeNote.css");
        VBox toolBarContainer = new VBox();
        toolBarContainer.getChildren().add(toolBar);
        sceneContainer.setAlignment(Pos.CENTER);
        sceneContainer.getChildren().addAll(toolBarContainer, titleBar, textArea);
        notePad.getChildren().add(sceneContainer);
        notePad.setAutoSizeChildren(true);
        App.stage.setScene(scene);
    }

    static void takeNote() throws Exception {
        NotePad.lastScene = App.stage.getScene();
        App.stage.setScene(pad());
    }
}
