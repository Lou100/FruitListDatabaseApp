package com.example.fruitlistdatabaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    List<Model> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    //layout manager for recyclerview
    RecyclerView.LayoutManager layoutManager;

    Button addButton;

    //firestore instance
    FirebaseFirestore db;

    CustomAdapter adapter;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //init firestore
        db = FirebaseFirestore.getInstance();


        //initialize views
        mRecyclerView = findViewById(R.id.recycler_view);
        addButton = (Button) findViewById(R.id.addButton);

        //set recycler view properties
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //init Progress Dialog
        pd = new ProgressDialog(this);

        //show data in recyclerView
        showData();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void showData() {
       //set title of progress dialog
       pd.show();
        db.collection("Documents")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    //called when data is retrieved
                        pd. dismiss();
                        //show data
                        for (DocumentSnapshot doc: task.getResult()) {
                            Model model = new Model(doc.getString("id"),
                                    doc.getString("title"),
                                    doc.getString("description"));
                            modelList.add(model);

                        }
                        //adapter
                        adapter = new CustomAdapter(ListActivity.this, modelList);
                        //set adapter to recyclerview
                        mRecyclerView.setAdapter(adapter);

                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    //called when there is any error while retrieving
                        pd.dismiss();

                        Toast.makeText(ListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
