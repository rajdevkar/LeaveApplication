package com.example.demo_app;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demo_app.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "RegisterAcivity";

    EditText editFName, editLName, editAge, editPhone, editAddress, editDOB, editExperience, editEmail, editDOJ, password, confirmPassword;
    RadioGroup radioGroupSex;
    RadioButton radioButton;
    Button btn_submit;
    Spinner userType;

    int checkFlag = 0;
    String str_fname, str_lname, str_age, str_gender,
            str_phone, str_addr, str_dob, str_yowexp, str_email,
            str_doj, str_pass, str_confirmpass, str_userType;
    String editDOBString, editDOJString;
    String uid, username;
    String userTypeArray[] = {"Student", "HOD", "Principal"};

    private FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        final ProgressDialog dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Creating");
        dialog.setMessage("Please wait.");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        editFName = findViewById(R.id.editFName);
        editLName = findViewById(R.id.editLName);
        editAge = findViewById(R.id.editAge);
        editPhone = findViewById(R.id.editPhone);
        editAddress = findViewById(R.id.editAddress);
        editDOB = findViewById(R.id.editDOB);
        editExperience = findViewById(R.id.editExperience);
        editEmail = findViewById(R.id.editEmail);
        editDOJ = findViewById(R.id.editDOJ);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        radioGroupSex = findViewById(R.id.radioGroupSex);
        btn_submit = findViewById(R.id.btn_submit);

        SpinnerSetup();

        editDOB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    checkFlag = 0;
                    showDatePickerDialog();
                }
            }
        });
        editDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFlag = 0;
                showDatePickerDialog();
            }
        });
        editDOJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFlag = 1;
                showDatePickerDialog();
            }
        });
        editDOJ.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    checkFlag = 1;
                    showDatePickerDialog();
                }
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroupSex.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);

                str_fname = editFName.getText().toString();
                str_lname = editLName.getText().toString();
                str_age = editAge.getText().toString();
                str_gender = radioButton.getText().toString();
                str_phone = editPhone.getText().toString();
                str_addr = editAddress.getText().toString();
                str_dob = editDOB.getText().toString();
                str_yowexp = editExperience.getText().toString();
                str_email = editEmail.getText().toString();
                str_doj = editDOJ.getText().toString();
                str_pass = password.getText().toString();
                str_confirmpass = confirmPassword.getText().toString();

                if (str_fname.equals("") | str_lname.equals("") | str_age.equals("") | str_phone.equals("") |
                        str_addr.equals("") | str_dob.equals("") | str_yowexp.equals("") | str_email.equals("") |
                        str_doj.equals("") | str_pass.equals("") | str_confirmpass.equals("") | radioGroupSex.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(RegisterActivity.this, "Please fill all the fields",
                            Toast.LENGTH_LONG).show();
                } else {
                    if (!isValidEmail(str_email)) {
                        editEmail.setError("Invalid Email");
                    } else if (str_pass.trim().length() < 8) {
                        password.setError("Minimum 8 characters");
                    } else {
                        if (!str_pass.equals(str_confirmpass)) {
                            Log.d(TAG, str_pass + " " + str_confirmpass);
                            confirmPassword.setError("Passwords Don't match");
                        } else {
                            dialog.show();
                            auth.createUserWithEmailAndPassword(str_email, str_confirmpass)
                                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "Try again later",
                                                        Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            } else {
                                                FirebaseUser curUser = auth.getInstance().getCurrentUser();
                                                if (curUser != null) {
                                                    uid = curUser.getUid();
                                                    String[] For_split_email = str_email.split("[@]");
                                                    username = For_split_email[0];
                                                }
                                                User user = new User(
                                                        uid,
                                                        str_fname,
                                                        str_lname,
                                                        username,
                                                        str_age,
                                                        str_gender,
                                                        str_phone,
                                                        str_addr,
                                                        str_dob,
                                                        str_yowexp,
                                                        str_email,
                                                        str_doj,
                                                        str_userType
                                                );
                                                db.collection("Users")
                                                        .document(uid)
                                                        .set(user);
                                                if (str_userType.equals("Student"))
                                                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                                else if (str_userType.equals("HOD"))
                                                    startActivity(new Intent(RegisterActivity.this, HODActivity.class));
                                                else if (str_userType.equals("Principal"))
                                                    startActivity(new Intent(RegisterActivity.this, PrincipalActivity.class));
                                                finish();
                                                dialog.dismiss();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, e.toString());
                                        }
                                    });
                        }
                    }
                }
            }
        });

    }

    private void SpinnerSetup() {
        userType = findViewById(R.id.userType);
        userType.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, userTypeArray);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userType.setAdapter(aa);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        str_userType = userTypeArray[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        str_userType = userTypeArray[0];
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
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
            editDOBString = dayOfMonth + "/" + (month + 1) + "/" + year;
            editDOB.setText(editDOBString);
        } else if (checkFlag == 1) {
            editDOJString = dayOfMonth + "/" + (month + 1) + "/" + year;
            editDOJ.setText(editDOJString);
        }
    }

}
