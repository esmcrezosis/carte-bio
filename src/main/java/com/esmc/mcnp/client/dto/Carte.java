/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esmc.mcnp.client.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author HP
 */
public class Carte implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codeMembre;
	private String raisonSociale;
	private String nomMmebre;
	private String prenomMembre;
	private LocalDate dateNais;
	private String lieuNais;
	private LocalDate dateDemande;
	private LocalDate dateImpression;
	private LocalDate dateLivraison;
	private boolean livrer;
	private boolean imprimer;
	private Long idUtilisateur;
	private String userType;
	private String photo;
	private String qrCode;
	private String image;
	private String telephone;
	private String cel;
	private String email;
	private String codeRep;
	private boolean modifier;

	public Carte() {
	}

	public Carte(String codeMembre, Long idUtilisateur, String image) {
		this.codeMembre = codeMembre;
		this.idUtilisateur = idUtilisateur;
		this.image = image;
	}

	public String getCodeMembre() {
		return codeMembre;
	}

	public void setCodeMembre(String codeMembre) {
		this.codeMembre = codeMembre;
	}

	public String getRaisonSociale() {
		return raisonSociale;
	}

	public void setRaisonSociale(String raisonSociale) {
		this.raisonSociale = raisonSociale;
	}

	public String getNomMmebre() {
		return nomMmebre;
	}

	public void setNomMmebre(String nomMmebre) {
		this.nomMmebre = nomMmebre;
	}

	public String getPrenomMembre() {
		return prenomMembre;
	}

	public void setPrenomMembre(String prenomMembre) {
		this.prenomMembre = prenomMembre;
	}

	public LocalDate getDateNais() {
		return dateNais;
	}

	public void setDateNais(LocalDate dateNais) {
		this.dateNais = dateNais;
	}

	public String getLieuNais() {
		return lieuNais;
	}

	public void setLieuNais(String lieuNais) {
		this.lieuNais = lieuNais;
	}

	public LocalDate getDateDemande() {
		return dateDemande;
	}

	public void setDateDemande(LocalDate dateDemande) {
		this.dateDemande = dateDemande;
	}

	public LocalDate getDateImpression() {
		return dateImpression;
	}

	public void setDateImpression(LocalDate dateImpression) {
		this.dateImpression = dateImpression;
	}

	public LocalDate getDateLivraison() {
		return dateLivraison;
	}

	public void setDateLivraison(LocalDate dateLivraison) {
		this.dateLivraison = dateLivraison;
	}

	public boolean isLivrer() {
		return livrer;
	}

	public void setLivrer(boolean livrer) {
		this.livrer = livrer;
	}

	public boolean isImprimer() {
		return imprimer;
	}

	public void setImprimer(boolean imprimer) {
		this.imprimer = imprimer;
	}

	public Long getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(Long idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCel() {
		return cel;
	}

	public void setCel(String cel) {
		this.cel = cel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isModifier() {
		return modifier;
	}

	public void setModifier(boolean modifier) {
		this.modifier = modifier;
	}

	public String getCodeRep() {
		return codeRep;
	}

	public void setCodeRep(String codeRep) {
		this.codeRep = codeRep;
	}

}
