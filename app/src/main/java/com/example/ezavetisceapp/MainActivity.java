package com.example.ezavetisceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private TextView zivali;
    private String url = "https://dev-e-zavetisce.azurewebsites.net/api/v1/Pets";

    public static final String EXTRA_MESSAGE = "com.example.ezavetisceapp.MESSAGE";

    public void addPetActivity (View view) {
        Intent intent = new Intent(this,AddPetActivity.class);
        String message = "Dodaj zival v seznam.";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        zivali = (TextView) findViewById(R.id.zivali);
    }

    public void prikaziZivali(View view) {
        if (view != null) {
            JsonArrayRequest request = new JsonArrayRequest(url, jsonArrayListener, errorListener);
            requestQueue.add(request);
        }
    }

    private Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            ArrayList<String> data = new ArrayList<>();

            for (int i = 0; i < response.length(); i++) {
                try {
                   JSONObject object = response.getJSONObject(i);
                   String name = object.getString("name");
                   String dateAdded = object.getString("dateAdded");
                   String adopted = object.getString("adopted");

                   data.add(name + " " + dateAdded + " " + "posvojena: " +  adopted);

                } catch (JSONException e) {
                    e.printStackTrace();
                    return;

                }
            }

            zivali.setText("");

            for (String row: data) {
                String currentText = zivali.getText().toString();
                zivali.setText(currentText + "\n\n" + row);
            }

        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("REST error", error.getMessage());
        }
    };
}