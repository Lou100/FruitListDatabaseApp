package com.example.fruitlistdatabaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.admin.v1beta1.Progress;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    //Views
    EditText descriptionTitle;
    EditText fruitTitle;
    Button saveButton;
    Button showListButton;

    //Progress Dialog
    ProgressDialog pd;


    //Firestore instance
    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //action bar and it's title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Data");



        fruitTitle = (EditText)findViewById(R.id.fruitTitle);
        descriptionTitle = (EditText)findViewById(R.id.descriptionTitle);
        saveButton = (Button)findViewById(R.id.saveButton);
        showListButton = (Button)findViewById(R.id.showListButton);

        //progress dialog
        pd = new ProgressDialog(this);

        //firestore
        db = FirebaseFirestore.getInstance();

        //click button to upload data
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input data

                String title = fruitTitle.getText().toString().trim();
                String description = descriptionTitle.getText().toString().trim();
                //function call to upload
                uploadData(title, description);
            }
        });

        //click button start start List Activity
        showListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListActivity.class));
                finish();

            }
        });



    }

    private void uploadData(String title, String description) {
        //set title of progress bar
        pd.setTitle("Adding Data to Firestore");
        //show progress bar when user clicks save button
        pd.show();
        //random id for each data stored
        String id = UUID.randomUUID().toString().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("id",id);//id of data
        doc.put("title", title);
        doc.put("description", description);

        //add this data

        db.collection("Documents").document(id).set(doc)
              .addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      //this will be called when data is added successfully

                      pd.dismiss();
                      Toast.makeText(MainActivity.this, "Uploaded...",Toast.LENGTH_SHORT).show();

                  }
              })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                     //this will be called if there is any error while uploading

                        pd.dismiss();
                        //get and show error message
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }
}
