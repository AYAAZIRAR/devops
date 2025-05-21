package com.example.azirarquiz;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Quiz1 extends AppCompatActivity {

    RadioGroup rg;
    RadioButton rb;
    Button bNext;
    int score = 0;
    String RepCorrect = "États-Unis";
    String imageName = "img_2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1);

        rg = findViewById(R.id.rg);
        bNext = findViewById(R.id.bNext);

        // Affichage de l'image depuis drawable
        ImageView imageView = findViewById(R.id.imageViewQuiz);
        imageView.setImageResource(R.drawable.img_2);  // Affiche l'image img_2 depuis drawable

        // Définir l'URL de l'image que tu veux stocker dans Firestore
        String imageUrl = "https://drive.google.com/file/d/1f6InVAHSszBMjzOU6YuZDXyXaPTOHasX/view?usp=drive_link";

        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rg.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Merci de choisir une réponse S.V.P !", Toast.LENGTH_SHORT).show();
                } else {
                    rb = findViewById(rg.getCheckedRadioButtonId());

                    // Vérifier la réponse
                    if (rb.getText().toString().equals(RepCorrect)) {
                        score += 1;
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
                            .collection("reponses").document("quiz1")
                            .set(data)
                            .addOnSuccessListener(aVoid -> {
                                // Aller à Quiz2 après enregistrement
                                Intent intent = new Intent(Quiz1.this, Quiz2.class);
                                intent.putExtra("score", score);
                                startActivity(intent);
                                overridePendingTransition(R.anim.exit, R.anim.entry);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(Quiz1.this, "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });
    }
}
