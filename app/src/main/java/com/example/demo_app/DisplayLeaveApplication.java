package com.example.demo_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class DisplayLeaveApplication extends AppCompatActivity {
    private static final String TAG = "DisplayLeaveApplication";

    private FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser curUser;

    TextView leaveType, leaveReason, fromDate, toDate;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_leave_application_form);
        Intent intent = getIntent();

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        curUser = auth.getCurrentUser();

        if (curUser != null)
            uid = curUser.getUid();

        leaveType = findViewById(R.id.leavetype);
        leaveReason = findViewById(R.id.leavereason);
        fromDate = findViewById(R.id.from);
        toDate = findViewById(R.id.todate);

        db.collection("Leave")
                .whereEqualTo("uid", intent.getStringExtra("leaveid"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                leaveType.setText(document.get("leaveType").toString());
                                leaveReason.setText(document.get("leaveReason").toString());
                                fromDate.setText(document.get("fromDate").toString());
                                toDate.setText(document.get("toDate").toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}

