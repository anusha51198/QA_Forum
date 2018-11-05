package com.example.anurag.floatbtn;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.annotation.Nullable;

import io.opencensus.tags.Tag;

public class MainActivity extends AppCompatActivity {
    public String THOUGHT_REF="Thoughts";
    private String text1,text2,text3;
    private long time;
    CollectionReference thoughtsCollectionRef = FirebaseFirestore.getInstance().collection(THOUGHT_REF);
    ArrayList<Thought> elements = new ArrayList<Thought>();

   ArrayList<String> idData = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddThoughtActivity.class);
                startActivity(intent);
            }
        });

        thoughtsCollectionRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {


                            for (final QueryDocumentSnapshot document : task.getResult())
                            {
                                //Object data1 = document.getData();
                                text1 = (String) document.get("Usename");
                                text2 = (String) document.get("addThoughtText");
                                text3 = (String) document.get("timestamp").toString();
                                Thought t = new Thought(text1,text2,text3);

                                elements.add(t);

                                idData.add(document.getId());

                            }

                            final ListView listView = (ListView) findViewById(R.id.list_View);
                            CustomAdapter customAdapter = new CustomAdapter(MainActivity.this,elements);

                            listView.setAdapter(customAdapter);

                            ListView listView2 = (ListView) findViewById(R.id.list_View);
                            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    for (int i=0;i<idData.size();i++) {
                                        if (i==position) {
                                            Intent intent = new Intent(MainActivity.this, Answer.class);
                                            intent.putExtra("anscol", idData.get(i));
                                            startActivity(intent);
                                        }
                                    }
                                }
                            });







                        }
                    }
                });


        thoughtsCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                for (final DocumentChange dc:queryDocumentSnapshots.getDocumentChanges())
                   {
                       switch (dc.getType())
                       {
                           case MODIFIED:
                               text1 = (String) dc.getDocument().get("Usename");
                               text2 = (String) dc.getDocument().get("addThoughtText");
                               text3 = (String) dc.getDocument().get("timestamp").toString();
                               Thought tt = new Thought(text1,text2,text3);
                               elements.add(tt);
                               idData.add(dc.getDocument().getId());

                               ListView listView = (ListView) findViewById(R.id.list_View);
                               CustomAdapter customAdapter = new CustomAdapter(MainActivity.this,elements);
                               listView.setAdapter(customAdapter);

                               listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                   @Override
                                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                       for (int i = 0; i < idData.size(); i++) {
                                           if (i == position) {
                                               Intent intent = new Intent(MainActivity.this, Answer.class);
                                               intent.putExtra("anscol", idData.get(i));
                                               startActivity(intent);
                                           }
                                       }
                                   }
                               });



                       }

                   }

            }
        });






        //ArrayList<Thought> elements = new ArrayList<Thought>();




        //elements.add(new Thought("C","D"));


//
//        CustomAdapter customAdapter = new CustomAdapter(this,elements);
//
//
//        ListView listView = (ListView) findViewById(R.id.list_View);
//
//        listView.setAdapter(customAdapter);

          //Collection   Doc         Collec            Doc            Collec
        //  Thoughts    Documents     Info Q and A    Que info         Answer info
                                                     // Answer docs



    }



}
