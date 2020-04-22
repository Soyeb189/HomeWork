package com.example.homework.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homework.Model.User;
import com.example.homework.R;
import com.example.homework.UserDAO;
import com.example.homework.UserDataBase;


public class MainActivity extends AppCompatActivity {

    private Button buttonLogin,buttonReg;
    private EditText editTextEmail,editTextPassword;
    private CheckBox checkBoxRemember;
    private UserDAO dao;
    private UserDataBase dataBase;
    private User user;
    private int m;
    private String uuid = "";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogin = findViewById(R.id.btnLogin);
        buttonReg = findViewById(R.id.btnReg);
        editTextEmail = findViewById(R.id.edtLoginEmail);
        editTextPassword = findViewById(R.id.edtLoginPassword);
        checkBoxRemember = findViewById(R.id.checkboxReMe);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home Work");
        setSupportActionBar(toolbar);




        uuid = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        user = UserDataBase.getDataBase(getApplicationContext()).getUserDao().getCheckedVal(uuid,1);


        //Toast.makeText(this, user.getEmail(), Toast.LENGTH_SHORT).show();


        if (user != null){
            checkBoxRemember.setChecked(true);
            editTextEmail.setText(user.getEmail());
            editTextPassword.setText(user.getPassword());
            //Toast.makeText(this, user.getUuid(), Toast.LENGTH_SHORT).show();

        }else {
            checkBoxRemember.setChecked(false);
        }

        if (checkBoxRemember.isChecked()){
            m = 1;
        }else {
            m = 0;
        }


        checkBoxRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxRemember.isChecked()){
                    m = 1;
//                    user.setCheckValue(m);
//                    UserDataBase.getDataBase(getApplicationContext()).getUserDao().updateUser(user);
                }
                if (!checkBoxRemember.isChecked()){
                    m = 0;
//                    user.setCheckValue(m);
//                    UserDataBase.getDataBase(getApplicationContext()).getUserDao().updateUser(user);
                }
               // Toast.makeText(MainActivity.this, String.valueOf(m), Toast.LENGTH_SHORT).show();
            }
        });



        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ValidateEmail() | !ValidatePassword()){
                    return;
                }
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();



//                user = dao.getUser(email,password);
                user = UserDataBase.getDataBase(getApplicationContext()).getUserDao().getUser(email,password);

                if (user != null){
                    user.setCheckValue(m);
                    user.setUuid(uuid);
                    UserDataBase.getDataBase(getApplicationContext()).getUserDao().updateUser(user);
                    Intent i = new Intent(MainActivity.this, ProfileDashboard.class);
                    i.putExtra("User", user);
                    startActivity(i);

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
