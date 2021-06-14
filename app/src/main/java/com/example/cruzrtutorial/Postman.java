package com.example.cruzrtutorial;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class Postman {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Response postfile(String pathSave, String outputStream) {
        File myFile = new File(pathSave);
        try {
            URI myURI = new URI(pathSave);
            myFile = new File(myURI);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.d(TAG, "Not a URIIIII");
        }

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("", pathSave,
                        RequestBody.create(MediaType.parse("application/octet-stream"), myFile))
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.1.52:3000/upload_file_v3")
                .method("POST", body)
                .addHeader("header", "value")
                .build();
        try {
            Response response = client.newCall(request).execute();
            FileOutputStream f = new FileOutputStream(outputStream);
            InputStream in = response.body().byteStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();
            in.close();

        } catch (Exception e) {
            Log.d(TAG, "Postman exception");
            Log.d(TAG, "ResponsePostman exception: " + request.body());
            e.printStackTrace();
        }

        return null;
    }


}