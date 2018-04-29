package com.technorex.browser;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.net.URL;
import java.util.ArrayList;

public class Tabs{
    private URL currURL;
    private ArrayList<String> localHistory = new ArrayList<>();
    private int currentIndex;
    private WebView webView;
    private WebEngine webEngine;

    WebEngine getWebEngine() {
        return webEngine;
    }

    public void setWebEngine() {
        webEngine=webView.getEngine();
    }
    public URL getCurrURL() {
        return currURL;
    }

    public void setCurrURL(URL currURL) {
        this.currURL = currURL;
    }

    public ArrayList<String> getLocalHistory() {
        return localHistory;
    }

    public void setLocalHistory(ArrayList<String> localHistory) {
        this.localHistory = localHistory;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public WebView getWebView() {
        return webView;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }
}
