package com.example.drake.vtlabstest.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.drake.vtlabstest.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button imageSelection;
    private Button imageUpload;
    private ImageView myImage;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageSelection = (Button)findViewById(R.id.buttonSelect);
        imageUpload = (Button)findViewById(R.id.buttonUpload);

        myImage = (ImageView)findViewById(R.id.imageView);

        imageSelection.setOnClickListener(this);
        imageUpload.setOnClickListener(this);
    }

    private void ImageSelection(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "Choose Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode ==RESULT_OK && data != null && data.getData() != null){
            Uri filePath = data.getData();
            try{
                // Use to get bitmap form gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // set the bitmap in ImageView
                myImage.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    private void ImageUpload (){
        String url = "https://stampload1.000webhostapp.com/API/upload.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }

    }){
            protected Map<String,String> getParams() throws AuthFailureError{
                String image = getStringImage(bitmap);
                String IMAGE_KEY = "image";
                Map<String,String> params = new Hashtable<String,String>();
                params.put(IMAGE_KEY, image);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onClick(View view) {
        if( view == imageSelection){
          ImageSelection();
        }
        if(view == imageUpload){
ImageUpload();
        }
    }
}
