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
import java.util.ArrayList;
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
    public ImageView historyButton;
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
    public ImageView search;
    @FXML
    public ImageView menuBar;
    private Image JSImageOn = new Image("Icons/JS.png");
    private Image JSImageOff = new Image("Icons/JSOff.png");
    private ArrayList<URL> history,future;
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
    public void toggleJS() {
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

    public void menuOnHover() {
        menuBar.setOpacity(1.0);
    }

    public void menuNotHovered() {
        menuBar.setOpacity(0.8);
    }

    public void backOnHover() {
        backward.setOpacity(1.0);
    }

    public void backNotHovered() {
        backward.setOpacity(0.8);
    }

    public void forwardOnHover() {
        forward.setOpacity(1.0);
    }

    public void forwardNotHovered() {
        forward.setOpacity(0.8);
    }

    public void JSOnHover() {
        toggleJS.setOpacity(1.0);
    }

    public void JSNotHovered() {
        toggleJS.setOpacity(0.8);
    }

    public void historyOnHover() {
        historyButton.setOpacity(1.0);
    }

    public void historyNotHovered() {
        historyButton.setOpacity(0.8);
    }

    public void searchOnHover() {
        search.setOpacity(1.0);
    }

    public void searchNotHovered() {
        search.setOpacity(0.8);
    }

    public void bookmarkOnHover() {
        bookmark.setOpacity(1.0);
    }

    public void bookmarkNotHovered() {
        bookmark.setOpacity(0.8);
    }

    public void goBack() {
        ObservableList<WebHistory.Entry> entries = webEngine.getHistory().getEntries();
        WebHistory.Entry last = null;
        if(entries.size()>=2)
            last = entries.get(entries.size()-2);
        assert last != null;
        webEngine.load(last.getUrl());
    }

    public void goForward() {
        System.out.println("TESTING:--->>>");
        ObservableList<WebHistory.Entry> entries = webEngine.getHistory().getEntries();
        for (WebHistory.Entry entry: entries){
            System.out.println(entry);
        }
        System.out.println(webEngine.getHistory().getCurrentIndex());
    }
}
