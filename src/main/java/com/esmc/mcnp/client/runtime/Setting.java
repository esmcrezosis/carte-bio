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
public class Setting extends Request {

    private ParamsSetting parameters;

    public ParamsSetting getParameters() {
        return parameters;
    }

    public void setParameters(ParamsSetting parameters) {
        this.parameters = parameters;
    }

    public Setting() {
    }

}
