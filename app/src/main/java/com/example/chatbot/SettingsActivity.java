package com.example.chatbot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private Button updateAccountSettings, mBack;
    private EditText userName, userStatus;
    private CircleImageView userProfileImage;

    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference RootReference;

    private static final int gallarypick = 1;
    private StorageReference UserProfileImageRef;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        InitalizeFields();
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        RootReference = FirebaseDatabase.getInstance().getReference();
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        updateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSettings();
            }
        });

        RetrieveUserInfo();

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallaryIntent = new Intent();
                gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                gallaryIntent.setType("image/*");
                startActivityForResult(gallaryIntent, gallarypick);
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToMainActivity();
            }
        });
    }

    private void InitalizeFields() {
        updateAccountSettings = findViewById(R.id.update_settings_button);
        userName = findViewById(R.id.set_user_name);
        userStatus = findViewById(R.id.set_profile_status);
        userProfileImage = findViewById(R.id.set_profile_image);
        loadingbar = new ProgressDialog(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == gallarypick && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                loadingbar.setTitle("Set Profile Image");
                loadingbar.setMessage("Please Wait");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
                Uri resultUri = result.getUri();

                StorageReference filePath = UserProfileImageRef.child(currentUserId + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SettingsActivity.this, "Profile Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            final String downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();
                            RootReference.child("Users").child(currentUserId).child("image").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Toast.makeText(SettingsActivity.this, "Image Saved In Database Successfully", Toast.LENGTH_SHORT).show();
                                                loadingbar.dismiss();
                                            }
                                            else {
                                                String message = task.getException().toString();
                                                Toast.makeText(SettingsActivity.this, "Errod: " + message, Toast.LENGTH_SHORT).show();
                                                loadingbar.dismiss();
                                            }
                                        }
                                    });
                        }
                        else {
                            String message = task.getException().toString();
                            Toast.makeText(SettingsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                        }
                    }
                });

            }
        }
    }

    private void updateSettings() {
        String setUserName = userName.getText().toString();
        String setStatus = userStatus.getText().toString();

        if(TextUtils.isEmpty(setUserName)) {
            Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(setStatus)) {
            Toast.makeText(this, "Please Write Your Status", Toast.LENGTH_LONG).show();
        }
        else {
            HashMap<String, String> profileMap = new HashMap<>();
                profileMap.put("uid", currentUserId);
                profileMap.put("name", setUserName);
                profileMap.put("status", setStatus);
             RootReference.child("Users").child(currentUserId).setValue(profileMap)
                     .addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if(task.isSuccessful()) {
                                 sendUserToMainActivity();
                                 Toast.makeText(SettingsActivity.this, "Profile Updated", Toast.LENGTH_LONG).show();
                             }
                             else {
                                 String message = task.getException().toString();
                                 Toast.makeText(SettingsActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
                             }
                         }
                     });
        }


    }
    private void sendUserToMainActivity() {
        Intent mainInten = new Intent(SettingsActivity.this, MainActivity.class);
        mainInten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainInten);
        finish();
    }

    private void RetrieveUserInfo() {
        RootReference.child("Users").child(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("image")))) {
                            String retrieveUserName = dataSnapshot.child("name").getValue().toString();
                            String retrieveStatus = dataSnapshot.child("status").getValue().toString();
                            String retrieveProfileImage = dataSnapshot.child("image").getValue().toString();
//                            String str = "https://firebasestorage.googleapis.com/v0/b/sust-db.appspot.com/o/Profile%20Images%2F" + currentUserId + ".jpg?alt=media&token=e2f4b894-3b00-4a96-814e-9b8df1172131";
                            userName.setText(retrieveUserName);
                            userStatus.setText(retrieveStatus);
                            Picasso.get().load(retrieveProfileImage).into(userProfileImage);
                        }
                        else if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))){
                            String retrieveUserName = dataSnapshot.child("name").getValue().toString();
                            String retrieveStatus = dataSnapshot.child("status").getValue().toString();

                            userName.setText(retrieveUserName);
                            userStatus.setText(retrieveStatus);
                        }
                        else {

                            Toast.makeText(SettingsActivity.this, "Please Update Your Profile", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
