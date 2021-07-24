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
public class CarteRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codeMembre;
	private LocalDate DateDemande;
	private LocalDate datefin;
	private boolean impLivrer;
	private String requestType;
	private Long idUser;
	private String userType;

	public CarteRequest() {
	}

	public CarteRequest(LocalDate DateDemande, boolean impLivrer) {
		this.DateDemande = DateDemande;
		this.impLivrer = impLivrer;
	}

	public CarteRequest(LocalDate DateDemande, LocalDate datefin, boolean impLivrer) {
		this.DateDemande = DateDemande;
		this.datefin = datefin;
		this.impLivrer = impLivrer;
	}

	public CarteRequest(String codeMembre, LocalDate DateDemande, LocalDate datefin, boolean impLivrer) {
		this.codeMembre = codeMembre;
		this.DateDemande = DateDemande;
		this.datefin = datefin;
		this.impLivrer = impLivrer;
	}

	public CarteRequest(String codeMembre, LocalDate DateDemande, LocalDate datefin, boolean impLivrer,
			String requestType) {
		this.codeMembre = codeMembre;
		this.DateDemande = DateDemande;
		this.datefin = datefin;
		this.impLivrer = impLivrer;
		this.requestType = requestType;
	}
	
	public CarteRequest(String codeMembre, LocalDate DateDemande, LocalDate datefin, boolean impLivrer, String requestType, Long idUser) {
        this.codeMembre = codeMembre;
        this.DateDemande = DateDemande;
        this.datefin = datefin;
        this.impLivrer = impLivrer;
        this.requestType = requestType;
        this.setIdUser(idUser);
    }

	public String getCodeMembre() {
		return codeMembre;
	}

	public void setCodeMembre(String codeMembre) {
		this.codeMembre = codeMembre;
	}

	public LocalDate getDateDemande() {
		return DateDemande;
	}

	public void setDateDemande(LocalDate DateDemande) {
		this.DateDemande = DateDemande;
	}

	public LocalDate getDatefin() {
		return datefin;
	}

	public void setDatefin(LocalDate datefin) {
		this.datefin = datefin;
	}

	public boolean isImpLivrer() {
		return impLivrer;
	}

	public void setImpLivrer(boolean impLivrer) {
		this.impLivrer = impLivrer;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

}
