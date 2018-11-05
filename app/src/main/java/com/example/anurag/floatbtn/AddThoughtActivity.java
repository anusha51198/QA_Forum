package com.example.anurag.floatbtn;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddThoughtActivity extends AppCompatActivity {

    public String THOUGHT_REF = "Thoughts";
    EditText editText1,editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thought);


        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);

        Button button = (Button) findViewById(R.id.postButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,Object> data = new HashMap<>();
                data.put("addThoughtText",editText2.getText().toString());
                data.put("timestamp",FieldValue.serverTimestamp());
                data.put("Usename",editText1.getText().toString());


                FirebaseFirestore.getInstance().collection(THOUGHT_REF)
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Error: " , e.toString());
                                Toast toast =Toast.makeText(getApplicationContext(),"Check again",Toast.LENGTH_LONG);
                                toast.setMargin(50,50);
                                toast.show();
                            }
                        });

            }
        });



    }


}
