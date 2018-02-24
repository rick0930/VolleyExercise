package com.demo.volley3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyTag";
    private static String url = "http://data.ntpc.gov.tw/api/v1/rest/datastore/382000000A-000352-001";
    RequestQueue mRequestQueue;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        init();
        sendRequest();
    }

    private void init() {
        MySingleton ms = MySingleton.getInstance(this);
        mRequestQueue = ms.getRequestQueue();
    }

    private void sendRequest() {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                ArrayList<String> stations = new ArrayList<>();

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String name = null;
                        int count = 0;
                        JSONArray array = response.getJSONObject("result").getJSONArray("records");
                        while (true) {
                            if (count < array.length()) {
                                name = array.getJSONObject(count).getString("sna");
                                stations.add(name);
                            } else
                                break;

                            count++;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, stations);
                    listView.setAdapter(adapter);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub

                }
            });

        jsObjRequest.setTag(TAG);
        mRequestQueue.add(jsObjRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }
}
