package com.technorex.browser;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

/**
 * Main class that extends Application Class as to initiate the Turbo browser.
 *
 * @author Samnan Rahee, Sihan Tawsik
 */

public class App extends Application {

    private double scWidth = Screen.getPrimary().getBounds().getWidth();
    private double scHeight = Screen.getPrimary().getBounds().getHeight();

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Performs activity associated with the initialization of the Stage and Scene
     *
     * @param stage Input Stage.
     */
    @Override
    public void start(Stage stage) throws InterruptedException {
        InitPage initPage = new InitPage();
        stage.setScene(initPage.getInitPage());
        stage.setMaximized(true);
        stage.setTitle("Turbo");
        stage.getIcons().add(new Image("Icons/icon.png"));
        stage.initStyle(StageStyle.DECORATED);
        stage.show();
        Thread.sleep(1000);
        init(stage);
    }

    private void init(Stage stage) {

        /*
          Necessary Variables
         */
        Group root = new Group();
        BorderPane borderPane = new BorderPane();
        final TabPane tabPane = new TabPane();
        TabManager tabManager = new TabManager();
        final Tab newTab = new Tab();

        /*
          Logic and Graphics handling
         */
        stage.setScene(new Scene(root, scWidth, scHeight));
        tabPane.setPrefSize(scWidth, scHeight);
        tabPane.setSide(Side.TOP);
        tabPane.setStyle("-fx-background-color: #f7f7f7");
        newTab.setText("+");
        newTab.setClosable(false);
        tabPane.getTabs().addAll(newTab);
        createAndSelectNewTab(tabPane);
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldSelectedTab, newSelectedTab) -> {
            if (newSelectedTab == newTab) {
                Tab tab = tabManager.createNewTab();
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
        File file = new File("/Icons/InitPage.png");
        Image initImage = new Image(file.getPath());
        ImageView imageView = new ImageView(initImage);
        imageView.setSmooth(true);
        final ObservableList<Tab> tabs = tabPane.getTabs();
        tab.closableProperty().bind(Bindings.size(tabs).greaterThan(2));
        tabs.add(tabs.size() - 1, tab);
        tabPane.getSelectionModel().select(tab);
    }
}
