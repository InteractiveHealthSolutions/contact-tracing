package com.example.moiz_ihs.contracttracing.network;

import org.json.JSONArray;
import org.json.JSONObject;

public interface Sendable {

	public void send(JSONArray data, int respId);

    public void onResponseReceived(JSONObject resp, int respId);

}