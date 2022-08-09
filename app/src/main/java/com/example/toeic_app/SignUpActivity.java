package com.example.toeic_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private EditText name, email, password, confirmPass;
    private Button signUpButton;
    private ImageView backB;
    private FirebaseAuth mAuth;
    private String emailStr, passStr, confirmPassStr, nameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.username);
        email = findViewById(R.id.emailID);
        password = findViewById(R.id.passwordSingUp);
        confirmPass = findViewById(R.id.confirm_pass);
        signUpButton = findViewById(R.id.singUpB);
        backB = findViewById(R.id.backB);

        mAuth = FirebaseAuth.getInstance(); // Khởi tạo

        // Bấm vào nút quay lại
        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Bấm đắng ký tài khoản
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Validate()){
                    SignNewUser();
                }
            }
        });
    }

    private boolean Validate(){
        nameStr = name.getText().toString().trim();
        emailStr = email.getText().toString().trim();
        passStr = password.getText().toString().trim();
        confirmPassStr = confirmPass.getText().toString().trim();
        if(nameStr.isEmpty()){
            name.setError("Vui lòng nhập họ tên");
            return false;
        }
        if(emailStr.isEmpty()){
            email.setError("Vui lòng nhập email");
            return false;
        }
        if(passStr.isEmpty()){
            password.setError("Vui lòng nhập mật khẩu");
            return false;
        }
        if(confirmPassStr.isEmpty()){
            confirmPass.setError("Vui lòng nhập lại mật khẩu");
            return false;
        }
        if(passStr.compareTo(confirmPassStr)!=0){
            Toast.makeText(this, "Mật khẩu nhập lại không chính xác", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    // Hàm đăng ký tài khoản với Fire Base
    private  void SignNewUser(){
        mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                    SignUpActivity.this.finish();
                }else{
                    Toast.makeText(SignUpActivity.this, "Email hoặc mật khẩu không đúng định dạng ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}