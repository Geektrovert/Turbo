package com.technorex.browser;

import javafx.concurrent.Worker;
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
import javafx.stage.Screen;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
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
        final Button notePad = new Button();
        final ArrayList<String> tempHistory = new ArrayList<>();
        final Button bookmark = new Button();
        final ComboBox<String> menu = new ComboBox<>();
        final WebView webView = new WebView();
        final WebEngine webEngine = webView.getEngine();
        final TextField urlField = new TextField(DEFAULT_URL);
        final TextField searchField = new TextField();
        final History webHistory = new History();
        final ProgressBar progressBar = new ProgressBar(0.3);
        final Worker<Void> worker = webEngine.getLoadWorker();
        final double scWidth = Screen.getPrimary().getBounds().getWidth();
        final Button burn = new Button();
        tab.setText("New Tab");
        urlField.setMinHeight(30.0);
        urlField.setMaxHeight(36.0);
        urlField.setPrefHeight(30.0);
        searchField.setPromptText(DEFAULT_Search);
        searchField.setMinHeight(30.0);
        searchField.setMaxHeight(36.0);
        searchField.setPrefHeight(30.0);
        progressBar.setPrefWidth(scWidth);
        progressBar.progressProperty().bind(worker.progressProperty());
        progressBar.setVisible(false);
        /*
        Action Handler for WebEngine
         */
        webEngine.locationProperty().addListener((observable1, oldValue, newValue) -> {
            progressBar.setVisible(true);
            urlField.setText(newValue);
            try {
                URL url = new URL(webEngine.getLocation());
                URLConnection c = url.openConnection();
                String contentType = c.getContentType();
                if(!contentType.contains("text")) {
                    DownloadThread downloadThread = new DownloadThread(url.toExternalForm(), App.stage);
                    downloadThread.fileDirectory();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        EventHandler<ActionEvent> goAction = event ->{
            progressBar.setVisible(true);
            webEngine.load((urlField.getText().startsWith("http://") || urlField.getText().startsWith("https://")) ? urlField.getText() : "https://" + urlField.getText());
        };

        EventHandler<ActionEvent> searchAction =  event ->{
            progressBar.setVisible(true);
            webEngine.load("https://www.google.com/search?q=" + searchField.getText().replace(' ','+') + "&ie=utf-8&oe=utf-8");
        };

        worker.stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                progressBar.setVisible(false);
                webHistory.addHistory(webEngine.getLocation());
                App.localHistory.addHistory(webEngine.getLocation());
                tempHistory.add(webEngine.getLocation());
                String titleName = webEngine.getHistory().getEntries().get(webEngine.getHistory().getEntries().size()-1).getTitle();
                if(!titleName.isEmpty())
                    tab.setText(titleName);
                else {
                    titleName = urlField.getText();
                    if(titleName.contains("https://"))
                        titleName = titleName.replace("https://","");
                    else if(titleName.contains("http://"))
                        titleName = titleName.replace("http://","");
                    if(titleName.contains("www."))
                        titleName = titleName.replace("www.","");
                    if(titleName.contains("."))
                        titleName = titleName.substring(0,titleName.indexOf('.'));
                    titleName = Character.toTitleCase(titleName.charAt(0))+titleName.substring(1,titleName.length());
                    tab.setText(titleName);
                }
            }
        });
        EventHandler<ActionEvent> addBookmark = event -> {
            File file = new File(System.getProperty("user.dir")+"\\src\\data\\sv\\cache");
            try {
                EncryptionDecryption.encrypt(webEngine.getLocation(),file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

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
        EventHandler<ActionEvent> takeNote = event -> {
            try {
                NotePad.takeNote();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        EventHandler<ActionEvent> burnActivity = event -> {
            File[] listOfFiles = null;
            File dir = new File(System.getProperty("user.dir")+"\\src\\data\\nts\\");
            if(dir.isDirectory())
                listOfFiles = dir.listFiles();
            assert listOfFiles != null;
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    file.delete();
                }
            }
            dir = new File(System.getProperty("user.dir")+"\\src\\data\\sv\\");
            if(dir.isDirectory())
                listOfFiles = dir.listFiles();
            assert listOfFiles != null;
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    file.delete();
                }
            }
            App.stage.close();
            App.init(App.stage);
            App.stage.show();
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
        burn.setPrefSize(30.0,30.0);

        goButton.setMinSize(30.0, 30.0);
        toggleJs.setMinSize(30.0, 30.0);
        forward.setMinSize(30.0, 30.0);
        backward.setMinSize(30.0, 30.0);
        history.setMinSize(30.0, 30.0);
        bookmark.setMinSize(30.0, 30.0);
        notePad.setMinSize(30.0, 30.0);
        menu.setMinSize(30.0, 30.0);
        burn.setPrefSize(30.0,30.0);



        goButton.setDefaultButton(true);
        toggleJs.setDefaultButton(true);
        forward.setDefaultButton(true);
        backward.setDefaultButton(true);
        bookmark.setDefaultButton(true);
        notePad.setDefaultButton(true);
        burn.setDefaultButton(true);

        menu.getItems().addAll("History","Bookmarks","Downloads","About");

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
        progressBar.getStylesheets().add("/stylesheets/ProgressBar.css");
        burn.getStylesheets().add("/stylesheets/Burn.css");

        /*
        Adding event handlers to buttons
         */
        urlField.setOnAction(goAction);
        goButton.setOnAction(goAction);
        toggleJs.setOnAction(toggleJS);
        notePad.setOnAction(takeNote);
        bookmark.setOnAction(addBookmark);
        history.setOnMouseClicked(showHistory);
        history.setOnAction(chooseEntry);
        backward.setOnAction(goBackward);
        forward.setOnAction(goForward);
        searchField.setOnAction(searchAction);
        burn.setOnAction(burnActivity);

        HBox hBox = new HBox(10);
        hBox.getChildren().setAll(backward, forward, toggleJs, history, burn, urlField, searchField, goButton, bookmark, notePad, menu);
        hBox.setPadding(new Insets(6,12,6,12));
        hBox.setStyle("-fx-background-color: #343434");
        hBox.setMinHeight(48.0);
        hBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(urlField, Priority.ALWAYS);
        final VBox vBox = new VBox();
        vBox.getChildren().setAll(hBox, progressBar, webView);
        vBox.setStyle("-fx-background-color: #343434");
        VBox.setVgrow(webView, Priority.ALWAYS);
        vBox.setMinHeight(40);
        tab.setContent(vBox);
        tab.setOnCloseRequest(event -> webEngine.load("https://www.example.com"));
        webEngine.load(DEFAULT_URL);
        webHistory.addHistory(DEFAULT_URL);
        return tab;
    }
}
