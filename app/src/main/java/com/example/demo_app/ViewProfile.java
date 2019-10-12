package com.example.demo_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewProfile extends Fragment {
    private static final String TAG = "ViewProfile";

    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser curUser;

    TextView fname, lname, age, sex, phone, address, dob, experience, emailID, doj;
    String uid, username;


    public ViewProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_view_profile, container, false);

        mAuth = FirebaseAuth.getInstance();

        curUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (curUser != null)
            uid = curUser.getUid();

        fname = v.findViewById(R.id.fname);
        lname = v.findViewById(R.id.lname);
        age = v.findViewById(R.id.age);
        sex = v.findViewById(R.id.sex);
        phone = v.findViewById(R.id.phone);
        address = v.findViewById(R.id.address);
        dob = v.findViewById(R.id.dob);
        experience = v.findViewById(R.id.experience);
        emailID = v.findViewById(R.id.emailID);
        doj = v.findViewById(R.id.doj);

        db.collection("Users").document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            fname.setText(documentSnapshot.getString("fname"));
                            lname.setText(documentSnapshot.getString("lname"));
                            age.setText(documentSnapshot.getString("age"));
                            sex.setText(documentSnapshot.getString("gender"));
                            phone.setText(documentSnapshot.getString("phone"));
                            address.setText(documentSnapshot.getString("addr"));
                            dob.setText(documentSnapshot.getString("dob"));
                            experience.setText(documentSnapshot.getString("yowexp"));
                            emailID.setText(documentSnapshot.getString("email"));
                            doj.setText(documentSnapshot.getString("doj"));
                        }else{
                            Toast.makeText(getContext(), "Please try again later",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Please try again later",
                                Toast.LENGTH_LONG).show();
                    }
                });

        return v;
    }
}
