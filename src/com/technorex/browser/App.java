package com.technorex.browser;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Main class that extends Application Class as to initiate the Turbo browser.
 *
 * @author Samnan Rahee, Sihan Tawsik
 */

public class App extends Application {

    private static boolean JSval = true;
    private final String DEFAULT_URL = "https://www.duckduckgo.com";
    private double scWidth = Screen.getPrimary().getBounds().getWidth();
    private double scHeight = Screen.getPrimary().getBounds().getHeight();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:s a");
    /**
     * Performs activity associated with the initialization of the Stage and Scene
     *
     * @param stage Input Stage.
     */
    @Override
    public void start(Stage stage) {
        InitPage initPage = new InitPage();
        stage.setScene(initPage.getInitPage());
        stage.setMaximized(true);
        stage.setTitle("Turbo");
        stage.getIcons().add(new Image("Icons/icon.png"));
        stage.initStyle(StageStyle.DECORATED);
        stage.show();
        waitSeconds(5);
        init(stage);
    }

    private void init(Stage stage) {

        /*
          Necessary Variables
         */
        Group root = new  Group();
        BorderPane borderPane = new BorderPane();
        final TabPane tabPane = new TabPane();
        final Tab newTab = new Tab();
        Button goButton = new Button();
        Button forward = new Button();
        Button toggleJs = new Button();
        Button backward = new Button();
        ComboBox<String> history = new ComboBox<>();
        ComboBox<TextField> notePad = new ComboBox<>();
        Button bookmark = new Button();
        History webHistory = new History();

        /*
          Logic and Graphics handling
         */
        stage.setScene(new Scene(root,scWidth,scHeight));
        tabPane.setPrefSize(scWidth,scHeight);
        tabPane.setSide(Side.TOP);
        tabPane.setStyle("-fx-background-color: #f7f7f7");
        newTab.setText("+");
        newTab.setClosable(false);
        tabPane.getTabs().addAll(newTab);
        createAndSelectNewTab(tabPane);
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldSelectedTab, newSelectedTab) -> {
            if (newSelectedTab == newTab) {
                Tab tab = new Tab();
                tab.setText("New Tab");
                tab.setStyle("-fx-background-color: #f7f7f7");
                WebView webView = new WebView();
                final WebEngine webEngine = webView.getEngine();
                final TextField urlField = new TextField(DEFAULT_URL);
                urlField.setMinHeight(36.0);


                /*
                Action Handler for WebEngine
                 */
                webEngine.locationProperty().addListener((observable1, oldValue, newValue) -> urlField.setText(newValue));

                EventHandler<ActionEvent> goAction = event -> {
                    webEngine.load( (urlField.getText().startsWith("http://") ||urlField.getText().startsWith("https://"))
                            ? urlField.getText()
                            : "https://" + urlField.getText());
                    webHistory.addHistory(webEngine.getLocation());
                };


                /*
                Action handler for JS toggle button
                 */
                EventHandler<ActionEvent> toggleJS = event -> {
                    JSval=!JSval;
                    if(JSval) {
                        webEngine.setJavaScriptEnabled(true);
                        toggleJs.getStylesheets().removeAll(toggleJs.getStylesheets());
                        toggleJs.getStylesheets().add("/stylesheets/ToggleJs.css");
                        webEngine.reload();
                    }
                    else {
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
                goButton.setPrefSize(36.0,36.0);
                toggleJs.setPrefSize(36.0,36.0);
                forward.setPrefSize(36.0,36.0);
                backward.setPrefSize(36.0,36.0);
                history.setPrefSize(36.0,36.0);
                bookmark.setPrefSize(36.0,36.0);
                notePad.setPrefSize(36.0,36.0);

                goButton.setMinSize(36.0,36.0);
                toggleJs.setMinSize(36.0,36.0);
                forward.setMinSize(36.0,36.0);
                backward.setMinSize(36.0,36.0);
                history.setMinSize(36.0,36.0);
                bookmark.setMinSize(36.0,36.0);
                notePad.setMinSize(36.0,36.0);

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
                hBox.getChildren().setAll(backward,forward,toggleJs,history,urlField,goButton,bookmark,notePad);
                hBox.setStyle("-fx-background-color: #f7f7f7");
                hBox.setMinHeight(36);
                HBox.setHgrow(urlField, Priority.ALWAYS);
                final VBox vBox = new VBox();
                vBox.getChildren().setAll(hBox, webView);
                VBox.setVgrow(webView, Priority.ALWAYS);
                vBox.setMinHeight(36);
                tab.setContent(vBox);
                final ObservableList<Tab> tabs = tabPane.getTabs();
                tab.closableProperty().bind(Bindings.size(tabs).greaterThan(2));
                tabs.add(tabs.size() - 1, tab);
                tabPane.getSelectionModel().select(tab);
                webEngine.load(DEFAULT_URL);
                webHistory.addHistory(DEFAULT_URL);
            }
        });
        borderPane.setCenter(tabPane);
        root.getChildren().add(borderPane);
    }

    private void createAndSelectNewTab(final TabPane tabPane) {
        Tab tab = new Tab("Home");
        File file = new File("/Icons/InitPage.png");
        Image initImage = new Image(file.getPath());
        ImageView imageView = new ImageView(initImage);
        imageView.setSmooth(true);
        final ObservableList<Tab> tabs = tabPane.getTabs();
        tab.closableProperty().bind(Bindings.size(tabs).greaterThan(2));
        tabs.add(tabs.size() - 1, tab);
        tabPane.getSelectionModel().select(tab);
    }

    private void waitSeconds(int seconds) {
        String currTime = LocalDateTime.now().format(formatter);
        currTime = currTime.substring(6,currTime.length()-3);
        String temp = currTime;
        while ((Integer.parseInt(currTime) + seconds) != Integer.parseInt(temp)) {
            temp = LocalDateTime.now().format(formatter);
            temp = temp.substring(6,temp.length()-3);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
