package com.example.chatbot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class ChatWithTeacherActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton sendMessageButton;
    private EditText userMessageInput;
    private ScrollView mScrollView;
    private TextView displayTextMessage;

    FirebaseAuth mAuth;
    private DatabaseReference TeacherRef, UsersRef, MessageKeyRef;
    private String TeacherName, currentTeacherID, currentDate, currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        TeacherName = getIntent().getExtras().get("teacherName").toString();
        Toast.makeText(ChatWithTeacherActivity.this, TeacherName, Toast.LENGTH_SHORT).show();

        mAuth = FirebaseAuth.getInstance();
        currentTeacherID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        TeacherRef = FirebaseDatabase.getInstance().getReference().child("Users").child(TeacherName);


        InitializeFields();

        getUserInfo();

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMessageInfoToDatabase();
                userMessageInput.setText("");
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        TeacherRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                if(dataSnapshot.exists()) {
//                    DisplayMessages(dataSnapshot);
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                if(dataSnapshot.exists()) {
//                    DisplayMessages(dataSnapshot);
//                }
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }




    private void InitializeFields() {
        mToolbar = findViewById(R.id.chat_bar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(TeacherName);

        sendMessageButton = findViewById(R.id.send_mesg_button);
        userMessageInput = findViewById(R.id.input_msg);
        displayTextMessage = findViewById(R.id.chat_text_display);
        mScrollView = findViewById(R.id.scroll_view);
    }

    private void getUserInfo() {
        TeacherRef.child(currentTeacherID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    TeacherName = dataSnapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveMessageInfoToDatabase() {
        String message = userMessageInput.getText().toString();
        String msgkey = TeacherRef.push().getKey();

        if(TextUtils.isEmpty(message)) {
            Toast.makeText(this, "Please Write Message first..", Toast.LENGTH_SHORT).show();
        }
        else {
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            currentDate = currentDateFormat.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = currentTimeFormat.format(calForTime.getTime());

            HashMap<String, Object> MessageKey = new HashMap<>();

            TeacherRef.updateChildren(MessageKey);
            MessageKeyRef = TeacherRef.child(msgkey);

            HashMap<String, Object> messageInfoMap = new HashMap<>();
            messageInfoMap.put("name", currentTeacherID);
            messageInfoMap.put("message", message);
            messageInfoMap.put("date", currentDate);
            messageInfoMap.put("time", currentTime);

            MessageKeyRef.updateChildren(messageInfoMap);

        }
    }

    private void DisplayMessages(DataSnapshot dataSnapshot) {
        Iterator iterator = (Iterator) dataSnapshot.getChildren();
        while(iterator.hasNext()) {
            String chatDate = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatMessage = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatName = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatTime = (String) ((DataSnapshot) iterator.next()).getValue();

            displayTextMessage.append(chatName + " :\n" + chatMessage + "\n" + chatTime + "     " + chatDate + "\n\n\n");
            mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }
}
