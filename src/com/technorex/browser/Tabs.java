package com.technorex.browser;

import javafx.scene.web.WebEngine;

import java.net.URL;

public class Tabs extends Thread{
    private App app;
    private URL url;
    private WebEngine webEngine;
    public Tabs(String name,App app){
        super(name);
        this.app=app;
    }
    @Override
    public void run(){

    }
}
