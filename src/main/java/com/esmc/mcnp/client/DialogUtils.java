package com.esmc.mcnp.client;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.esmc.mcnp.client.dto.CarteInfo;
import com.esmc.mcnp.client.dto.InfoMembre;
import com.esmc.mcnp.client.util.Utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;

public class DialogUtils {

	public void showInfoMembre(InfoMembre membre) {

		// Custom dialog
		Dialog<InfoMembre> dialog = new Dialog<>();
		dialog.setTitle("Demande de Carte");
		dialog.setHeaderText("Ce Membre a déjà effectué une demande de carte");
		dialog.setResizable(true);

		// Widgets
		Label label1 = new Label("Code        : ");
		Label label2 = new Label("Nom         : ");
		Label label3 = new Label("Prénoms     : ");
		Label label4 = new Label("Date Demande: ");
		TextField text1 = new TextField(membre.getCodeMembre());
		text1.setDisable(true);
		TextField text2 = new TextField(membre.getNomMembre());
		text2.setDisable(true);
		TextField text3 = new TextField(membre.getPrenomMembre());
		text3.setDisable(true);
		DatePicker date = new DatePicker(membre.getDateDemande());
		date.setDisable(true);

		// Create layout and add to dialog
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 35, 20, 35));
		grid.add(label1, 1, 1); // col=1, row=1
		grid.add(text1, 2, 1);
		grid.add(label2, 1, 2); // col=1, row=2
		grid.add(text2, 2, 2);
		grid.add(label3, 1, 3);
		grid.add(text3, 2, 3);
		grid.add(label4, 1, 4);
		grid.add(date, 2, 4);
		dialog.getDialogPane().setContent(grid);

		// Add button to dialog
		ButtonType buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

		// Result converter for dialog
		dialog.setResultConverter(new Callback<ButtonType, InfoMembre>() {
			@Override
			public InfoMembre call(ButtonType b) {
				if (b == buttonTypeOk) {
					return membre;
				}
				return null;
			}
		});
		// Show dialog
		dialog.showAndWait();
	}

	public static CarteInfo showInfoCarte(CarteInfo carte) {
		boolean already = carte.isImprimer();
		// Custom dialog
		Dialog<CarteInfo> dialog = new Dialog<>();
		final Window window = dialog.getDialogPane().getScene().getWindow();
		Stage stage = (Stage) window;

		stage.setMinHeight(300);
		stage.setMinWidth(500);
		dialog.setTitle("Demande de Carte");
		dialog.setHeaderText("Vue de la demande de carte");
		// dialog.setWidth(500);
		dialog.setResizable(false);

		// Widgets
		Label label1 = new Label("Code");
		Label label2 = new Label("Nom");
		Label label3 = new Label("Prénoms");
		Label label4 = new Label("Date Demande");
		Label label5 = new Label("Imprimer");
		TextField text1 = new TextField(carte.getCodeMembre());
		text1.setDisable(true);
		TextField text2 = new TextField(carte.getNomMembre());
		text2.setDisable(true);
		TextField text3 = new TextField(carte.getPrenomMembre());
		text3.setDisable(true);
		DatePicker date = new DatePicker(Utils.convertToLocalDate(carte.getDateDemande()));
		date.setDisable(true);
		CheckBox ckbImprimer = new CheckBox();
		ckbImprimer.setSelected(carte.isImprimer());
		ckbImprimer.setOnAction(a -> {
			carte.setImprimer(ckbImprimer.isSelected());
		});

		// Create layout and add to dialog
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.autosize();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 15, 10, 15));

		grid.add(label1, 0, 1); // col=1, row=1
		grid.add(text1, 1, 1);

		grid.add(label2, 0, 2); // col=1, row=2
		grid.add(text2, 1, 2);

		grid.add(label3, 0, 3);
		grid.add(text3, 1, 3);

		grid.add(label4, 0, 4);
		grid.add(date, 1, 4);

		grid.add(label5, 0, 5);
		grid.add(ckbImprimer, 1, 5);

		final ColumnConstraints column1 = new ColumnConstraints(100, Control.USE_COMPUTED_SIZE,
				Control.USE_COMPUTED_SIZE);
		grid.getColumnConstraints().add(column1);

		ColumnConstraints column2 = new ColumnConstraints(300, Control.USE_COMPUTED_SIZE, Double.MAX_VALUE);
		column2.setHgrow(Priority.ALWAYS);
		grid.getColumnConstraints().add(column2);
		grid.setPrefSize(stage.getWidth(), stage.getHeight()); // Default width and height
		grid.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
		dialog.getDialogPane().setContent(grid);

		// Add button to dialog
		ButtonType buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

		// Result converter for dialog
		dialog.setResultConverter(new Callback<ButtonType, CarteInfo>() {
			@Override
			public CarteInfo call(ButtonType b) {
				if (b == buttonTypeOk) {
					if (!already && carte.isImprimer()) {
						return carte;
					}
				}
				return null;
			}
		});
		// Show dialog
		return dialog.showAndWait().orElse(null);
	}

	public static void showErrorDialog(Exception ex) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Boîte de Dialogue d'Erreur");
		alert.setHeaderText("Une erreur est survenue au cours de l'execution");
		alert.setContentText(ex.getLocalizedMessage());

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("Details de l'erreur:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}

	public static void showInfoDialog(String header, String info) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Boîte d'Information");
		alert.setHeaderText(header);
		alert.setContentText(info);
		alert.showAndWait();
	}

	public static void errorDialog(String header, String info) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Boîte d'Erreur");
		alert.setHeaderText(header);
		alert.setContentText(info);
		alert.showAndWait();
	}
}
