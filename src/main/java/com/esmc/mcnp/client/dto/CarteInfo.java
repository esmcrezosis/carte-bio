/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esmc.mcnp.client.dto;

import java.io.Serializable;

/**
 *
 * @author HP
 */
public class CarteInfo  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long idCarte;
	private Long idUtilisateur;
	private String codeMembre;
	private String nomMembre;
	private String prenomMembre;
	private String contact;
	private String url;
	private String dateDemande;
	private boolean imprimer;
	private boolean livrer;
	private String image;
    private String nomAsso;

	public CarteInfo() {
	}

	public CarteInfo(Long idCarte, String codeMembre, String nomMembre, String prenomMembre, String contact,
			String dateDemande) {
		this.idCarte = idCarte;
		this.codeMembre = codeMembre;
		this.nomMembre = nomMembre;
		this.prenomMembre = prenomMembre;
		this.contact = contact;
		this.dateDemande = dateDemande;
	}

	public Long getIdCarte() {
		return idCarte;
	}

	public void setIdCarte(Long idCarte) {
		this.idCarte = idCarte;
	}

	public Long getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(Long idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}

	public String getCodeMembre() {
		return codeMembre;
	}

	public void setCodeMembre(String codeMembre) {
		this.codeMembre = codeMembre;
	}

	public String getNomMembre() {
		return nomMembre;
	}

	public void setNomMembre(String nomMembre) {
		this.nomMembre = nomMembre;
	}

	public String getPrenomMembre() {
		return prenomMembre;
	}

	public void setPrenomMembre(String prenomMembre) {
		this.prenomMembre = prenomMembre;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDateDemande() {
		return dateDemande;
	}

	public void setDateDemande(String dateDemande) {
		this.dateDemande = dateDemande;
	}

	public boolean isImprimer() {
		return imprimer;
	}

	public void setImprimer(boolean imprimer) {
		this.imprimer = imprimer;
	}

	public boolean isLivrer() {
		return livrer;
	}

	public void setLivrer(boolean livrer) {
		this.livrer = livrer;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getNomAsso() {
		return nomAsso;
	}

	public void setNomAsso(String nomAsso) {
		this.nomAsso = nomAsso;
	}

	public CarteInfoBean convertToBean() {
		CarteInfoBean bean = new CarteInfoBean();
		bean.setIdCarte(this.idCarte);
		bean.setIdUser(this.idUtilisateur);
		bean.setCodeMembre(this.codeMembre);
		bean.setContact(this.contact);
		bean.setDateDemande(this.dateDemande);
		bean.setImage(this.image);
		bean.setImprimer(this.imprimer);
		bean.setNomMembre(this.nomMembre);
		bean.setPrenomMembre(this.prenomMembre);
		bean.setLivrer(this.livrer);
		bean.setNomAsso(this.nomAsso);
		bean.setImage(this.image);
		return bean;
	}

}
