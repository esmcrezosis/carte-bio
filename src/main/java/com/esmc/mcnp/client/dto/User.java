package com.esmc.mcnp.client.dto;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private String userName;
    private String password;
    private String passwordHash;
    private String nomUser;
    private String prenomUser;
    private boolean imprimer;
    private List<String> groupes;
    private String userType;

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public String getPrenomUser() {
        return prenomUser;
    }

    public void setPrenomUser(String prenomUser) {
        this.prenomUser = prenomUser;
    }

    public boolean isImprimer() {
        return imprimer;
    }

    public void setImprimer(boolean imprimer) {
        this.imprimer = imprimer;
    }

    public List<String> getGroupes() {
        return groupes;
    }

    public void setGroupes(List<String> groupes) {
        this.groupes = groupes;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

}
