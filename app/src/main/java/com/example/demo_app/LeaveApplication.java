package com.example.demo_app;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.demo_app.models.Leave;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class LeaveApplication extends Fragment implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "LeaveApplication";

    private FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser curUser;

    Button submit;
    EditText leaveType, leaveReason, fromDate, toDate;
    String str_leaveType, str_leaveReason, str_fromDate, str_toDate;
    int checkFlag = 0;

    String uid, username;

    public LeaveApplication() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.leave_application_form, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        curUser = auth.getCurrentUser();

        if (curUser != null) {
            uid = curUser.getUid();
            String[] For_split_email = curUser.getEmail().split("[@]");
            username = For_split_email[0];
        }

        final ProgressDialog dialog = new ProgressDialog(getContext()); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait.");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        leaveType = v.findViewById(R.id.leave_type);
        leaveReason = v.findViewById(R.id.leave_reason);
        fromDate = v.findViewById(R.id.from_date);
        toDate = v.findViewById(R.id.to_date);
        submit = v.findViewById(R.id.submit);

        fromDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    checkFlag = 0;
                    showDatePickerDialog();
                }
            }
        });
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFlag = 0;
                showDatePickerDialog();
            }
        });
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFlag = 1;
                showDatePickerDialog();
            }
        });
        toDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    checkFlag = 1;
                    showDatePickerDialog();
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                str_leaveType = leaveType.getText().toString();
                str_leaveReason = leaveReason.getText().toString();

                if (str_leaveType.equals("") | str_leaveReason.equals("") | str_fromDate.equals("") | str_toDate.equals("")) {
                    Toast.makeText(getContext(), "Please fill all the fields",
                            Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } else {
                    final String key = db.collection("Leave").document().getId();
                    Leave leave = new Leave(
                            curUser.getUid(),
                            username,
                            key,
                            str_leaveReason,
                            str_leaveType,
                            str_fromDate,
                            str_toDate,
                            "pending"
                    );
                    db.collection("Leave").document(key)
                            .set(leave)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Submitted",
                                            Toast.LENGTH_LONG).show();
                                    Intent displayIntent = new Intent(getContext(), DisplayLeaveApplication.class);
                                    displayIntent.putExtra("leaveid", key);
                                    startActivity(displayIntent);
                                    dialog.dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                    dialog.dismiss();
                                }
                            });
                }
            }
        });

        return v;
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        if (checkFlag == 0) {
            str_fromDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            fromDate.setText(str_fromDate);
        } else if (checkFlag == 1) {
            str_toDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            toDate.setText(str_toDate);
        }

    }

    @Override
    public void onResume() {
        leaveType.setText("");
        leaveReason.setText("");
        fromDate.setText("");
        toDate.setText("");
        super.onResume();
    }
}

