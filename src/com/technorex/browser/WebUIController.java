package com.technorex.browser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
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
    public ComboBox<String> historyButton;
    @FXML
    public ImageView backward;
    @FXML
    public ImageView forward;
    @FXML
    public ImageView bookmark;
    @FXML
    public TextField txtURL;
    @FXML
    WebView webView;
    private WebEngine webEngine;
    @FXML
    public ImageView search;
    @FXML
    public ImageView menuBar;
    private Image JSImageOn = new Image("Icons/JS.png");
    private Image JSImageOff = new Image("Icons/JSOff.png");
    private ArrayList<String> history = new ArrayList<>();
    private boolean JSVal = true;
    private int currIndex = 0;
    private final double hoverVal = 1.0, hoverRelease = 0.6;
    private Tabs tabs;
    @FXML
    private void goAction() {
        String url;
        if (txtURL.getText().startsWith("http://") || txtURL.getText().startsWith("https://"))
            url = txtURL.getText();
        else
            url = "https://" + txtURL.getText();
        webEngine.load(url);
        history.add(++currIndex,url);
        for(int ind = currIndex+1; ind<history.size(); ind++)
            history.remove(ind);
    }
    @FXML
    private void goAction(String historyUrl) {
        String url;
        if (txtURL.getText().startsWith("http://") || txtURL.getText().startsWith("https://"))
            url = historyUrl;
        else
            url = "https://" + historyUrl;
        webEngine.load(url);
        history.add(++currIndex,url);
        for(int ind = currIndex+1; ind<history.size(); ind++)
            history.remove(currIndex);
        //System.out.println(webEngine.getTitle());
    }
    /**
     * Perform functionality associated with starting the browser for every time.
     * @param url Input URL
     * @param rb Input ResourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tabs=new Tabs(webView);
        webEngine = tabs.getWebEngine();
        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> txtURL.setText(newValue));
        if (!webEngine.isJavaScriptEnabled())
            webEngine.setJavaScriptEnabled(true);
        txtURL.setText("https://duckduckgo.com");
        webEngine.load(txtURL.getText());
        history.add(webEngine.getLocation());
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
        System.out.println("History:--->>>");
        historyButton.getItems().removeAll(historyButton.getItems());
        for(int i=history.size()-1;i>=0;i--) {
            historyButton.getItems().add(history.get(i));
        }
        String titleName = webEngine.getHistory().getEntries().get(webEngine.getHistory().getEntries().size() - 1).getTitle();
        System.out.println("Current: " + titleName);
    }

    /**
     * Following functions performs functionality associated with hovering activity over icons
     *
     */

    public void menuOnHover() {
        menuBar.setOpacity(hoverVal);
    }

    public void menuNotHovered() {
        menuBar.setOpacity(hoverRelease);
    }

    public void backOnHover() {
        if(currIndex>0)
            backward.setOpacity(hoverVal);
    }

    public void backNotHovered() {
        backward.setOpacity(hoverRelease);
    }

    public void forwardOnHover() {
        if(currIndex<history.size()-1)
            forward.setOpacity(hoverVal);
    }

    public void forwardNotHovered() {
        forward.setOpacity(hoverRelease);
    }

    public void JSOnHover() {
        toggleJS.setOpacity(hoverVal);
    }

    public void JSNotHovered() {
        toggleJS.setOpacity(hoverRelease);
    }

    public void historyOnHover() {
        historyButton.setOpacity(hoverVal);
    }

    public void historyNotHovered() {
        historyButton.setOpacity(hoverRelease);
    }

    public void searchOnHover() {
        search.setOpacity(hoverVal);
    }

    public void searchNotHovered() {
        search.setOpacity(hoverRelease);
    }

    public void bookmarkOnHover() {
        bookmark.setOpacity(hoverVal);
    }

    public void bookmarkNotHovered() {
        bookmark.setOpacity(hoverRelease);
    }

    public void goBack() {
        if(currIndex>0)
            webEngine.load(history.get(--currIndex));
    }

    public void goForward() {
        if(currIndex<history.size()-1)
            webEngine.load(history.get(++currIndex));
    }
}
