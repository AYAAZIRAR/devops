package com.example.azirarquiz;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Quiz2 extends AppCompatActivity {
    RadioGroup rg;
    RadioButton rb;
    Button bNext;
    int score;
    String RepCorrect="Inde";
    String imageUrl = "https://drive.google.com/file/d/1pLX2CmlKh22KHHtjX6S89Zt9rIJVn7Av/view?usp=drive_link";  // Remplace par ton URL d'image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz2);
        rg=(RadioGroup) findViewById(R.id.rg);
        bNext=(Button) findViewById(R.id.bNext);
        Intent intent=getIntent();
        score=intent.getIntExtra("score",0) ;

        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb=(RadioButton) findViewById(rg.getCheckedRadioButtonId());
                if(rg.getCheckedRadioButtonId()==-1){
                    Toast.makeText(getApplicationContext(),"Merci de choisir une réponse S.V.P !",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(rb.getText().toString().equals(RepCorrect)){
                        score+=1;
                    }

                    // Enregistrer la réponse dans Firebase Firestore
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                    Map<String, Object> data = new HashMap<>();
                    data.put("reponse", rb.getText().toString());
                    data.put("correct", rb.getText().toString().equals(RepCorrect));
                    data.put("score", score);
                    data.put("image", imageUrl);  // Ajoute l'URL de l'image ici

                    db.collection("quiz_reponses").document(userEmail)
                            .collection("reponses").document("quiz2")
                            .set(data)
                            .addOnSuccessListener(aVoid -> {
                                // Aller à Quiz3 après enregistrement
                                Intent intent = new Intent(Quiz2.this, Quiz3.class);
                                intent.putExtra("score", score);
                                startActivity(intent);
                                overridePendingTransition(R.anim.exit, R.anim.entry);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(Quiz2.this, "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });
    }
}
