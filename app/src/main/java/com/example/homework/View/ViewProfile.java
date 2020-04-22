package com.example.homework.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homework.Model.ImageUtil;
import com.example.homework.Model.User;
import com.example.homework.R;
import com.example.homework.UserDataBase;

public class ViewProfile extends AppCompatActivity {

    private User user;
    private Toolbar toolbar;
    private TextView textViewName,textViewEmail,textViewPhone;
    private ImageView imageViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);

        textViewName = findViewById(R.id.tvProfileName);
        textViewEmail = findViewById(R.id.tvProfileEmail);
        textViewPhone = findViewById(R.id.tvProfilePhone);
        imageViewProfile = findViewById(R.id.proImage);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        user = (User) getIntent().getSerializableExtra("User");

        if (user!=null){
            textViewName.setText(user.getName());
            textViewPhone.setText(user.getPhoneNumber());
            textViewEmail.setText(user.getEmail());
            if (user.getImage().equals("image")){
                imageViewProfile.setBackgroundResource(R.drawable.user_bac);

            }else {
                Bitmap image = ImageUtil.convert(user.getImage());
                imageViewProfile.setImageBitmap(image);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        UserDataBase.getDataBase(getApplicationContext()).getUserDao().updateUser(user);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        UserDataBase.getDataBase(getApplicationContext()).getUserDao().updateUser(user);
        super.onStop();
    }

    @Override
    protected void onPause() {
        UserDataBase.getDataBase(getApplicationContext()).getUserDao().updateUser(user);
        super.onPause();
    }

    @Override
    protected void onRestart() {
        UserDataBase.getDataBase(getApplicationContext()).getUserDao().updateUser(user);
        super.onRestart();
    }
}
