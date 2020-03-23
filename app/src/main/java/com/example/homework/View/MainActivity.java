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

public class MainActivity extends AppCompatActivity {

    private Button buttonLogin,buttonReg;
    private EditText editTextEmail,editTextPassword;
    UserDAO dao;
    UserDataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogin = findViewById(R.id.btnLogin);
        buttonReg = findViewById(R.id.btnReg);
        editTextEmail = findViewById(R.id.edtLoginEmail);
        editTextPassword = findViewById(R.id.edtLoginPassword);

        dataBase = Room.databaseBuilder(getApplicationContext(),UserDataBase.class,"UserDb")
                .allowMainThreadQueries()
                .build();
        dao = dataBase.getUserDao();


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ValidateEmail() | !ValidatePassword()){
                    return;
                }
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                User user = dao.getUser(email,password);
                if (user != null){
                    Intent i = new Intent(MainActivity.this, UpdateInformation.class);
                    startActivity(i);
                    finish();
                }else {
                    Toast.makeText(MainActivity.this, "Email or password is invalid", Toast.LENGTH_SHORT).show();

                }

            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Registration.class);
                startActivity(i);
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

    private boolean ValidatePassword(){
        String passwordInput = editTextPassword.getText().toString().trim();
        if (passwordInput.isEmpty()){
            editTextPassword.setError("Field can't be empty");
            return false;
        }else {
            editTextPassword.setError(null);
            return true;
        }

    }
}
