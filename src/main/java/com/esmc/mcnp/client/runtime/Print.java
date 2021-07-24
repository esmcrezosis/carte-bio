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
public class Print extends Request{
    private ParamsPrint parameters;

    public Print() {
    }

    public Print(String method, String id, ParamsPrint parameters) {
        super(method, id);
        this.parameters = parameters;
    }

    public ParamsPrint getParameters() {
        return parameters;
    }

    public void setParameters(ParamsPrint parameters) {
        this.parameters = parameters;
    }
    
}
