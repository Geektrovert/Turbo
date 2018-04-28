package com.technorex.browser;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    public ImageView toggleJS;
    @FXML
    public ImageView HistoryButton;
    @FXML
    public ImageView backward;
    @FXML
    public ImageView forward;
    @FXML
    public ImageView bookmark;
    @FXML
    TextField txtURL;
    @FXML
    WebView webView;
    private WebEngine webEngine;
    @FXML
    public ImageView imageView;
    @FXML
    public ImageView menuBar;
    private Image searchIconNotHovered = new Image("Icons/Search.png");
    private Image searchIconHovered = new Image("Icons/SearchOnHover.png");
    private Image JSImageOn = new Image("Icons/JS.png");
    private Image JSImageOff = new Image("Icons/JSOff.png");
    private boolean JSVal = true;

    @FXML
    private void goAction() {
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
     * Perform functionality associated with "JS" toggle button.
     */
    @FXML
    public void changeJS() {
        if (JSVal) {
            webEngine.setJavaScriptEnabled(false);
            toggleJS.setImage(JSImageOff);
            webEngine.reload();
        }
        else {
            webEngine.setJavaScriptEnabled(true);
            toggleJS.setImage(JSImageOn);
            webEngine.reload();
        }
        JSVal = !JSVal; //Toggles the value of JSVal every time this function is called
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

    public void onHover() {
        imageView.setImage(searchIconHovered);
    }

    public void notHovered() {
        imageView.setImage(searchIconNotHovered);
    }
}
