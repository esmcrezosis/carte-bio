package com.esmc.mcnp.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.esmc.mcnp.client.dto.Carte;
import com.esmc.mcnp.client.dto.DateUtility;
import com.esmc.mcnp.client.dto.FileUtils;
import com.esmc.mcnp.client.dto.InfoMembre;
import com.esmc.mcnp.client.dto.Result;
import com.esmc.mcnp.client.image.QRCodeUtils;
import com.esmc.mcnp.client.runtime.SerializationTools;
import com.esmc.mcnp.client.services.RestClient;

import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CarteController implements Initializable {

	@FXML
	private Label label;
	@FXML
	private Button btnValider;
	@FXML
	private Button btnGenQrcode;
	@FXML
	private Button btnReadQrcode;
	@FXML
	private Button btnPhoto;
	@FXML
	private Button quitBtn;
	@FXML
	private ImageView imgViewVerso;
	@FXML
	private ImageView photoViewImage;
	@FXML
	private ImageView qrcodeImageView;
	@FXML
	private TextField txtCodeMembre;
	@FXML
	private TextField txtNomMembre;
	@FXML
	private TextField txtPrenomMembre;
	@FXML
	private DatePicker dtDateNaisMembre;
	@FXML
	private TextField txtLieuNaisMembre;
	@FXML
	private TextField txtTelMembre;
	@FXML
	private TextField txtEmailMembre;

	private static InfoMembre infoMembre;
	private boolean modifier = false;
	private boolean creer = false;

	private void handleBtnVersoAction() {
		String name = getClass().getClassLoader().getResource("images/CarteESMCRECTO.jpg").toExternalForm();
		Image img = new Image(name, true);
		imgViewVerso.setImage(img);
	}

	@FXML
	private void readQrcode(ActionEvent e) {
		try {
			final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/QrCodeReader.fxml"));
			final Parent root = loader.load();
			final Scene scene = new Scene(root, 400, 400);
			QrCodeReaderController qrcodeController = loader.getController();
			Stage stage = new Stage();
			stage.setTitle("Prendre une Photo");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnReadQrcode.getScene().getWindow());
			stage.centerOnScreen();
			stage.setScene(scene);
			stage.setResizable(false);
			qrcodeController.init();
			qrcodeController.setParentStage(stage);
			stage.showAndWait();
			String result = qrcodeController.getResult();
			if (StringUtils.isNotBlank(result)) {
				String[] codes = result.split(",");
				if (codes.length > 0) {
					String codeMembre = codes[0];
					getMembreAction(codeMembre);
				} else {
					qrcodeController.onClose();
				}
			} else {
				qrcodeController.onClose();
			}
		} catch (IOException ex) {
			Logger.getLogger(CarteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	private void createCarte(ActionEvent e) {
		if ((Objects.nonNull(imgViewVerso.getImage()) && Objects.nonNull(photoViewImage.getImage())
				&& Objects.nonNull(qrcodeImageView.getImage())) && !infoMembre.isDoublon()
				&& infoMembre.isAutoriser()) {
			Image baseImage = imgViewVerso.getImage();
			Image photo = photoViewImage.getImage();
			Image qrCode = qrcodeImageView.getImage();
			PixelReader reader = baseImage.getPixelReader();
			Canvas canvas = new Canvas(baseImage.getWidth(), baseImage.getHeight());
			GraphicsContext gc = canvas.getGraphicsContext2D();
			gc.drawImage(baseImage, 0, 0, baseImage.getWidth(), baseImage.getHeight());
			gc.drawImage(photo, 15, 15, photo.getWidth(), photo.getHeight());
			gc.drawImage(qrCode, 15, 30 + photo.getHeight(), qrCode.getWidth(), qrCode.getHeight());
			// gc.save();
			gc.setFill(Color.WHITE);
			gc.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
			gc.fillText("CARTE D'ACCES AU COMPTE MARCHAND", photo.getWidth() + 40, 70);

			gc.setFont(Font.font("Consolas", FontWeight.BOLD, 40));
			gc.fillText("Nom     :" + txtNomMembre.getText(), photo.getWidth() + 40, 120);
			gc.fillText("Prénoms :" + txtPrenomMembre.getText(), photo.getWidth() + 40, 180);
			// gc.restore();
			final WritableImage writableImage = new WritableImage(reader, (int) canvas.getWidth(),
					(int) canvas.getHeight());
			final WritableImage snapshot = canvas.snapshot(new SnapshotParameters(), writableImage);
			imgViewVerso.setImage(snapshot);
			creer = true;
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Opérations Non Autorisée");
			if (infoMembre.isDoublon()) {
				alert.setContentText("Ce membre a déjà effectué une demande de carte!!!");
			} else {
				alert.setContentText(
						"Soyons concentrer sur ce que nous faisons:\nVeuillez prendre la photo du membre et créer son QR CODE \navant de créer la carte!!!");
			}
			alert.showAndWait();
		}
	}

	@FXML
	private void generateQrcode(ActionEvent e) {
		if (infoMembre.isAutoriser() && !infoMembre.isDoublon()) {
			Task<BufferedImage> qrcodeTask = new Task<BufferedImage>() {
				@Override
				protected BufferedImage call() throws Exception {
					String qrText = txtCodeMembre.getText();
					System.out.println("Code Membre : " + qrText);
					if (qrText != null && !qrText.equals("")) {
						BufferedImage image = QRCodeUtils.generateQrcode(txtCodeMembre.getText(), 347, 320);
						return image;
					} else {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Information");
						alert.setHeaderText("Manque de renseignements");
						alert.setContentText("Veuillez saisir le Code du Membre!");
						alert.showAndWait();
						return null;
					}
				}
			};
			qrcodeTask.setOnSucceeded(
					ev -> qrcodeImageView.setImage(SwingFXUtils.toFXImage(qrcodeTask.getValue(), null)));
			new Thread(qrcodeTask).start();

		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Opérations Non Autorisée");
			if (infoMembre.isDoublon()) {
				alert.setContentText("Ce membre a déjà effectué une demande de carte!!!");
			} else {
				alert.setContentText("Ce membre n'a pas assez de nouveaux BAn!!!");
			}
			alert.showAndWait();
		}

	}

	@FXML
	private void handleBtnPhotoAction(ActionEvent event) {
		if (Objects.nonNull(infoMembre) && infoMembre.isAutoriser() && !infoMembre.isDoublon()) {
			try {
				final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PhotoDialog.fxml"));
				final Parent root = loader.load();
				final Scene scene = new Scene(root, 450, 400);
				PhotoDialogController dialogController = loader.getController();
				Stage stage = new Stage();
				stage.setTitle("Prendre une Photo");
				stage.initModality(Modality.WINDOW_MODAL);
				stage.initOwner(photoViewImage.getScene().getWindow());
				stage.centerOnScreen();
				stage.setScene(scene);
				stage.showAndWait();
				photoViewImage.setImage(dialogController.getImage());
			} catch (IOException ex) {
				Logger.getLogger(CarteController.class.getName()).log(Level.SEVERE, null, ex);
			}

		} else {
			if (Objects.isNull(infoMembre)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText("Les informations du Membre ne sont pas encore disponible");
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				alert.setHeaderText("Opérations Non Autorisée");
				if (infoMembre.isDoublon()) {
					alert.setContentText("Ce membre a déjà effectué une demande de carte!!!");
				} else {
					alert.setContentText("Ce membre n'a pas assez de nouveaux BAn!!!");
				}
				alert.showAndWait();
			}
		}

	}

	@FXML
	private void sendCarte(ActionEvent e) {
		Task<Result> sendCarteTask = new Task<Result>() {
			@Override
			protected Result call() throws Exception {
				if (imgViewVerso.getImage() != null && creer) {
					try {
						BufferedImage img = SwingFXUtils.fromFXImage(imgViewVerso.getImage(), null);
						BufferedImage photo = SwingFXUtils.fromFXImage(photoViewImage.getImage(), null);
						BufferedImage qrCodeImg = SwingFXUtils.fromFXImage(qrcodeImageView.getImage(), null);
						Carte carte = new Carte();
						carte.setCodeMembre(txtCodeMembre.getText());
						carte.setDateDemande(LocalDate.now());
						carte.setDateNais(dtDateNaisMembre.getValue());
						carte.setLieuNais(txtLieuNaisMembre.getText());
						carte.setNomMmebre(txtNomMembre.getText());
						carte.setPrenomMembre(txtPrenomMembre.getText());
						carte.setTelephone(txtTelMembre.getText());
						carte.setEmail(txtEmailMembre.getText());
						carte.setDateImpression(null);
						carte.setDateLivraison(null);
						carte.setIdUtilisateur(null);
						carte.setLivrer(false);
						carte.setImprimer(false);
						carte.setModifier(modifier);
						carte.setIdUtilisateur(MainApp.getInstance().getUser().getId());
						carte.setUserType(MainApp.getInstance().getUser().getUserType());
						carte.setPhoto(
								Base64.getEncoder().encodeToString(FileUtils.toByteArrayAutoClosable(photo, "png")));
						carte.setQrCode(Base64.getEncoder()
								.encodeToString(FileUtils.toByteArrayAutoClosable(qrCodeImg, "png")));
						carte.setImage(
								Base64.getEncoder().encodeToString(FileUtils.toByteArrayAutoClosable(img, "png")));
						String json = SerializationTools.jsonSerialise(carte);
						String res = RestClient.executePost(MainApp.getInstance().getServerUrl() + "carte/creer/", json,
								MainApp.getInstance().getUser().getUserName(),
								MainApp.getInstance().getUser().getPassword());
						Result result = (Result) SerializationTools.jsonDeserialise(res, Result.class);
						return result;
					} catch (IOException ex) {
						Logger.getLogger(CarteController.class.getName()).log(Level.SEVERE, null, ex);
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Information");
						alert.setHeaderText("Opération de sauvegarde de la carte");
						alert.setContentText("Pas de carte à sauvegarde");
						alert.showAndWait();
						return null;
					}
				} else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information");
					alert.setHeaderText("Opération de sauvegarde de la carte");
					alert.setContentText("Pas de carte à sauvegarde");
					alert.showAndWait();
					return null;
				}
			}
		};
		sendCarteTask.setOnSucceeded(ev -> {
			Result res = sendCarteTask.getValue();
			if (Objects.nonNull(res)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				alert.setHeaderText("Opération de sauvegarde de la carte");
				alert.setContentText(res.getMessage());
				alert.showAndWait();
				if (res.getResultat() == 1) {
					resetWindow(e);
				}
			}
		});
		new Thread(sendCarteTask).start();
	}

	private void getMembreAction(String codeMembre) throws MalformedURLException {
		String login = MainApp.getInstance().getUser().getUserName();
		String password = MainApp.getInstance().getPassword();
		System.out.println("Mot de passe = " + password);
		if (codeMembre.length() == 20 && (codeMembre.endsWith("P") || codeMembre.endsWith("M"))) {
			Task<String> membreTask = new Task<String>() {
				@Override
				protected String call() throws Exception {
					return RestClient.executeGet(MainApp.getInstance().getServerUrl() + "carte/membre/" + codeMembre,
							login, password);
				}
			};
			membreTask.setOnSucceeded(em -> {
				String res = membreTask.getValue();
				System.out.println("json = " + res);
				infoMembre = (InfoMembre) SerializationTools.jsonDeserialise(res, InfoMembre.class);
				if (Objects.nonNull(infoMembre)) {
					txtCodeMembre.setText(infoMembre.getCodeMembre());
					txtNomMembre.setText(infoMembre.getNomMembre());
					txtPrenomMembre.setText(infoMembre.getPrenomMembre());
					txtLieuNaisMembre.setText(infoMembre.getLieuNaissance());
					txtTelMembre.setText(infoMembre.getTelephone());
					txtEmailMembre.setText(infoMembre.getEmail());
					dtDateNaisMembre.setValue(DateUtility.asLocalDate(infoMembre.getDateNaissance()));
				}
			});
			new Thread(membreTask).start();
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Information");
			alert.setHeaderText("Numéro Membre");
			alert.setContentText("Le Code Membre " + codeMembre + " est incorrect");
			alert.showAndWait();
		}

	}

	@FXML
	private void closeWindow(ActionEvent e) {
		Stage stage = (Stage) quitBtn.getScene().getWindow();
		creer = false;
		stage.close();
	}

	@FXML
	private void resetWindow(ActionEvent e) {
		txtCodeMembre.setText(null);
		txtNomMembre.setText(null);
		txtPrenomMembre.setText(null);
		txtLieuNaisMembre.setText(null);
		txtTelMembre.setText(null);
		txtEmailMembre.setText(null);
		dtDateNaisMembre.setValue(LocalDate.now());
		photoViewImage.setImage(null);
		qrcodeImageView.setImage(null);
		imgViewVerso.setImage(null);
		creer = false;
		handleBtnVersoAction();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		txtLieuNaisMembre.textProperty().addListener((obs, oldVal, newVal) -> {
			if (!oldVal.equalsIgnoreCase(newVal)) {
				modifier = true;
			}
		});
		txtNomMembre.textProperty().addListener((obs, oldVal, newVal) -> {
			if (!oldVal.equalsIgnoreCase(newVal)) {
				modifier = true;
			}
		});
		txtPrenomMembre.textProperty().addListener((obs, oldVal, newVal) -> {
			if (!oldVal.equalsIgnoreCase(newVal)) {
				modifier = true;
			}
		});
		txtTelMembre.textProperty().addListener((obs, oldVal, newVal) -> {
			if (!oldVal.equalsIgnoreCase(newVal)) {
				modifier = true;
			}
		});

		dtDateNaisMembre.setOnAction((ActionEvent event) -> {
			modifier = true;
		});
		txtEmailMembre.textProperty().addListener((obs, oldVal, newVal) -> {
			if (!oldVal.equalsIgnoreCase(newVal)) {
				modifier = true;
			}
		});
		txtCodeMembre.setOnAction((ActionEvent event) -> {
			try {
				getMembreAction(txtCodeMembre.getText());
			} catch (MalformedURLException e) {
				Logger.getLogger(CarteController.class.getName()).log(Level.SEVERE, null, e);
			}
		});
		handleBtnVersoAction();
	}
}
