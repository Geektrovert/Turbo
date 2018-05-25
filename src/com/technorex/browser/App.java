package com.technorex.browser;

import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Main class that extends Application Class as to initiate the Turbo browser.
 *
 * @author Samnan Rahee, Sihan Tawsik
 */

public class App extends Application {

    static Stage stage = new Stage();
    private static double scWidth = Screen.getPrimary().getBounds().getWidth();
    private static double scHeight = Screen.getPrimary().getBounds().getHeight();
    static History localHistory = new History();
    public static void main(String[] args) {
        launch(args);
    }
    private static TabPane tabPan;
    /**
     * Performs activity associated with the initialization of the Stage and Scene
     *
     * @param stage Input Stage.
     */
    @Override
    public void start(Stage stage) {
        init(App.stage);
        stage.initStyle(StageStyle.DECORATED);
        App.stage.show();
    }

    static void init(Stage stage) {

        /*
          Necessary Variables
         */
        Group root = new Group();
        BorderPane borderPane = new BorderPane();
        final TabPane tabPane = new TabPane();
        tabPan=tabPane;
        TabManager tabManager = new TabManager();
        final Tab newTab = new Tab();
        /*
          Logic and Graphics handling
         */
        try {
            HistoryWindow.addHash();
        } catch (Exception e) {
            e.printStackTrace();
        }
        stage.setTitle("Turbo");
        stage.setMaximized(true);
        stage.setScene(new Scene(root, scWidth, scHeight));
        stage.getIcons().add(new Image("Icons/icon.png"));

        tabPane.setSide(Side.TOP);
        tabPane.setPrefSize(scWidth, scHeight);
        tabPane.getStylesheets().add("/stylesheets/tab.css");
        tabPane.setMinHeight(44.0);

        newTab.setText("+");
        newTab.setStyle("-fx-font-size: 12pt;");
        newTab.setClosable(false);
        tabPane.getTabs().addAll(newTab);
        createHomeTab(tabPane);
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

    private static void createHomeTab(final TabPane tabPane) {
        Tab tab = new Tab("Home");
        tab.setStyle("-fx-border-width: 0 0 0 0");
        final VBox vBox = new VBox();
        ImageView imageView = new ImageView(new Image("/Icons/InitPage.png"));
        imageView.setFitWidth(scWidth);
        imageView.setFitHeight(scHeight-10);
        vBox.getChildren().setAll(imageView);
        vBox.setMinSize(scWidth,scHeight);
        vBox.setPrefSize(scWidth,scHeight);
        vBox.setMaxSize(scWidth,scHeight);
        tab.setContent(vBox);
        final ObservableList<Tab> tabs = tabPane.getTabs();
        tab.closableProperty().bind(Bindings.size(tabs).greaterThan(2));
        tabs.add(tabs.size() - 1, tab);
        tabPane.getSelectionModel().select(tab);
    }
    static void createAboutTab() {
        Tab tab = new Tab("About");
        tab.setStyle("-fx-border-width: 0 0 0 0");
        final VBox vBox = new VBox();
        ImageView imageView = new ImageView(new Image("/Icons/AboutPage.png"));
        imageView.setFitWidth(scWidth);
        imageView.setFitHeight(scHeight-10);
        vBox.getChildren().setAll(imageView);
        vBox.setMinSize(scWidth,scHeight);
        vBox.setPrefSize(scWidth,scHeight);
        vBox.setMaxSize(scWidth,scHeight);
        tab.setContent(vBox);
        final ObservableList<Tab> tabs = tabPan.getTabs();
        tab.closableProperty().bind(Bindings.size(tabs).greaterThan(2));
        tabs.add(tabs.size() - 1, tab);
        tabPan.getSelectionModel().select(tab);
    }
    static void startTask(String Url)
    {
        Runnable task = () -> runTask(Url);
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }
    private static void runTask(String Url)
    {
        try {
            Platform.runLater(() -> {
                try {
                    DownloadThread downloadThread = new DownloadThread(Url);
                    downloadThread.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}