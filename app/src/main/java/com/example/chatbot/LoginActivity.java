package com.example.chatbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference RootRef;
    private Button logInButton;
    private EditText userEmail, userPassword;
    private TextView forgetPass;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
//        currentUser = mAuth.getCurrentUser();
        RootRef = FirebaseDatabase.getInstance().getReference();
        InitializerFields();

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToLogin();
            }
        });
    }

    private void InitializerFields() {
        logInButton = findViewById(R.id.login_button);
        userEmail = findViewById(R.id.login_id);
        userPassword = findViewById(R.id.login_password);
        forgetPass = findViewById(R.id.forget_password_link);
        loadingBar = new ProgressDialog(this);
    }

    private void AllowUserToLogin() {
        String email = userEmail.getText().toString();
        String pass = userPassword.getText().toString();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter Your ID", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Enter Password to LogIn", Toast.LENGTH_LONG).show();
        }
        else {
            loadingBar.setTitle("Sign In");
            loadingBar.setMessage("Wait a Moment to Enter this Beautiful World...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendUserToMainActivity();
                                Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }
                            else {
                                String message = task.getException().toString();
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }

    }

    private void sendUserToMainActivity() {
        Intent mainInten = new Intent(LoginActivity.this, MainActivity.class);
        mainInten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainInten);
        finish();
    }
}
