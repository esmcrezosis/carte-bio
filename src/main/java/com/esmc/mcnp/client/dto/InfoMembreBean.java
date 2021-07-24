package com.esmc.mcnp.client.dto;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class InfoMembreBean {
	private SimpleStringProperty codeMembre;
    private SimpleStringProperty nomMembre;
    private SimpleStringProperty prenomMembre;
    private SimpleStringProperty telephone;
    private SimpleBooleanProperty principal;
	public InfoMembreBean() {
		this.codeMembre = new SimpleStringProperty();
		this.nomMembre = new SimpleStringProperty();
		this.prenomMembre = new SimpleStringProperty();
		this.telephone = new SimpleStringProperty();
		this.principal = new SimpleBooleanProperty();
	}
	public String getCodeMembre() {
		return codeMembre.get();
	}
	public void setCodeMembre(String codeMembre) {
		this.codeMembre.set(codeMembre);
	}
	public String getNomMembre() {
		return nomMembre.get();
	}
	public void setNomMembre(String nomMembre) {
		this.nomMembre.set(nomMembre);
	}
	public String getPrenomMembre() {
		return prenomMembre.get();
	}
	public void setPrenomMembre(String prenomMembre) {
		this.prenomMembre.set(prenomMembre);
	}
	public String getTelephone() {
		return telephone.get();
	}
	public void setTelephone(String telephone) {
		this.telephone.set(telephone);
	}
	public Boolean getPrincipal() {
		return principal.get();
	}
	
	public BooleanProperty principalProperty() {
        return principal;
    }
	
	public void setPrincipal(Boolean principal) {
		this.principal.set(principal);
	}
	
	@Override
	public boolean equals(Object obj) {
		InfoMembreBean other = (InfoMembreBean) obj;
		return codeMembre.get().equals(other.codeMembre.get());
	}
	
	

}
