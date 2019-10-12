package com.example.demo_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    TextView tv_signup;
    EditText et_username, et_password;
    Button btn_login;
    String str_email, str_pass;
    String uid, userType;

    private FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait.");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        et_username = findViewById(R.id.username);
        et_password = findViewById(R.id.pwd);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_email = et_username.getText().toString();
                str_pass = et_password.getText().toString();
                if (str_email.equals("") | str_pass.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please fill all the fields",
                            Toast.LENGTH_LONG).show();
                } else {
                    dialog.show();
                    auth.signInWithEmailAndPassword(str_email, str_pass)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        dialog.dismiss();
                                        if (str_pass.length() < 6) {
                                            Toast.makeText(LoginActivity.this, "password length short", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        dialog.dismiss();
                                        FirebaseUser curUser = auth.getInstance().getCurrentUser();
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
                                                            if (userType.equals("Student")) {
                                                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                                                finish();
                                                            } else if (userType.equals("HOD")) {
                                                                startActivity(new Intent(LoginActivity.this, HODActivity.class));
                                                                finish();
                                                            } else if (userType.equals("Principal")) {
                                                                startActivity(new Intent(LoginActivity.this, PrincipalActivity.class));
                                                                finish();
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                }
            }
        });

        tv_signup = findViewById(R.id.tv_sign_up);
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                startActivity(myIntent);
            }
        });

    }
}