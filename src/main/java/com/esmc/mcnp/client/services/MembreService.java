/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esmc.mcnp.client.services;

/**
 *
 * @author HP
 */
import com.esmc.mcnp.client.runtime.Response;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class MembreService extends Service<Response> {

    private final String loc;

    public MembreService(String loc) {
        this.loc = loc;
    }

    @Override
    protected Task<Response> createTask() {
        return new Task<Response>() {
            @Override
            protected Response call() throws Exception {
                URL host = new URL(loc);
                JsonReader jr = Json.createReader(new GZIPInputStream(host.openConnection().
                        getInputStream()));
                JsonObject jsonObject = jr.readObject();
                Response response = (Response) jsonObject.getValue("response");
                return response;
            }
        };
    }
}
