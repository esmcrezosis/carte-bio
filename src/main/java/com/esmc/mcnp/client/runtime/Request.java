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
public class Request {
    private String id;
    private String jsonrpc;
    private String method;

    public Request() {
        jsonrpc = "2.0";
    }

    public Request(String method, String id) {
        jsonrpc = "2.0";
        this.method = method;
        this.id = id;
    }

    public Request(String jsonrpc, String method, String id) {
        this.jsonrpc = jsonrpc;
        this.method = method;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
