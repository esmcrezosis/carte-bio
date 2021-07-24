package com.esmc.mcnp.client;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.esmc.mcnp.client.dto.User;
import com.esmc.mcnp.client.services.Authenticator;
import com.esmc.mcnp.client.services.PropertiesLoader;
import com.esmc.mcnp.client.util.HttpUtil;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeoutException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage stage;
    private User user;
    private String serverUrl;
    private int reseau = 0;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    private static MainApp instance;

    public MainApp() {
        instance = this;
    }

    public static MainApp getInstance() {
        return instance;
    }

    @Override
    public void start(Stage pstage) throws Exception {
        try {
            stage = pstage;
            stage.setOnCloseRequest(ev -> {
                try {
                    HttpUtil.shutdown();
                    Platform.exit();
                } catch (IOException e) {
                }
            });
            gotoLogin();
            stage.setResizable(false);
            stage.show();
        } catch (Exception ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public String getServerUrl() {
        if (reseau == 0) {
            serverUrl = PropertiesLoader.getProperty("local.server.url");
        } else {
            serverUrl = PropertiesLoader.getProperty("internet.server.url");
        }
        return serverUrl;
    }

    public User getUser() {
        return user;
    }

    public int getReseau() {
        return reseau;
    }

    public void setReseau(int reseau) {
        this.reseau = reseau;
    }

    public boolean userLogging(String userId, String password, int integrateur) throws RejectedExecutionException, NullPointerException, UnsupportedEncodingException, InterruptedException, ExecutionException, IOException, TimeoutException {
        if (Authenticator.validate(userId, password, integrateur)) {
            user = Authenticator.getUser();
            gotoMainWindow();
            return true;
        } else {
            return false;
        }
    }

    private void gotoLogin() {
        try {
            replaceSceneContent("/fxml/Login.fxml");
        } catch (Exception ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void gotoMainWindow() {
        try {
            replaceSceneContent("/fxml/MainWindow.fxml");
        } catch (Exception ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Parent replaceSceneContent(String fxml) throws Exception {
        System.out.println(fxml);
        URL url = getClass().getResource(fxml);
        System.out.println(url);
        FXMLLoader loader = new FXMLLoader(url);
        Parent page = loader.load();
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(page);
            scene.getStylesheets().add(MainApp.class.getResource("/styles/Styles.css").toExternalForm());
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(page);
        }
        stage.sizeToScene();
        stage.centerOnScreen();
        return page;
    }

}
