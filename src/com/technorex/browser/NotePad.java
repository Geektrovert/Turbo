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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
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
        while (ind < cnt) {
            HBox hBox = new HBox();
            for (int i = 0; i < 4; i++, ind++) {
                if (ind < cnt) {
                    Button button = new Button();
                    button.setText(notes.get(ind));
                    button.setAlignment(Pos.TOP_LEFT);
                    button.setPrefWidth(scWidth / 4.0);
                    button.setMinWidth(scWidth / 4.0);
                    button.setMaxHeight(scHeight / 3.0 - 50);
                    button.setMinHeight(scHeight / 3.0 - 50);
                    button.getStylesheets().add("/stylesheets/NotePadTextButton.css");
                    hBox.getChildren().add(button);
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
                    str = str.concat(scanner.nextLine());
                    str += "\n";
                }
                notes.add(str);
            }
        }
        addToHBox(notes);
    }

    private static void readSavedNotes() throws FileNotFoundException {
        row.clear();
        final File folder = new File(System.getProperty("user.dir") + "\\src\\data\\nts\\");
        listFilesForFolder(folder);
    }

    private static String getNextName(String name) {
        name = name.substring(0,name.length()-4);
        int num = Integer.parseInt(name);
        num++;
        name = Integer.toString(num);
        name = name.concat(".txt");
        return name;
    }

    private static String getLastFileName() {
        String string = "";
        final File folder = new File(System.getProperty("user.dir") + "\\src\\data\\nts\\");
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                string = file.getName();
            }
        }
        return string;
    }

    private static void saveDataAsFile(String name, String data, String path) throws FileNotFoundException {
        name = getNextName(name);
        File file = new File(path+name);
        PrintStream printStream = new PrintStream(new FileOutputStream(file));
        printStream.println(data);
        printStream.close();
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
        Button close = new Button("Exit notepad");
        newNote.getStylesheets().add("/stylesheets/NotePadButton.css");
        close.getStylesheets().add("/stylesheets/NotePadButton.css");
        EventHandler<ActionEvent> closeNotepad = event -> App.stage.setScene(NotePad.lastScene);
        EventHandler<ActionEvent> openPad = event -> newNote();
        close.setOnAction(closeNotepad);
        newNote.setOnAction(openPad);
        toolBar.setAlignment(Pos.CENTER);
        toolBar.setMinHeight(80);
        toolBar.setSpacing(scWidth-420);
        toolBar.getChildren().addAll(newNote,close);
        VBox toolBarContainer = new VBox();
        toolBarContainer.getChildren().add(toolBar);
        sceneContainer.getChildren().addAll(toolBarContainer,vBox);
        notePad.getChildren().add(sceneContainer);
        return scene;
    }

    private static void newNote() {
        Group notePad = new Group();
        Scene scene = new Scene(notePad,scWidth,scHeight);
        VBox sceneContainer = new VBox();
        sceneContainer.setStyle("-fx-background-color: #fafafa; -fx-alignment: center");
        sceneContainer.setMinSize(scWidth,scHeight);
        TextArea textArea = new TextArea();
        textArea.setPromptText("Type here to take note");
        textArea.setMinSize(scWidth/4.0,7.0*scHeight/10.0);
        textArea.setMaxSize(scWidth/4.0,7.0*scHeight/10.0);
        HBox toolBar= new HBox();
        toolBar.setMinHeight(48.0);
        Button saveNote = new Button("Save note");
        Button close = new Button("Exit notepad");
        saveNote.getStylesheets().add("/stylesheets/NotePadButton.css");
        close.getStylesheets().add("/stylesheets/NotePadButton.css");
        textArea.getStylesheets().add("/stylesheets/TakeNote.css");
        EventHandler<ActionEvent> closeNotepad = event -> {
            try {
                App.stage.setScene(NotePad.pad());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        };

        EventHandler<ActionEvent> save = event -> {
            String note = textArea.getText();
            try {
                NotePad.saveDataAsFile(getLastFileName(),note,System.getProperty("user.dir") + "\\src\\data\\nts\\");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                App.stage.setScene(NotePad.pad());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        };
        close.setOnAction(closeNotepad);
        saveNote.setOnAction(save);
        toolBar.setAlignment(Pos.CENTER);
        toolBar.setMinHeight(80);
        toolBar.setSpacing(scWidth-3.0*scWidth/4.0-420);
        toolBar.getChildren().addAll(saveNote,close);
        VBox toolBarContainer = new VBox();
        toolBarContainer.getChildren().add(toolBar);
        sceneContainer.getChildren().addAll(toolBarContainer,textArea);
        notePad.getChildren().add(sceneContainer);
        App.stage.setScene(scene);
    }

    static void takeNote() throws FileNotFoundException {
        NotePad.lastScene = App.stage.getScene();
        App.stage.setScene(pad());
    }
}
