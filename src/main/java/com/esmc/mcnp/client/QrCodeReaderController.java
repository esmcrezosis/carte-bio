package com.esmc.mcnp.client;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.HybridBinarizer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.embed.swing.SwingNode;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Created by Tanuj on 2/12/17.
 */
public class QrCodeReaderController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(QrCodeReaderController.class);

    /**
     * UI Controls *
     */
    public AnchorPane mainContainer;
    public AnchorPane webcamContainerAnchorPane;
    public TextField txtBarcodeNumber;
    public ComboBox<BarcodeFormat> cmbBarcodeType;
    public Button btnGenerate;
    public Label lblStatus;
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private Stage parentStage;

    public Stage getParentStage() {
        return parentStage;
    }

    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }

    ObservableList<BarcodeFormat> options = FXCollections.observableArrayList();
    private Webcam defaultWebcam = null;
    private WebcamPanel defaultWebcamPanel = null;
    private final SwingNode defaultWebcamPanelNode = new SwingNode();
    Writer writer = new MultiFormatWriter();

    private Runnable barcodeScannerRunnable = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        defaultWebcam = Webcam.getDefault();
        defaultWebcam.setViewSize(WebcamResolution.QVGA.getSize());
        defaultWebcamPanel = new WebcamPanel(defaultWebcam, true);
        creatDefaultWebcamPanel(defaultWebcamPanelNode);
    }

    private void creatDefaultWebcamPanel(final SwingNode swingNode) {
        SwingUtilities.invokeLater(() -> {
            if (defaultWebcamPanel != null) {
                swingNode.setContent(defaultWebcamPanel);
            }
        });
    }

    public void init() {
        initUI();
        barcodeScannerRunnable = getBarcodeReaderThread();
        new Thread(barcodeScannerRunnable).start();
    }

    private void initUI() {
        Platform.runLater(() -> {
            webcamContainerAnchorPane.getChildren().clear();
            webcamContainerAnchorPane.getChildren().add(defaultWebcamPanelNode);
            setRenderBarcodeTypeList();
        });
        btnGenerate.setOnAction(event -> generateBarcode());

    }

    private void setRenderBarcodeTypeList() {
        options.addAll(Arrays.asList(BarcodeFormat.values()));
        cmbBarcodeType.setItems(options);
        cmbBarcodeType.getSelectionModel().select(BarcodeFormat.QR_CODE);
    }

    private void generateBarcode() {
        Task<BufferedImage> barcodeWriterTask = new Task<BufferedImage>() {
            @Override
            protected BufferedImage call() throws Exception {
                String contents = txtBarcodeNumber.getText().trim();
                BarcodeFormat format = cmbBarcodeType.getSelectionModel().getSelectedItem();
                int width = 400;
                int height = 300;
                if (contents == null || (contents != null && contents.isEmpty()) || format == null) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Please enter valid content and barcode format!");
                        alert.setResult(ButtonType.CLOSE);
                        alert.initStyle(StageStyle.UNDECORATED);
                        alert.show();
                    });
                    return null;
                }
                try {
                    if (format == BarcodeFormat.QR_CODE) {
                        height = 400;
                    }
                    BufferedImage image = MatrixToImageWriter.toBufferedImage(writer.encode(contents, format, width, height));
                    return image;
                } catch (WriterException e) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Cannot generate barcode reason : " + e.getMessage());
                        alert.setResult(ButtonType.CLOSE);
                        alert.initStyle(StageStyle.UNDECORATED);
                        alert.show();
                    });
                    logger.error("Exception : " + e.getMessage());
                    return null;
                }
            }
        };

        barcodeWriterTask.setOnSucceeded(event -> {
            BufferedImage qrResult = barcodeWriterTask.getValue();
            if (qrResult != null) {
                openBarcodeModalWindow(qrResult);
            }
        });

        new Thread(barcodeWriterTask).start();
    }

    private void openBarcodeModalWindow(BufferedImage result) {
        WritableImage image = SwingFXUtils.toFXImage(result, null);
        Stage stage = new Stage();
        ImageView imageView = new ImageView(image);
        imageView.setLayoutX(10);
        imageView.setLayoutY(10);
        imageView.setStyle("-fx-border-color: black");
        AnchorPane parent = new AnchorPane(imageView);
        parent.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6)");
        parent.setPrefWidth(image.getWidth() + 20);
        parent.setPrefHeight(image.getHeight() + 20);
        stage.setScene(new Scene(parent));
        stage.setTitle("Generated Barcode");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.initOwner(parentStage);
        stage.show();
    }

    private Runnable getBarcodeReaderThread() {
        return () -> {
            logger.info("Starting Barcode Reader Thread");
            BufferedImage image = null;
            Reader reader = new MultiFormatReader();
            Result lastResult = null;
            if (defaultWebcam != null) {
                while (defaultWebcam.isOpen()) {
                    if ((image = defaultWebcam.getImage()) == null) {
                        continue;
                    }
                    try {
                        LuminanceSource source = new BufferedImageLuminanceSource(image);
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                        final Result coderesult = reader.decode(bitmap);
                        if (coderesult != null && coderesult.getText() != null) {
                            if (lastResult == null) {
                                logger.info("Barcode text is " + coderesult.getText());
                                setResult(coderesult.getText());
                                Platform.runLater(() -> {
                                    onClose();
                                    getParentStage().close();
                                });
                            } else if (lastResult.getText() != null && !lastResult.getText().equals(coderesult.getText())) {
                                logger.info("Barcode text is " + coderesult.getText());
                                setResult(coderesult.getText());
                                Platform.runLater(() -> {
                                    onClose();
                                    getParentStage().close();
                                });
                            }
                        }
                        lastResult = coderesult;
                    } catch (NotFoundException notEx) {

                    } catch (ChecksumException | FormatException ex) {
                        logger.error("Exception while reading barcode : ", ex);
                    }
                }
            }
        };
    }

    public void onClose() {
        defaultWebcam.close();
        logger.info("Closing Application Root Controller");
    }
}
