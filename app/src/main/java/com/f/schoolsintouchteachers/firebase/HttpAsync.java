package com.f.schoolsintouchteachers.firebase;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HttpAsync extends AsyncTask<String,String,String> {
    Context context;
    String message,token;
    private Response response;
    private StringBuilder sb;


    public HttpAsync(String message,String token) {
        this.message=message;
        this.token=token;
    }

    @Override
    protected String doInBackground(String... voids)  {



            return doing();


    }

    private String doing() {
        URL url= null;
        try {
            url = new URL("http://10.0.2.2:5000/schoolsintouch-d84bb/us-central1/helloWorld");
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("message",message);
            jsonObject.put("token",token);
            HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();

            OutputStream os = urlConnection.getOutputStream();
            os.write(jsonObject.toString().getBytes("UTF-8"));
           // os.close();
            os.flush();

            sb = new StringBuilder();
            int HttpResult = urlConnection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                urlConnection.disconnect();
                return sb.toString();
            } else {
                urlConnection.disconnect();
                return urlConnection.getResponseMessage();

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

//            OkHttpClient httpClient=new OkHttpClient();
//            RequestBody formBody =new
//                    .add("message", message) // A sample POST field
//                    .add("token", token) // Another sample POST field
//                    .build();
//            Request request = new Request.Builder()
//                    .url("http://localhost:5000/schoolsintouch-d84bb/us-central1/helloWorld")
//                    .post(formBody)
//                    .build();
//            response = httpClient.newCall(request).execute();
//
//
//        return response.body().string();
        return sb.toString();
    }


}
