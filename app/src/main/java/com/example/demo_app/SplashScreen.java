package com.example.demo_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SplashScreen extends AppCompatActivity {
    String uid, userType;
    FirebaseUser curUser;
    private FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        curUser = auth.getCurrentUser();

        if (curUser != null)
            uid = curUser.getUid();

        db.collection("Users")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userType = document.get("userType").toString();
                            }
                            if (curUser != null) {
                                if (userType.equals("Student"))
                                    startActivity(new Intent(SplashScreen.this, HomeActivity.class));
                                else if (userType.equals("HOD"))
                                    startActivity(new Intent(SplashScreen.this, HODActivity.class));
                                else if (userType.equals("Principal"))
                                    startActivity(new Intent(SplashScreen.this, PrincipalActivity.class));
                            } else {
                                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                            }
                            finish();
                        }
                    }
                });
    }
}
