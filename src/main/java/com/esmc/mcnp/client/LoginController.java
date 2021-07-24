package com.esmc.mcnp.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {

    @FXML
    private TextField userId;
    @FXML
    private PasswordField password;
    @FXML
    private Label errorMessage;
    @FXML
    private Button loginButton;
    @FXML
    private CheckBox ckIntegrateur;
    @FXML
    private CheckBox ckReseau;

    public LoginController() {
    }

    @FXML
    protected void processLogin(ActionEvent e) {
        try {
            int integrateur = 0;
            if (ckIntegrateur.isSelected()) {
                integrateur = 1;
            }
            if (ckReseau.isSelected()) {
                MainApp.getInstance().setReseau(1);
            } else {
                MainApp.getInstance().setReseau(0);
            }
            if (!MainApp.getInstance().userLogging(userId.getText(), password.getText(), integrateur)) {
                errorMessage.setText("Données Invalides");
            } else {
                MainApp.getInstance().setPassword(password.getText());
            }
        } catch (RejectedExecutionException | NullPointerException | InterruptedException | ExecutionException | IOException | TimeoutException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Echec de l'authentication");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ckReseau.setOnAction((ActionEvent event) -> {
            if (ckReseau.isSelected()) {
                MainApp.getInstance().setReseau(1);
            } else {
                MainApp.getInstance().setReseau(0);
            }
        });
        userId.setPromptText("Nom d'utilisateur");
        password.setPromptText("Mot de passe");
    }

}
