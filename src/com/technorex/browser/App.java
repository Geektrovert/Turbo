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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
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

    private boolean JSval = true;

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
        double scWidth = Screen.getPrimary().getBounds().getWidth();
        double scHeight = Screen.getPrimary().getBounds().getHeight();
        final String DEFAULT_URL = "www.example.com";
        Group root = new  Group();
        BorderPane borderPane = new BorderPane();
        final TabPane tabPane = new TabPane();
        final Tab newTab = new Tab();
        Button goButton = new Button();
        Button forward = new Button();
        Button toggleJs = new Button();
        Button backward = new Button();
        ComboBox<String> history = new ComboBox<>();

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
        newTab.setText("+");
        newTab.setClosable(false);
        tabPane.getTabs().addAll(newTab);
        createAndSelectNewTab(tabPane);
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldSelectedTab, newSelectedTab) -> {
            if (newSelectedTab == newTab) {
                Tab tab = new Tab();
                tab.setText("New Tab");
                WebView webView = new WebView();
                final WebEngine webEngine = webView.getEngine();
                webEngine.load(DEFAULT_URL);
                final TextField urlField = new TextField(DEFAULT_URL);
                urlField.setMinHeight(36.0);
                webEngine.locationProperty().addListener((observable1, oldValue, newValue) -> urlField.setText(newValue));
                EventHandler<ActionEvent> goAction = event -> webEngine.load( (urlField.getText().startsWith("http://") ||urlField.getText().startsWith("https://"))
                        ? urlField.getText()
                        : "https://" + urlField.getText());
                EventHandler<ActionEvent> toggleJS = event -> {
                    JSval = (!JSval);
                    if(JSval) {
                        webEngine.setJavaScriptEnabled(JSval);
                        toggleJs.getStylesheets().add("/com/technorex/browser/ToggleJs.css");
                        webEngine.reload();
                    }
                    else {
                        webEngine.setJavaScriptEnabled(JSval);
                        toggleJs.getStylesheets().add("/com/technorex/browser/notToggleJs.css");
                        webEngine.reload();
                    }
                };

                /*
                Defining button sizes and styles
                 */
                goButton.setPrefSize(36.0,36.0);
                toggleJs.setPrefSize(36.0,36.0);
                forward.setPrefSize(36.0,36.0);
                backward.setPrefSize(36.0,36.0);
                history.setMaxSize(36.0,36.0);
                goButton.setDefaultButton(true);
                toggleJs.setDefaultButton(true);
                forward.setDefaultButton(true);
                backward.setDefaultButton(true);
                goButton.getStylesheets().add("/com/technorex/browser/GoButton.css");
                toggleJs.getStylesheets().add("/com/technorex/browser/ToggleJs.css");
                forward.getStylesheets().add("/com/technorex/browser/Forward.css");
                backward.getStylesheets().add("/com/technorex/browser/Backward.css");
                history.getStylesheets().add("/com/technorex/browser/HistoryButton.css");

                /*
                Adding event handlers to buttons
                 */
                urlField.setOnAction(goAction);
                goButton.setOnAction(goAction);
                toggleJs.setOnAction(toggleJS);
                HBox hBox = new HBox(10);
                hBox.getChildren().setAll(history,backward,forward,toggleJs,urlField, goButton);
                hBox.setStyle("-fx-background-color: #f7f7f7");
                hBox.setMinHeight(36);
                HBox.setHgrow(urlField, Priority.ALWAYS);
                final VBox vBox = new VBox(5);
                vBox.getChildren().setAll(hBox, webView);
                VBox.setVgrow(webView, Priority.ALWAYS);
                vBox.setMinHeight(36);
                tab.setContent(vBox);
                final ObservableList<Tab> tabs = tabPane.getTabs();
                tab.closableProperty().bind(Bindings.size(tabs).greaterThan(2));
                tabs.add(tabs.size() - 1, tab);
                tabPane.getSelectionModel().select(tab);
            }
        });
        borderPane.setCenter(tabPane);
        root.getChildren().add(borderPane);
    }

    private void createAndSelectNewTab(final TabPane tabPane) {
        Tab tab = new Tab("Home");
        Label aboutLabel = new Label();
        aboutLabel.setText("\n\n\n\n\t\t\t\t\t\t\tTURBO" +
                "\n\n\t\t\t\t\tA lightweight, Secure and User Friendly Web Browser" +
                "\n\t\t\t\t\tCSE-2112 Java project, CSEDU" +
                "\n\t\t\t\t\tStart browsing by opening new tab");
        aboutLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 20));
        aboutLabel.setAlignment(Pos.TOP_CENTER);
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
