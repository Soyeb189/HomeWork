package com.example.homework.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homework.Model.ImageUtil;
import com.example.homework.Model.RegStatus;
import com.example.homework.Model.User;
import com.example.homework.R;
import com.example.homework.UserDataBase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileDashboard extends AppCompatActivity {

    private User user;


    private Dialog dialog;

    private TextView textViewName,textViewEmail,textViewPhone;
    private FloatingActionButton fabPassword,fabName,fabEmail,fabPhone,fabProImage;
    private ImageView imageViewProfile;
    private Button buttonProfile;
    private Toolbar toolbar;
    Bitmap bitmap;
    private int m = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_dashboard);

        textViewName = findViewById(R.id.tvProfileName);
        textViewEmail = findViewById(R.id.tvProfileEmail);
        textViewPhone = findViewById(R.id.tvProfilePhone);
        fabPassword = findViewById(R.id.fabChangePassword);
        fabName = findViewById(R.id.fabChangeName);
        fabEmail = findViewById(R.id.fabChangeEmail);
        fabPhone = findViewById(R.id.fabChangePhone);
        fabProImage = findViewById(R.id.update_profile_image_btn);
        imageViewProfile = findViewById(R.id.proImage);
        buttonProfile = findViewById(R.id.btnViewProfile);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("DashBoard");
        setSupportActionBar(toolbar);




         user = (User) getIntent().getSerializableExtra("User");
        //user = (user)getIntent().getSerializableExtra("User");
        Toast.makeText(this, String.valueOf(user.getCheckValue()), Toast.LENGTH_SHORT).show();
        String uuid = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
//        m = user.getCheckValue();
//
//
//        user.setUuid(uuid);
//        user.setCheckValue(m);
//        UserDataBase.getDataBase(getApplicationContext()).getUserDao().updateUser(user);


        if (user!= null){
            textViewName.setText(user.getName());
            textViewEmail.setText(user.getEmail());
            textViewPhone.setText(user.getPhoneNumber());
           //Toast.makeText(this, user.getUuid(), Toast.LENGTH_SHORT).show();
            if (user.getImage().equals("image")){
                imageViewProfile.setBackgroundResource(R.drawable.user_bac);

            }else {
                Bitmap image = ImageUtil.convert(user.getImage());
                imageViewProfile.setImageBitmap(image);
            }
        }

        fabPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPasswordDIalog();
            }
        });

        fabName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowNameDialog();
            }
        });

        fabEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowEmailDialog();
            }
        });

        fabPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPhoneDialog();
            }
        });

        fabProImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage(ProfileDashboard.this);
            }
        });

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileDashboard.this,ViewProfile.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });
    }

    private void SelectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);//one can be replaced with any action code

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        bitmap= (Bitmap) data.getExtras().get("data");
                        imageViewProfile.setImageBitmap(bitmap);
                        Upload();
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();

                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
                            imageViewProfile.setImageBitmap(bitmap);
                            if (bitmap==null){

                            }else {
                                Upload();
                            }
                            // img.setVisibility(View.VISIBLE);
                            //phone.setVisibility(View.VISIBLE);
                            //uploadButton.setEnabled(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                        if (selectedImage != null) {
//                            Cursor cursor = getContentResolver().query(selectedImage,
//                                    filePathColumn, null, null, null);
//                            if (cursor != null) {
//                                cursor.moveToFirst();
//
//                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                                String picturePath = cursor.getString(columnIndex);
//                                imageViewProfile.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//                                cursor.close();
//                            }
//                        }

                    }
                    break;
            }
        }
    }

    private void Upload() {
//        Bitmap bitmapImage = BitmapFactory.decodeFile(String.valueOf(bitmap));
//        int nh = (int) ( bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()) );
//        Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
        String base64String = ImageUtil.convert(bitmap);
        user.setImage(base64String);


        UserDataBase.getDataBase(getApplicationContext()).getUserDao().updateUser(user);
    }


    private void ShowPasswordDIalog() {

        Button btnCancle,btnSubmit;
        final EditText editTextNew,editTextOld;

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.password_change_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnCancle = dialog.findViewById(R.id.dialogBtnCancle);
        btnSubmit = dialog.findViewById(R.id.dialogBtnSubmit);
        editTextOld = dialog.findViewById(R.id.edtOldPassword);
        editTextNew = dialog.findViewById(R.id.edtNewPassword);


        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = user.getPassword();
                String oldp = editTextOld.getText().toString().trim();
                String newp = editTextNew.getText().toString().trim();
                if (password.equals(oldp)){
                    user.setPassword(newp);
                    UserDataBase.getDataBase(getApplicationContext()).getUserDao().updateUser(user);
                    dialog.dismiss();
                    Toast.makeText(ProfileDashboard.this, "Your Password is updated Successfully", Toast.LENGTH_SHORT).show();
                }else {
                    editTextNew.setError("Your password Doesn't matched");
                }

            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }


    private void ShowNameDialog() {

        Button btnCancle,btnSubmit;
        final EditText editTextName;

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.name_change_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnCancle = dialog.findViewById(R.id.dialogBtnCancle);
        btnSubmit = dialog.findViewById(R.id.dialogBtnSubmit);
        editTextName = dialog.findViewById(R.id.edtChangeName);
        editTextName.setText(user.getName());

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                if (name.isEmpty()){
                    editTextName.setError("Field can't be empty");
                }else if (!name.matches("^[A-Za-z]+$")){
                    editTextName.setError("Name must be valid");
                }else {
                    editTextName.setError(null);
                    user.setName(name);
                    UserDataBase.getDataBase(getApplicationContext()).getUserDao().updateUser(user);
                    dialog.dismiss();
                    Toast.makeText(ProfileDashboard.this, "Your Name is updated Successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    private void ShowEmailDialog() {

        Button btnCancle,btnSubmit;
        final EditText editTextEmail;

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.email_change_poppup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnCancle = dialog.findViewById(R.id.dialogBtnCancle);
        btnSubmit = dialog.findViewById(R.id.dialogBtnSubmit);
        editTextEmail = dialog.findViewById(R.id.edtChangeEmail);
        editTextEmail.setText(user.getEmail());

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = editTextEmail.getText().toString();
                if (email.isEmpty()){
                    editTextEmail.setError("Filed can't be empty");
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editTextEmail.setError("Email must be valid ");
                }else {
                    editTextEmail.setError(null);
                    user.setEmail(email);

                    UserDataBase.getDataBase(getApplicationContext()).getUserDao().updateUser(user);
                    dialog.dismiss();
                    Toast.makeText(ProfileDashboard.this, "Your Email is updated Successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


    }

    private void ShowPhoneDialog() {

        Button btnCancle,btnSubmit;
        final EditText editTextPhone;

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.phone_change_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnCancle = dialog.findViewById(R.id.dialogBtnCancle);
        btnSubmit = dialog.findViewById(R.id.dialogBtnSubmit);
        editTextPhone = dialog.findViewById(R.id.edtChangePhone);
        editTextPhone.setText(user.getPhoneNumber());

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editTextPhone.getText().toString();
                if (phone.isEmpty()){
                    editTextPhone.setError("Phone number is required");
                }else if (phone.length()<11){
                    editTextPhone.setError("Valid Phone number is required");
                }else {
                    editTextPhone.setError(null);
                    user.setPhoneNumber(phone);

                    UserDataBase.getDataBase(getApplicationContext()).getUserDao().updateUser(user);
                    dialog.dismiss();
                    Toast.makeText(ProfileDashboard.this, "Your Phone is updated Successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }


}
