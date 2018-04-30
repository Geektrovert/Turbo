package com.technorex.browser;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    /**
     * Performs activity associated with the initialization of the Stage and Scene
     *
     * @param stage Input Stage.
     */
    @Override
    public void start(Stage stage) {
        init(stage);
        stage.show();
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

        /*
          Logic and Graphics handling
         */
        stage.setTitle("Turbo");
        stage.getIcons().add(new Image("Icons/icon.png"));
        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(new Scene(root,scWidth,scHeight));
        stage.setMaximized(true);
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

                EventHandler<ActionEvent> goAction = event -> webEngine.load( (urlField.getText().startsWith("http://") ||urlField.getText().startsWith("https://"))
                        ? urlField.getText()
                        : "https://" + urlField.getText());


                /*
                Action handler for JS toggle button
                 */
                EventHandler<ActionEvent> toggleJS = event -> {
                    JSval=!JSval;
                    if(JSval) {
                        webEngine.setJavaScriptEnabled(true);
                        toggleJs.getStylesheets().add("/stylesheets/ToggleJs.css");
                        webEngine.reload();
                    }
                    else {
                        webEngine.setJavaScriptEnabled(false);
                        toggleJs.getStylesheets().add("/stylesheets/notToggleJs.css");
                        webEngine.reload();
                    }
                };

                EventHandler<ActionEvent> notePadClicked = event -> {
                    notePad.getItems().removeAll(notePad.getItems());
                    notePad.getItems().add(new TextField());
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
            }
        });
        borderPane.setCenter(tabPane);
        root.getChildren().add(borderPane);
    }

    private void createAndSelectNewTab(final TabPane tabPane) {
        Tab tab = new Tab("Home");
        Label aboutLabel = new Label();
        aboutLabel.setText("\n\nTURBO" +
                "\n\n--->>> A lightweight, Secure and User Friendly Web Browser <<<---" +
                  "\n  ----------------(CSE-2112 Java project, CSEDU)---------------- " +
                  "\n            -->  Start browsing by opening new tab <--" +
                "\n\n\n\n  <-------- FEATURES -------->");
        aboutLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 20));
        aboutLabel.setAlignment(Pos.BOTTOM_CENTER);
        tab.setContent(aboutLabel);
        final ObservableList<Tab> tabs = tabPane.getTabs();
        tab.closableProperty().bind(Bindings.size(tabs).greaterThan(2));
        tabs.add(tabs.size() - 1, tab);
        tabPane.getSelectionModel().select(tab);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
