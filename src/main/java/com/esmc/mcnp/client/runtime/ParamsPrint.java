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
public class ParamsPrint {

    private String session;
    private String face;
    private String panel;
    private String data;
    private String device;

    public ParamsPrint() {
    }
    
    public ParamsPrint(String face, String panel, String data, String session) {
        this.face = face;
        this.panel = panel;
        this.data = data;
        this.session = session;
    }

    public ParamsPrint(String face, String panel, String data, String device, String session) {
        this.session = session;
        this.face = face;
        this.panel = panel;
        this.data = data;
        this.device = device;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getPanel() {
        return panel;
    }

    public void setPanel(String panel) {
        this.panel = panel;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

}
