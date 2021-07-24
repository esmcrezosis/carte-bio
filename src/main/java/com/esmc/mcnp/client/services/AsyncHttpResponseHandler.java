package com.esmc.mcnp.client.services;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncHttpResponseHandler implements ResponseHandler<CustomPair> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncHttpResponseHandler.class);

    @Override
    public CustomPair handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        CustomPair result = new CustomPair();
        Integer status = response.getStatusLine().getStatusCode();

        LOGGER.info("Status: " + status);
        HttpEntity entity = response.getEntity();
        String res = entity != null ? EntityUtils.toString(entity) : null;
        result.setKey(status);
        result.setValue(res);
        return result;
    }

}
