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
public class Response implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer resultat;
    private Object object;

    public Response() {
    }

    public Response(Integer resultat, Object object) {
        this.resultat = resultat;
        this.object = object;
    }

    public Integer getResultat() {
        return resultat;
    }

    public void setResultat(Integer resultat) {
        this.resultat = resultat;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

}
