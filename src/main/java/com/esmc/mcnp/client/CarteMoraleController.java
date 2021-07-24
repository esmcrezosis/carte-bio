package com.esmc.mcnp.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.esmc.mcnp.client.dto.Carte;
import com.esmc.mcnp.client.dto.FileUtils;
import com.esmc.mcnp.client.dto.InfoMembre;
import com.esmc.mcnp.client.dto.InfoMembreBean;
import com.esmc.mcnp.client.dto.InfoMembreMorale;
import com.esmc.mcnp.client.dto.Result;
import com.esmc.mcnp.client.image.QRCodeUtils;
import com.esmc.mcnp.client.runtime.SerializationTools;
import com.esmc.mcnp.client.services.AsyncRestClient;
import com.esmc.mcnp.client.services.CustomPair;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CarteMoraleController implements Initializable {

	@FXML
	private Label label;
	@FXML
	private Button btnValider;
	@FXML
	private Button btnGenQrcode;
	@FXML
	private Button btnReadQrcode;
	@FXML
	private Button btnCreateCarte;
	@FXML
	private Button quitBtn;
	@FXML
	private ImageView imgViewVerso;
	@FXML
	private ImageView qrcodeImageView;
	@FXML
	private TextField txtCodeMembre;
	@FXML
	private TextField txtRaisonsociale;
	@FXML
	private TextField txtCelMembre;
	@FXML
	private TextField txtTelMembre;
	@FXML
	private TextField txtEmailMembre;
	@FXML
	private TableView<InfoMembreBean> repTableView;
	@FXML
	private TableColumn<InfoMembreBean, String> codeRepColumn;
	@FXML
	private TableColumn<InfoMembreBean, String> nomRepColumn;
	@FXML
	private TableColumn<InfoMembreBean, String> prenomRepColumn;
	@FXML
	private TableColumn<InfoMembreBean, String> telRepColumn;
	@FXML
	private TableColumn<InfoMembreBean, Boolean> princRepColumn;

	private static InfoMembreMorale infoMembre;
	private boolean modifier = false;
	private boolean creer = false;

	private void handleBtnVersoAction() {
		String name = getClass().getClassLoader().getResource("images/CarteESMCRECTO.jpg").toExternalForm();
		Image img = new Image(name, true);
		imgViewVerso.setImage(img);
	}

	private void getMembreMoraleAction(String codeMembre) throws MalformedURLException {
		if (codeMembre.length() == 20 && (codeMembre.endsWith("M"))) {
			Task<CustomPair> membreTask = new Task<CustomPair>() {
				@Override
				protected CustomPair call() throws Exception {
					return AsyncRestClient.executeGet("carte/morale/" + codeMembre);
				}
			};
			membreTask.setOnSucceeded(em -> {
				CustomPair res = membreTask.getValue();
				infoMembre = (InfoMembreMorale) SerializationTools.jsonDeserialise(res.getValue(),
						InfoMembreMorale.class);
				if (Objects.nonNull(infoMembre)) {
					txtCodeMembre.setText(infoMembre.getCodeMembre());
					txtRaisonsociale.setText(infoMembre.getRaisonSociale());
					txtCelMembre.setText(infoMembre.getCel());
					txtTelMembre.setText(infoMembre.getTelephone());
					txtEmailMembre.setText(infoMembre.getEmail());

					List<InfoMembreBean> infos = new ArrayList<>();
					infoMembre.getMembres().forEach(m -> {
						infos.add(m.convertToBean());
					});
					ObservableList<InfoMembreBean> beans = FXCollections.observableArrayList(infos);
					// FXCollections.copy(beans, items);
					repTableView.setItems(beans);
					repTableView.refresh();
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

	private void getMembreAction(String codeMembre) throws MalformedURLException {
		if (codeMembre.length() == 20 && (codeMembre.endsWith("P"))) {
			Task<CustomPair> membreTask = new Task<CustomPair>() {
				@Override
				protected CustomPair call() throws Exception {
					return AsyncRestClient.executeGet("carte/membre/" + codeMembre);
				}
			};
			membreTask.setOnSucceeded(em -> {
				CustomPair res = membreTask.getValue();
				InfoMembre infoMembre = (InfoMembre) SerializationTools.jsonDeserialise(res.getValue(),
						InfoMembre.class);
				if (Objects.nonNull(infoMembre)) {
					addPrincipal(infoMembre);
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Erreur");
					alert.setHeaderText("Code Membre");
					alert.setContentText("Le Code Membre " + codeMembre + " Non trouvé");
					alert.showAndWait();
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

	public void codeMembreChangedAction(ActionEvent e) {
		try {
			getMembreMoraleAction(txtCodeMembre.getText());
		} catch (MalformedURLException ex) {
			Logger.getLogger(CarteMoraleController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public InfoMembreBean getPrincipal() {
		ObservableList<InfoMembreBean> beans = repTableView.getItems();
		InfoMembreBean princ = beans.stream().filter(b -> b.getPrincipal()).findFirst().get();
		return princ;
	}

	public boolean isMembreExist(ObservableList<InfoMembreBean> beans, InfoMembreBean bean) {
		return beans.contains(bean);
	}

	public void addPrincipal(InfoMembre membre) {
		ObservableList<InfoMembreBean> items = repTableView.getItems();
		InfoMembreBean bean = membre.convertToBean();
		if (!isMembreExist(items, bean)) {
			InfoMembreBean princ = getPrincipal();
			princ.setPrincipal(false);
			bean.setPrincipal(true);
			items.add(bean);
			repTableView.setItems(items);
			repTableView.refresh();
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Ajout de Principal");
			alert.setContentText("Le Code Membre " + membre.getCodeMembre()
					+ " est déjà représentant. \n Veuillez le selectionner dans la liste des représentants comme principal");
			alert.showAndWait();
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		repTableView.setEditable(true);
		codeRepColumn.setCellValueFactory(new PropertyValueFactory<InfoMembreBean, String>("codeMembre"));
		nomRepColumn.setCellValueFactory(new PropertyValueFactory<InfoMembreBean, String>("nomMembre"));
		prenomRepColumn.setCellValueFactory(new PropertyValueFactory<InfoMembreBean, String>("prenomMembre"));
		telRepColumn.setCellValueFactory(new PropertyValueFactory<InfoMembreBean, String>("telephone"));
		princRepColumn.setCellValueFactory(new PropertyValueFactory<InfoMembreBean, Boolean>("principal"));
		princRepColumn.setCellFactory(CheckBoxTableCell.forTableColumn(princRepColumn));
		princRepColumn.setOnEditCommit(event -> repTableView.getItems().get(event.getTablePosition().getRow())
				.setPrincipal(event.getNewValue()));

		txtRaisonsociale.textProperty().addListener((obs, oldVal, newVal) -> {
			if (StringUtils.isNotBlank(oldVal) && !oldVal.equalsIgnoreCase(newVal)) {
				modifier = true;
			}
		});
		txtCelMembre.textProperty().addListener((obs, oldVal, newVal) -> {
			if (StringUtils.isNotBlank(oldVal) && !oldVal.equalsIgnoreCase(newVal)) {
				modifier = true;
			}
		});
		txtTelMembre.textProperty().addListener((obs, oldVal, newVal) -> {
			if (StringUtils.isNotBlank(oldVal) && !oldVal.equalsIgnoreCase(newVal)) {
				modifier = true;
			}
		});
		txtEmailMembre.textProperty().addListener((obs, oldVal, newVal) -> {
			if (StringUtils.isNotBlank(oldVal) && !oldVal.equalsIgnoreCase(newVal)) {
				modifier = true;
			}
		});
		handleBtnVersoAction();
	}

	@FXML
	private void addPrincSaisie(ActionEvent e) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Ajout de Représentant principal");
		dialog.setHeaderText("Entrer le code Membre du représentant :");
		dialog.setContentText("Code Membre : ");

		Optional<String> result = dialog.showAndWait();
		result.ifPresent(code -> {
			try {
				getMembreAction(code);
			} catch (MalformedURLException e1) {
				Logger.getLogger(CarteMoraleController.class.getName()).log(Level.SEVERE, null, e1);
			}
		});
	}

	@FXML
	private void addPincQrcode(ActionEvent e) {
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
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(CarteMoraleController.class.getName()).log(Level.SEVERE, null, ex);
		}
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
					getMembreMoraleAction(codeMembre);
				} else {
					qrcodeController.onClose();
				}
			} else {
				qrcodeController.onClose();
			}
		} catch (IOException ex) {
			Logger.getLogger(CarteMoraleController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	private void createCarte(ActionEvent e) {
		if ((Objects.nonNull(imgViewVerso.getImage()) && Objects.nonNull(qrcodeImageView.getImage()))
				&& !infoMembre.isDoublon() && infoMembre.isAutoriser()) {
			Image baseImage = imgViewVerso.getImage();
			// Image photo = photoViewImage.getImage();
			Image qrCode = qrcodeImageView.getImage();
			PixelReader reader = baseImage.getPixelReader();
			Canvas canvas = new Canvas(baseImage.getWidth(), baseImage.getHeight());
			GraphicsContext gc = canvas.getGraphicsContext2D();
			gc.drawImage(baseImage, 0, 0, baseImage.getWidth(), baseImage.getHeight());
			gc.drawImage(qrCode, 15, 270, qrCode.getWidth(), qrCode.getHeight());
			gc.setFill(Color.WHITE);
			gc.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
			gc.setTextAlign(TextAlignment.CENTER);
			gc.setTextBaseline(VPos.CENTER);
			gc.fillText("CARTE D'ACCES AU COMPTE MARCHAND", Math.round(canvas.getWidth() / 2), 70);
			gc.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
			gc.fillText(txtRaisonsociale.getText(), Math.round(canvas.getWidth() / 2), 120);
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
			} else if (!infoMembre.isAutoriser()) {
				alert.setContentText("Ce membre n'a pas assez de nouveaux BAn!!!");
			} else {
				alert.setContentText(
						"Soyons concentrer sur ce que nous faisons:\nVeuillez prendre la photo du membre et créer son QR CODE \navant de créer la carte!!!");
			}
			alert.showAndWait();
		}

	}

	@FXML
	private void generateQrcode(ActionEvent e) {
		if (infoMembre.isAutoriser()) {
			Task<BufferedImage> qrcodeTask = new Task<BufferedImage>() {
				@Override
				protected BufferedImage call() throws Exception {
					String qrText = txtCodeMembre.getText();
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
			alert.setContentText("Ce membre n'a pas assez de nouveaux BAn!!!");
			alert.showAndWait();
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
						BufferedImage qrCodeImg = SwingFXUtils.fromFXImage(qrcodeImageView.getImage(), null);
						Carte carte = new Carte();
						carte.setCodeMembre(txtCodeMembre.getText());
						carte.setDateDemande(LocalDate.now());
						carte.setRaisonSociale(txtRaisonsociale.getText());
						carte.setCel(txtCelMembre.getText());
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
						if (getPrincipal() != null) {
							carte.setCodeRep(getPrincipal().getCodeMembre());
						}
						carte.setPhoto(null);
						carte.setQrCode(Base64.getEncoder()
								.encodeToString(FileUtils.toByteArrayAutoClosable(qrCodeImg, "png")));
						carte.setImage(
								Base64.getEncoder().encodeToString(FileUtils.toByteArrayAutoClosable(img, "png")));
						String json = SerializationTools.jsonSerialise(carte);
						CustomPair res = AsyncRestClient.executePost("carte/creerMorale/", json);
						Result result = (Result) SerializationTools.jsonDeserialise(res.getValue(), Result.class);
						return result;
					} catch (RejectedExecutionException | NullPointerException | InterruptedException
							| ExecutionException | IOException ex) {
						Logger.getLogger(CarteMoraleController.class.getName()).log(Level.SEVERE, null, ex);
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("ERROR");
						alert.setHeaderText("Erreur de sauvegarde de la carte");
						alert.setContentText(ex.getMessage());
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
				resetWindow(e);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				alert.setHeaderText("Opération de sauvegarde de la carte");
				alert.setContentText(res.getMessage());
				alert.showAndWait();
			}
		});
		new Thread(sendCarteTask).start();
	}

	@FXML
	private void closeWindow(ActionEvent e) {
		Stage stage = (Stage) quitBtn.getScene().getWindow();
		stage.close();
	}

	@FXML
	private void resetWindow(ActionEvent e) {
		txtCodeMembre.setText(null);
		txtRaisonsociale.setText(null);
		txtCelMembre.setText(null);
		txtTelMembre.setText(null);
		txtEmailMembre.setText(null);
		qrcodeImageView.setImage(null);
		imgViewVerso.setImage(null);
		repTableView.getItems().clear();
		repTableView.refresh();
		creer = false;
		handleBtnVersoAction();
	}

}
