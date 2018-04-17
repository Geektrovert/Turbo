package com.technorex.browser;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    public ImageView imageView;
    private Image notPressed=new Image("Icons/Go.png");
    private Image pressed=new Image("Icons/stay.png");
    @FXML
    private void goAction() {
        imageView.setImage(pressed);
        webEngine.load(
                (txtURL.getText().startsWith("http://")||txtURL.getText().startsWith("https://"))
                        ? txtURL.getText() : "http://" + txtURL.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        webEngine = webView.getEngine();
        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> txtURL.setText(newValue));
        txtURL.setText("https://duckduckgo.com");
        webEngine.load(txtURL.getText());
    }

    public void release() {
        imageView.setImage(notPressed);
    }
}
