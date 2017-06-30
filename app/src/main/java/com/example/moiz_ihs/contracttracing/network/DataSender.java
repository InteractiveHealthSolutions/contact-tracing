package com.example.moiz_ihs.contracttracing.network;
/**
 * @author naveed.iqbal
 * @description this class run in background on request of other class, communicate with server,
 *  and receive response. If class calling it need it to return the response then calling class
 *  must have to implement AsyncTaskResponseBridge interface, and call its 
 *  SendDataToserver(AsyncTaskResponseBridge atrb, int respId)constructor. Response will be returned as string
 *  in recievedResponse(String resp, int respId) method of AsyncTaskResponseBridge interface. 
 * 
 * */

import android.R.integer;
import android.content.Context;
import android.os.AsyncTask;

import com.example.moiz_ihs.contracttracing.models.ParamNames;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class DataSender extends AsyncTask<JSONArray, integer, JSONObject> {
	
	private int respId;
	private Sendable sendable;
	private Context context;
	
	public DataSender(Context context, Sendable sendable, int respId) {
		this.sendable = sendable;
		this.respId = respId;
		this.context = context;
	}
	
	@Override
	protected JSONObject doInBackground(JSONArray... params) {
		try {
			if(true) {
				String resp = "";
				String serverAddress = "http://199.172.1.129:8181/tbrmobileweb/RequestHandlerServlet";
				HttpPost httppost = new HttpPost(serverAddress);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair(ParamNames.DATA, params[0].toString()));
				try {
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				} catch (UnsupportedEncodingException e1) {
					resp = Global.errorMessage[6];
					e1.printStackTrace();
				}
				
				HttpParams httpParameters = new BasicHttpParams();
				// Set the timeout in milliseconds until a connection is established.
				// The default value is zero, that means the timeout is not used. 
				int timeoutConnection = 120000;
				HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
				// Set the default socket timeout (SO_TIMEOUT) 
				// in milliseconds which is the timeout for waiting for data.
				int timeoutSocket = 120000;
				HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				
				try {
					HttpResponse response = httpclient.execute(httppost);
					int statusCode = response.getStatusLine().getStatusCode(); 
					if (statusCode == 200) {
						InputStreamReader ir = new InputStreamReader(response.getEntity().getContent());
						BufferedReader br = new BufferedReader(ir);
						String b;
						while ((b = br.readLine()) != null) {
							resp += b;
						}
					} else {
						resp = Global.errorMessage[5]+statusCode;
					}
				} catch (HttpHostConnectException e) {
					resp = Global.errorMessage[1];
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					resp = Global.errorMessage[2];
					e.printStackTrace();
				} catch (IOException e) {
					resp = Global.errorMessage[3];
					e.printStackTrace();
				}
				
				try {
					return new JSONObject(resp);
				} catch (JSONException e) {
					e.printStackTrace();
					System.out.println(resp);
					return new JSONObject().put(ParamNames.SERVER_RESPONSE, "Error: Request not completed due to some error");
				}
				
			} else {
				return new JSONObject().put(ParamNames.SERVER_RESPONSE, "Device is not connected to internet");
			}
		} catch (JSONException ee) {
			ee.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		sendable.onResponseReceived(result, respId);
	}
	
	//TODO make use of it
	/*private boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) ((Activity)context).getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected())
                return true;
            else
                return false;    
    }*/
}
