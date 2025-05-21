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

public class Quiz3 extends AppCompatActivity {
    RadioGroup rg;
    RadioButton rb;
    Button bNext;
    int score;
    String RepCorrect = "France";
    String imageUrl = "https://drive.google.com/file/d/15hQkvzEv11Aip7HpC5A5X9SHVPFgQGO-/view?usp=drive_link"; // Remplace cette URL par la tienne

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz3);

        rg = (RadioGroup) findViewById(R.id.rg);
        bNext = (Button) findViewById(R.id.bNext);
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);

        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb = (RadioButton) findViewById(rg.getCheckedRadioButtonId());

                if (rg.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Merci de choisir une réponse S.V.P !", Toast.LENGTH_SHORT).show();
                } else {
                    if (rb.getText().toString().equals(RepCorrect)) {
                        score += 1;
                    }

                    // Enregistrement dans Firestore
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                    Map<String, Object> data = new HashMap<>();
                    data.put("reponse", rb.getText().toString());
                    data.put("correct", rb.getText().toString().equals(RepCorrect));
                    data.put("score", score);
                    data.put("image", imageUrl); // Ajout du lien de l'image

                    db.collection("quiz_reponses").document(userEmail)
                            .collection("reponses").document("quiz3")
                            .set(data)
                            .addOnSuccessListener(aVoid -> {
                                Intent intent = new Intent(Quiz3.this, Quiz4.class);
                                intent.putExtra("score", score);
                                startActivity(intent);
                                overridePendingTransition(R.anim.exit, R.anim.entry);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(Quiz3.this, "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });
    }
}
