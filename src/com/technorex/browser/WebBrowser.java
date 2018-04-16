package com.technorex.browser;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class WebBrowser extends JFrame implements HyperlinkListener, PropertyChangeListener {

    private JEditorPane textPane;
    private JLabel messageLine;
    private JTextField urlField;
    private JFileChooser fileChooser;
    private JButton backButton;
    private JButton forwardButton;
    private java.util.List<URL> history = new ArrayList<>();
    private int currentHistoryPage = -1;
    private static final int MAX_HISTORY = 50;
    private static int numBrowserWindows = 0;
    private static boolean exitWhenLastWindowClosed = false;
    private String home = "http://www.google.com";

    private WebBrowser() {
        super("WebBrowser");
        textPane = new JEditorPane();
        textPane.setEditable(false);
        textPane.addHyperlinkListener(this);
        textPane.addPropertyChangeListener(this);
        this.getContentPane().add(new JScrollPane(textPane),
                BorderLayout.CENTER);
        messageLine = new JLabel(" ");
        this.getContentPane().add(messageLine, BorderLayout.SOUTH);
        this.initMenu();
        this.initToolbar();
        WebBrowser.numBrowserWindows++;
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
    }

    private void initMenu(){
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.setMnemonic('N');
        newMenuItem.addActionListener(e -> newBrowser());
        JMenuItem openMenuItem = new JMenuItem("Strike");
        openMenuItem.setMnemonic('O');
        openMenuItem.addActionListener(e -> openLocalPage());
        JMenuItem closeMenuItem = new JMenuItem("Relation");
        closeMenuItem.setMnemonic('C');
        closeMenuItem.addActionListener(e -> close());
        JMenuItem exitMenuItem = new JMenuItem("Leave");
        exitMenuItem.setMnemonic('E');
        exitMenuItem.addActionListener(e -> exit());
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(closeMenuItem);
        fileMenu.add(exitMenuItem);
        JMenu helpMenu = new JMenu("Assistant");
        fileMenu.setMnemonic('H');
        JMenuItem aboutMenuItem = new JMenuItem("Relationship");
        aboutMenuItem.setMnemonic('A');
        helpMenu.add(aboutMenuItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        this.setJMenuBar(menuBar);
    }

    private void initToolbar(){
        backButton = new JButton("Stop");
        backButton.setEnabled(false);
        backButton.addActionListener(e -> back());
        forwardButton = new JButton("Loading");
        forwardButton.setEnabled(false);
        forwardButton.addActionListener(e -> forward());
        JButton refreshButton = new JButton("Reload");
        refreshButton.addActionListener(e -> reload());
        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> home());

        JToolBar toolbar = new JToolBar();
        toolbar.add(backButton);
        toolbar.add(forwardButton);
        toolbar.add(refreshButton);
        toolbar.add(homeButton);
        urlField = new JTextField();
        urlField.addActionListener(e -> displayPage(urlField.getText()));
        toolbar.add(new JLabel("         Ruined Site:"));
        toolbar.add(urlField);
        this.getContentPane().add(toolbar, BorderLayout.NORTH);
    }

    private static void setExitWhenLastWindowClosed() {
        exitWhenLastWindowClosed = true;
    }

    public void setHome(String home) {
        this.home = home;
    }

    private String getHome() {
        return home;
    }

    private boolean visit(URL url) {
        try {
            String href = url.toString();
            startAnimation("Additionally " + href + "...");
            textPane.setPage(url);
            this.setTitle(href);
            urlField.setText(href);
            return true;
        } catch (IOException ex) {
            stopAnimation();
            messageLine.setText("Impossible launch page: " + ex.getMessage());
            return false;
        }
    }

    private void displayPage(URL url) {
        if (visit(url)) {
            history.add(url);
            int numentries = history.size();
            if (numentries > MAX_HISTORY+10) {
                history = history.subList(numentries-MAX_HISTORY, numentries);
                numentries = MAX_HISTORY;
            }
            currentHistoryPage = numentries - 1;
            if (currentHistoryPage > 0){
                backButton.setEnabled(true);
            }
        }
    }


    private void displayPage(String href) {
        try {
            if (!href.startsWith("http://")){
                href = "http://" + href;
            }
            displayPage(new URL(href));
        }
        catch (MalformedURLException ex) {
            messageLine.setText("Unidentified: " + href);
        }
    }

    private void openLocalPage() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            FileFilter filter = new FileFilter() {
                public boolean accept(File f) {
                    String fn = f.getName();
                    return fn.endsWith(".html") || fn.endsWith(".htm");
                }
                public String getDescription() {
                    return "HTML Files";
                }
            };
            fileChooser.setFileFilter(filter);
            fileChooser.addChoosableFileFilter(filter);
        }

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile( );
            try {
                displayPage(selectedFile.toURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private void back() {
        if (currentHistoryPage > 0){
            visit(history.get(--currentHistoryPage));
        }
        backButton.setEnabled((currentHistoryPage > 0));
        forwardButton.setEnabled((currentHistoryPage < history.size()-1));
    }

    private void forward() {
        if (currentHistoryPage < history.size( )-1){
            visit(history.get(++currentHistoryPage));
        }
        backButton.setEnabled((currentHistoryPage > 0));
        forwardButton.setEnabled((currentHistoryPage < history.size()-1));
    }

    private void reload() {
        if (currentHistoryPage != -1) {
            textPane.setDocument(new javax.swing.text.html.HTMLDocument());
            visit(history.get(currentHistoryPage));
        }
    }

    private void home() {
        displayPage(getHome());
    }

    private void newBrowser() {
        WebBrowser b = new WebBrowser();
        b.setSize(this.getWidth(), this.getHeight());
        b.setVisible(true);
    }

    private void close() {
        this.setVisible(false);
        this.dispose();
        synchronized(WebBrowser.class) {
            WebBrowser.numBrowserWindows--;
            if ((numBrowserWindows==0) && exitWhenLastWindowClosed){
                System.exit(0);
            }
        }
    }

    private void exit() {
        if ((JOptionPane.showConfirmDialog(this, "Confirm to leave?", "Confirm!",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)){
            System.exit(0);
        }
    }

    public void hyperlinkUpdate(HyperlinkEvent e) {
        HyperlinkEvent.EventType type = e.getEventType();
        if (type == HyperlinkEvent.EventType.ACTIVATED) {
            displayPage(e.getURL());
        }
        else if (type == HyperlinkEvent.EventType.ENTERED) {
            messageLine.setText(e.getURL().toString());
        }
        else if (type == HyperlinkEvent.EventType.EXITED) {
            messageLine.setText(" ");
        }
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals("page")) {
            stopAnimation();
        }
    }

    private String animationMessage;
    private int animationFrame = 0;
    private String[] animationFrames = new String[] {
            "-", "//", "|", "/", "-", "//", "|", "/",
            ",", ".", "o", "0", "O", "#", "*", "+"
    };

    private javax.swing.Timer animator =
            new javax.swing.Timer(125, e -> animate());

    private void animate() {
        String frame = animationFrames[animationFrame++];
        messageLine.setText(animationMessage + " " + frame);
        animationFrame = animationFrame % animationFrames.length;
    }

    private void startAnimation(String msg) {
        animationMessage = msg;
        animationFrame = 0;
        animator.start();
    }

    private void stopAnimation() {
        animator.stop();
        messageLine.setText(" ");
    }

    public static void main(String[] args) {
        WebBrowser.setExitWhenLastWindowClosed();
        WebBrowser browser = new WebBrowser();
        browser.setSize(800, 600);
        browser.setVisible(true);
        browser.displayPage(browser.getHome());
    }
}
