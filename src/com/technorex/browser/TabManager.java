package com.technorex.browser;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

class TabManager {
    private boolean JSval = true;
    Tab createNewTab() {
        final String DEFAULT_URL = "https://duckduckgo.com";
        Tab tab = new Tab();
        Button goButton = new Button();
        Button forward = new Button();
        Button toggleJs = new Button();
        Button backward = new Button();
        ComboBox<String> history = new ComboBox<>();
        ComboBox<TextField> notePad = new ComboBox<>();
        Button bookmark = new Button();
        WebView webView = new WebView();
        final WebEngine webEngine = webView.getEngine();
        final TextField urlField = new TextField(DEFAULT_URL);
        History webHistory = new History();

        tab.setText("New Tab");
        tab.setStyle("-fx-background-color: #f7f7f7");
        urlField.setMinHeight(36.0);


                /*
                Action Handler for WebEngine
                 */
        webEngine.locationProperty().addListener((observable1, oldValue, newValue) -> urlField.setText(newValue));

        EventHandler<ActionEvent> goAction = event -> {
            webEngine.load((urlField.getText().startsWith("http://") || urlField.getText().startsWith("https://"))
                    ? urlField.getText()
                    : "https://" + urlField.getText());
            webHistory.addHistory(webEngine.getLocation());
        };


                /*
                Action handler for JS toggle button
                 */
        EventHandler<ActionEvent> toggleJS = event -> {
            JSval = !JSval;
            if (JSval) {
                webEngine.setJavaScriptEnabled(true);
                toggleJs.getStylesheets().removeAll(toggleJs.getStylesheets());
                toggleJs.getStylesheets().add("/stylesheets/ToggleJs.css");
                webEngine.reload();
            } else {
                webEngine.setJavaScriptEnabled(false);
                toggleJs.getStylesheets().removeAll(toggleJs.getStylesheets());
                toggleJs.getStylesheets().add("/stylesheets/notToggleJs.css");
                webEngine.reload();
            }
        };


                /*
                Action handler for notePad button
                 */
        EventHandler<ActionEvent> notePadClicked = event -> {
            notePad.getItems().removeAll(notePad.getItems());
            notePad.getItems().add(new TextField());
        };

                /*
                Action handler for history button
                 */
        EventHandler<ActionEvent> showHistory = event -> {
            history.getItems().removeAll(history.getItems());
            ComboBox<String> temp = webHistory.getHistory(history);
            history.getItems().addAll(temp.getItems());
        };


                /*
                Defining button sizes and styles
                 */
        goButton.setPrefSize(36.0, 36.0);
        toggleJs.setPrefSize(36.0, 36.0);
        forward.setPrefSize(36.0, 36.0);
        backward.setPrefSize(36.0, 36.0);
        history.setPrefSize(36.0, 36.0);
        bookmark.setPrefSize(36.0, 36.0);
        notePad.setPrefSize(36.0, 36.0);

        goButton.setMinSize(36.0, 36.0);
        toggleJs.setMinSize(36.0, 36.0);
        forward.setMinSize(36.0, 36.0);
        backward.setMinSize(36.0, 36.0);
        history.setMinSize(36.0, 36.0);
        bookmark.setMinSize(36.0, 36.0);
        notePad.setMinSize(36.0, 36.0);

        goButton.setDefaultButton(true);
        toggleJs.setDefaultButton(true);
        forward.setDefaultButton(true);
        backward.setDefaultButton(true);
        bookmark.setDefaultButton(true);
        goButton.getStylesheets().add("/stylesheets/GoButton.css");
        toggleJs.getStylesheets().add("/stylesheets/ToggleJs.css");
        forward.getStylesheets().add("/stylesheets/Forward.css");
        backward.getStylesheets().add("/stylesheets/Backward.css");
        history.getStylesheets().add("/stylesheets/HistoryButton.css");
        bookmark.getStylesheets().add("/stylesheets/bookmark.css");
        notePad.getStylesheets().add("/stylesheets/notePad.css");


                /*
                Adding event handlers to buttons
                 */
        urlField.setOnAction(goAction);
        goButton.setOnAction(goAction);
        toggleJs.setOnAction(toggleJS);
        notePad.setOnAction(notePadClicked);
        history.setOnAction(showHistory);


        HBox hBox = new HBox(5);
        hBox.getChildren().setAll(backward, forward, toggleJs, history, urlField, goButton, bookmark, notePad);
        hBox.setStyle("-fx-background-color: #f7f7f7");
        hBox.setMinHeight(36);
        HBox.setHgrow(urlField, Priority.ALWAYS);
        final VBox vBox = new VBox();
        vBox.getChildren().setAll(hBox, webView);
        VBox.setVgrow(webView, Priority.ALWAYS);
        vBox.setMinHeight(36);
        tab.setContent(vBox);
        webEngine.load(DEFAULT_URL);
        webHistory.addHistory(DEFAULT_URL);
        return tab;
    }
}
