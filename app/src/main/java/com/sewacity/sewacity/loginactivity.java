package com.sewacity.sewacity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class loginactivity extends AppCompatActivity {

    EditText mobileNumberT,verificationCodeT;
    Button getCodeBtn,verifyBtn;
    FirebaseAuth mAuth;
    String codeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mobileNumberT = findViewById(R.id.mobileNumberID);
        getCodeBtn = findViewById(R.id.getcodeBtnID);
        verifyBtn = findViewById(R.id.getcodeverificationID);

        mAuth = FirebaseAuth.getInstance();
        getCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobileNum = mobileNumberT.getText().toString();
                if (mobileNum.isEmpty()){
                    mobileNumberT.setError("Please enter Mobile number");
                    mobileNumberT.requestFocus();
                    return;
                }else {
                    getVerificationCode();
                }

            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codeText = verificationCodeT.getText().toString();
                if (codeText.isEmpty()){
                    verificationCodeT.setError("Please enter verification Code");
                    verificationCodeT.requestFocus();
                    return;
                }else {
                    codeVerification();
                }

            }
        });

    }
    public void getVerificationCode (){
        String phoneNumber = mobileNumberT.getText().toString();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {


        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            if (e instanceof FirebaseAuthInvalidCredentialsException) {

                // ...
            } else if (e instanceof FirebaseTooManyRequestsException) {

            }


            // ...
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            codeSent = verificationId;

        }
    };

    public void codeVerification (){
        String code = verificationCodeT.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(loginactivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(MainActivity.this,HomeAcitvity.class));

                        } else {
                            Toast.makeText(loginactivity.this, "Login Failed", Toast.LENGTH_SHORT).show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(loginactivity.this, "You entered a wring code", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}

