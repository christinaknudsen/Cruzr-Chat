package com.example.cruzrtutorial;

import com.google.gson.Gson;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Dialogflow {

    private String message = "";

    private String route = "";

    private JSONObject postReq(String text, String route) throws ParseException {
        String res = ConsumerKt.post(text, route);
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(res);
        return json;
    }

    private JSONObject getReq() throws ParseException {
        String res = ConsumerKt.get("/get_intent");
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(res);
        return json;
    }

    public Object getMessage() throws ParseException {
        JSONObject json = postReq(message, route);
        Object o = new Gson().fromJson(String.valueOf(json), Object.class);
        return o;
    }

    public Object getIntent() throws ParseException {
        JSONObject json = getReq();
        Object o = new Gson().fromJson(String.valueOf(json), Object.class);
        return o;
    }
    public void setMessage(String msg) {
        message = msg;
    }
    public void setRoute(String rt) {route = rt;}
}