package com.esmc.mcnp.client.services;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import com.esmc.mcnp.client.MainApp;
import com.esmc.mcnp.client.util.HttpUtil;

public class AsyncRestClient {

    public AsyncRestClient() {
    }

    public static CustomPair executeGet(String requete) throws InterruptedException, ExecutionException, IOException, TimeoutException {
        System.out.println("Requete : " + requete);
    	CustomPair res = new CustomPair();
        String url = MainApp.getInstance().getServerUrl() + requete;
        System.out.println("URL : " + url);
        HttpGet req = new HttpGet(url);
        req.setHeader("Accept", "application/json");
        req.setHeader("Content-type", "application/json");
        Future<CustomPair> futres = HttpUtil.run(req, new AsyncHttpResponseHandler());
        res = futres.get(30, TimeUnit.SECONDS);
        //HttpUtil.shutdown();
        return res;
    }
    

    public static CustomPair executePost(String url, String params) throws RejectedExecutionException, NullPointerException, InterruptedException, ExecutionException, IOException, TimeoutException {
        System.out.println("SERVER URL : " + MainApp.getInstance().getServerUrl());
        CustomPair res = new CustomPair();
        HttpPost req = new HttpPost(MainApp.getInstance().getServerUrl() + url);
        req.setHeader("Accept", "application/json");
        req.setHeader("Content-type", "application/json");
        StringEntity json = new StringEntity(params);
        req.setEntity(json);
        Future<CustomPair> futres = HttpUtil.run(req, new AsyncHttpResponseHandler());
        res = futres.get(30, TimeUnit.SECONDS);
        //HttpUtil.shutdown();
        return res;
    }

}
