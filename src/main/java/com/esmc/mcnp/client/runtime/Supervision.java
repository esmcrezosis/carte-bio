/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esmc.mcnp.client.runtime;

/**
 *
 * @author HP
 */
public class Supervision extends Request{

    private ParamsSupervision parameters;

    public Supervision() {
    }

    Supervision(String method, String id, ParamsSupervision p) {
        super(method, id);
        this.parameters = p;
    }

    public ParamsSupervision getParameters() {
        return parameters;
    }

    public void setParameters(ParamsSupervision parameters) {
        this.parameters = parameters;
    }

}
