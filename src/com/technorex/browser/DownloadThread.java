package com.technorex.browser;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class DownloadThread {
    private static final int BUFFER_SIZE = 4096;
    private String Url;

    DownloadThread(String url) {
        Url=url;
    }
    void load(Stage primaryStage) throws IOException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("JavaFX Projects");
        File defaultDirectory = new File("C:\\Users\\User\\Desktop\\");
        chooser.setInitialDirectory(defaultDirectory);
        downloadFile(chooser.showDialog(primaryStage).getPath());
    }

    private void downloadFile(String saveDir) throws IOException {
        URL url = new URL(Url);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();

            if (disposition != null) {
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = Url.substring(Url.lastIndexOf("/") + 1,
                        Url.length());
            }
            System.out.println("Downloading");
            System.out.println("fileName = " + fileName);
            System.out.println("File-Size = " + contentLength + " bytes");
            System.out.println("File-Type = " + contentType);
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = saveDir + File.separator + fileName;
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            System.out.println("File downloaded");
        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();

    }
}
