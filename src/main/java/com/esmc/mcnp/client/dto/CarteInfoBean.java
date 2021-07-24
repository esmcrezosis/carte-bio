/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esmc.mcnp.client.dto;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author HP
 */
public class CarteInfoBean {

	private SimpleLongProperty idCarte;
	private SimpleLongProperty idUser;
	private SimpleStringProperty codeMembre;
	private SimpleStringProperty nomMembre;
	private SimpleStringProperty prenomMembre;
	private SimpleStringProperty contact;
	private SimpleStringProperty dateDemande;
	private SimpleBooleanProperty imprimer;
	private SimpleBooleanProperty livrer;
	private SimpleStringProperty image;
	private SimpleStringProperty nomAsso;

	public CarteInfoBean() {
		this.idCarte = new SimpleLongProperty();
		this.idUser = new SimpleLongProperty();
		this.codeMembre = new SimpleStringProperty();
		this.nomMembre = new SimpleStringProperty();
		this.prenomMembre = new SimpleStringProperty();
		this.contact = new SimpleStringProperty();
		this.dateDemande = new SimpleStringProperty();
		this.imprimer = new SimpleBooleanProperty();
		this.livrer = new SimpleBooleanProperty();
	}

	public Long getIdCarte() {
		return idCarte.get();
	}

	public void setIdCarte(Long id) {
		idCarte.set(id);
	}

	public Long getIdUser() {
		return idUser.get();
	}

	public void setIdUser(Long userId) {
		idUser.set(userId);
	}

	public String getCodeMembre() {
		return codeMembre.get();
	}

	public void setCodeMembre(String code) {
		codeMembre.set(code);
	}

	public String getNomMembre() {
		return nomMembre.get();
	}

	public void setNomMembre(String nom) {
		this.nomMembre.set(nom);
	}

	public String getPrenomMembre() {
		return prenomMembre.get();
	}

	public void setPrenomMembre(String prenom) {
		this.prenomMembre.set(prenom);
	}

	public String getContact() {
		return contact.get();
	}

	public void setContact(String tel) {
		this.contact.set(tel);
	}

	public String getDateDemande() {
		return dateDemande.get();
	}

	public void setDateDemande(String date) {
		this.dateDemande.set(date);
	}

	public boolean getImprimer() {
		return imprimer.get();
	}

	public void setImprimer(boolean imp) {
		imprimer.set(imp);
	}
	
	public BooleanProperty imprimerProperty() {
        return imprimer;
    }

	public boolean getLivrer() {
		return livrer.get();
	}

	public void setLivrer(boolean liv) {
		livrer.set(liv);
	}
	
	public BooleanProperty livrerProperty() {
        return livrer;
    }

	public String getImage() {
		return image.get();
	}

	public void setImage(String image) {
		this.image.set(image);
	}

	public String getNomAsso() {
		return nomAsso.get();
	}

	public void setNomAsso(String nomAsso) {
		this.nomAsso.set(nomAsso);
	}

	public CarteInfo convertToInto() {
		CarteInfo info = new CarteInfo();
		info.setCodeMembre(getCodeMembre());
		info.setContact(getContact());
		info.setDateDemande(getDateDemande());
		info.setIdCarte(getIdCarte());
		info.setIdUtilisateur(getIdUser());
		info.setImprimer(getImprimer());
		info.setNomMembre(getNomMembre());
		info.setPrenomMembre(getPrenomMembre());
		info.setLivrer(getLivrer());
		return info;
	}

}
