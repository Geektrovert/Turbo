package com.technorex.browser;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;

public class NotePadUIController {
    @FXML
    public TextField noteTXT;
    @FXML
    public BorderPane notePad;

    private ArrayList<String> notes = new ArrayList<>();

    public void saveNote() {
        notes.add(noteTXT.getText());
    }
}
