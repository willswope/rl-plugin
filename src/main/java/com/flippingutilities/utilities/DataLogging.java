package com.flippingutilities.utilities;

import com.flippingutilities.model.ItemViewEvent;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;

@Slf4j
public class DataLogging {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static void postViewedItemEvent (String url, ItemViewEvent event) {
        Gson gson = new Gson();
        RequestBody messagePayload = RequestBody.create(JSON, gson.toJson(event));

        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder().url(url).post(messagePayload).build();

        try {
            client.newCall(req).execute();
        } catch (Exception e) {
            log.warn("Flipping utilities item view logging failed!");
        }
    }
    
}
