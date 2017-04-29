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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    Button btnSignUp;
    TextView btnLogin,btnForgotPass;
    EditText input_email,input_pass,userid,cpwd;
    RelativeLayout activity_sign_up;
    DatabaseHelper helper=new DatabaseHelper(this);


    private FirebaseAuth auth;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //view
        userid = (EditText) findViewById(R.id.uid);
        cpwd = (EditText) findViewById(R.id.cpwd); 
        btnSignUp = (Button) findViewById(R.id.signup_btn_register);
        btnLogin = (TextView) findViewById(R.id.signup_btn_login);
        btnForgotPass = (TextView) findViewById(R.id.signup_btn_forgot_pass);
        input_email = (EditText) findViewById(R.id.signup_email);
        input_pass = (EditText) findViewById(R.id.signup_password);
        activity_sign_up = (RelativeLayout) findViewById(R.id.activity_sign_up);

        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnForgotPass.setOnClickListener(this);

        //init Firebase
        auth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
            if(view.getId() == R.id.signup_btn_login){
                startActivity(new Intent(SignUp.this,MainActivity.class));
                finish();
            }
            else  if(view.getId() == R.id.signup_btn_forgot_pass) {
                startActivity(new Intent(SignUp.this,ForgotPassword.class));
                finish();
            }
            else  if(view.getId() == R.id.signup_btn_register) {
                SignUpUSer(input_email.getText().toString(),input_pass.getText().toString());

                //new
                boolean flag=false;
                boolean flag2=false;

                String username = userid.getText().toString();
                String password = input_pass.getText().toString();
                String cpassword = cpwd.getText().toString();
                String emailid = input_email.getText().toString();


                flag= passwordValidation(username,password,cpassword,emailid);

                if(flag==true)
                {
                    Contact c=new Contact();

                    c.setPass(password);
                    c.setUid(username);
                    c.setEmail(emailid);

                    helper.insertData(c);



                    Toast.makeText(SignUp.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    // Closing regstration screen
                    // Switching to Login Screen/closing register screen
                    finish();

                }

                else
                    Toast.makeText(SignUp.this, "Registration unsuccessful.", Toast.LENGTH_SHORT).show();
            }

    }

    private boolean passwordValidation(String username, String password, String cpassword, String emailid) {
        {

            boolean valid = true;

            if (username.length() < 3) {
                Toast.makeText(SignUp.this, "Username should be more than 3 characters in length.", Toast.LENGTH_LONG).show();
                return false;
            }
            if (password.length() > 15 || password.length() < 8) {
                Toast.makeText(SignUp.this, "Password should be less than 15 and more than 8 characters in length.", Toast.LENGTH_LONG).show();
                return false;
            }

            String upperCaseChars = "(.*[A-Z].*)";
            if (!password.matches(upperCaseChars)) {
                Toast.makeText(SignUp.this, "Password should contain atleast one upper case alphabet.", Toast.LENGTH_LONG).show();
                return false;
            }
            String lowerCaseChars = "(.*[a-z].*)";
            if (!password.matches(lowerCaseChars)) {
                Toast.makeText(SignUp.this, "Password should contain atleast one lower case alphabet.", Toast.LENGTH_LONG).show();
                return false;
            }
            String numbers = "(.*[0-9].*)";
            if (!password.matches(numbers)) {
                Toast.makeText(SignUp.this, "Password should contain atleast one number.", Toast.LENGTH_LONG).show();
                return false;
            }
            String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
            if (!password.matches(specialChars)) {
                Toast.makeText(SignUp.this, "Password should contain atleast one special character.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (!password.matches(cpassword)) {
                Toast.makeText(SignUp.this, "Passwords dont match.", Toast.LENGTH_LONG).show();
                return false;
            }
            if (username == "" && password == "" && cpassword == "" && emailid == "") {
                Toast.makeText(SignUp.this, "Missing field", Toast.LENGTH_LONG).show();
                return false;
            }

            return valid;
        }
    }

    private void SignUpUSer(String email, final String password) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            snackbar = Snackbar.make(activity_sign_up,"Error: "+task.getException(),Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                        /*if (password.length() > 15 || password.length() < 8)
                        {
                            Snackbar snackbar = Snackbar.make(activity_sign_up,"Password should be less than 15 and more than 8 characters in length.",Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }

                        String upperCaseChars = "(.*[A-Z].*)";
                        if (!password.matches(upperCaseChars ))
                        {
                            Snackbar snackbar = Snackbar.make(activity_sign_up,"Password should contain atleast one upper case alphabet",Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                        String lowerCaseChars = "(.*[a-z].*)";
                        if (!password.matches(lowerCaseChars ))
                        {
                            Snackbar snackbar = Snackbar.make(activity_sign_up,"Password should contain atleast one lower case alphabet.",Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                        String numbers = "(.*[0-9].*)";
                        if (!password.matches(numbers ))
                        {
                            Snackbar snackbar = Snackbar.make(activity_sign_up,"Password should contain atleast one number.",Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                        String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
                        if (!password.matches(specialChars ))
                        {
                            Snackbar snackbar = Snackbar.make(activity_sign_up,"Password should contain atleast one special character.",Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                        */
                        else{
                            snackbar = Snackbar.make(activity_sign_up,"Register Success: ",Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }

                    }
                });
    }
}
