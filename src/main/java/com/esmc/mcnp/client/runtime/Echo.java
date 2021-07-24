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
public class Echo extends Request {

    private ParamsEcho parameters;

    public Echo() {
    }

    public Echo(ParamsEcho parameters) {
        this.parameters = parameters;
    }

    public Echo(String method, String id, ParamsEcho parameters) {
        super(method, id);
        this.parameters = parameters;
    }

    public ParamsEcho getParameters() {
        return parameters;
    }

    public void setParameters(ParamsEcho parameters) {
        this.parameters = parameters;
    }

}
