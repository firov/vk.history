package org.therg.vk.history.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;

public abstract class ApiClientBase {
    private static Logger logger = LogManager.getLogger("vk.history.api");

    private Gson gson = new Gson();

    private static final String baseUrl = "https://api.vk.com/method/";

    AbstractHttpClient httpClient;

    private String requestPostfix;

    public ApiClientBase(String token) {
        httpClient = new DefaultHttpClient();
        requestPostfix = "&v=5.32&access_token=" + token;
    }

    private HttpResponse executeRequest(String method, String args) throws IOException {
        String url = String.format("%s%s?%s%s", baseUrl, method, args, requestPostfix);
        logger.debug(url);

        HttpGet getRequest = new HttpGet(url);
        getRequest.addHeader("accept", "application/json");

        return httpClient.execute(getRequest);
    }

    public void downloadTarget(String url, OutputStream outputStream) {
        logger.debug(url);

        HttpGet getRequest = new HttpGet(url);

        try {
            HttpResponse response = httpClient.execute(getRequest);

            byte[] buffer = new byte[1024];
            int bytesRead;

            try (InputStream inputStream = response.getEntity().getContent()) {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.flush();
            }
        } catch (IOException exception) {
            logger.error("input/output error", exception);
        }
    }

    private JsonObject getJson(HttpResponse response) throws IOException {
        InputStreamReader reader = new InputStreamReader((response.getEntity().getContent()));
        return gson.fromJson(reader, JsonObject.class);
    }

    private <T extends ApiResult> T createErrorResult(Class<T> resultClass, String message) {
        T result;
        try {
            result = resultClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Failed to create result class instance", e);
            throw new RuntimeException("Failed to create result class instance");
        }

        result.errorCode = -1;
        result.errorMessage = message;

        return result;
    }

    private <T extends ApiResult> T executeGet(Class<T> resultClass, boolean isArray, String method, String args) {
        JsonObject parsedContent;
        try {
            HttpResponse response = executeRequest(method, args);

            if (response.getStatusLine().getStatusCode() != 200) {
                logger.error("HTTP error: " + +response.getStatusLine().getStatusCode());
                return createErrorResult(resultClass, "HTTP error");
            }

            parsedContent = getJson(response);
        } catch (IOException e) {
            logger.error("Communication error", e);
            return createErrorResult(resultClass, "Communication error");
        }

        logger.debug(parsedContent.toString());

        JsonElement errorElement = parsedContent.get("error");
        if (errorElement != null) {
            return gson.fromJson(errorElement, resultClass);
        }

        if (isArray)
            return gson.fromJson(parsedContent, resultClass);

        JsonElement responseElement = parsedContent.get("response");
        return gson.fromJson(responseElement, resultClass);
    }

    protected <T extends ApiResult> T executeGet(Class<T> resultClass, boolean isArray, String method, Map<String, Object> args) {
        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, Object> entry : args.entrySet()) {
            if (entry.getValue() == null)
                continue;

            query.append(entry.getKey());
            query.append("=");
            query.append(entry.getValue());
            query.append("&");
        }

        return executeGet(resultClass, isArray, method, query.toString());
    }
}
