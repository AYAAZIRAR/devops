package com.example.azirarquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.util.HashMap;
import java.util.Map;

public class Score extends AppCompatActivity {
    private Button bLogout, bTry,bLocateme;
    private ProgressBar progressBar;
    private TextView tvScore;
    private int score;
    private static final int TOTAL_QUESTIONS = 5;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // 1) Récupération des vues
        tvScore    = findViewById(R.id.tvScore);
        progressBar = findViewById(R.id.progressBar);
        bLogout    = findViewById(R.id.bLogout);
        bTry       = findViewById(R.id.bTry);

        // 2) Récupération du score brut passé par l’Intent
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);

        // 3) Calcul du pourcentage et affichage
        int pourcentage = Math.round(score * 100f / TOTAL_QUESTIONS);
        progressBar.setMax(100);
        progressBar.setProgress(pourcentage);
        tvScore.setText(pourcentage + " %");

        // 4) Enregistrement du POURCENTAGE dans Firestore
        saveScoreInFirestore(pourcentage);

        // 5) Listeners des boutons
        bLogout.setOnClickListener(v -> {
            Toast.makeText(Score.this, "Merci de votre participation !", Toast.LENGTH_SHORT).show();
            finish();
        });

        bTry.setOnClickListener(v -> {
            startActivity(new Intent(Score.this, Quiz1.class));
            finish();
        });
        // Initialisation du bouton
        bLocateme = findViewById(R.id.bLocateme);

// Lancer MapsActivity au clic
        bLocateme.setOnClickListener(v -> {
            startActivity(new Intent(Score.this, MapsActivity.class));
        });

    }

    private void saveScoreInFirestore(int pourcentage) {
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (userEmail == null) return;

        Map<String, Object> data = new HashMap<>();
        data.put("scoreFinal", pourcentage);

        FirebaseFirestore.getInstance()
                .collection("quiz_reponses")
                .document(userEmail)
                .set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    // Enregistrement OK
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Score.this,
                            "Erreur enregistrement score : " + e.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }
}
