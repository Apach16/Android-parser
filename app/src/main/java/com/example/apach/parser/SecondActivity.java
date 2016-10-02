package com.example.apach.parser;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    ArrayList<JSONData> dataArrayList;

    JSONDataAdapter adapter;

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get JSON
    private static String url = "http://api.pandem.pro/healthcheck/w/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        dataArrayList = new ArrayList<JSONData>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lv = (ListView) findViewById(R.id.list);
        adapter = new JSONDataAdapter(getApplicationContext(), R.layout.list_item, dataArrayList);
        lv.setAdapter(adapter);

        new GetJSONData().execute();
    }

    private class GetJSONData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SecondActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray widgets = jsonObj.getJSONArray("widgets");

                    // looping through All elements
                    for (int i = 0; i < widgets.length(); i++) {
                        JSONObject c = widgets.getJSONObject(i);

                        if(c.getString("type").equals("news")) {
                            String type = c.getString("type");

                            String title = c.getString("title");
                            JSONObject img = c.getJSONObject("img");
                            String url = img.getString("url");
                            String desc = c.getString("desc");

                            JSONData data = new JSONData();
                            data.setType(type);
                            data.setTitle(title);
                            data.setUrl(url);
                            data.setDesc(desc);
                            dataArrayList.add(data);
                        }
                        if(c.getString("type").equals("person")){
                            String type = c.getString("type");

                            JSONObject content = c.getJSONObject("content");

                            JSONArray person = content.getJSONArray("person");
                            JSONObject tmp = person.getJSONObject(0);
                            JSONObject img = tmp.getJSONObject("img");
                            String title = tmp.getString("text");

                            String url = img.getString("url");

                            String desc = content.getString("text");

                            JSONData data = new JSONData();
                            data.setType(type);
                            data.setTitle(title);
                            data.setUrl(url);
                            data.setDesc(desc);
                            dataArrayList.add(data);
                        }
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            adapter.notifyDataSetChanged();
        }

    }
}
