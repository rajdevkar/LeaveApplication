package com.example.demo_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.demo_app.adapters.listAdapter;
import com.example.demo_app.models.Leave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity implements listAdapter.listListener {
    private FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser curUser;

    java.util.List<Leave> List = new ArrayList<>();
    private RecyclerView recyclerView;
    private listAdapter mAdapter;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Toolbar toolbarTop = findViewById(R.id.toolbar);
        setSupportActionBar(toolbarTop);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        curUser = auth.getCurrentUser();

        if (curUser != null)
            uid = curUser.getUid();

        recyclerView = findViewById(R.id.list);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mAdapter = new listAdapter(List, this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        getList();
    }

    private void getList() {
        List.clear();
        mAdapter.notifyDataSetChanged();
        db.collection("Leave")
                .whereEqualTo("approved", "HOD_approved")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Leave leave = new Leave(document.get("uid").toString(), document.get("username").toString(),
                                        document.get("leaveReason").toString(), document.get("fromDate").toString(),
                                        document.get("toDate").toString(), document.get("approved").toString());
                                List.add(leave);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    @Override
    public void onListClickListener(String uid) {
        final String uuid = uid;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Approve?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        db.collection("Leave")
                                .document(uuid)
                                .update("approved", "approved");
                        List.clear();
                        mAdapter.notifyDataSetChanged();
                        getList();
                    }
                });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_two, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                getList();
                return true;
            case R.id.logout:
                auth.signOut();
                startActivity(new Intent(PrincipalActivity.this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
