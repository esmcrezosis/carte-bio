/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esmc.mcnp.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.esmc.mcnp.client.util.HttpUtil;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class MainWindowController implements Initializable {

    @FXML
    private Button logoutButton;
    @FXML
    private Button cardDemandButton;
    @FXML
    private Button cardPrintButton;
    @FXML
    private Label sessionLabel;
    @FXML
    private VBox vbox;
    @FXML
    private AnchorPane pane;
    @FXML
    private CheckMenuItem ckInternet;
    @FXML
    private MenuItem mnClose;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sessionLabel.setText("Nom Utilisateur : " + MainApp.getInstance().getUser().getNomUser());
        ckInternet.setOnAction(a -> {
            if (ckInternet.isSelected()) {
                MainApp.getInstance().setReseau(1);
                System.out.println(MainApp.getInstance().getServerUrl());
            } else {
                MainApp.getInstance().setReseau(0);
                System.out.println(MainApp.getInstance().getServerUrl());
            }
        });
    }

    @FXML
    private void openCardDemand(ActionEvent e) throws IOException {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CarteWindow.fxml"));
        final Parent root = loader.load();
        final Scene scene = new Scene(root, 760, 665);
        Stage stage = new Stage();
        stage.setTitle("Demande de Cartes");
        stage.centerOnScreen();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void openMoraleCardDemand(ActionEvent e) throws IOException {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CarteWindowMorale.fxml"));
        final Parent root = loader.load();
        final Scene scene = new Scene(root, 780, 670);
        Stage stage = new Stage();
        stage.setTitle("Demande de Cartes");
        stage.centerOnScreen();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void openCardPint(ActionEvent e) throws IOException {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PrinterWindow.fxml"));
        final Parent root = loader.load();
        final Scene scene = new Scene(root, 1110, 650);
        Stage stage = new Stage();
        stage.setTitle("Impression de Cartes");
        stage.centerOnScreen();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void openPintingCard(ActionEvent e) throws IOException {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PrintingView.fxml"));
        final Parent root = loader.load();
        final Scene scene = new Scene(root, 850, 565);
        Stage stage = new Stage();
        stage.setTitle("Impression de Cartes");
        stage.centerOnScreen();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void openLivCard(ActionEvent e) throws IOException {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LivraisonView.fxml"));
        final Parent root = loader.load();
        final Scene scene = new Scene(root, 850, 565);
        Stage stage = new Stage();
        stage.setTitle("Livraison de Cartes");
        stage.centerOnScreen();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void logout(ActionEvent e) throws IOException {
        HttpUtil.shutdown();
        Platform.exit();
    }

}
