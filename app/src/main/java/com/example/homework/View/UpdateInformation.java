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

public class UpdateInformation extends AppCompatActivity {

    private EditText editTextName,editTextEmail,editTextPhone,editTextPassword,editTextConfirmPassword;
    private Button buttonUpdate;
    User user;
    UserDataBase dataBase;
    UserDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_information);

        editTextName = findViewById(R.id.edtUpdateName);
        editTextEmail = findViewById(R.id.edtUpdateEmail);
        editTextPhone = findViewById(R.id.edtUpdatePhone);
        editTextPassword = findViewById(R.id.edtUpdatePassword);
        editTextConfirmPassword = findViewById(R.id.edtUpdateConfirmPassword);
        buttonUpdate = findViewById(R.id.btnUpdate);

        dataBase = Room.databaseBuilder(getApplicationContext(),UserDataBase.class,"UserDb")
                .allowMainThreadQueries()
                .build();
        dao = dataBase.getUserDao();


        user = (User) getIntent().getSerializableExtra("User");


        if (user!= null){
            editTextName.setText(user.getName());
            editTextEmail.setText(user.getEmail());
            editTextPhone.setText(user.getPhoneNumber());
            editTextPassword.setText(user.getPassword());

        }

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ValidateName() | !ValidateEmail() | !ValidatePhone() | !ValidatePassword()){
                    return;
                }
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String phone = editTextPhone.getText().toString();
                String password = editTextPassword.getText().toString().trim();


                long u_id = user.getId();

                user.setName(name);
                user.setEmail(email);
                user.setPassword(password);
                user.setPhoneNumber(phone);
                user.setId(u_id);

                dataBase.getUserDao().updateUser(user);

                Toast.makeText(UpdateInformation.this, "Your Data is updated Successfully", Toast.LENGTH_SHORT).show();

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

    private boolean ValidatePhone(){
        String phone = editTextPhone.getText().toString().trim();
        if (phone.isEmpty()){
            editTextPhone.setError("Phone number is required");
            return false;
        }else {
            editTextPhone.setError(null);
            return true;
        }
    }

    private boolean ValidatePassword(){
        String password = editTextPassword.getText().toString();
        if (password.isEmpty()){
            editTextPassword.setError("Field Can't be empty");
            return false;
        }else if (password.length()<6){
            editTextPassword.setError("Length must be more than 6 ");
            return false;
        }else {
            editTextPassword.setError(null);
            return true;
        }
    }

    private boolean ValidateName(){
        String userName = editTextName.getText().toString();
        if (userName.isEmpty()){
            editTextName.setError("Field Can't be empty");
            return false;
        }else if (!userName.matches("^[A-Za-z]+$")){
            editTextName.setError("Valid Name is required");
            return false;
        }else {
            editTextName.setError(null);
            return true;
        }
    }
}
