package com.example.admin.fuzzauthentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    private TextView txtWelcome;
    private EditText input_new_password;
    private Button btnChangePass, btnLogout;
    private RelativeLayout activty_dashboard;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //view
        txtWelcome = (TextView) findViewById(R.id.dashboard_welcome);
        input_new_password = (EditText) findViewById(R.id.dashboard_new_password);
        btnChangePass = (Button) findViewById(R.id.dashboard_btn_change_pass);
        btnLogout = (Button) findViewById(R.id.dashboard_btn_logout);
        activty_dashboard = (RelativeLayout) findViewById(R.id.activty_dashboard);
        btnChangePass.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        //init firebase
        auth = FirebaseAuth.getInstance();

        //session check
        if(auth.getCurrentUser()!= null)
            txtWelcome.setText("Welcome ,"+auth.getCurrentUser().getEmail());

    }


    @Override
    public void onClick(View view) {
        if(view.getId() ==  R.id.dashboard_btn_change_pass)
            changePassword(input_new_password.getText().toString());
        else if(view.getId() ==  R.id.dashboard_btn_logout)
            logoutUser();
    }

    private void logoutUser() {
        auth.signOut();
        if(auth.getCurrentUser() == null){
            startActivity(new Intent(Dashboard.this,MainActivity.class));
            finish();
        }
    }

    private void changePassword(String newPassword) {
        FirebaseUser user = auth.getCurrentUser();
        user.updatePassword(newPassword).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Snackbar snackbar = Snackbar.make(activty_dashboard,"Password Changed",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
    }
}
