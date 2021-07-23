package com.naffi.applecare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Detection extends AppCompatActivity {
    String encodedimage;
    private static final String apiurl = "http://192.168.1.9/getfiles/fileupload.php";

    // Variables used
    TextView detectedDisease;
    TextView diseaseName;
    TextView diseaseClass;

    String titles[] = {"I am a programmer","I am a developer","Hello I am naffi"};
    String diseaseClasses[] = {"Insect","Virus","Fungus"};
    String descriptions[] = {"Hello I am naffi and I am a programmer","Hello I am naffi and I am a developer","Hello I am naffi and I am a Engineer"};
    ListView lvSymptoms;
    ListView lvNCSolutions;
    ListView lvRecommendations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);
        Bundle bundle  = getIntent().getExtras();
        String u = bundle.getString("imageUri");
        Uri fileUri = Uri.parse(u) ;
        ImageView iv_disease = findViewById(R.id.iv_disease_photp);
        iv_disease.setImageURI(fileUri);

        //Variable initialization
        detectedDisease = findViewById(R.id.tv_detectedDisease);
        diseaseName = findViewById(R.id.tv_diseaseName);
        diseaseClass = findViewById(R.id.tv_diseaseClass);
        lvSymptoms = findViewById(R.id.lv_symptoms);
        lvNCSolutions = findViewById(R.id.lv_ncSolutions);

        lvRecommendations=findViewById(R.id.lv_recommendations);
        //MyAdopterRecommendations adptrR = new MyAdopterRecommendations(this,titles,descriptions,diseaseClasses);
        //lvRecommendations.setAdapter(adptrR);


        //toBitmap
        Bitmap bitmap = null;
        Bitmap mybitmap = null;
        try {
            mybitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),fileUri);
//            Matrix matrix = new Matrix();
//            matrix.setRotate(90);
//            bitmap = Bitmap.createBitmap(mybitmap, 0, 0, mybitmap.getWidth(), mybitmap.getHeight(), matrix, true); // rotating bitmap

        } catch (IOException e) {
            Toast.makeText(this, "something wrong", Toast.LENGTH_SHORT).show();
        }
        encodeBitmap(mybitmap);

        try {
            uploadtoserver();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void encodeBitmap(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] byteofimages = byteArrayOutputStream.toByteArray();
        encodedimage = android.util.Base64.encodeToString(byteofimages, Base64.DEFAULT);
    }
    public void uploadtoserver() throws JSONException {

        JSONObject jsonImageObject = new JSONObject();
        jsonImageObject.put("image_name","Clicked");
        jsonImageObject.put("image",encodedimage);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apiurl, jsonImageObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // Log.d("ahanger","InResponse");
                //Toast.makeText(Detection.this, response.toString(), Toast.LENGTH_SHORT).show();
                // response used
                try {
                    detectedDisease.setText("Detected disease");
                    diseaseName.setText(response.getString("disease_name"));
                    diseaseClass.setText(response.getString("disease_class"));

                    //Symptoms
                    JSONArray symptomsArray=response.getJSONArray("symptoms");
                    //Toast.makeText(Detection.this, symptomsArray.toString(), Toast.LENGTH_SHORT).show();
                    String[] javaSymptoms = new String[symptomsArray.length()];
                     for(int i=0; i<symptomsArray.length(); i++) {
                            javaSymptoms[i]=symptomsArray.getString(i);
                            //Log.d("ahanger",javaSymptoms[i]);
                     }
                    MyAdopter adptrSymptoms = new MyAdopter(getApplicationContext(), javaSymptoms);
                    lvSymptoms.setAdapter(adptrSymptoms);

                    //for non-chemical solutions
                    JSONArray ncSolutionsArray=response.getJSONArray("nc_solutions");
                    String[] javaNCSolutions = new String[ncSolutionsArray.length()];
                    for(int i=0; i<ncSolutionsArray.length(); i++) {
                        javaNCSolutions[i]=ncSolutionsArray.getString(i);
                        //Log.d("ahanger",javaSymptoms[i]);
                    }
                    MyAdopter adptrNCSolutions = new MyAdopter(getApplicationContext(), javaNCSolutions);
                    lvNCSolutions.setAdapter(adptrNCSolutions);
                    //for recommendations
                    JSONArray recommendationsJson=response.getJSONArray("recommendations");
//                    Toast.makeText(Detection.this, recommendationsJson.toString(), Toast.LENGTH_SHORT).show();
                    String[] recTitles = new String[recommendationsJson.length()];
                    String[] recDescriptions = new String[recommendationsJson.length()];
                    String[] recClasses = new String[recommendationsJson.length()];
                    for(int i=0; i<recommendationsJson.length(); i++) {
                        JSONObject obj=recommendationsJson.getJSONObject(i);
                        recTitles[i]=obj.getString("title");
                        recDescriptions[i]=obj.getString("description");
                        recClasses[i]=obj.getString("class");
                    }
                    MyAdopterRecommendations adptrR = new MyAdopterRecommendations(getApplicationContext(),recTitles,recDescriptions,recClasses);
                    lvRecommendations.setAdapter(adptrR);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(Detection.this, "inerror", Toast.LENGTH_SHORT).show();
                Log.d("ahanger",error.toString());
            }
        });


        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(jsonObjectRequest);
    }
}
class MyAdopter extends ArrayAdapter<String> {
    Context context;
    String tt1[];

    public MyAdopter(Context c, String tt1[]) {
        super(c, R.layout.myrow,R.id.symptoms1,tt1);
        context = c;
        this.tt1 = tt1;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.myrow,parent,false);
        TextView tv1 = row.findViewById(R.id.symptoms1);

        tv1.setText(tt1[position]);

        return row;
    }
}

class MyAdopterRecommendations extends ArrayAdapter<String> {
    Context context;
    String tt1[];
    String dsc[];
    String classes[];

    public MyAdopterRecommendations(Context c, String tt1[],String dsc[],String classes[]) {
        super(c, R.layout.myrow_recommendations,R.id.recommendCard1Heading17,tt1);
        context = c;
        this.tt1 = tt1;
        this.dsc = dsc;
        this.classes = classes;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.myrow_recommendations,parent,false);
        TextView tv1 = row.findViewById(R.id.recommendCard1Heading17);
        TextView tv2 = row.findViewById(R.id.recommendCard1Description17);
        TextView tv3 = row.findViewById(R.id.mybutton117);

        tv1.setText(tt1[position]);
        tv2.setText(dsc[position]);
        tv3.setText(classes[position]);
        return row;
    }
}
