package com.bebound.template.controller;

import com.bebound.template.AESCrypt;
import com.bebound.template.model.Request;
import com.bebound.template.response.Failure;
import com.bebound.template.response.Response;
import com.bebound.template.response.Success;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.logging.Logger;

@RestController
public class TemplateController {

    private static final Gson gson = new GsonBuilder().create();

    /**
     * We are listening for HTTP request, with JSON
     * First, we check mandatory fields
     * Then, we process the parameters from the JSON object
     * Finally, we send the response, with the correct parameters
     * @param headers
     * @param body
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<String> check(@RequestHeader Map<String, String> headers, @RequestBody String body) {
        // You may want to check authorization here, request should have Basic auth header
        JsonObject jsonRequestObject = new JsonParser().parse(body).getAsJsonObject();

        if (!jsonRequestObject.has("moduleName"))
            return new ResponseEntity<>("moduleName missing", HttpStatus.BAD_REQUEST);

        if (!jsonRequestObject.has("moduleId"))
            return new ResponseEntity<>("moduleId missing", HttpStatus.BAD_REQUEST);

        if (!jsonRequestObject.has("moduleVersion"))
            return new ResponseEntity<>("moduleVersion missing", HttpStatus.BAD_REQUEST);

        if (!jsonRequestObject.has("operation"))
            return new ResponseEntity<>("operation missing", HttpStatus.BAD_REQUEST);

        if (!jsonRequestObject.has("params"))
            return new ResponseEntity<>("params missing", HttpStatus.BAD_REQUEST);

        if (!jsonRequestObject.has("transport"))
            return new ResponseEntity<>("transport missing", HttpStatus.BAD_REQUEST);

        Request.MapRequest request = gson.fromJson(body, Request.MapRequest.class);
        request.setJsonParams(gson.toJson(request.getParameters()));

        try {
            return new ResponseEntity<>(gson.toJson(performRequest(request)), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * We perform the request:
     * 1- We check if operation name from Be-App Manifest is equal to the expected operation name
     * 2- If ok, we create a success response with the parameter "length", as defined in our Be-App Manifest
     * 3- If ko, we create a failure response with a dedicated message
     * @param request from the JSON we parsed
     * @return the response
     */
    private Response performRequest(Request request) {
        Logger.getAnonymousLogger().info("Operation name: " + request.getOperationName());

        if ("send_encrypted".equals(request.getOperationName())) {
            Logger.getAnonymousLogger().info("PERFORM REQUEST TO BE-BOUND SERVER - " + request.getOperationName());
            String encryptedMsg = request.getParameters().get("text_encrypted").toString();
            Logger.getAnonymousLogger().info("encryptedMsg - " + encryptedMsg);

            String key = "Bar12345Bar12345"; // 128 bit key
            String initVector = "RandomInitVector"; // 16 bytes IV

            String messageAfterDecrypt = AESCrypt.decrypt(key, initVector, encryptedMsg);
            return new Success.Builder().withParameter("text_decrypted", messageAfterDecrypt).build();
            //return new Failure.Builder().withErrorStatus("Impossible to decrypt " + request.getOperationName()).build();
        }
        Logger.getAnonymousLogger().info("ERROR REQUEST");

        return new Failure.Builder().withErrorStatus("This operation: " + request.getOperationName() + " doesn't exist").build();
    }
}
