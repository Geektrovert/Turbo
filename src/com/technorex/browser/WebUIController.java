package com.technorex.browser;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This file is associated with controlling of WebUI.fxml for GUI
 *
 * @author Samnan Rahee, Sihan Tawsik
 */

public class WebUIController implements Initializable {

    @FXML
    public ToggleButton toggleJS;
    @FXML
    TextField txtURL;
    @FXML
    WebView webView;
    private WebEngine webEngine;
    @FXML
    public ImageView imageView;
    private Image notPressed=new Image("Icons/Go.png");
    private Image pressed=new Image("Icons/stay.png");
    @FXML
    public MenuBar menuBar;
    private boolean JSVal = true;

    /**
     * Handle action related to "About" menu item.
     */

    @FXML
    public void handleAboutAction() {
        provideAboutFunctionality();
    }

    /**
     * Handle action related to input
     *
     * @param event Input event.
     */

    @FXML
    private void handleKeyInput(final InputEvent event)
    {
        if (event instanceof KeyEvent)
        {
            final KeyEvent keyEvent = (KeyEvent) event;
            if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.A)
            {
                provideAboutFunctionality();
            }
        }
    }

    /**
     * Perform functionality associated with "About" menu selection or CTRL-A.
     */
    private void provideAboutFunctionality()
    {
        System.out.println("You clicked on About!");
    }

    /**
     * Perform functionality associated with "Exit" menu selection.
     */
    public void close(){
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void goAction() {
        imageView.setImage(pressed);
        webEngine.load(
                (txtURL.getText().startsWith("http://")||txtURL.getText().startsWith("https://"))
                        ? txtURL.getText() : "http://" + txtURL.getText());
        //System.out.println(webEngine.getTitle());
    }

    /**
     * Perform functionality associated with starting the browser for every time.
     * @param url Input URL
     * @param rb Input ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        webEngine = webView.getEngine();
        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> txtURL.setText(newValue));
        if (!webEngine.isJavaScriptEnabled())
            webEngine.setJavaScriptEnabled(true);
        txtURL.setText("https://duckduckgo.com");
        webEngine.load(txtURL.getText());
        //System.out.println(webEngine.getTitle());
    }

    /**
     * Perform functionality associated with the image on the right of SearchBar.
     */
    public void release() {
        imageView.setImage(notPressed);
    }

    /**
     * Perform functionality associated with "JS" toggle button.
     */
    @FXML
    public void changeJS() {
        if (JSVal) {
            JSVal = false;
            webEngine.setJavaScriptEnabled(false);
            webEngine.reload();
        }
        else {
            JSVal = true;
            webEngine.setJavaScriptEnabled(true);
            webEngine.reload();
        }
    }

    /**
     * Performs functionality associated with printing WebHistory
     */
    @FXML
    public void printHistory() {
        ObservableList<WebHistory.Entry> entries = webEngine.getHistory().getEntries();
        System.out.println("Web History--->>>");
        for (WebHistory.Entry entry:
                entries){
            System.out.println(entry);
        }
    }
}
