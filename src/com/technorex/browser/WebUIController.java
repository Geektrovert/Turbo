package com.technorex.browser;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Samnan Rahee
 */
public class WebUIController implements Initializable {

    @FXML
    TextField txtURL;
    @FXML
    WebView webView;
    private WebEngine webEngine;

    @FXML
    private void goAction() {
        webEngine.load(txtURL.getText().startsWith("http://") ? txtURL.getText() : "http://" + txtURL.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        webEngine = webView.getEngine();
        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> txtURL.setText(newValue));
        txtURL.setText("http://www.google.com");
    }
}
