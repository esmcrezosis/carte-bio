/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esmc.mcnp.client;

import com.github.sarxos.webcam.Webcam;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class PhotoDialogController implements Initializable {

    private Webcam selWebCam = null;
    private boolean stopCamera = false;
    private BufferedImage grabbedImage;
    private Image image;
    ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();
    private final String cameraListPromptText = "Choisir un Camera";
    @FXML
    private Button btnStartCamera;
    @FXML
    private Button btnStopCamera;
    @FXML
    private Button btnDisposeCamera;
    @FXML
    private ImageView imgWebCamCapturedImage;
    @FXML
    private ComboBox<WebCamInfo> cbCameraOptions;
    @FXML
    BorderPane bpWebCamPaneHolder;
    @FXML
    FlowPane fpBottomPane;

    private class WebCamInfo {

        private String webCamName;
        private int webCamIndex;

        public String getWebCamName() {
            return webCamName;
        }

        public void setWebCamName(String webCamName) {
            this.webCamName = webCamName;
        }

        public int getWebCamIndex() {
            return webCamIndex;
        }

        public void setWebCamIndex(int webCamIndex) {
            this.webCamIndex = webCamIndex;
        }

        @Override
        public String toString() {
            return webCamName;
        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        createTopPanel();
    }

    protected void setImageViewSize() {
        double height = bpWebCamPaneHolder.getHeight();
        double width = bpWebCamPaneHolder.getWidth();
        imgWebCamCapturedImage.setFitHeight(height);
        imgWebCamCapturedImage.setFitWidth(width);
        imgWebCamCapturedImage.prefHeight(height);
        imgWebCamCapturedImage.prefWidth(width);
        imgWebCamCapturedImage.setPreserveRatio(true);

    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    private void createTopPanel() {
        fpBottomPane.setDisable(true);
        ObservableList<WebCamInfo> options = FXCollections.observableArrayList();
        int webCamCounter = 0;
        for (Webcam webcam : Webcam.getWebcams()) {
            WebCamInfo webCamInfo = new WebCamInfo();
            webCamInfo.setWebCamIndex(webCamCounter);
            webCamInfo.setWebCamName(webcam.getName());
            options.add(webCamInfo);
            webCamCounter++;
        }
        cbCameraOptions.setItems(options);
        cbCameraOptions.setPromptText(cameraListPromptText);
        cbCameraOptions.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<WebCamInfo>() {

            @Override
            public void changed(ObservableValue<? extends WebCamInfo> arg0, WebCamInfo arg1, WebCamInfo arg2) {
                if (arg2 != null) {
                    System.out.println("WebCam Index: " + arg2.getWebCamIndex() + ": WebCam Name:" + arg2.getWebCamName());
                    initializeWebCam(arg2.getWebCamIndex());
                }
            }
        });
        Platform.runLater(this::setImageViewSize);
    }

    protected void initializeWebCam(final int webCamIndex) {
        Task<Void> webCamIntilizer = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Dimension resolution = new Dimension(290, 290);
                if (selWebCam == null) {
                    selWebCam = Webcam.getWebcams().get(webCamIndex);
                    selWebCam.setCustomViewSizes(new Dimension[]{resolution});
                    selWebCam.setViewSize(resolution);
                    selWebCam.open();
                } else {
                    closeCamera();
                    selWebCam = Webcam.getWebcams().get(webCamIndex);
                    selWebCam.setCustomViewSizes(new Dimension[]{resolution});
                    selWebCam.setViewSize(resolution);
                    selWebCam.open();
                }
                startWebCamStream();
                return null;
            }

        };

        new Thread(webCamIntilizer).start();
        fpBottomPane.setDisable(false);
        btnStartCamera.setDisable(true);
    }

    protected void startWebCamStream() {
        stopCamera = false;
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                while (!stopCamera) {
                    try {
                        if ((grabbedImage = selWebCam.getImage()) != null) {
                            Platform.runLater(() -> {
                                image = SwingFXUtils
                                        .toFXImage(grabbedImage, null);
                                imageProperty.set(image);
                            });
                            grabbedImage.flush();
                        }
                    } catch (Exception e) {
                    } finally {

                    }
                }
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        imgWebCamCapturedImage.imageProperty().bind(imageProperty);

    }

    private void closeCamera() {
        if (selWebCam != null) {
            selWebCam.close();
        }
    }

    @FXML
    public void stopCamera(ActionEvent event) {
        stopCamera = true;
        btnStartCamera.setDisable(false);
        btnStopCamera.setDisable(true);
    }

    @FXML
    public void startCamera(ActionEvent event) {
        stopCamera = false;
        startWebCamStream();
        btnStartCamera.setDisable(true);
        btnStopCamera.setDisable(false);
    }

    @FXML
    public void disposeCamera(ActionEvent event) {
        stopCamera = true;
        closeCamera();
        btnStopCamera.setDisable(true);
        btnStartCamera.setDisable(true);
        imgWebCamCapturedImage.getScene().getWindow().hide();
    }
}
