package com.technorex.browser;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Browser {
    private JFrame frame;
    private JPanel panelTop;
    private JEditorPane editor;
    private JScrollPane scroll;
    private JTextField field;
    private JButton button;
    private URL url;

    private Browser(String title) {
        initComponents();

        //set the title of the frame
        frame.setTitle(title);

        //set the default cloe op of the jframe
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //set size of frame
        frame.setSize(800,600);

        //add jpanel to north of jframe
        frame.add(BorderLayout.NORTH, panelTop);

        //add textfield and navigation button to jpanel.
        panelTop.add(field);
        panelTop.add(button);

        //add scroll pane to jframe center
        frame.add(BorderLayout.CENTER, scroll);


        //set the frame visible
        frame.setVisible(true);
    }//end Browser() constructor

    private void initComponents() {
        //create the JFrame
        frame = new JFrame();

        //create the JPanel used to hold the text field and button.
        panelTop = new JPanel();

        //set the url
        try {
            url = new URL("http://www.javagaming.org");
        }
        catch(MalformedURLException mue) {
            JOptionPane.showMessageDialog(null,mue);
        }

        //create the JEditorPane
        try {
            editor = new JEditorPane(url);

            //set the editor pane to false.
            editor.setEditable(false);
        }
        catch(IOException ioe) {
            JOptionPane.showMessageDialog(null,ioe);
        }

        //create the scroll pane and add the JEditorPane to it.
        scroll = new JScrollPane(editor, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        //create the JTextField
        field = new JTextField();

        //set the JTextField text to the url.
        //we're not doing this on the event dispatch thread, so we need to use
        //SwingUtilities.
        SwingUtilities.invokeLater(() -> field.setText(url.toString()));

        //create the button for chanign pages.
        button = new JButton("Go");

        //add action listener to the button
        button.addActionListener(e -> {
            try {
                editor.setPage(field.getText());
            }
            catch(IOException ioe) {
                JOptionPane.showMessageDialog(null, ioe);
            }
        });

        //end hyperlinkUpdate()
        editor.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                try {
                    editor.setPage(e.getURL());
                }
                catch(IOException ioe) {
                    JOptionPane.showMessageDialog(null,ioe);
                }
        });//end HyperlinkListener
    }//end initComponents()

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Browser("Simple web browser"));
    }//end main method.
}//end Browser class
