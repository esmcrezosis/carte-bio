/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esmc.mcnp.client;

import com.esmc.mcnp.client.dto.CarteInfo;
import com.esmc.mcnp.client.dto.CarteInfoBean;
import com.esmc.mcnp.client.dto.CarteRequest;
import com.esmc.mcnp.client.dto.ExceptionMessage;
import com.esmc.mcnp.client.runtime.BusinessHelper;
import com.esmc.mcnp.client.runtime.ClientProcessor;
import com.esmc.mcnp.client.runtime.ParamsPrint;
import com.esmc.mcnp.client.runtime.Print;
import com.esmc.mcnp.client.runtime.SerializationTools;
import com.esmc.mcnp.client.services.RestClient;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.StringUtils;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class PrinterWindowController implements Initializable {

	public String session;
	@FXML
	private DatePicker dateDebPicker;
	@FXML
	private DatePicker dateFinPicker;
	@FXML
	private TextField codeMembreTextField;
	@FXML
	private ComboBox<String> listeImpComboBox;
	@FXML
	private TableView<CarteInfoBean> carteTableView;

	@FXML
	private TableColumn<CarteInfoBean, String> codeMembreColumn;
	@FXML
	private TableColumn<CarteInfoBean, String> nomMembreColumn;
	@FXML
	private TableColumn<CarteInfoBean, String> prenomMembreColumn;
	@FXML
	private TableColumn<CarteInfoBean, String> dateDemandeColumn;
	@FXML
	private TableColumn<CarteInfoBean, String> contactColumn;
	@FXML
	private ImageView rectoViewImage;
	@FXML
	private ImageView versoViewImage;
	private BufferedImage bimg;
	private String versoPath;

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		dateDemandeColumn.setCellValueFactory(new PropertyValueFactory<CarteInfoBean, String>("dateDemande"));
		codeMembreColumn.setCellValueFactory(new PropertyValueFactory<CarteInfoBean, String>("codeMembre"));
		nomMembreColumn.setCellValueFactory(new PropertyValueFactory<CarteInfoBean, String>("nomMembre"));
		prenomMembreColumn.setCellValueFactory(new PropertyValueFactory<CarteInfoBean, String>("prenomMembre"));
		contactColumn.setCellValueFactory(new PropertyValueFactory<CarteInfoBean, String>("contact"));
		carteTableView.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends CarteInfoBean> observable, CarteInfoBean oldValue,
						CarteInfoBean newValue) -> {
					System.out.println(newValue.getCodeMembre());
					Image img = createImage(newValue);
					if (Objects.nonNull(img)) {
						try {
							rectoViewImage.setImage(loadImage(versoPath));
							versoViewImage.setImage(loadVersoImage());
						} catch (FileNotFoundException ex) {
							Logger.getLogger(PrinterWindowController.class.getName()).log(Level.SEVERE, null, ex);
						}
					}
				});
		supervisedPrinters();
	}

	private Image createImage(CarteInfoBean carteInfo) {
		if (Objects.nonNull(carteInfo)) {
			String path = System.getProperty("user.dir");
			try {
				try (ByteArrayInputStream bis = new ByteArrayInputStream(
						Base64.getDecoder().decode(carteInfo.getImage()))) {
					bimg = ImageIO.read(bis);
					File file = new File(path + "/" + carteInfo.getCodeMembre() + ".jpg");
					ImageIO.write(bimg, "jpg", file);
					versoPath = file.getAbsolutePath();
				}
				return SwingFXUtils.toFXImage(bimg, null);
			} catch (IOException ex) {
				Logger.getLogger(PrinterWindowController.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return null;
	}

	private Image loadVersoImage() {
		String name = getClass().getClassLoader().getResource("images/CarteESMCVERSONew.jpg").toExternalForm();
		return new Image(name, true);
	}

	private Image loadImage(String name) throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(name);
		return new Image(fis);
	}

	@FXML
	private void getSupervisedPrinters(ActionEvent e) {
		supervisedPrinters();
	}

	private void supervisedPrinters() {
		String[] res = BusinessHelper.setSupervisedPrinter(BusinessHelper.MODEL, "0");
		if (res.length > 0) {
			Arrays.asList(res).forEach(r -> System.out.print(r));
			ObservableList<String> data = FXCollections.observableArrayList();
			data.addAll(res);
			listeImpComboBox.itemsProperty().setValue(data);
		} else {
			System.out.println("Nothing");
		}
	}

	@FXML
	private void getPrinterState(ActionEvent e) {
		try {
			String res = "";
			String device = listeImpComboBox.getSelectionModel().getSelectedItem();
			if (StringUtils.isNotBlank(device)) {
				res = BusinessHelper.printerGetState(device);
			} else {
				res = BusinessHelper.printerGetState(BusinessHelper.MODEL);
			}
			if (StringUtils.isNotBlank(res)) {
				Arrays.asList(res).forEach(r -> System.out.print(r));
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Etat Imprimante:");
				if (StringUtils.isNotBlank(device)) {
				} else {
					alert.setHeaderText(device);
				}
				alert.setContentText(res);
				alert.showAndWait();
			} else {
				System.out.println("Nothing");
			}
		} catch (Exception ex) {
			Logger.getLogger(PrinterWindowController.class.getName()).log(Level.SEVERE, null, ex);
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Erreur:");
			alert.setHeaderText("Cannot send request !");
			alert.setContentText(ex.getMessage());
			alert.showAndWait();
		}
	}

	private static String Bmp2Base64(BufferedImage image) throws FileNotFoundException, IOException {
		if (image != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(image, "bmp", bos);
			byte[] bytes = bos.toByteArray();
			return Base64.getEncoder().encodeToString(bytes);
		} else {
			return null;
		}
	}

	private String printSetBitmap(Image bitmap, String faceStr, String bmptype) throws Exception {
		String id = "PRINT845";
		String base64 = Bmp2Base64(SwingFXUtils.fromFXImage(bitmap, null));
		ParamsPrint p = new ParamsPrint(faceStr, bmptype, "base64:" + base64, this.session);

		ClientProcessor.send(BusinessHelper.IP, BusinessHelper.Port, BusinessHelper.getCommType(),
				new Print("PRINT.SetBitmap", id, p));

		return String.valueOf(base64.length());
	}

	private void printBegin() throws Exception {
		String id = "PRINT562";
		ParamsPrint p = new ParamsPrint();
		String device = listeImpComboBox.getSelectionModel().getSelectedItem();
		p.setDevice(device);
		this.session = ClientProcessor.send(BusinessHelper.IP, BusinessHelper.Port, BusinessHelper.getCommType(),
				new Print("PRINT.Begin", id, p));
	}

	@FXML
	private void printBtnClick(ActionEvent e) {
		try {
			if (rectoViewImage.getImage() != null && versoViewImage.getImage() != null) {
				printBegin();
				String pdata = "BColorBrightness=VAL10;BColorContrast=VAL10;BHalftoning=THRESHOLD;BMonochromeContrast=VAL10;BOverlayContrast=VAL10;BOverlayManagement=FULLVARNISH;BPageRotate180=OFF;FColorBrightness=VAL10;FColorContrast=VAL10;FHalftoning=THRESHOLD;FMonochromeContrast=VAL10;FOverlayContrast=VAL10;FPageRotate180=OFF;Duplex=NONE;GInputTray=FEEDER;GOutputTray=HOPPER;GPipeDetection=OFF;GRejectBox=DEFAULTREJECT;GRibbonType=RC_YMCKO;GSmoothing=ADVSMOOTH;IGSendIQLA=OFF";

				String id = "PRINT224";
				ParamsPrint p = new ParamsPrint();
				p.setData(pdata);
				p.setSession(this.session);

				ClientProcessor.send(BusinessHelper.IP, BusinessHelper.Port, BusinessHelper.getCommType(),
						new Print("PRINT.Set", id, p));
				printSetBitmap(rectoViewImage.getImage(), "front", "bmp");
				// printSetBitmap(versoViewImage.getImage(), "back", "bmp");
			}
		} catch (Exception ex) {
			Logger.getLogger(PrinterWindowController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	private void printCarte(ActionEvent e) {
		String id = "PRINT866";
		ParamsPrint p = new ParamsPrint();
		p.setSession(this.session);

		try {
			ClientProcessor.send(BusinessHelper.IP, BusinessHelper.Port, BusinessHelper.getCommType(),
					new Print("PRINT.Print", id, p));
		} catch (Exception ex) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Erreur:");
			alert.setHeaderText("Cannot send request !");
			alert.setContentText(ex.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	private void printEnd() {
		String id = "PRINT142";
		ParamsPrint p = new ParamsPrint();
		p.setSession(this.session);
		try {
			ClientProcessor.send(BusinessHelper.IP, BusinessHelper.Port, BusinessHelper.getCommType(),
					new Print("PRINT.End", id, p));
		} catch (Exception ex) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("Envoie des commandes au serveur d'impression");
			alert.setContentText(ex.getMessage());
			alert.showAndWait();
		}
	}

	@SuppressWarnings("unchecked")
	@FXML
	private void searchBtnClick(ActionEvent e) {
		LocalDate dateDeb = dateDebPicker.getValue();
		LocalDate dateFin = dateFinPicker.getValue();
		String codeMembre = codeMembreTextField.getText();
		CarteRequest request = new CarteRequest(codeMembre, dateDeb, dateFin, false);
		String jrequest = SerializationTools.jsonSerialise(request);
		String json;
		try {
			json = RestClient.executePost("http://localhost:8080/mcnp-api/carte/listInfo/", jrequest, MainApp.getInstance().getUser().getUserName(), MainApp.getInstance().getUser().getPassword());
			int status = RestClient.status;
			if (status == 200) {
				List<CarteInfo> cartes = (List<CarteInfo>) SerializationTools.jsonListDeserialise(json,
						CarteInfo.class);
				if (Objects.nonNull(cartes) && cartes.size() > 0) {
					System.out.println("Nombre de cartes = " + cartes.size());
					List<CarteInfoBean> items = new ArrayList<>();
					cartes.forEach(c -> items.add(c.convertToBean()));
					System.out.println("Nombre de cartes BIS = " + items.size());
					ObservableList<CarteInfoBean> beans = FXCollections.observableArrayList(items);
					// FXCollections.copy(beans, items);
					carteTableView.setItems(beans);
					carteTableView.refresh();
				} else {
				}
			} else {
				ExceptionMessage message = (ExceptionMessage) SerializationTools.jsonDeserialise(json,
						ExceptionMessage.class);
				System.out.println(message.getMessage());
			}
		} catch (MalformedURLException | UnsupportedEncodingException ex) {
			Logger.getLogger(PrinterWindowController.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

}
