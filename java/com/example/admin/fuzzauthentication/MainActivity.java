package com.example.admin.fuzzauthentication;

import android.content.Intent;
import android.content.Loader;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin;
    EditText input_email,input_password;
    TextView btnSignup,btnForgotPass;
    DatabaseHelper helper=new DatabaseHelper(this);

    RelativeLayout activity_main;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //view
        btnLogin = (Button) findViewById(R.id.login_btn_login);
        input_email = (EditText) findViewById(R.id.login_email);
        input_password = (EditText) findViewById(R.id.login_password);
        btnSignup = (TextView) findViewById(R.id.login_btn_signup);
        btnForgotPass = (TextView) findViewById(R.id.login_btn_forgot_password);
        activity_main = (RelativeLayout) findViewById(R.id.activity_main);

        btnSignup.setOnClickListener(this);
        btnForgotPass.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        //init firebase auth
        auth = FirebaseAuth.getInstance();

        //check session
        if (auth.getCurrentUser()!= null )
            startActivity(new Intent(MainActivity.this,Dashboard.class));

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.login_btn_forgot_password)
        {
            startActivity(new Intent(MainActivity.this, ForgotPassword.class));
            finish();
        }
        else if(v.getId() == R.id.login_btn_signup)
        {
            startActivity(new Intent(MainActivity.this,SignUp.class));
            finish();
        }
        else if(v.getId() == R.id.login_btn_login)
        {
            String login_email = input_email.getText().toString();
            String login_password = input_password.getText().toString();

            String pass=helper.searchPass(login_email);


            if (pass.equals(login_password) )
            {
                Intent i = new Intent(getApplicationContext(), Applist.class);
                i.putExtra("Email",login_email);
                startActivity(i);
                // Closing regstration screen
                // Switching to Login Screen/closing register screen
            }

            else

            {Toast.makeText(MainActivity.this, "Invalid Credentials",Toast.LENGTH_SHORT).show();

            loginUSer(input_email.getText().toString(),input_password.getText().toString());}
           // startActivity(new Intent(MainActivity.this,Dashboard.class));
                }
      }



    private void loginUSer(String email, final String password) {
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            if (password.length() > 15 || password.length() < 8)
                            {
                                Snackbar snackbar = Snackbar.make(activity_main,"Password should be less than 15 and more than 8 characters in length.",Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }

                            String upperCaseChars = "(.*[A-Z].*)";
                            if (!password.matches(upperCaseChars ))
                            {
                                Snackbar snackbar = Snackbar.make(activity_main,"Password should contain atleast one upper case alphabet",Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                            String lowerCaseChars = "(.*[a-z].*)";
                            if (!password.matches(lowerCaseChars ))
                            {
                                Snackbar snackbar = Snackbar.make(activity_main,"Password should contain atleast one lower case alphabet.",Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                            String numbers = "(.*[0-9].*)";
                            if (!password.matches(numbers ))
                            {
                                Snackbar snackbar = Snackbar.make(activity_main,"Password should contain atleast one number.",Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                            String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
                            if (!password.matches(specialChars ))
                            {
                                Snackbar snackbar = Snackbar.make(activity_main,"Password should contain atleast one special character.",Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }

                            else{
                                startActivity(new Intent(MainActivity.this,Dashboard.class));
                            }
                        }
                    }
                });
    }
}
