package com.esmc.mcnp.client;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.esmc.mcnp.client.dto.CarteInfo;
import com.esmc.mcnp.client.dto.CarteInfoBean;
import com.esmc.mcnp.client.dto.CarteRequest;
import com.esmc.mcnp.client.dto.ExceptionMessage;
import com.esmc.mcnp.client.dto.Result;
import com.esmc.mcnp.client.dto.User;
import com.esmc.mcnp.client.runtime.SerializationTools;
import com.esmc.mcnp.client.services.AsyncRestClient;
import com.esmc.mcnp.client.services.CustomPair;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

public class LivraisonController implements Initializable {
	@FXML
	private DatePicker dateDebPicker;
	@FXML
	private DatePicker dateFinPicker;
	@FXML
	private TextField codeMembreTextField;
	@FXML
	private TableColumn<CarteInfoBean, String> idUserColumn;
	@FXML
	private TableColumn<CarteInfoBean, String> dateDemandeColumn;
	@FXML
	private TableColumn<CarteInfoBean, String> codeMembreColumn;
	@FXML
	private TableColumn<CarteInfoBean, String> nomMembreColumn;
	@FXML
	private TableColumn<CarteInfoBean, String> prenomMembreColumn;
	@FXML
	private TableColumn<CarteInfoBean, Boolean> imprimerColumn;
	@FXML
	private TableView<CarteInfoBean> tableCarte;

	@FXML
	private DatePicker dateDebPicker1;
	@FXML
	private DatePicker dateFinPicker1;
	@FXML
	private TextField codeMembreTextField1;
	@FXML
	private TableColumn<CarteInfoBean, String> idUserColumn1;
	@FXML
	private TableColumn<CarteInfoBean, String> dateDemandeColumn1;
	@FXML
	private TableColumn<CarteInfoBean, String> codeMembreColumn1;
	@FXML
	private TableColumn<CarteInfoBean, String> nomMembreColumn1;
	@FXML
	private TableColumn<CarteInfoBean, String> prenomMembreColumn1;
	@FXML
	private TableColumn<CarteInfoBean, Boolean> imprimerColumn1;
	@FXML
	private TableView<CarteInfoBean> tableCarte1;

	private ObservableList<CarteInfoBean> carteInfos;
	private final User user = MainApp.getInstance().getUser();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initTableCarte();
		initTableCarte1();
	}

	private void initTableCarte() {
		idUserColumn.setCellValueFactory(new PropertyValueFactory<>("idUser"));
		dateDemandeColumn.setCellValueFactory(new PropertyValueFactory<>("dateDemande"));
		codeMembreColumn.setCellValueFactory(new PropertyValueFactory<>("codeMembre"));
		nomMembreColumn.setCellValueFactory(new PropertyValueFactory<>("nomMembre"));
		prenomMembreColumn.setCellValueFactory(new PropertyValueFactory<>("prenomMembre"));
		imprimerColumn.setCellValueFactory(new PropertyValueFactory<>("imprimer"));
		imprimerColumn.setCellFactory(CheckBoxTableCell.forTableColumn(imprimerColumn));
		tableCarte.setRowFactory(e -> {
			TableRow<CarteInfoBean> row = new TableRow<>();
			row.setOnMouseClicked(ev -> {
				if (ev.getClickCount() == 2 && !row.isEmpty()) {
					CarteInfoBean var = row.getItem();
					CarteInfo carte = DialogUtils.showInfoCarte(var.convertToInto());
					if (Objects.nonNull(carte)) {
						try {
							CustomPair answer = AsyncRestClient.executePost("carte/updateCarte",
									SerializationTools.jsonSerialise(carte));
							if (answer.getKey() == 200) {
								Result result = (Result) SerializationTools.jsonDeserialise(answer.getValue(),
										Result.class);
								carteInfos.remove(row.getIndex());
								tableCarte.setItems(carteInfos);
								tableCarte.refresh();
								DialogUtils.showInfoDialog("Mise à Jour de Carte", result.getMessage());
							} else {
								ExceptionMessage message = (ExceptionMessage) SerializationTools
										.jsonDeserialise(answer.getValue(), ExceptionMessage.class);
								DialogUtils.errorDialog("Mise à jour de la Carte", message.getMessage());
							}
						} catch (RejectedExecutionException | NullPointerException | InterruptedException
								| ExecutionException | IOException | TimeoutException ex) {
							DialogUtils.showErrorDialog(ex);
						}
					}
				}
			});
			return row;
		});
	}

	private void initTableCarte1() {
		// tableCarte1.setEditable(true);
		idUserColumn1.setCellValueFactory(new PropertyValueFactory<>("idUser"));
		dateDemandeColumn1.setCellValueFactory(new PropertyValueFactory<>("dateDemande"));
		codeMembreColumn1.setCellValueFactory(new PropertyValueFactory<>("codeMembre"));
		nomMembreColumn1.setCellValueFactory(new PropertyValueFactory<>("nomMembre"));
		prenomMembreColumn1.setCellValueFactory(new PropertyValueFactory<>("prenomMembre"));
		imprimerColumn1.setCellValueFactory(new PropertyValueFactory<>("imprimer"));
		imprimerColumn1.setCellFactory(CheckBoxTableCell.forTableColumn(imprimerColumn));
	}

	@FXML
	@SuppressWarnings("unchecked")
	private void searchBtnClick(ActionEvent e) {
		LocalDate dateDeb = dateDebPicker.getValue();
		LocalDate dateFin = dateFinPicker.getValue();
		String codeMembre = codeMembreTextField.getText();
		CarteRequest request = new CarteRequest(codeMembre, dateDeb, dateFin, false, "liv", user.getId());
		request.setUserType(user.getUserType());
		String jrequest = SerializationTools.jsonSerialise(request);
		CustomPair res;
		try {
			res = AsyncRestClient.executePost("carte/listInfo/", jrequest);
			if (res.getKey() == 200) {
				List<CarteInfo> cartes = (List<CarteInfo>) SerializationTools.jsonListDeserialise(res.getValue(),
						CarteInfo.class);
				if (Objects.nonNull(cartes) && cartes.size() > 0) {
					List<CarteInfoBean> items = new ArrayList<>();
					cartes.forEach(c -> items.add(c.convertToBean()));
					carteInfos = FXCollections.observableArrayList(items);
					tableCarte.setItems(carteInfos);
					tableCarte.refresh();
				} else {
					DialogUtils.errorDialog("Chargement des demandes de Cartes",
							"Pas de demandes de cartes correspondantes");
				}
			} else {
				ExceptionMessage message = (ExceptionMessage) SerializationTools.jsonDeserialise(res.getValue(),
						ExceptionMessage.class);
				DialogUtils.errorDialog("Mise à jour de la Carte", message.getMessage());
			}
		} catch (RejectedExecutionException | NullPointerException | InterruptedException | ExecutionException
				| IOException | TimeoutException ex) {
			Logger.getLogger(PrinterWindowController.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	@FXML
	@SuppressWarnings("unchecked")
	private void searchImpBtnClick(ActionEvent e) {
		LocalDate dateDeb = dateDebPicker1.getValue();
		LocalDate dateFin = dateFinPicker1.getValue();
		String codeMembre = codeMembreTextField1.getText();
		CarteRequest request = new CarteRequest(codeMembre, dateDeb, dateFin, true, "liv", user.getId());
		request.setUserType(user.getUserType());
		String jrequest = SerializationTools.jsonSerialise(request);
		CustomPair res;
		try {
			res = AsyncRestClient.executePost("carte/listInfo/", jrequest);
			if (res.getKey() == 200) {
				List<CarteInfo> cartes = (List<CarteInfo>) SerializationTools.jsonListDeserialise(res.getValue(),
						CarteInfo.class);
				if (Objects.nonNull(cartes) && cartes.size() > 0) {
					List<CarteInfoBean> items = new ArrayList<>();
					cartes.forEach(c -> items.add(c.convertToBean()));
					ObservableList<CarteInfoBean> beans = FXCollections.observableArrayList(items);
					tableCarte1.setItems(beans);
					tableCarte1.refresh();
				} else {
					DialogUtils.errorDialog("Chargement des demandes de Cartes",
							"Pas de demandes de cartes correspondantes");
				}
			} else {
				ExceptionMessage message = (ExceptionMessage) SerializationTools.jsonDeserialise(res.getValue(),
						ExceptionMessage.class);
				DialogUtils.errorDialog("Mise à jour de la Carte", message.getMessage());
			}
		} catch (RejectedExecutionException | NullPointerException | InterruptedException | ExecutionException
				| IOException | TimeoutException ex) {
			Logger.getLogger(PrinterWindowController.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

}
