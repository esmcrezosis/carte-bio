/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esmc.mcnp.client.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author HP
 */
public class InfoMembreMorale implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String codeMembre;
    private String raisonSociale;
    private String telephone;
    private String cel;
    private String email;
    private List<InfoMembre> membres;
    private boolean autoriser;
    private boolean doublon;
    private LocalDate dateDemande;

    public InfoMembreMorale() {
    }

    public InfoMembreMorale(String codeMembre, String raisonSociale, String telephone, String cel, String email,
            List<InfoMembre> membres) {
        this.codeMembre = codeMembre;
        this.raisonSociale = raisonSociale;
        this.telephone = telephone;
        this.cel = cel;
        this.email = email;
        this.membres = membres;
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

    public List<InfoMembre> getMembres() {
        return membres;
    }

    public void setMembres(List<InfoMembre> membres) {
        this.membres = membres;
    }

    public boolean isAutoriser() {
        return autoriser;
    }

    public void setAutoriser(boolean autoriser) {
        this.autoriser = autoriser;
    }

    public boolean isDoublon() {
        return doublon;
    }

    public void setDoublon(boolean doublon) {
        this.doublon = doublon;
    }

    public LocalDate getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
    }

}
