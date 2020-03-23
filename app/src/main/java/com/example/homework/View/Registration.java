package com.example.homework.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homework.Model.User;
import com.example.homework.R;
import com.example.homework.UserDAO;
import com.example.homework.UserDataBase;

public class Registration extends AppCompatActivity {

    private EditText editTextName,editTextEmail,editTextPhone,editTextPassword,editTextConfirmPassword;
    private Button buttonReg;
    private UserDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextName = findViewById(R.id.edtRegName);
        editTextEmail = findViewById(R.id.edtRegEmail);
        editTextPhone = findViewById(R.id.edtRegPhone);
        editTextPassword = findViewById(R.id.edtRegPassword);
        editTextConfirmPassword = findViewById(R.id.edtRegConfirmPassword);

        buttonReg = findViewById(R.id.btnRegister);

        dao = Room.databaseBuilder(getApplicationContext(), UserDataBase.class,"UserDb")
                .allowMainThreadQueries()
                .build().getUserDao();

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                if (password.equals(confirmPassword)){
                    User user = new User(name,email,phone,password,0);
                    dao.addUser(user);
                    Intent moveToLogin = new Intent(Registration.this,MainActivity.class);
                    startActivity(moveToLogin);
                    finish();
                }else {
                    Toast.makeText(Registration.this, "Password is not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean ValidateEmail(){
        String emailInput = editTextEmail.getText().toString().trim();

        if (emailInput.isEmpty()) {
            editTextEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            editTextEmail.setError("Please enter a valid email address");
            return false;
        } else {
            editTextEmail.setError(null);
            return true;
        }
    }
}
