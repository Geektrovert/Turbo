package com.technorex.browser;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.util.ArrayList;

class TabManager {
    Tab createNewTab() {
        final String DEFAULT_URL = "https://duckduckgo.com";
        final String DEFAULT_Search = "Search";
        final Tab tab = new Tab();
        final Button goButton = new Button();
        final Button forward = new Button();
        final Button toggleJs = new Button();
        final Button backward = new Button();
        final ComboBox<String> history = new ComboBox<>();
        final ComboBox<TextField> notePad = new ComboBox<>();
        final ArrayList<String> tempHistory = new ArrayList<>();
        final Button bookmark = new Button();
        final Button menu = new Button();
        final WebView webView = new WebView();
        final WebEngine webEngine = webView.getEngine();
        final TextField urlField = new TextField(DEFAULT_URL);
        final TextField searchField = new TextField(DEFAULT_Search);
        final History webHistory = new History();

        tab.setText("New Tab");
        urlField.setMinHeight(30.0);
        urlField.setMaxHeight(30.0);
        urlField.setPrefHeight(30.0);
        searchField.setPromptText(DEFAULT_Search);
        searchField.setMinHeight(30.0);
        searchField.setMaxHeight(30.0);
        searchField.setPrefHeight(30.0);


        /*
        Action Handler for WebEngine
         */
        webEngine.locationProperty().addListener((observable1, oldValue, newValue) -> {
            urlField.setText(newValue);
            webHistory.addHistory(webEngine.getLocation());
            App.localHistory.addHistory(webEngine.getLocation());
            tempHistory.add(webEngine.getLocation());
        });

        EventHandler<ActionEvent> goAction = event -> webEngine.load((urlField.getText().startsWith("http://") || urlField.getText().startsWith("https://"))
                ? urlField.getText()
                : "https://" + urlField.getText());


                /*
                Action handler for JS toggle button
                 */
        EventHandler<ActionEvent> toggleJS = event -> {
            if (!webEngine.isJavaScriptEnabled()) {
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
        EventHandler<MouseEvent> showHistory = event -> {
            history.getItems().removeAll(history.getItems());
            for(int i=tempHistory.size()-1;i>=0;i--) {
                history.getItems().add(tempHistory.get(i));
            }
        };

        EventHandler<ActionEvent> chooseEntry = event -> {
            if(history.getValue()!=null){
                webEngine.load(history.getValue());
                urlField.setText(webEngine.getLocation());
                tempHistory.add(webEngine.getLocation());
                history.getItems().removeAll(history.getItems());
                for(int i=tempHistory.size()-1;i>=0;i--) {
                    history.getItems().add(tempHistory.get(i));
                }
            }
        };

        EventHandler<ActionEvent> goBackward = event -> {
            String url = webHistory.backward();
            if(url!=null) {
                webEngine.load(url);
            }
        };

        EventHandler<ActionEvent> goForward = event -> {
            String url = webHistory.forward();
            if(url!=null) {
                webEngine.load(url);
            }
        };

        /*
        Defining button sizes and styles
        */
        goButton.setPrefSize(30.0, 30.0);
        toggleJs.setPrefSize(30.0, 30.0);
        forward.setPrefSize(30.0, 30.0);
        backward.setPrefSize(30.0, 30.0);
        history.setPrefSize(30.0, 30.0);
        bookmark.setPrefSize(30.0, 30.0);
        notePad.setPrefSize(30.0, 30.0);
        menu.setPrefSize(30.0, 30.0);

        goButton.setMinSize(30.0, 30.0);
        toggleJs.setMinSize(30.0, 30.0);
        forward.setMinSize(30.0, 30.0);
        backward.setMinSize(30.0, 30.0);
        history.setMinSize(30.0, 30.0);
        bookmark.setMinSize(30.0, 30.0);
        notePad.setMinSize(30.0, 30.0);
        menu.setMinSize(30.0, 30.0);



        goButton.setDefaultButton(true);
        toggleJs.setDefaultButton(true);
        forward.setDefaultButton(true);
        backward.setDefaultButton(true);
        bookmark.setDefaultButton(true);
        menu.setDefaultButton(true);
        toggleJs.getStylesheets().add("/stylesheets/ToggleJs.css");
        forward.getStylesheets().add("/stylesheets/Forward.css");
        backward.getStylesheets().add("/stylesheets/Backward.css");
        history.getStylesheets().add("/stylesheets/HistoryButton.css");
        urlField.getStylesheets().add("/stylesheets/URLField.css");
        searchField.getStylesheets().add("/stylesheets/URLField.css");
        goButton.getStylesheets().add("/stylesheets/GoButton.css");
        bookmark.getStylesheets().add("/stylesheets/bookmark.css");
        notePad.getStylesheets().add("/stylesheets/notePad.css");
        menu.getStylesheets().add("/stylesheets/menuButton.css");


        /*
        Adding event handlers to buttons
         */
        urlField.setOnAction(goAction);
        goButton.setOnAction(goAction);
        toggleJs.setOnAction(toggleJS);
        notePad.setOnAction(notePadClicked);
        history.setOnMouseClicked(showHistory);
        history.setOnAction(chooseEntry);
        backward.setOnAction(goBackward);
        forward.setOnAction(goForward);

        HBox hBox = new HBox(10);
        hBox.getChildren().setAll(backward, forward, toggleJs, history, urlField, searchField, goButton, bookmark, notePad, menu);
        hBox.setPadding(new Insets(0,10,0,10));
        hBox.setStyle("-fx-background-color: #323234");
        hBox.setMinHeight(40.0);
        hBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(urlField, Priority.ALWAYS);
        final VBox vBox = new VBox();
        vBox.getChildren().setAll(hBox, webView);
        VBox.setVgrow(webView, Priority.ALWAYS);
        vBox.setMinHeight(40);
        tab.setContent(vBox);
        webEngine.load(DEFAULT_URL);
        webHistory.addHistory(DEFAULT_URL);
        return tab;
    }
}
