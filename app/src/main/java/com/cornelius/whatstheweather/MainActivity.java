package com.cornelius.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    TextView textView1 ;
    TextView textView2 ;
    EditText editText;

    public void whatsTheWeather(View view ){


        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);

        String city ;

        city = editText.getText().toString();


        try {
            String encodedCity = URLEncoder.encode(city,"UTF-8");

            WeatherData task = new WeatherData();

            task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + encodedCity + "&APPID=4922915c5611da71898ae9e258227992");
        }
        catch (Exception e){

            Toast.makeText(MainActivity.this, "Could not find weather", Toast.LENGTH_LONG).show();

            e.printStackTrace();

        }
    }

    public class WeatherData extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            String result ="";

            URL url;

            HttpURLConnection httpURLConnection;

            try{

                url = new URL(urls[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream in = httpURLConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1){

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }

                return result;

            }
            catch (Exception e){

                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Could not find weather", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                JSONObject jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");

                JSONArray arr = new JSONArray(weatherInfo);

                for (int i =0 ; i < arr.length(); i++){

                    JSONObject jsonPart = arr.getJSONObject(i);

                    textView1.setText("Weather:"+jsonPart.getString("main"));
                    textView2.setText("Description:"+jsonPart.getString("description"));

                }

            } catch (Exception e) {

                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Could not find weather", Toast.LENGTH_LONG).show();

            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        editText = findViewById(R.id.editText);

    }
}