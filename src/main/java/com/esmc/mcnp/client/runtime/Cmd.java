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
public class Cmd extends Request {

    private ParamsCmd parameters;

    public Cmd() {
    }

    public Cmd(String method, String id, ParamsCmd parameters) {
        super(method, id);
        this.parameters = parameters;
    }

    public ParamsCmd getParameters() {
        return parameters;
    }

    public void setParameters(ParamsCmd parameters) {
        this.parameters = parameters;
    }

}
